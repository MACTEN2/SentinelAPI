package com.sentinel.api.handler;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // 1. Get the requested path (e.g., "/" or "/audio/tysm.mp3")
        String path = exchange.getRequestURI().getPath();
        
        // 2. Default to index.html if no path is specified
        if (path.equals("/") || path.isEmpty()) {
            path = "/index.html";
        }

        // 3. Map the URL path to your physical "resources" folder
        String filePath = "resources" + path;
        File file = new File(filePath);

        if (file.exists() && !file.isDirectory()) {
            byte[] response = Files.readAllBytes(Paths.get(filePath));

            // 4. Set the correct Content-Type based on file extension
            if (path.endsWith(".mp3")) {
                exchange.getResponseHeaders().set("Content-Type", "audio/mpeg");
            } else if (path.endsWith(".html")) {
                exchange.getResponseHeaders().set("Content-Type", "text/html");
            } else if (path.endsWith(".css")) {
                exchange.getResponseHeaders().set("Content-Type", "text/css");
            }

            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        } else {
            // 5. If file not found, send 404
            String error = "404 Not Found: " + path;
            exchange.sendResponseHeaders(404, error.length());
            OutputStream os = exchange.getResponseBody();
            os.write(error.getBytes());
            os.close();
        }
    }
}