package kr.co.breadfeetserver.infra.config;

import com.contech.tugoserver.global.filter.AuthWhitelistFilter;
import com.contech.tugoserver.global.filter.MemberIdFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final MemberIdFilter memberIdFilter;
    private final AuthWhitelistFilter authWhitelistFilter;

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsFilter(corsConfigurationSource));
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<MemberIdFilter> memberIdFilterRegistration() {
        FilterRegistrationBean<MemberIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(memberIdFilter);
        registrationBean.setOrder(2);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthWhitelistFilter> authWhitelistFilterRegistration() {
        FilterRegistrationBean<AuthWhitelistFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authWhitelistFilter);
        registrationBean.setOrder(3);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
