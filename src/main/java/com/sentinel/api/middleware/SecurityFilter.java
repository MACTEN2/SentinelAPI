package com.sentinel.api.middleware;

import com.sentinel.api.config.SentinelLogger;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class SecurityFilter extends Filter {
    private static final String VALID_API_KEY = "sentinel-secret-2026";

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        // 1. Inject Security Headers
        exchange.getResponseHeaders().add("X-Content-Type-Options", "nosniff");
        exchange.getResponseHeaders().add("X-Frame-Options", "DENY");

        // 2. Authentication Logic
        String apiKey = exchange.getRequestHeaders().getFirst("X-API-KEY");

        if (apiKey != null && apiKey.equals(VALID_API_KEY)) {
            // Updated to use .log() with correct parameter order
            SentinelLogger.log(ip, method, path, 200, "Authorized Access");
            chain.doFilter(exchange);
        } else {
            // Updated to use .log() with correct parameter order
            SentinelLogger.log(ip, method, path, 401, "Unauthorized Attempt - Blocked");
            
            String error = "401 Unauthorized - Valid API Key Required";
            exchange.sendResponseHeaders(401, error.length());
            exchange.getResponseBody().write(error.getBytes());
            exchange.getResponseBody().close();
        }
    }

    @Override
    public String description() { return "Security and Logging Filter"; }
}