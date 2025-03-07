package egovframework.com.sec.security.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
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
import egovframework.com.utl.sim.service.EgovClntInfo;

/**
 *
 * @author 공통서비스 개발팀 서준식
 * @since 2011. 8. 29.
 * @version 1.0
 * @see
 *
 *      <pre>
 * 개정이력(Modification Information)
 *
 *  수정일               수정자        	 수정내용
 *  ----------   --------   ---------------------------
 *  2011.08.29   서준식            최초생성
 *  2011.12.12   유지보수          사용자 로그인 정보 간섭 가능성 문제(멤버 변수 EgovUserDetails userDetails를 로컬변수로 변경)
 *  2014.03.07   유지보수          로그인된 상태에서 다시 로그인 시 미처리 되는 문제 수정 (로그인 처리 URL 파라미터화)
 *  2017.03.03   조성원 	       시큐어코딩(ES)-부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
 *  2017.07.10   장동한            실행환경 v3.7(Spring Security 4.0.3 적용)
 *  2017.07.21   장동한 		   로그인인증제한 작업
 *  2020.06.25   신용호 		   로그인 메시지 처리 수정
 *
 *      </pre>
 */

public class EgovSpringSecurityLoginFilter implements Filter {

	// Filter 인터페이스는 **서블릿 컨테이너(Tomcat, Jetty 등)**에서 제공하는 기능으로,
	// 클라이언트의 요청이 들어오면 서블릿(또는 Spring Controller)으로 전달되기 전에 요청을 가로채어 원하는 작업을 수행
	private FilterConfig config; // 필터 설정 정보를 저장하는 변수

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSpringSecurityLoginFilter.class); // 로깅을 위한 Logger

	// 필터 종료 시 호출되는 메서드, 현재 별도 구현 없음
	public void destroy() {
	}

	// 필터의 핵심 로직을 수행하는 메서드
	// ServletRequest와 ServletResponse를 필터링하여 추가적인 인증 로직을 수행함
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		LOGGER.info("EgovSpringSecurityLoginFilter called..."); // 필터가 실행될 때 로그 출력

		// 로그인 URL을 가져와 개행 문자 제거
		String loginURL = config.getInitParameter("loginURL");
		loginURL = loginURL.replaceAll("\r", "").replaceAll("\n", "");

		// 로그인 처리 URL을 가져와 개행 문자 제거
		String loginProcessURL = config.getInitParameter("loginProcessURL");
		loginProcessURL = loginProcessURL.replaceAll("\r", "").replaceAll("\n", "");

		// Spring ApplicationContext에서 필요한 Bean을 가져옴
		ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
		EgovLoginService loginService = (EgovLoginService) act.getBean("loginService"); // 로그인 서비스 객체
		EgovLoginConfig egovLoginConfig = (EgovLoginConfig) act.getBean("egovLoginConfig"); // 로그인 설정 객체
		EgovMessageSource egovMessageSource = (EgovMessageSource) act.getBean("egovMessageSource"); // 메시지 소스 객체

		HttpServletRequest httpRequest = (HttpServletRequest) request; // 요청 객체를 HttpServletRequest로 변환
		HttpServletResponse httpResponse = (HttpServletResponse) response; // 응답 객체를 HttpServletResponse로 변환
		HttpSession session = httpRequest.getSession(); // 세션 가져오기
		String isRemotelyAuthenticated = (String) session.getAttribute("isRemotelyAuthenticated"); // 원격 인증 여부 확인

		String requestURL = ((HttpServletRequest) request).getRequestURI(); // 요청된 URL 가져오기

		// 스프링 시큐리티를 통해 사용자 인증 여부 확인
		if (EgovUserDetailsHelper.getAuthenticatedUser() == null || requestURL.contains(loginProcessURL)) {

			if (isRemotelyAuthenticated != null && isRemotelyAuthenticated.equals("true")) {
				try {
					// 세션에서 사용자 정보를 가져와 DB 인증 수행
					LoginVO loginVO = (LoginVO) session.getAttribute("loginVOForDBAuthentication");
					loginVO = loginService.actionLoginByEsntlId(loginVO);
					
					if (loginVO != null && loginVO.getId() != null && !loginVO.getId().equals("")) {
						// 클라이언트 IP 설정
						String userIp = EgovClntInfo.getClntIP(httpRequest);
						loginVO.setIp(userIp);

						// 세션에 로그인 정보 저장
						session.setAttribute("loginVO", loginVO);
						
						//로컬 인증결과 세션에 저장
						session.setAttribute("isLocallyAuthenticated", "true");
						
						// 스프링 시큐리티 로그인 처리
						UsernamePasswordAuthenticationFilter springSecurity = null;

						Map<String, UsernamePasswordAuthenticationFilter> beans = act.getBeansOfType(UsernamePasswordAuthenticationFilter.class);
						if (beans.size() > 0) {
							springSecurity = (UsernamePasswordAuthenticationFilter) beans.values().toArray()[0];
							springSecurity.setUsernameParameter("egov_security_username");
							springSecurity.setPasswordParameter("egov_security_password");
							springSecurity.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(httpRequest.getContextPath() +"/egov_security_login", "POST"));
						} else {
							LOGGER.error("No AuthenticationProcessingFilter");
							throw new IllegalStateException("No AuthenticationProcessingFilter");
						}

						LOGGER.debug("before security filter call....");
						springSecurity.doFilter(new RequestWrapperForSecurity(httpRequest, loginVO.getUserSe() + loginVO.getId(), loginVO.getUniqId()), httpResponse, chain);
						LOGGER.debug("after security filter call....");
					}
					//2017.03.03 	조성원 	시큐어코딩(ES)-부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
				} catch(IllegalArgumentException e) {
					LOGGER.error("[IllegalArgumentException] Try/Catch...usingParameters Runing : "+ e.getMessage());
				} catch(Exception e) {
					LOGGER.error("["+e.getClass()+"] Try/Catch...Exception : " + e.getMessage());
				}
				
			} else if (isRemotelyAuthenticated == null) {
				if (requestURL.contains(loginProcessURL)) {

					String password = httpRequest.getParameter("password");
					String id = httpRequest.getParameter("id");
					
					// 보안점검 후속 조치(Password 검증)
					if ((id == null || "".equals(id)) && (password == null || "".equals(password))) {
						RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(loginURL);
						httpRequest.setAttribute("loginMessage", "");
						dispatcher.forward(httpRequest, httpResponse);
						//chain.doFilter(request, response);
						return;
					}
					else if (password == null || password.equals("") || password.length() < 1 || password.length() > 20) {
						httpRequest.setAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login.password",request.getLocale()));
						RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(loginURL);
						
						dispatcher.forward(httpRequest, httpResponse);
						//chain.doFilter(request, response);
						return;
					}

					LoginVO loginVO = new LoginVO();

					loginVO.setId(httpRequest.getParameter("id"));
					loginVO.setPassword(password);
					loginVO.setUserSe("GNR");
					
					//------------------------------------------------------------------
				    // 로그인시 로그인인증제한 활성화 처리
				    //------------------------------------------------------------------
				    if(egovLoginConfig.isLock()){
				        try{
				             Map<?,?> mapLockUserInfo = (EgovMap)loginService.selectLoginIncorrect(loginVO);
				             
				             if(mapLockUserInfo != null){
				                //로그인인증제한 처리
				                String sLoginIncorrectCode = loginService.processLoginIncorrect(loginVO, mapLockUserInfo);
				                if(!sLoginIncorrectCode.equals("E")){
				                    if(sLoginIncorrectCode.equals("L")){
				                        request.setAttribute("loginMessage", egovMessageSource.getMessageArgs("fail.common.loginIncorrect", new Object[] {egovLoginConfig.getLockCount(),request.getLocale()}));
				                    }else if(sLoginIncorrectCode.equals("C")){
				                        request.setAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login",request.getLocale()));
				                    }
				                    httpRequest.getRequestDispatcher(loginURL).forward(request, response);
				                    return;
				                }
				            }else{
				                request.setAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login",request.getLocale()));
				                httpRequest.getRequestDispatcher(loginURL).forward(request, response);
				                return;
				            }
				        } catch(IllegalArgumentException e) {
				            LOGGER.error("[IllegalArgumentException] : "+ e.getMessage());
				        } catch(Exception ex) {
							LOGGER.error("Login Exception : {}", ex.getCause(), ex);
							httpRequest.setAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login",request.getLocale()));
							RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(loginURL);
							dispatcher.forward(httpRequest, httpResponse);
				        }
				    }

					//------------------------------------------------------------------
				    // 사용자 로그인 처리
				    //------------------------------------------------------------------
					try {
						//사용자 입력 id, password로 DB 인증을 실행함
						loginVO = loginService.actionLogin(loginVO);
					
						//사용자 IP 기록
						String userIp = EgovClntInfo.getClntIP(httpRequest);
                        loginVO.setIp(userIp);
						if (loginVO != null && loginVO.getId() != null && !loginVO.getId().equals("")) {
							//세션 로그인
							session.setAttribute("loginVO", loginVO);

							//로컬 인증결과 세션에 저장
							session.setAttribute("isLocallyAuthenticated", "true");

							//스프링 시큐리티 로그인
							//httpResponse.sendRedirect(httpRequest.getContextPath() + "/j_spring_security_check?j_username=" + loginVO.getUserSe() + loginVO.getId() + "&j_password=" + loginVO.getUniqId());

							UsernamePasswordAuthenticationFilter springSecurity = null;

							Map<String, UsernamePasswordAuthenticationFilter> beans = act.getBeansOfType(UsernamePasswordAuthenticationFilter.class);
							if (beans.size() > 0) {
								springSecurity = (UsernamePasswordAuthenticationFilter) beans.values().toArray()[0];
								springSecurity.setUsernameParameter("egov_security_username");
								springSecurity.setPasswordParameter("egov_security_password");
								springSecurity.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(request.getServletContext().getContextPath() +"/egov_security_login", "POST"));
							} else {
								LOGGER.error("No AuthenticationProcessingFilter");
								throw new IllegalStateException("No AuthenticationProcessingFilter");
							}
							//springSecurity.setContinueChainBeforeSuccessfulAuthentication(false);	// false 이면 chain 처리 되지 않음.. (filter가 아닌 경우 false로...)

							LOGGER.debug("before security filter call....");
							springSecurity.doFilter(new RequestWrapperForSecurity(httpRequest, loginVO.getId(), loginVO.getPassword()), httpResponse, chain);
							LOGGER.debug("after security filter call....");

						//	RequestDispatcher dispatcher = httpRequest.getRequestDispatcher("/loginSuccesss.do");
						//	dispatcher.forward(httpRequest, httpResponse);
							
						} else {
							//사용자 정보가 없는 경우 로그인 화면으로 redirect 시킴
							httpRequest.setAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login",request.getLocale()));
							RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(loginURL);
							dispatcher.forward(httpRequest, httpResponse);
							
							//chain.doFilter(request, response);

							return;

						}
						
						
			        } catch(IllegalArgumentException e) {
			            LOGGER.error("[IllegalArgumentException] : "+ e.getMessage());
					} catch (Exception ex) {
						//DB인증 예외가 발생할 경우 로그인 화면으로 redirect 시킴
						LOGGER.error("Login Exception : {}", ex.getCause(), ex);
						httpRequest.setAttribute("loginMessage", egovMessageSource.getMessage("fail.common.login",request.getLocale()));
						RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(loginURL);
						dispatcher.forward(httpRequest, httpResponse);
						//chain.doFilter(request, response);

						return;

					}
					return;
				}

			}
		}

		chain.doFilter(request, response); // 필터 체인을 계속 실행
	}

	// 필터 초기화 메서드, 필터 설정을 초기화함
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;
	}
}

// Spring Security 인증을 위한 요청 래퍼 클래스
class RequestWrapperForSecurity extends HttpServletRequestWrapper {
	private String username = null; // 사용자 이름
	private String password = null; // 비밀번호

	// 생성자, 요청 객체와 사용자 정보를 받아 초기화
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
		if (name.equals("egov_security_username")) {
			return username;
		}
		if (name.equals("egov_security_password")) {
			return password;
		}
		return super.getParameter(name);
	}
}
