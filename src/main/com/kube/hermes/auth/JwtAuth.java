package com.kube.hermes.auth;

import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class JwtAuth {
    private static final String SECRET_KEY = "supersecret";

    public static String generateToken(String username) {
        String payload = "{\"user\":\"" + username + "\", \"exp\":" + (System.currentTimeMillis() + 3600000) + "}";
        return Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8)) + "." + SECRET_KEY;
    }

    public static boolean verifyToken(String token) {
        String[] parts = token.split("\\.");
        return parts.length == 2 && parts[1].equals(SECRET_KEY);
    }
}

