package com.sentinel.api.middleware;

import com.sentinel.api.config.SentinelLogger;
import com.sentinel.api.config.TokenService;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SecurityFilter extends Filter {

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        String ip = exchange.getRemoteAddress().getAddress().getHostAddress();
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        // 1. Extract the Authorization Header
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        boolean isAuthenticated = false;

        // 2. Validate the JWT Token (Bearer eyJ...)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (TokenService.validateToken(token)) {
                isAuthenticated = true;
            }
        }

        if (isAuthenticated) {
            // --- AUTHORIZED PATH ---
            SentinelLogger.log(ip, method, path, 200, "Authorized JWT Access");
            
            try {
                byte[] response = Files.readAllBytes(Paths.get("resources/cmd_center.html"));
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, response.length);
                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
            } catch (IOException e) {
                // Fallback if file is missing
                String fallback = "{\"status\":\"Authorized\", \"message\":\"Command Center file missing\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, fallback.length());
                exchange.getResponseBody().write(fallback.getBytes());
                exchange.getResponseBody().close();
            }
        } else {
            // --- UNAUTHORIZED PATH (The Wallpaper & GIF Screen) ---
            SentinelLogger.log(ip, method, path, 401, "Unauthorized Attempt - Blocked");

            String imageUrl = "https://images.steamusercontent.com/ugc/35562945673976438/8B9CB721CABAE32A5C3804C9B7169D0E0267C80C/";
            
            String deniedHtml = "<html><head><style>" +
                "body {" +
                "  background-image: url('" + imageUrl + "');" +
                "  background-repeat: repeat;" +
                "  background-size: 200px;" +
                "  color: red;" +
                "  font-family: 'Courier New', Courier, monospace;" +
                "  text-align: center;" +
                "  padding: 0; margin: 0;" +
                "  background-color: black;" +
                "}" +
                ".overlay {" +
                "  background-color: rgba(0, 0, 0, 0.38);" +
                "  min-height: 100vh;" +
                "  display: flex; flex-direction: column; align-items: center; justify-content: center;" +
                "}" +
                "img { width: 400px; border: 5px solid red; border-radius: 10px; margin-bottom: 20px; }" +
                "h1 { font-size: 3rem; text-transform: uppercase; letter-spacing: 5px; text-shadow: 2px 2px #000; }" +
                ".warning { color: white; font-size: 1.2rem; background: rgba(255,0,0,0.3); padding: 10px; border-radius: 5px; }" +
                "</style></head><body>" +
                "<div class='overlay'>" +
                "<h1>ACCESS DENIED</h1>" +
                "<img src='https://media1.tenor.com/m/-AfeL_9UjlUAAAAC/derek-stranger-things.gif' alt='Access Denied'>" +
                "<p class='warning'>SECURITY BREACH DETECTED: INVALID OR MISSING TOKEN</p>" +
                "<p>Your IP: " + ip + " has been logged by SentinelAPI.</p>" +
                "</div>" +
                "</body></html>";

            exchange.getResponseHeaders().set("Content-Type", "text/html");
            byte[] responseBytes = deniedHtml.getBytes();
            exchange.sendResponseHeaders(401, responseBytes.length);
            
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    @Override
    public String description() {
        return "JWT Security Filter";
    }
}