package kr.co.breadfeetserver.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.breadfeetserver.infra.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        if(request.getMethod().equals("OPTIONS")){
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        // 3. 토큰이 없거나 "Bearer "로 시작하지 않으면 탈락
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 에러
            response.getWriter().write("로그인이 필요합니다.");
            return false; // 컨트롤러로 못 가게 막음
        }
        // 4. "Bearer " 떼고 순수 토큰만 추출
        String token = authHeader.substring(7);

        // 5. 토큰 검사
        try {
            Long memberId = jwtUtil.getMemberId(token);
            // 검증 성공! 요청 객체에 'memberId'를 붙여서 컨트롤러로 보냄
            request.setAttribute("loginMemberId", memberId);
            return true;
        } catch (RuntimeException e) {
            // 검증 실패
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("토큰이 유효하지 않습니다.");
            return false;
        }
    }
}
