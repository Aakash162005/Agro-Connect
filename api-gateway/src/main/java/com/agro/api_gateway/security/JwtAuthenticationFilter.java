package com.agro.api_gateway.security;

import com.agro.api_gateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();

        // ===== PUBLIC APIs (No Token Required) =====
        // Anyone can login, register, or view products (GET requests only)

        if (path.equals("/api/users/login") ||
                path.equals("/api/users/register") ||

                // Public Product APIs
                (path.equals("/api/products") && method == HttpMethod.GET) ||

                (path.matches("/api/products/\\d+") && method == HttpMethod.GET) ||

                (path.startsWith("/api/products/search") && method == HttpMethod.GET) ||

                (path.startsWith("/api/products/category") && method == HttpMethod.GET) ||

                (path.startsWith("/api/products/price") && method == HttpMethod.GET) ||

                (path.startsWith("/api/products/sort") && method == HttpMethod.GET)) {

            return chain.filter(exchange);
        }

        // ===== TOKEN VALIDATION =====
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        System.out.println("========== JWT DEBUG ==========");
        System.out.println("Email  : " + email);
        System.out.println("Role   : " + role);
        System.out.println("Path   : " + path);
        System.out.println("Method : " + method);
        System.out.println("================================");

        // ===== ROLE-BASED ACCESS CONTROL (RBAC) =====

        // ================= ADMIN APIs =================

        if (path.startsWith("/api/admin")
                && !role.equals("ADMIN")) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.FORBIDDEN);

            return exchange.getResponse().setComplete();
        }

        // ================= PRODUCT ROUTES =================

        if (path.startsWith("/api/products")) {
            // Only ADMIN and SHOPKEEPER can add (POST), update (PUT), or delete (DELETE) products
            if ((method == HttpMethod.POST ||
                    method == HttpMethod.PUT ||
                    method == HttpMethod.DELETE)
                    && !(role.equals("ADMIN") || role.equals("SHOPKEEPER"))) {

                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }
        }

        // 2. ORDER ROUTES
        if (path.startsWith("/api/orders")) {
            // Only CUSTOMER can place a new order
            if (method == HttpMethod.POST && !role.equals("CUSTOMER")) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // Only ADMIN and SHOPKEEPER can update (e.g., mark as shipped) or delete orders
            if ((method == HttpMethod.PUT ||
                    method == HttpMethod.PATCH ||
                    method == HttpMethod.DELETE)
                    && !(role.equals("ADMIN") || role.equals("SHOPKEEPER"))) {

                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            // Note: GET requests for /api/orders pass through for all authenticated roles.
            // Your Order Service microservice should handle the logic to ensure CUSTOMERS
            // only see their own orders, while ADMIN/SHOPKEEPER can see all orders.
        }

        // ===== HEADER FORWARDING =====
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> builder
                        .header("X-User-Email", email)
                        .header("X-User-Role", role))
                .build();

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

}