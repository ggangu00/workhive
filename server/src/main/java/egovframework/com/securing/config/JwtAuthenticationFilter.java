package egovframework.com.securing.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import egovframework.com.securing.service.CustomerUser;
import egovframework.com.securing.util.JwtUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // Lombok을 이용해 생성자 주입 자동 생성
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT 유틸 클래스
     * - 토큰에서 정보 추출 및 유효성 검증을 담당
     */
    private final JwtUtil jwtUtil;

    /**
     * 사용자 정보를 조회하는 서비스
     * - UserDetailsService를 통해 DB에서 사용자 정보 조회
     */
    private final UserDetailsService userDetailsService;

    /**
     * 요청마다 실행되는 필터
     * - 요청의 Authorization 헤더에서 JWT 추출
     * - JWT에서 사용자 정보를 추출하여 SecurityContext에 저장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        // 1. Authorization 헤더 확인
        String authHeader = request.getHeader("Authorization");

        // 2. Authorization 헤더가 없거나 Bearer로 시작하지 않으면 다음 필터로 넘김
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Bearer 다음의 토큰 값 추출
        String token = authHeader.substring(7);

        // 4. 토큰에서 사용자명(아이디) 추출
        String username = jwtUtil.extractUsername(token);

        // 5. 이미 인증된 상태가 아니라면 인증 처리 시도
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // 6. DB에서 사용자 정보 조회
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 7. 토큰 유효성 검증 성공 시
            if (jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // ✅ 8. 세션에 사용자 정보 저장 (필요한 경우만 저장)
                HttpSession session = request.getSession(true);
                if (userDetails instanceof CustomerUser) {
                	CustomerUser customUser = (CustomerUser) userDetails;
                    session.setAttribute("loginUser", customUser.getUserDTO());  // 세션에 UserDTO만 저장
                }
            }
        }

        // 9. 다음 필터로 넘김
        chain.doFilter(request, response);
    }

}