package com.bts.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI jwtOpenApi() {
    final String schemeName = "bearerAuth";

    SecurityScheme bearerScheme = new SecurityScheme()
        .name(schemeName)
        .type(SecurityScheme.Type.HTTP) // HTTP authentication (as opposed to API key or OAuth2)
        .scheme("bearer") // the HTTP auth scheme — tells Swagger UI to send "Bearer <token>"
        .bearerFormat("JWT"); // hint to the UI and documentation generators

    SecurityRequirement globalSecurityRequirement = new SecurityRequirement().addList(schemeName);

    return new OpenAPI()
        .info(new Info()
            .title("Spring Auth API")
            .version("1.0.0")
            .description("""
                    API Description
                """))
        .components(new Components()
            .addSecuritySchemes(schemeName, bearerScheme))
        .addSecurityItem(globalSecurityRequirement);
  }
}
