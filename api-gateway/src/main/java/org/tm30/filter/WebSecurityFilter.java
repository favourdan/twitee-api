package org.tm30.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.tm30.util.JwtUtil;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSecurityFilter implements WebFilter {
    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        boolean iSecured = routeValidator.isSecured.test(exchange.getRequest());
        //log.info("exchange.getRequest(): {}", exchange.getRequest().getURI().getPath());
        log.info("Boolean value exchange.getRequest(): {}", iSecured);

        if (BooleanUtils.isFalse(iSecured)) {
            log.info("Endpoint tightly secured for {}", exchange.getRequest().getURI());

            // header contains token or not
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("missing authorization header");
            }

            String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }

            String userEmail;
            try {
                userEmail = jwtUtil.extractUsername(authHeader);
                log.info("signature: {}", userEmail);
            } catch (Exception e) {
                System.out.println("invalid access...!   " + e.getMessage());
                throw new RuntimeException("unauthorized access to application");
            }

            ServerHttpRequest mutateRequest = exchange.getRequest().mutate()
                    .header("user-token", userEmail)
                    .build();
            ServerWebExchange mutateServerWebExchange = exchange.mutate().request(mutateRequest).build();
            return chain.filter(mutateServerWebExchange);

        } else {
            log.info("Endpoint not secured for {}", exchange.getRequest().getURI());
            return chain.filter(exchange);
        }

    }
}
