package com.sentinel.api;

import com.sun.net.httpserver.HttpServer;
import com.sentinel.api.handler.*; // Import all handlers
import com.sentinel.api.middleware.SecurityFilter;
import java.net.InetSocketAddress;
import java.io.IOException;

public class SentinelServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 1. LOGIN (Public - No Filter)
        server.createContext("/api/login", new LoginHandler());

        // 2. STATUS (Secure - Has Filter)
        var apiContext = server.createContext("/api/status", new ApiHandler());
        apiContext.getFilters().add(new SecurityFilter());

        // 3. HOME PAGE (The "Catch-all")
        // Note: Java matches the most specific path first. 
        server.createContext("/", new WebHandler()); 

        server.createContext("/api/shutdown", exchange -> {
        String response = "Sentinel System Shuting Down...";
        exchange.sendResponseHeaders(200, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
        
        System.out.println("Shutdown signal received. Closing server...");
        System.exit(0); // This kills the Java process and the Docker container
    });

        System.out.println("🚀 SentinelAPI v1.0.3 is online!");
        server.setExecutor(null); 
        server.start();
    }
}