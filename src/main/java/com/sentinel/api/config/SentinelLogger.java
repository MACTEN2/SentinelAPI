package com.sentinel.api.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SentinelLogger {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static void logRequest(String method, String path, String ip, int statusCode, String message) {
        String timestamp = dtf.format(LocalDateTime.now());
        //uses a structured format: [Timestamp] [Level] [IP] [Method] [Path] [Status] - Message
        String logEntry = String.format("[%s] [INFO] [%s] %s %s - Status: %d - %s", 
                            timestamp, ip, method, path, statusCode, message);
        
        System.out.println(logEntry);
    }
}