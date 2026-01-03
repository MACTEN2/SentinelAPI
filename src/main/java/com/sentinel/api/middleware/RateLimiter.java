package com.sentinel.api.middleware;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

public class RateLimiter {
    private static final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 5;

    public static boolean isAllowed(String ip) {
        requestCounts.putIfAbsent(ip, new AtomicInteger(0));
        int currentRequests = requestCounts.get(ip).incrementAndGet();
        
        // DEBUG LINE: This helps us see what's happening in the terminal
        System.out.println("DEBUG: IP " + ip + " has made " + currentRequests + " requests.");

        return currentRequests <= MAX_REQUESTS;
    }
}