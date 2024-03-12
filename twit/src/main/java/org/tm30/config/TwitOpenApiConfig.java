package org.tm30.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class TwitOpenApiConfig {

	@Value("${favourite.openapi.dev-url}")
	private String devUrl;

	@Value("${favourite.openapi.prod-url}")
	private String prodUrl;


	@Bean
	@Qualifier("twit-api")
	@Primary
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("twit")
				.packagesToScan("org.tm30.api")
				.build();
	}

	@Bean
	public OpenAPI myOpenAPI() {
		Server devServer = new Server();
		devServer.setUrl(devUrl);
		devServer.setDescription("Server URL in Development environment");

		Server prodServer = new Server();
		prodServer.setUrl(prodUrl);
		prodServer.setDescription("Server URL in Production environment");

		Contact contact = new Contact();
		contact.setEmail("favourdaniel74@gmail.com");
		contact.setName("Twit API");
		contact.setUrl("https://www.twitee.com");

		License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

		Info info = new Info().title("Twitee API").version("1.0").contact(contact)
				.description("This API exposes endpoints to manage tutorials.")
				.termsOfService("https://www.favourite-collections.com/terms").license(mitLicense);

		return new OpenAPI()
				.security(List.of(new SecurityRequirement().addList("Bearer Authentication"),
						new SecurityRequirement().addList("Basic Authentication")))
				.components(new Components().securitySchemes(createSecuritySchemesMap())).info(info)
				.servers(List.of(devServer, prodServer));
	}

	private Map<String, SecurityScheme> createSecuritySchemesMap() {

		SecurityScheme createAPIKeyScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT")
				.description("Enter a valid JWT.").scheme("bearer");

		SecurityScheme createBasicScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")
				.description("Input username and password");

		Map<String, SecurityScheme> securitySchemesMap = new HashMap<>();
		securitySchemesMap.put("Basic Authentication", createBasicScheme);
		securitySchemesMap.put("Bearer Authentication", createAPIKeyScheme);

		return securitySchemesMap;
	}
}
