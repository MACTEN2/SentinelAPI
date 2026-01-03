package com.sentinel.api;

import com.sentinel.api.middleware.SecurityFilter;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SentinelServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Register the "/" context
        HttpContext rootContext = server.createContext("/", exchange -> {
            String response = "{\"status\": \"secure\", \"message\": \"Access Granted to SentinelAPI\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        // ATTACH THE MIDDLEWARE HERE
        rootContext.getFilters().add(new SecurityFilter());

        System.out.println("SentinelAPI started on port " + port);
        server.start();
    }
}