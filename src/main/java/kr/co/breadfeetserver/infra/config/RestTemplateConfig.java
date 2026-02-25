package kr.co.breadfeetserver.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 외부 HTTP API 호출에 사용하는 RestTemplate Bean 설정.
 *
 * <p>KakaoService 처럼 호출마다 new RestTemplate()을 생성하면
 * 커넥션 풀을 재사용하지 못하므로, Bean으로 등록해 싱글턴으로 공유한다.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
