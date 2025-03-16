package com.kube.hermes.auth;

import com.kube.hermes.http.HttpRequest;

public class Authenticator {
    public static boolean isAuthenticated(HttpRequest request) {
        String authHeader = request.getHeaders().get("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false; // No token provided
        }

        String token = authHeader.substring(7); // Extract the token after "Bearer "
        return JwtAuth.verifyToken(token);
    }
}

