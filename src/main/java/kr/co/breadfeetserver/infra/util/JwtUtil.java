package kr.co.breadfeetserver.infra.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key;
    private final long expirationTime;

    public JwtUtil(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiration-time}") long expirationTime
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expirationTime = expirationTime;
    }

    // 3. 토큰 생성 메서드
    public String createToken(Long memberId, String username) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId)) // 토큰 주인(ID)
                .claim("username", username)          // 추가 정보(이름)
                .setIssuedAt(new Date())              // 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 비밀키로 서명
                .compact();
    }

    public Long getMemberId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            // 토큰이 위조되었거나 만료되었으면 예외 발생
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }
    // [추가] 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
