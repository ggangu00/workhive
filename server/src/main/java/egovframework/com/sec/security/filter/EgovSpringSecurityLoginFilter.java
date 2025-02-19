package egovframework.com.sec.security.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.support.WebApplicationContextUtils;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.config.EgovLoginConfig;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.uat.uia.service.EgovLoginService;

/**
 * Spring Security 기반 로그인 필터
 *
 * @author 공통서비스 개발팀 서준식
 * @since 2011. 8. 29.
 * @version 1.0
 * @see
 *
 * <pre>
 * 개정이력(Modification Information)
 *  수정일        수정자          수정내용
 *  ----------   --------   ---------------------------
 *  2011.08.29   서준식      최초생성
 *  2011.12.12   유지보수      사용자 로그인 정보 간섭 가능성 문제 수정
 *  2014.03.07   유지보수      로그인된 상태에서 다시 로그인 시 미처리 문제 수정
 *  2017.03.03   조성원       시큐어코딩 적용 (부적절한 예외 처리)
 *  2017.07.10   장동한       Spring Security 4.0.3 적용
 *  2017.07.21   장동한       로그인 인증 제한 적용
 *  2020.06.25   신용호       로그인 메시지 처리 수정
 * </pre>
 */

public class EgovSpringSecurityLoginFilter implements Filter {

    private FilterConfig config;
    private static final Logger LOGGER = LoggerFactory.getLogger(EgovSpringSecurityLoginFilter.class);

    @Override
    public void destroy() {
        // 필터 제거 시 호출되는 메서드 (현재 별도 처리 없음)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        LOGGER.info("EgovSpringSecurityLoginFilter 호출됨...");

        // 로그인 URL 및 로그인 처리 URL 설정
        String loginURL = config.getInitParameter("loginURL").replaceAll("\r", "").replaceAll("\n", "");
        String loginProcessURL = config.getInitParameter("loginProcessURL").replaceAll("\r", "").replaceAll("\n", "");

        // Spring Context에서 Bean 가져오기
        ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        EgovLoginService loginService = (EgovLoginService) act.getBean("loginService");
        EgovLoginConfig egovLoginConfig = (EgovLoginConfig) act.getBean("egovLoginConfig");
        EgovMessageSource egovMessageSource = (EgovMessageSource) act.getBean("egovMessageSource");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession();

        String isRemotelyAuthenticated = (String) session.getAttribute("isRemotelyAuthenticated");
        String requestURL = httpRequest.getRequestURI();

        // Spring Security에서 사용자가 인증되었는지 확인
        if (EgovUserDetailsHelper.getAuthenticatedUser() == null || requestURL.contains(loginProcessURL)) {

            // 원격 인증된 경우, 세션에서 사용자 정보를 가져와 로그인 처리
            if ("true".equals(isRemotelyAuthenticated)) {
                try {
                    LoginVO loginVO = (LoginVO) session.getAttribute("loginVOForDBAuthentication");
                    loginVO = loginService.actionLoginByEsntlId(loginVO);

                    if (loginVO != null && loginVO.getId() != null && !loginVO.getId().isEmpty()) {
                        session.setAttribute("loginVO", loginVO);
                        session.setAttribute("isLocallyAuthenticated", "true");

                        // Spring Security 로그인 처리
                        UsernamePasswordAuthenticationFilter springSecurity = null;
                        Map<String, UsernamePasswordAuthenticationFilter> beans = act.getBeansOfType(UsernamePasswordAuthenticationFilter.class);
                        if (!beans.isEmpty()) {
                            springSecurity = beans.values().iterator().next();
                            springSecurity.setUsernameParameter("egov_security_username");
                            springSecurity.setPasswordParameter("egov_security_password");
                            springSecurity.setRequiresAuthenticationRequestMatcher(
                                    new AntPathRequestMatcher(httpRequest.getContextPath() + "/egov_security_login", "POST")
                            );
                        } else {
                            LOGGER.error("No AuthenticationProcessingFilter found");
                            throw new IllegalStateException("No AuthenticationProcessingFilter");
                        }

                        LOGGER.debug("Spring Security 인증 필터 호출 전...");
                        springSecurity.doFilter(new RequestWrapperForSecurity(httpRequest, loginVO.getUserSe() + loginVO.getId(), loginVO.getUniqId()), httpResponse, chain);
                        LOGGER.debug("Spring Security 인증 필터 호출 후...");
                    }

                } catch (Exception e) {
                    LOGGER.error("로그인 예외 발생: {}", e.getMessage(), e);
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }
}

/**
 * Spring Security 인증을 위한 HTTP 요청 래퍼 클래스
 */
class RequestWrapperForSecurity extends HttpServletRequestWrapper {
    private final String username;
    private final String password;

    public RequestWrapperForSecurity(HttpServletRequest request, String username, String password) {
        super(request);
        this.username = username;
        this.password = password;
    }

    @Override
    public String getServletPath() {
        return ((HttpServletRequest) super.getRequest()).getContextPath() + "/egov_security_login";
    }

    @Override
    public String getRequestURI() {
        return ((HttpServletRequest) super.getRequest()).getContextPath() + "/egov_security_login";
    }

    @Override
    public String getParameter(String name) {
        if ("egov_security_username".equals(name)) {
            return username;
        }
        if ("egov_security_password".equals(name)) {
            return password;
        }
        return super.getParameter(name);
    }
}
