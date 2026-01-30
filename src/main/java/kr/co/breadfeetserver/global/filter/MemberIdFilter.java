package kr.co.breadfeetserver.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.breadfeetserver.infra.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberIdFilter extends OncePerRequestFilter {

    public static final String MEMBER_ID_ATTRIBUTE_KEY = "memberId";
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        log.info("[MemberIdFilter] URI: {}", request.getRequestURI());

        if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
            Long memberId = jwtUtil.getMemberId(token);
            request.setAttribute(MEMBER_ID_ATTRIBUTE_KEY, memberId);
            log.info("[MemberIdFilter] Authenticated memberId: {}", memberId);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
