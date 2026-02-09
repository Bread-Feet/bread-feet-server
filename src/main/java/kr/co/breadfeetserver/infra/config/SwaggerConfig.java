package kr.co.breadfeetserver.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private final List<Server> servers = List.of(
            new Server().url("https://api.breadfeet.co.kr/api/v1")
                    .description("BREAD FEET SERVER"),
            new Server().url("http://localhost:8080/api/v1")
                    .description("로컬 환경")
    );

    @Bean
    public SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }

    @Bean
    public SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList("BearerAuth");
    }

    @Bean
    public OpenAPI customOpenAPI(SecurityScheme securityScheme, SecurityRequirement securityRequirement) {
        return new OpenAPI()
                .info(new Info()
                        .title("Bread Feet API 문서")
                        .version("1.0.0")
                ).servers(servers)
                .addSecurityItem(securityRequirement)
                .schemaRequirement("BearerAuth", securityScheme);
    }
}
