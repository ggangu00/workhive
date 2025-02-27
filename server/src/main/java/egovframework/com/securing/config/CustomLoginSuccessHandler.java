package egovframework.com.securing.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.securing.service.CustomerUser;
import egovframework.com.securing.service.UserDTO;


public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환을 위한 ObjectMapper
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, 
			                            HttpServletResponse response, 
			                            Authentication auth) throws IOException, ServletException {

        // 로그인한 사용자 정보 가져오기
        UserDTO dto = ((CustomerUser) auth.getPrincipal()).getUserDTO();
		
		// username, role
        // 유저의 역할(Role) 리스트 생성
		List<String> roleNames = new ArrayList<>();
		// role정보들, return값은 배열이다
		auth.getAuthorities().forEach(authority -> {
			roleNames.add(authority.getAuthority());
		});
		
		// 응답 데이터를 Map 형태로 생성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("result", "success"); // 로그인 성공 메시지
        responseData.put("user", dto); // UserDTO 데이터를 포함
		
		// JSON 응답 설정
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value()); // HTTP 200 OK
        response.setCharacterEncoding("UTF-8");
        // json 응답으로 넘겨야함 !!
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
	}

}
