package com.sentinel.api.config;

import java.util.Base64;
import java.util.Date;

public class TokenService {
    private static final String SECRET_SIGNATURE = "SENTINEL_SIG_2026";

    public static String createToken(String username) {
        // Part 1: Header (Algorithm & Type)
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        
        // Part 2: Payload (User data & Expiration)
        long expiry = System.currentTimeMillis() + 3600000; // Valid for 1 hour
        String payload = "{\"sub\":\"" + username + "\",\"exp\":" + expiry + "}";

        // Encode parts to Base64
        String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes());
        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());

        // Part 3: Signature (Simulated for this build)
        String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(SECRET_SIGNATURE.getBytes());

        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    public static boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            // Check if the signature matches our secret
            String signature = new String(Base64.getUrlDecoder().decode(parts[2]));
            return signature.equals(SECRET_SIGNATURE);
        } catch (Exception e) {
            return false;
        }
    }
}