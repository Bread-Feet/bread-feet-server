package kr.co.breadfeetserver.infra.config;

import kr.co.breadfeetserver.global.interceptor.JwtInterceptor;
import kr.co.breadfeetserver.global.resolver.MemberIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MemberIdArgumentResolver memberIdArgumentResolver;
    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberIdArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login/**",
                        "/oauth/**",
                        "/css/**", "/js/**",
                        "/favicon.ico",
                        "/error",
                        "/swagger",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/api-docs/json/**",
                        "/v3/api-docs/**",
                        "/webjars/**"
                );
    }
}
