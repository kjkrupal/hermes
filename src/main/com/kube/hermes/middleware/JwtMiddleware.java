package com.kube.hermes.middleware;

import com.kube.hermes.auth.JwtAuth;
import com.kube.hermes.http.HttpRequest;
import com.kube.hermes.http.HttpResponse;

public class JwtMiddleware implements Middleware {
    @Override
    public HttpResponse handle(HttpRequest request, MiddlewareChain chain) {
        String authHeader = request.getHeaders().get("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new HttpResponse(401, "Unauthorized", "{\"error\": \"Missing token\"}");
        }

        String token = authHeader.substring(7);
        if (!JwtAuth.verifyToken(token)) {
            return new HttpResponse(401, "Unauthorized", "{\"error\": \"Invalid token\"}");
        }

        return chain.next(request);
    }
}

