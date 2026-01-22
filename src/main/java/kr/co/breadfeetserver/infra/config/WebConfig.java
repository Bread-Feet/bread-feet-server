package kr.co.breadfeetserver.infra.config;

import kr.co.breadfeetserver.global.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 모든 주소 검사!
                .excludePathPatterns(   // 단, 아래 주소들은 프리패스
                        "/login/**",           // 카카오 로그인 관련
                        "/oauth/**",           // 토큰 발급 관련
                        "/css/**", "/js/**",   // 정적 리소스
                        "/favicon.ico",
                        "/error"
                );
    }
}
