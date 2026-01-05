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
            SentinelLogger.log(ip, method, path, 200, "Authorized Access");
            chain.doFilter(exchange);
        } else {
            SentinelLogger.log(ip, method, path, 401, "Unauthorized Attempt - Blocked");

            // Define the "Access Denied" HTML with a GIF
            String deniedHtml = "<html><head><style>" +
                "body { background-color: black; color: red; font-family: 'Courier New', Courier, monospace; text-align: center; padding-top: 50px; }" +
                "img { width: 400px; border: 5px solid red; border-radius: 10px; margin-bottom: 20px; }" +
                "h1 { font-size: 3rem; text-transform: uppercase; letter-spacing: 5px; }" +
                ".warning { color: white; font-size: 1.2rem; }" +
                "</style></head><body>" +
                "<h1>ACCESS DENIED</h1>" +
                // You can replace this URL with any GIF you prefer
                "<img src='https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExNGJueW94ZXB6ZzR0eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHgmZXA9djFfaW50ZXJuYWxfZ2lmX2J5X2lkJmN0PWc/jgVXeRc9Jdfa0/giphy.gif' alt='Access Denied'>" +
                "<p class='warning'>SECURITY BREACH DETECTED: UNIDENTIFIED API KEY</p>" +
                "<p>Your IP: " + ip + " has been logged by SentinelAPI.</p>" +
                "</body></html>";

            // Set headers to HTML
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            byte[] responseBytes = deniedHtml.getBytes();
            exchange.sendResponseHeaders(401, responseBytes.length);
            
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    @Override
    public String description() { return "Security and Logging Filter"; }
}