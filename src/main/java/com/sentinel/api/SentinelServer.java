package com.sentinel.api;

import com.sun.net.httpserver.HttpServer;
import com.sentinel.api.handler.*; 
import com.sentinel.api.middleware.SecurityFilter;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SentinelServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // 1. STATS (Live Dashboard Data + IP History)
        server.createContext("/api/stats", exchange -> {
            long blockedCount = 0;
            List<String> lastIPs = new ArrayList<>();
            Path logPath = Paths.get("logs/security.log");
            
            try {
                if (Files.exists(logPath)) {
                    List<String> allLines = Files.readAllLines(logPath);
                    for (String line : allLines) {
                        if (line.contains("Blocked")) {
                            blockedCount++;
                            // Extract IP assuming space-separated log format
                            String[] parts = line.split(" ");
                            if (parts.length > 1) lastIPs.add(parts[1]);
                        }
                    }
                    // Filter to last 5 and reverse for newest first
                    if (lastIPs.size() > 5) lastIPs = lastIPs.subList(lastIPs.size() - 5, lastIPs.size());
                    Collections.reverse(lastIPs);
                }
            } catch (IOException e) {
                System.err.println("Error reading logs: " + e.getMessage());
            }

            String historyJson = lastIPs.isEmpty() ? "[]" : "[\"" + String.join("\",\"", lastIPs) + "\"]";
            String json = "{" +
                "\"active_sessions\": 1," +
                "\"total_blocks\": " + blockedCount + "," +
                "\"system_load\": \"Nominal\"," +
                "\"last_breach\": \"Active\"," +
                "\"history\": " + historyJson +
                "}";
            
            byte[] response = json.getBytes();
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        });

        // 2. PURGE LOGS
        server.createContext("/api/purge-logs", exchange -> {
            Path logPath = Paths.get("logs/security.log");
            String message;
            try {
                if (Files.exists(logPath)) {
                    Files.write(logPath, new byte[0]); 
                    message = "{\"status\":\"success\", \"message\":\"Logs purged successfully\"}";
                } else {
                    message = "{\"status\":\"error\", \"message\":\"Log file does not exist\"}";
                }
            } catch (IOException e) {
                message = "{\"status\":\"error\", \"message\":\"Failed to access logs\"}";
            }
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, message.length());
            exchange.getResponseBody().write(message.getBytes());
            exchange.getResponseBody().close();
        });

        // 3. LOGIN
        server.createContext("/api/login", new LoginHandler());

        // 4. STATUS (Secure)
        var apiContext = server.createContext("/api/status", new ApiHandler());
        apiContext.getFilters().add(new SecurityFilter());

        // 5. SHUTDOWN
        server.createContext("/api/shutdown", exchange -> {
            String response = "Sentinel System Shutting Down...";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            System.exit(0);
        });

        // 6. WEB & ASSETS (Catch-all)
        server.createContext("/", new WebHandler()); 

        System.out.println("🚀 SentinelAPI v1.0.5 is online!");
        server.setExecutor(null); 
        server.start();
    }
}