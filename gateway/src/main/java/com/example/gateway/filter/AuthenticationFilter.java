package com.example.gateway.filter;

import com.example.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/login",
            "/api/auth/register");
    
    // Admin-only endpoints
    private static final List<String> ADMIN_ENDPOINTS = List.of(
            "/api/reports",
            "/api/logs");
    
    // Operator-accessible endpoints
    private static final List<String> OPERATOR_ENDPOINTS = List.of(
            "/api/animals",
            "/api/staff",
            "/api/health",
            "/api/enclosures",
            "/api/feeding");
    
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String OPERATOR_ROLE = "OPERATOR";

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();

            Predicate<ServerHttpRequest> isApiSecured = r -> PUBLIC_ENDPOINTS.stream()
                    .noneMatch(uri -> r.getURI().getPath().contains(uri));

            if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            }

            List<String> authHeaders = request.getHeaders().get("Authorization");
            if (authHeaders == null || authHeaders.isEmpty()) {
                return this.onError(exchange, "Authorization header is empty", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = authHeaders.get(0);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return this.onError(exchange, "Invalid authorization header format. Expected: Bearer <token>", HttpStatus.UNAUTHORIZED);
            }

            final String token = authHeader.substring(7);
            
            if (token.isEmpty()) {
                return this.onError(exchange, "JWT token is empty", HttpStatus.UNAUTHORIZED);
            }

            if (!jwtUtil.validateToken(token)) {
                return this.onError(exchange, "Invalid or expired JWT token", HttpStatus.UNAUTHORIZED);
            }

            String userName = jwtUtil.extractUsername(token);
            String userRoles = jwtUtil.extractRole(token);
            
            if (userName == null || userRoles == null) {
                return this.onError(exchange, "Invalid token: missing user information", HttpStatus.UNAUTHORIZED);
            }

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Name", userName)
                    .header("X-User-Roles", userRoles)
                    .build();

            System.out.println("User name from token: " + userName);
            System.out.println("User roles from token: " + userRoles);

            // Check if accessing admin-only endpoints
            String requestPath = request.getURI().getPath();
            if (isAdminEndpoint(requestPath)) {
                if (!userRoles.contains(ADMIN_ROLE)) {
                    return this.onError(exchange, "ADMIN role required to access this resource",
                            HttpStatus.FORBIDDEN);
                }
            }
            // Check if accessing operator endpoints
            else if (isOperatorEndpoint(requestPath)) {
                if (!userRoles.contains(OPERATOR_ROLE) && !userRoles.contains(ADMIN_ROLE)) {
                    return this.onError(exchange, "OPERATOR or ADMIN role required to access this resource",
                            HttpStatus.FORBIDDEN);
                }
            }

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            System.err.println("Unexpected error in AuthenticationFilter: " + e.getMessage());
            e.printStackTrace();
            return this.onError(exchange, "Invalid token format", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean isAdminEndpoint(String path) {
        return ADMIN_ENDPOINTS.stream().anyMatch(path::startsWith);
    }

    private boolean isOperatorEndpoint(String path) {
        return OPERATOR_ENDPOINTS.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        System.err.println("Gateway Authentication Error: " + err);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // This filter will run before other filters
    }
}