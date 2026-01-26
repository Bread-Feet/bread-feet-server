package kr.co.breadfeetserver.infra.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 1. 비밀키: 토큰을 위조하지 못하게 잠그는 열쇠입니다. (실무에선 application.yml에 숨겨야 함)
    // 32글자 이상이어야 안전합니다.
    private static final String SECRET_KEY = "breadfeet_secret_key_breadfeet_secret_key";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 2. 토큰 유효시간 (예: 24시간 = 1000 * 60 * 60 * 24)
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24L;

    // 3. 토큰 생성 메서드
    public String createToken(Long memberId, String username) {
        return Jwts.builder()
                .setSubject(String.valueOf(memberId)) // 토큰 주인(ID)
                .claim("username", username)          // 추가 정보(이름)
                .setIssuedAt(new Date())              // 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
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
