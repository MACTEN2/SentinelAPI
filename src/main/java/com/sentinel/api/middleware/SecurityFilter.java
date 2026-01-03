package com.sentinel.api.middleware;

import com.sentinel.api.config.SentinelLogger; // Import the logger
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

        // 1. Security Headers
        exchange.getResponseHeaders().add("X-Content-Type-Options", "nosniff");

        // 2. Auth Logic
        String apiKey = exchange.getRequestHeaders().getFirst("X-API-KEY");

        if (apiKey != null && apiKey.equals(VALID_API_KEY)) {
            SentinelLogger.logRequest(method, path, ip, 200, "Authorized Access");
            chain.doFilter(exchange);
        } else {
            SentinelLogger.logRequest(method, path, ip, 401, "Unauthorized Attempt - Blocked");
            String error = "401 Unauthorized";
            exchange.sendResponseHeaders(401, error.length());
            exchange.getResponseBody().write(error.getBytes());
            exchange.getResponseBody().close();
        }
    }

    @Override
    public String description() { return "Security and Logging Filter"; }
}