package kr.co.breadfeetserver.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class MemberIdFilter extends OncePerRequestFilter {

    public static final String MEMBER_ID_ATTRIBUTE_KEY = "memberId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        log.info("[MemberIdFilter] URI: {}, Session exists: {}",
                request.getRequestURI(), session != null);

        if (session != null) {
            Object memberId = session.getAttribute("memberId");
            log.info("[MemberIdFilter] Session ID: {}, memberId: {}, type: {}",
                    session.getId(),
                    memberId,
                    memberId != null ? memberId.getClass().getName() : "null");

            if (memberId instanceof Long) {
                request.setAttribute(MEMBER_ID_ATTRIBUTE_KEY, memberId);
            } else if (memberId instanceof Number) {
                request.setAttribute(MEMBER_ID_ATTRIBUTE_KEY, ((Number) memberId).longValue());
            }
        }

        filterChain.doFilter(request, response);
    }
}
