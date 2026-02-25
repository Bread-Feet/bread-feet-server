package kr.co.breadfeetserver.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * 동기 HTTP 요청에 사용하는 RestClient Bean 설정.
 *
 * <p>RestTemplate은 Spring Framework 7.0에서 deprecated되었으며,
 * RestClient가 동기 HTTP 요청의 공식 대체제다.
 * WebClient와 동일한 fluent API를 제공하지만 동기 방식으로 동작한다.
 */
@Configuration
public class RestClientConfig {

    private static final int CONNECT_TIMEOUT_MS = 2_000;
    private static final int READ_TIMEOUT_MS    = 3_000;

    @Bean
    public RestClient restClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT_MS);
        factory.setReadTimeout(READ_TIMEOUT_MS);

        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }
}
