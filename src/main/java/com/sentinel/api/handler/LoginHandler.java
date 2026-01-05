package com.sentinel.api.handler;

import com.sentinel.api.config.TokenService;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // For this demo, any request to /login creates a token for "Admin"
        String token = TokenService.createToken("SentinelAdmin");
        String response = "{\"token\":\"" + token + "\", \"message\":\"Login Successful\"}";

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}