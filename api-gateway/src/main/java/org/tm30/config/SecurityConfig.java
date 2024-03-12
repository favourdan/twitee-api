package org.tm30.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.tm30.filter.WebSecurityFilter;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final WebSecurityFilter authenticationFilter;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeExchange().anyExchange().permitAll()
                .and().addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION);
        return http.build();
    }

}
