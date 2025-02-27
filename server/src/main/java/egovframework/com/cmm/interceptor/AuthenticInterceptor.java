package egovframework.com.cmm.interceptor;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import egovframework.com.cmm.util.EgovUserDetailsHelper;

/**
 * 인증여부 체크 인터셉터
 * Spring의 인터셉터(Interceptor) 를 사용하여 사용자의 인증 여부를 확인하는 기능을 수행하는 클래스야.
 * 이 AuthenticInterceptor 클래스는 요청(Request)이 컨트롤러로 전달되기 전에 실행되며, 사용자가 로그인했는지, 관리자 권한이 있는지 등을 체크하는 역할을 해.
 * 여기서 권한이 있는지 없는지 체크를해서 vue단으로 json형태로 넘겨주면 앞단에서 이것을 처리함
 */


public class AuthenticInterceptor implements HandlerInterceptor {



}
