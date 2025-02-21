package egovframework.com.cmm.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;


public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "로그인 성공");
        responseData.put("redirectUrl", "/home"); // 성공 후 이동할 URL

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
		
	}

}
