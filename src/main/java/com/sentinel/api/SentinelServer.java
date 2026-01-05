package com.sentinel.api;

import com.sun.net.httpserver.HttpServer;
import com.sentinel.api.handler.ApiHandler;
import com.sentinel.api.handler.WebHandler; // <--- ADD THIS LINE
import com.sentinel.api.middleware.SecurityFilter;
import java.net.InetSocketAddress;
import java.io.IOException;

public class SentinelServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Define the routes
        server.createContext("/", new WebHandler()); 
        
        var apiContext = server.createContext("/api/status", new ApiHandler());
        apiContext.getFilters().add(new SecurityFilter());

        System.out.println("🚀 SentinelAPI v1.0.2 is online!");
        server.setExecutor(null); 
        server.start();
    }
}