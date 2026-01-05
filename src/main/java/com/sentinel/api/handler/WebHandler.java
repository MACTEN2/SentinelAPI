package com.sentinel.api.handler;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Path to the HTML file inside the Docker container
        String htmlPath = "resources/index.html";
        byte[] response = Files.readAllBytes(Paths.get(htmlPath));

        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.length);
        
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}