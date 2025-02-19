package egovframework.com.uat.uia.web;

import javax.annotation.Resource; // Spring의 의존성 주입을 위한 Resource 어노테이션
import javax.servlet.http.HttpServletRequest; // HTTP 요청 관련 객체
import javax.servlet.http.HttpServletResponse; // HTTP 응답 관련 객체

import org.slf4j.Logger; // SLF4J 로깅 인터페이스
import org.slf4j.LoggerFactory; // SLF4J 로깅 객체 생성 클래스
import org.springframework.ui.ModelMap; // Spring MVC에서 View로 데이터를 전달하는 모델 객체
import org.springframework.web.bind.annotation.ModelAttribute; // 요청 파라미터를 객체로 바인딩하는 어노테이션
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; // URL을 컨트롤러 메서드와 매핑하는 어노테이션
import org.springframework.web.bind.annotation.RestController; // RESTful API를 위한 컨트롤러 어노테이션

import egovframework.com.cmm.EgovComponentChecker; // eGovFrame에서 컴포넌트 존재 여부를 체크하는 클래스
import egovframework.com.cmm.EgovMessageSource; // eGovFrame에서 메시지 관리를 위한 클래스
import egovframework.com.cmm.LoginVO; // 로그인 정보 VO (Value Object)
import egovframework.com.cmm.annotation.IncludedInfo; // eGovFrame에서 포함된 기능 정보를 설정하는 어노테이션
import egovframework.com.cmm.config.EgovLoginConfig; // 로그인 관련 설정을 관리하는 클래스
import egovframework.com.cmm.service.EgovCmmUseService; // 공통 코드 관리 서비스
import egovframework.com.cmm.service.EgovProperties; // eGovFrame의 프로퍼티 설정을 다루는 클래스
import egovframework.com.uat.uia.service.EgovLoginService; // 로그인 서비스 인터페이스
import egovframework.com.utl.sim.service.EgovClntInfo; // 클라이언트 IP 정보를 조회하는 유틸 클래스
import lombok.extern.slf4j.Slf4j; // Lombok의 로깅 어노테이션

/**
 * 일반 로그인, 인증서 로그인을 처리하는 컨트롤러 클래스
 */
@RestController // RESTful API를 제공하는 컨트롤러 선언 (Spring MVC Controller)
@RequestMapping("/login") // "/login" 경로로 오는 요청을 이 컨트롤러에서 처리
@Slf4j // Lombok을 이용한 로깅 기능 추가
public class EgovLoginController {

    /** 로그인 서비스 */
    @Resource(name = "loginService") // Spring에서 loginService 빈을 주입받음
    private EgovLoginService loginService;

    /** 공통 코드 서비스 */
    @Resource(name = "EgovCmmUseService") // 공통 코드 관리 서비스 주입
    private EgovCmmUseService cmmUseService;

    /** 메시지 소스 (다국어 메시지 처리) */
    @Resource(name = "egovMessageSource")
    EgovMessageSource egovMessageSource;

    /** 로그인 설정 서비스 */
    @Resource(name = "egovLoginConfig")
    EgovLoginConfig egovLoginConfig;

    /** 로깅 객체 생성 */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgovLoginController.class);

    /**
     * 로그인 화면을 반환하는 메서드
     * @param loginVO - 로그인 정보를 담고 있는 객체 (요청 파라미터 바인딩)
     * @param request - HTTP 요청 객체
     * @param response - HTTP 응답 객체
     * @param model - View로 데이터를 전달하는 ModelMap 객체
     * @return String - 로그인 페이지 뷰 이름 (JSP 페이지)
     * @throws Exception 예외 발생 시 처리
     */
    @IncludedInfo(name = "로그인", listUrl = "/uat/uia/egovLoginUsr.do", order = 10, gid = 10) // eGovFrame에서 제공하는 기능 설명용 어노테이션
    @RequestMapping(value = "/uat/uia/egovLoginUsr.do") // 해당 URL로 요청이 오면 이 메서드가 실행됨
    public String loginUsrView(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
        
        // 회원 관리 컴포넌트가 존재하는지 확인하여 사용 여부 설정
        if (EgovComponentChecker.hasComponent("mberManageService")) {
            model.addAttribute("useMemberManage", "true"); // JSP에서 해당 속성 사용 가능
        }

        // 권한 체크 시 에러가 발생한 경우, 접근 제한 페이지로 이동
        String authError = request.getParameter("auth_error");
        if ("1".equals(authError)) {
            return "egovframework/com/cmm/error/accessDenied"; // 권한 에러 페이지로 이동
        }

        // 인증 방식 설정값을 모델에 추가
        String authType = EgovProperties.getProperty("Globals.Auth").trim();
        model.addAttribute("authType", authType);

        // 로그인 메시지가 있을 경우 모델에 추가
        String message = request.getParameter("loginMessage");
        if (message != null) {
            model.addAttribute("loginMessage", message);
        }
        
        return "egovframework/com/uat/uia/EgovLoginUsr"; // 로그인 페이지로 이동
    }
    
    /**
     * 일반 로그인 처리 메서드
     * @param loginVO - 로그인 정보를 담고 있는 객체
     * @param request - HTTP 요청 객체
     * @param model - 화면에 데이터를 전달할 모델 객체
     * @return String - 로그인 성공 여부에 따라 페이지 이동 경로 반환
     * @throws Exception 예외 발생 시 처리
     */
//    @RequestMapping(value = "/uat/uia/actionLogin.do") // "/uat/uia/actionLogin.do" URL과 매핑
//    public String actionLogin(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, ModelMap model) throws Exception {
//        
//        // 로그인 인증 제한이 활성화된 경우 (비밀번호 5회 오류 등)
////        if (egovLoginConfig.isLock()) {
////            Map<?, ?> mapLockUserInfo = (EgovMap) loginService.selectLoginIncorrect(loginVO);
////            if (mapLockUserInfo != null) {
////                // 로그인 실패 횟수 확인 및 처리
////                String sLoginIncorrectCode = loginService.processLoginIncorrect(loginVO, mapLockUserInfo);
////                if (!"E".equals(sLoginIncorrectCode)) { // 로그인 실패가 연속되었을 경우
////                    if ("L".equals(sLoginIncorrectCode)) { // 계정 잠김
////                        model.addAttribute("loginMessage", egovMessageSource.getMessageArgs("fail.common.loginIncorrect", new Object[]{egovLoginConfig.getLockCount(), request.getLocale()}));
////                    } else { // 일반적인 로그인 실패
////                        model.addAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login", request.getLocale()));
////                    }
////                    return "redirect:/uat/uia/egovLoginUsr.do"; // 로그인 페이지로 리다이렉트
////                }
////            } else {
////                model.addAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login", request.getLocale()));
////                return "redirect:/uat/uia/egovLoginUsr.do"; // 로그인 페이지로 리다이렉트
////            }
////        }
//
//        // 로그인 처리 (DB에서 사용자 정보 조회)
//        LoginVO resultVO = loginService.actionLogin(loginVO);
//        String userIp = EgovClntInfo.getClntIP(request); // 클라이언트 IP 가져오기
//        resultVO.setIp(userIp); // 로그인 정보에 IP 설정
//        
//        // 로그인 성공 시 세션에 사용자 정보 저장
//        if (resultVO.getId() != null && !resultVO.getId().isEmpty()) {
//            request.getSession().setAttribute("loginVO", resultVO); // 세션에 로그인 정보 저장
//            request.getSession().setAttribute("accessUser", resultVO.getUserSe().concat(resultVO.getId())); // 사용자 세션 설정
//            return "redirect:/uat/uia/actionMain.do"; // 로그인 성공 후 메인 페이지로 이동
//        } else {
//            model.addAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login", request.getLocale()));
//            return "redirect:/uat/uia/egovLoginUsr.do"; // 로그인 실패 시 다시 로그인 페이지로 이동
//        }
//    }
    
    /**
     * 일반 로그인 처리 메서드
     * @param loginVO - 로그인 정보를 담고 있는 객체
     * @param request - HTTP 요청 객체
     * @param model - 화면에 데이터를 전달할 모델 객체
     * @return String - 로그인 성공 여부에 따라 페이지 이동 경로 반환
     * @throws Exception 예외 발생 시 처리
     */
    @PostMapping(value = "") // "/uat/uia/actionLogin.do" URL과 매핑
    public void actionLogin(@RequestBody LoginVO loginVO, HttpServletRequest request) throws Exception {
        
        log.info("로그인 정보 : ", loginVO.toString());
    	
        // 로그인 처리 (DB에서 사용자 정보 조회)
        LoginVO resultVO = loginService.actionLogin(loginVO);
//        String userIp = EgovClntInfo.getClntIP(request); // 클라이언트 IP 가져오기
//        resultVO.setIp(userIp); // 로그인 정보에 IP 설정
        
        // 로그인 성공 시 세션에 사용자 정보 저장
//        if (resultVO.getId() != null && !resultVO.getId().isEmpty()) {
//            request.getSession().setAttribute("loginVO", resultVO); // 세션에 로그인 정보 저장
//            request.getSession().setAttribute("accessUser", resultVO.getUserSe().concat(resultVO.getId())); // 사용자 세션 설정
//            return "redirect:/uat/uia/actionMain.do"; // 로그인 성공 후 메인 페이지로 이동
//        } else {
//            model.addAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login", request.getLocale()));
//            return "redirect:/uat/uia/egovLoginUsr.do"; // 로그인 실패 시 다시 로그인 페이지로 이동
//        }
    }
}
