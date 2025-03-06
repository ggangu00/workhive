package egovframework.com.securing.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomLoginFailedHandler implements AuthenticationFailureHandler  {

	private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환 객체
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {


		// 응답 데이터를 Map 형태로 생성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("result", "failed");
        
        // JSON 응답 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // HTTP 401 Unauthorized
        response.setCharacterEncoding("UTF-8");

        // JSON 변환 후 응답 전송
        response.getWriter().write(objectMapper.writeValueAsString(responseData));

        // 🔹 리다이렉트 처리 (클라이언트가 JSON 응답을 받도록 302 응답)
       //  response.sendRedirect("/login?error=true");

	}

}
