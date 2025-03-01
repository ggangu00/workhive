package egovframework.com.securing.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	@Resource(name="propertiesService")
    private EgovPropertyService propService;

	private static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60 * 24; // 24시간

    private Key getSigningKey() {
        String secretKey = propService.getString("jwt.secret");
        log.info("🔑 현재 JWT secretKey = {}", propService.getString("jwt.secret"));

        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 생성
    public String generateToken(String username) {
        log.info("✅ 토큰 발급 시작 - username: {}, 유효시간: {}ms", username, ACCESS_TOKEN_VALIDITY);
        log.info("✅ 사용 중인 secretKey = {}", propService.getString("jwt.secret"));

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            log.info("✅ 토큰 검증 시작 - token: {}", token);
            log.info("✅ 사용 중인 secretKey = {}", propService.getString("jwt.secret"));

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            log.info("✅ 토큰 유효성 검사 통과");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("❌ 토큰 유효성 검사 실패: {}", e.getMessage());
            return false;
        }
    }

}
