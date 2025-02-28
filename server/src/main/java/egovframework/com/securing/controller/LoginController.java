package egovframework.com.securing.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import egovframework.com.securing.service.CustomUserDetailService;
import egovframework.com.securing.service.CustomerUser;
import egovframework.com.securing.service.LoginRequest;
import egovframework.com.securing.service.UserDTO;
import egovframework.com.securing.util.JwtUtil;

@RestController // json
public class LoginController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final CustomUserDetailService userService;

	public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
			CustomUserDetailService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}

	@PostMapping("/loginproc")
	public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
	    try {
	        System.out.println("request.getUsername() => " + loginRequest.getUsername());

	        // 1. 사용자 인증 시도
	        Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
	        );

	        // 2. 인증 성공 시 SecurityContext에 저장
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        // 3. JWT 토큰 생성
	        String token = jwtUtil.generateToken(loginRequest.getUsername());

	        // 4. 사용자 정보 조회 (DB에서 상세 정보 가져오기)
	        CustomerUser customerUser = (CustomerUser) authentication.getPrincipal(); // 인증된 사용자 정보
	        UserDTO userDTO = customerUser.getUserDTO();

	        // 5. 세션에 사용자 정보 저장 (선택적 - eGovFrame 호환용)
	        request.getSession().setAttribute("loginUser", userDTO);

	        // 6. 응답 데이터 구성
	        Map<String, Object> response = new HashMap<>();
	        response.put("token", token);  // 프론트에서 저장할 토큰
	        response.put("user", userDTO);  // 프론트에서 사용할 유저 정보
	        response.put("result", "success");  // 로그인 성공 상태

	        // 디버깅 로그 (현재 로그인 유저 정보 확인)
	        System.out.println(userDTO);

	        // 7. 응답 반환 (프론트로 토큰, 유저 정보, 결과 전송)
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
	    }
	}
}
