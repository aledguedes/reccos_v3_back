package com.aledguedes.reccos_v3_back.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
	    return new OpenAPI()
	            .info(new Info().title("Reccos API")
	                    .version("1.0")
	                    .description("API para gerenciamento de usuários e federations"))
	            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
	            .components(new Components()
	                    .addSecuritySchemes("bearerAuth", new SecurityScheme()
	                            .type(SecurityScheme.Type.HTTP)
	                            .scheme("bearer")
	                            .bearerFormat("JWT")));
	}

}