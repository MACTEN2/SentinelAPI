package com.sentinel.api.handler;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sentinel.api.model.StatusResponse;
import java.io.OutputStream;
import java.io.IOException;

public class ApiHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        StatusResponse responseData = new StatusResponse("success", "Sentinel System Operational", "1.0.2");
        String response = responseData.toJson();
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length());
        
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}