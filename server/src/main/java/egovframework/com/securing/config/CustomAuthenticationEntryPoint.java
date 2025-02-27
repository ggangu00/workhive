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
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

// 로그인 안되었을 때 로그인 페이지로 돌리기
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		// JSON 응답 데이터 생성
		Map<String, String> responseData = new HashMap<>();
		responseData.put("message", "인증이 필요합니다. 로그인 후 이용하세요.");

		// HTTP 401 상태 코드 설정
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 Unauthorized
		response.setCharacterEncoding("UTF-8");

		// JSON 변환 후 응답 전송
		response.getWriter().write(objectMapper.writeValueAsString(responseData));
	}
}
