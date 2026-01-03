package com.sentinel.api.config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SentinelLogger {
    private static final String LOG_FILE = "/app/logs/security.log"; // Target file in Docker
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static void log(String ip, String method, String uri, int status, String message) {
        String timestamp = dtf.format(LocalDateTime.now());
        String logEntry = String.format("[%s] [INFO] [%s] %s %s - Status: %d - %s", 
                                        timestamp, ip, method, uri, status, message);
        
        // Print to console (for Docker logs)
        System.out.println(logEntry);

        // Write to file (for Persistence)
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}