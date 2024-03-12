package org.tm30.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@Slf4j
@Component
public class RouteValidator {

    public final Map<String, List<String>> openApiEndpoints = new HashMap<>();

    public RouteValidator() {
        openApiEndpoints.put("/home", List.of("ALL"));
        openApiEndpoints.put("index", List.of("ALL"));
        openApiEndpoints.put("/css", List.of("ALL"));
        openApiEndpoints.put("/js", List.of("ALL"));
        openApiEndpoints.put("/authenticated-user", List.of("ALL"));
        openApiEndpoints.put("/api/v1/email", List.of("ALL"));
        openApiEndpoints.put("/api/v1/auth", List.of("ALL"));
        openApiEndpoints.put("/api/v1/twits", List.of("GET"));
        openApiEndpoints.put("/v2/api-docs", List.of("ALL"));
        openApiEndpoints.put("/v3/api-docs", List.of("ALL"));
        openApiEndpoints.put("/configuration", List.of("ALL"));
        openApiEndpoints.put("/swagger", List.of("ALL"));
        openApiEndpoints.put("/swagger-ui", List.of("ALL"));
        openApiEndpoints.put("/webjars", List.of("ALL"));
        openApiEndpoints.put("/swagger-ui.html", List.of("ALL"));
        openApiEndpoints.put("/eureka", List.of("ALL"));
    }

    public Predicate<ServerHttpRequest> isSecured =
        request -> {
            AtomicReference<Boolean> isMatch = new AtomicReference<>(false);
            openApiEndpoints.forEach((key, value) -> {
                String endpoint = request.getURI().getPath();
                String requestMethod = String.valueOf(request.getMethod());
                if(endpoint.contains(key) && (value.contains(requestMethod) || value.contains("ALL"))) {
                    log.info("checkUrl: {}", endpoint.contains(key) && (value.contains(requestMethod) || value.contains("ALL")));
                    isMatch.set(true);
                }
            });
            return isMatch.get();
        };
    }
