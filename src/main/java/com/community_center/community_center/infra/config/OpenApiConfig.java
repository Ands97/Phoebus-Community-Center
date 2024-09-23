package com.community_center.community_center.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Community Center Api")
                        .version("1.0")
                        .description("Api de coleta de informações de centros comunitários de ajuda do Brasil."));
    }
}
