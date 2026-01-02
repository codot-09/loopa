package com.example.loopa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI loopaOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                )
                .info(new Info()
                        .title("LOOPA API")
                        .description("LOOPA Web App & Telegram Media API documentation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Otabek Nabiyev")
                                .email("otabek@example.com")
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")
                        )
                );
    }
}
