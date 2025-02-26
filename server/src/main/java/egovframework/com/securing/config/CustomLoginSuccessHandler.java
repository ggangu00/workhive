package egovframework.com.securing.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import egovframework.com.securing.service.CustomerUser;
import egovframework.com.securing.service.UserDTO;


public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, 
			                            HttpServletResponse response, 
			                            Authentication auth) throws IOException, ServletException {

		// 단건조회
		UserDTO dto = ((CustomerUser)auth.getPrincipal()).getUserDTO();
		
		// session
		request.getSession().setAttribute("userid", dto.getId());
		request.getSession().setAttribute("deptName", dto.getDeptName());
		
		// username, role
		List<String> roleNames = new ArrayList<>();

		// role정보들, return값은 배열이다
		auth.getAuthorities().forEach(authority -> {
			roleNames.add(authority.getAuthority());
		});

		System.out.println("roleName => " + roleNames);

		if (roleNames.contains("ROLE_ADMIN")) {
			response.sendRedirect("/emp/list");
			return;
		} else if (roleNames.contains("ROLE_USER")) {
			response.sendRedirect("/board/list");
			return;
		}
		response.sendRedirect("/");
	}

}
