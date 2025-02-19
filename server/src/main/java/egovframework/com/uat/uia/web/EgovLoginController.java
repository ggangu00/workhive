package egovframework.com.uat.uia.web;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.cmm.EgovComponentChecker;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.config.EgovLoginConfig;
import egovframework.com.cmm.service.EgovCmmUseService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.uat.uia.service.EgovLoginService;
import egovframework.com.utl.sim.service.EgovClntInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * 일반 로그인, 인증서 로그인을 처리하는 컨트롤러 클래스
 */
@RestController // RESTful API를 제공하는 컨트롤러
@RequestMapping("/login") // 로그인 관련 URL을 처리하는 컨트롤러로 매핑
@Slf4j // 로깅을 위한 Lombok 어노테이션
public class EgovLoginController {

    /** 로그인 서비스 */
    @Resource(name = "loginService")
    private EgovLoginService loginService;

    /** 공통 코드 서비스 */
    @Resource(name = "EgovCmmUseService")
    private EgovCmmUseService cmmUseService;

    /** 메시지 소스 */
    @Resource(name = "egovMessageSource")
    EgovMessageSource egovMessageSource;

    /** 로그인 설정 서비스 */
    @Resource(name = "egovLoginConfig")
    EgovLoginConfig egovLoginConfig;

    /** 로거 선언 */
    private static final Logger LOGGER = LoggerFactory.getLogger(EgovLoginController.class);

    /**
     * 로그인 화면을 반환하는 메서드
     * @param loginVO - 로그인 정보를 담고 있는 객체
     * @param request - HTTP 요청 객체
     * @param response - HTTP 응답 객체
     * @param model - 화면에 데이터를 전달할 모델 객체
     * @return 로그인 페이지 뷰 이름
     * @throws Exception 예외 발생 시 처리
     */
    @IncludedInfo(name = "로그인", listUrl = "/uat/uia/egovLoginUsr.do", order = 10, gid = 10) // 정보를 표현하는 어노테이션
    @RequestMapping(value = "/uat/uia/egovLoginUsr.do")
    public String loginUsrView(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
        
        // 회원 관리 컴포넌트가 존재하는지 확인하여 사용 여부 설정
        if (EgovComponentChecker.hasComponent("mberManageService")) {
            model.addAttribute("useMemberManage", "true");
        }

        // 권한 체크 시 에러가 발생한 경우, 접근 제한 페이지로 이동
        String authError = request.getParameter("auth_error");
        if ("1".equals(authError)) {
            return "egovframework/com/cmm/error/accessDenied";
        }

        // 인증 방식 설정값을 모델에 추가
        String authType = EgovProperties.getProperty("Globals.Auth").trim();
        model.addAttribute("authType", authType);

        // 로그인 메시지가 있을 경우 모델에 추가
        String message = request.getParameter("loginMessage");
        if (message != null) {
            model.addAttribute("loginMessage", message);
        }
        
        return "egovframework/com/uat/uia/EgovLoginUsr";
    }
    
    /**
     * 일반 로그인 처리 메서드
     * @param loginVO - 로그인 정보를 담고 있는 객체
     * @param request - HTTP 요청 객체
     * @param model - 화면에 데이터를 전달할 모델 객체
     * @return 로그인 처리 결과에 따른 페이지 이동 경로
     * @throws Exception 예외 발생 시 처리
     */
    @RequestMapping(value = "/uat/uia/actionLogin.do")
    public String actionLogin(@ModelAttribute("loginVO") LoginVO loginVO, HttpServletRequest request, ModelMap model) throws Exception {
        
        // 로그인 인증 제한이 활성화된 경우
        if (egovLoginConfig.isLock()) {
            Map<?, ?> mapLockUserInfo = (EgovMap) loginService.selectLoginIncorrect(loginVO);
            if (mapLockUserInfo != null) {
                // 로그인 실패 횟수 확인 및 처리
                String sLoginIncorrectCode = loginService.processLoginIncorrect(loginVO, mapLockUserInfo);
                if (!"E".equals(sLoginIncorrectCode)) {
                    if ("L".equals(sLoginIncorrectCode)) {
                        model.addAttribute("loginMessage", egovMessageSource.getMessageArgs("fail.common.loginIncorrect", new Object[]{egovLoginConfig.getLockCount(), request.getLocale()}));
                    } else {
                        model.addAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login", request.getLocale()));
                    }
                    return "redirect:/uat/uia/egovLoginUsr.do";
                }
            } else {
                model.addAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login", request.getLocale()));
                return "redirect:/uat/uia/egovLoginUsr.do";
            }
        }

        // 로그인 처리
        LoginVO resultVO = loginService.actionLogin(loginVO);
        String userIp = EgovClntInfo.getClntIP(request);
        resultVO.setIp(userIp);
        
        // 로그인 성공 시 세션에 사용자 정보 저장
        if (resultVO.getId() != null && !resultVO.getId().isEmpty()) {
            request.getSession().setAttribute("loginVO", resultVO);
            request.getSession().setAttribute("accessUser", resultVO.getUserSe().concat(resultVO.getId()));
            return "redirect:/uat/uia/actionMain.do";
        } else {
            model.addAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login", request.getLocale()));
            return "redirect:/uat/uia/egovLoginUsr.do";
        }
    }
}
