package com.sentinel.api;

import com.sun.net.httpserver.HttpServer;
import com.sentinel.api.handler.ApiHandler;
import com.sentinel.api.middleware.SecurityFilter;
import java.net.InetSocketAddress;
import java.io.IOException;

public class SentinelServer {
    public static void main(String[] args) throws IOException {
        // Create server instance
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Define the API endpoint
        var apiContext = server.createContext("/api/status", new ApiHandler());

        // Attach the security filter (which handles the logging)
        apiContext.getFilters().add(new SecurityFilter());

        System.out.println("🚀 SentinelAPI v1.0.2 is online!");
        System.out.println("📍 Endpoint: http://localhost:8080/api/status");
        
        server.setExecutor(null); 
        server.start();
    }
}