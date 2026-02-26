package kr.co.breadfeetserver.global.auth;

import java.util.stream.Stream;

public class PublicPaths {

    public static final String[] AUTH = {
            "/login/**",
            "/oauth/**"
    };

    public static final String[] PUBLIC_API = {
            "/api/v1/bakeries",
            "/api/v1/bakeries/*",
            "/api/v1/bakeries/recommendations/*",
            "/api/v1/bakery/*/review",
            "/api/v1/diaries"
    };

    public static final String[] SWAGGER = {
            "/swagger",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/api-docs/json/**",
            "/v3/api-docs/**",
            "/webjars/**"
    };

    public static final String[] STATIC = {
            "/css/**",
            "/js/**",
            "/favicon.ico",
            "/error"
    };

    public static String[] all() {
        return Stream.of(AUTH, PUBLIC_API, SWAGGER, STATIC)
                .flatMap(Stream::of)
                .toArray(String[]::new);
    }

    private PublicPaths() {
    }
}
