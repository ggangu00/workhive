package egovframework.com.securing.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import egovframework.com.common.service.CommonDTO;
import egovframework.com.common.service.CommonService;
import egovframework.com.member.service.MemberService;
import egovframework.com.securing.service.CustomerUser;
import egovframework.com.securing.service.LoginRequest;
import egovframework.com.securing.service.UserDTO;
import egovframework.com.securing.service.impl.CustomUserDetailService;
import egovframework.com.securing.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 요청을 처리하는 컨트롤러
 * - ID/PW 기반 인증
 * - 인증 성공 시 JWT 발급
 * - 사용자 정보 반환
 */
@Slf4j
@RestController 
public class LoginController {
    
	private final AuthenticationManager authenticationManager; // Spring Security 인증 매니저
    private final JwtUtil jwtUtil;                             // JWT 유틸리티 (토큰 발급/검증)
    private final CustomUserDetailService userService;         // 사용자 정보 조회 서비스 (UserDetailsService 구현체)
    private final CommonService commService;                   // 공통 서비스 (로그 저장)
    private final MemberService memberService;                 // 회원 서비스 (회원정보 조회 등)
    
    // 생성자 - 필요한 서비스 및 컴포넌트 주입
    public LoginController(AuthenticationManager authenticationManager, 
    						JwtUtil jwtUtil,
                            CustomUserDetailService userService,
                            CommonService commService,
                            MemberService memberService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.commService = commService;
        this.memberService = memberService;
    }

    /**
     * 로그인 처리
     * - 아이디/비번 확인
     * - 계정 잠금 확인
     * - 인증 성공 시 JWT 발급 및 사용자 정보 반환
     * - 로그인 실패 시 실패 로그 및 실패 카운트 증가
     */
    @PostMapping("/loginProc")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("로그인 요청 - username: {}, password: {}", loginRequest.getUsername(), loginRequest.getPassword());

        // 1. 회원 정보 조회
        //CustomerUser user = (CustomerUser) userService.loadUserByUsername(loginRequest.getUsername());
        
        // 1. 회원 정보 조회 (Security 인증과 동일한 방식으로 조회)
        Map<String, Object> response = new HashMap<>();
        
        CustomerUser requestUser = null;
        
        try {
            requestUser = (CustomerUser) userService.loadUserByUsername(loginRequest.getUsername());
		} catch (UsernameNotFoundException e) {
			loginLogInsert(loginRequest.getUsername(), getClientIp(), "A02");
            log.warn("❌ 로그인 실패 - 존재하지 않는 아이디");

            response.put("code", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "아이디 또는 비밀번호를 확인하세요.");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
        
        UserDTO user = requestUser.getUserDTO(); // 실제 사용자 정보 추출
        
        // 3. 계정잠금 확인
//        if ("A01".equals(user.getLockAt())) {
//            loginLogInsert(user.getMberId(), getClientIp(), "A02");
//            log.warn("❌ 로그인 실패 - 계정 잠금");
//            
//            response.put("code", HttpStatus.LOCKED.value());
//            response.put("message", "계정이 잠겼습니다. 관리자에게 문의하세요.");
//
//            return ResponseEntity.status(HttpStatus.LOCKED).body(response);
//        }

        try {
        	// 4. 인증 시도 (Spring Security 인증 매니저)
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            log.info("인증 성공 - authentication: {}", authentication);
            
            // 5. 인증 성공 시 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 6. 인증된 사용자 정보 추출
            CustomerUser customerUser = (CustomerUser) authentication.getPrincipal();
            UserDTO userDTO = customerUser.getUserDTO();
            
            // 7. 로그인 성공 시 실패 카운트 초기화
            memberService.memberLoginFailCountUpdate(userDTO.getMberId(), 0);

            // 8. JWT 발급 및 로그인 성공 로그 저장
            String token = jwtUtil.generateToken(userDTO.getMberId());
            loginLogInsert(userDTO.getMberId(), getClientIp(), "A01");
            
            // 9. 응답 데이터 구성 및 반환
            response.put("token", token);
            response.put("user", userDTO);
            response.put("result", "success");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
        	// 10. 인증 실패 시 처리
            log.warn("❌ 인증 실패", e);

            int failCount = user.getLockCnt() + 1;
            memberService.memberLoginFailCountUpdate(user.getMberId(), failCount);
            loginLogInsert(user.getMberId(), getClientIp(), "A02");
            
            // 11. 5회 이상 실패 시 계정 잠금 처리
            if (failCount >= 5) {
            	memberService.memberLockUpdate(user.getMberId(), "A01");
            	response.put("code", HttpStatus.LOCKED.value());
                response.put("message", "계정이 잠겼습니다. 관리자에게 문의하세요.");

                return ResponseEntity.status(HttpStatus.LOCKED).body(response);
            }
            
            // 12. 일반적인 비번 불일치 처리
            log.warn("❌ 비밀번호 불일치 - 입력 비번: {}, DB 비번: {}", loginRequest.getPassword(), user.getPassword());

            response.put("code", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "아이디 또는 비밀번호를 확인하세요");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * 로그인 시도 로그 저장
     * @param userId 사용자 아이디
     * @param ip 접속 IP
     * @param state 상태코드 (A01:성공, A02:실패)
     */
    private void loginLogInsert(String userId, String ip, String state) {
        CommonDTO logDto = new CommonDTO();
        logDto.setLogId(userId);
        logDto.setLogIp(ip);
        logDto.setState(state);

        commService.loginLogInsert(logDto);
    }

    /**
     * 요청한 클라이언트 IP 조회
     * @return 클라이언트 IP
     */
    private String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}