//package egovframework.com.cmm.interceptor;
//
//import java.util.Collections;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.ModelAndViewDefiningException;
//
//import egovframework.com.cmm.util.EgovUserDetailsHelper;
//
///**
// * 인증여부 체크 인터셉터
// * Spring의 인터셉터(Interceptor) 를 사용하여 사용자의 인증 여부를 확인하는 기능을 수행하는 클래스야.
// * 이 AuthenticInterceptor 클래스는 요청(Request)이 컨트롤러로 전달되기 전에 실행되며, 사용자가 로그인했는지, 관리자 권한이 있는지 등을 체크하는 역할을 해.
// * 여기서 권한이 있는지 없는지 체크를해서 vue단으로 json형태로 넘겨주면 앞단에서 이것을 처리함
// */
//
//
//public class AuthenticInterceptor implements HandlerInterceptor {
//    @SuppressWarnings("unused")
//    @Autowired
//    private Environment environment;
//    
//    /** 관리자 접근 권한 패턴 목록 */
//    private List<String> adminAuthPatternList;
//    
//    public List<String> getAdminAuthPatternList() {
//        return adminAuthPatternList;
//    }
//
//    public void setAdminAuthPatternList(List<String> adminAuthPatternList) {
//        this.adminAuthPatternList = adminAuthPatternList;
//    }
//
//    /**
//     * 요청이 들어올 때 권한을 체크하고, JSON 형태로 응답을 반환한다.
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        // 1. 로그인 여부 체크
//        boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
//        
//        if (!isAuthenticated) {
//            sendJsonResponse(response, 401, "로그인이 필요합니다.");
//            return false; // 요청 중단
//        }
//
//        // 2. 사용자 권한 가져오기
//        List<String> authList = (List<String>) EgovUserDetailsHelper.getAuthorities();
//        
//        // 3. 요청 URL이 관리자 페이지인지 확인
//        boolean isAdminPage = false;
//        AntPathRequestMatcher matcher;
//        
//        for (String pattern : adminAuthPatternList) {
//            matcher = new AntPathRequestMatcher(pattern);
//            if (matcher.matches(request)) {
//                isAdminPage = true;
//                break;
//            }
//        }
//
//        // 4. 관리자 권한 체크
//        if (isAdminPage && !authList.contains("ROLE_ADMIN")) {
//            sendJsonResponse(response, 403, "관리자 권한이 없습니다.");
//            return false; // 요청 중단
//        }
//
//        return true; // 요청 계속 진행
//    }
//
//    /**
//     * JSON 응답을 반환하는 메서드
//     */
//    private void sendJsonResponse(HttpServletResponse response, int status, String message) throws IOException {
//        response.setStatus(status);
//        response.setContentType("application/json;charset=UTF-8");
//
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("status", status);
//        responseData.put("message", message);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        response.getWriter().write(objectMapper.writeValueAsString(responseData));
//    }
//
//
//}
