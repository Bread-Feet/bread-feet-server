package kr.co.breadfeetserver.infra.config;

import kr.co.breadfeetserver.global.filter.MemberIdFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final MemberIdFilter memberIdFilter;

    @Bean
    public FilterRegistrationBean<MemberIdFilter> memberIdFilterRegistration() {
        FilterRegistrationBean<MemberIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(memberIdFilter);
        registrationBean.setOrder(2);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
