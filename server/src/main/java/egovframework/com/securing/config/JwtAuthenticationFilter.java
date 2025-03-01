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

/**
 * JWT 인증 필터
 * - 매 요청마다 실행되는 필터
 * - HTTP 요청의 Authorization 헤더에서 JWT를 추출하여 검증 및 사용자 인증 처리
 * - 유효한 JWT일 경우 SecurityContext에 인증 정보 저장
 * - 인증 정보가 있으면 해당 요청은 인증된 사용자로 동작
 */
@RequiredArgsConstructor // lombok - 의존성 주입을 위한 생성자 자동 생성
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT 처리 유틸 클래스
     * - 토큰에서 사용자 정보 추출
     * - 토큰 유효성 검사
     */
    private final JwtUtil jwtUtil;

    /**
     * 사용자 정보 조회 서비스
     * - 토큰의 사용자 정보를 기반으로 DB에서 사용자 상세 정보 조회
     */
    private final UserDetailsService userDetailsService;

    /**
     * 필터 실행 메서드 (요청마다 호출)
     * - 요청에서 Authorization 헤더 확인
     * - Bearer 토큰이 있으면 유효성 검사 및 사용자 정보 조회
     * - 인증 성공 시 SecurityContext에 인증 정보 저장
     * - 인증 실패 및 예외 상황 시 적절한 응답 반환
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            // 1. 요청 헤더에서 Authorization 추출
            String authHeader = request.getHeader("Authorization");

            // 2. Authorization 헤더가 없거나 Bearer 타입이 아니면 다음 필터로 넘김 (비인증 요청 허용)
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                chain.doFilter(request, response);
                return;
            }

            // 3. "Bearer " 다음의 실제 토큰 값 추출
            String token = authHeader.substring(7);

            // 4. 토큰에서 사용자명(아이디) 추출 (이 과정에서 토큰 유효성 검사 일부 수행)
            String username = jwtUtil.extractUsername(token);

            // 5. 이미 인증된 상태가 아니고, 토큰에서 사용자명을 정상적으로 추출한 경우만 처리
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 6. DB에서 사용자 상세 정보 조회
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 7. 토큰의 유효성 최종 확인
                if (jwtUtil.validateToken(token)) {
                    // 8. 인증 객체 생성 및 SecurityContext에 저장
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 9. 세션에 사용자 정보 저장 (선택적, 필요할 때만)
                    HttpSession session = request.getSession(true);
                    if (userDetails instanceof CustomerUser) {
                        CustomerUser customUser = (CustomerUser) userDetails;
                        session.setAttribute("loginUser", customUser.getUserDTO());
                    }
                }
            }

            // 10. 다음 필터로 요청 전달 (정상 처리 완료)
            chain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // ✅ 토큰 만료 예외 → 401 반환
            handleJwtException(response, "세션이 만료되었습니다. 다시 로그인해주세요.", HttpServletResponse.SC_UNAUTHORIZED);

        } catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.SignatureException e) {
            // ✅ 잘못된 토큰 형식 또는 서명 불일치 → 401 반환
            handleJwtException(response, "유효하지 않은 인증 정보입니다.", HttpServletResponse.SC_UNAUTHORIZED);

        } catch (Exception e) {
            // ✅ 기타 예상치 못한 서버 에러 → 500 반환
            e.printStackTrace();
            handleJwtException(response, "서버 내부 오류가 발생했습니다.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * JWT 관련 예외 응답 공통 처리 메서드
     * - 응답 상태 코드 설정
     * - JSON 형식으로 메시지 반환
     */
    private void handleJwtException(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }
}