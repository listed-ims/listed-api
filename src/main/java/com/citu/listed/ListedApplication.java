package com.citu.listed;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				description = "Open API documentation for Listed inventory management system.",
				title = "Listed IMS - API"
		),
		servers = {
				@Server(
						description = "Local ENV",
						url = "http://localhost:8080"
				)
		},
		security = {
				@SecurityRequirement(name = "Authorization")
		}
)
@SecurityScheme(
		name = "Authorization",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
)
@SpringBootApplication
public class ListedApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListedApplication.class, args);
	}

}
