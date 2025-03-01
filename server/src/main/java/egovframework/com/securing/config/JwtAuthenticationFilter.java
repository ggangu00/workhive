package egovframework.com.securing.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import egovframework.com.securing.service.CustomerUser;
import egovframework.com.securing.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            log.info("🔐 [JWT 필터] 요청 URL: {}", request.getRequestURI());
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("🚫 [JWT 필터] Authorization 헤더 없음 또는 Bearer 아님");
                chain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            log.info("🔑 [JWT 필터] 추출한 토큰: {}", token);

            String username = jwtUtil.extractUsername(token);
            log.info("🔑 [JWT 필터] 토큰에서 추출한 username: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 주의: 캐스팅 확정! CustomUserDetailService는 CustomerUser를 반환하도록 강제할 것
            	CustomerUser customerUser = (CustomerUser) userDetailsService.loadUserByUsername(username);
                log.info("✅ [JWT 필터] 유저 정보 조회 성공 - mberId: {}", customerUser.getUserDTO().getMberId());

                if (jwtUtil.validateToken(token)) {
                    log.info("✅ [JWT 필터] 토큰 유효성 검사 성공");
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(customerUser, null, customerUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("✅ [JWT 필터] SecurityContext에 인증 정보 저장 완료");
                } else {
                    log.warn("❌ [JWT 필터] 토큰 유효성 검사 실패");
                }
            }
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.warn("❌ [JWT 필터] 토큰 만료");
            handleJwtException(response, "토큰이 만료되었습니다. 다시 로그인해주세요.", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (MalformedJwtException | SignatureException e) {
            log.warn("❌ [JWT 필터] 토큰 형식 또는 서명 문제");
            handleJwtException(response, "유효하지 않은 인증 정보입니다.", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("❗ [JWT 필터] 예외 발생", e);
            handleJwtException(response, "서버 내부 오류가 발생했습니다.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleJwtException(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}
