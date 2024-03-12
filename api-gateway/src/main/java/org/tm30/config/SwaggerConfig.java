/* RICHARDS AND FAVOUR (C)2024 */
package org.tm30.config;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
public class SwaggerConfig {

	@Bean
	public CommandLineRunner openApiGroups(RouteDefinitionLocator locator, SwaggerUiConfigParameters swaggerUiParameters) {
		return args -> {
			List<RouteDefinition> routeDefinitions = Objects.requireNonNull(locator
					.getRouteDefinitions().collectList().block());

			log.info("RouteDefinitions: {}", routeDefinitions);
			log.info("RouteDefinitions Size: {}", routeDefinitions.size());

			routeDefinitions.stream()
					.map(RouteDefinition::getId)
					.filter(id -> id.matches(".*-service"))
					.map(id -> id.replace("-service", ""))
					.forEach(swaggerUiParameters::addGroup);
		};

	}
}
