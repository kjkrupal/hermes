package com.kube.hermes.middleware;

import com.kube.hermes.http.HttpRequest;
import com.kube.hermes.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class RateLimitMiddleware implements Middleware {
    private final Map<String, Integer> requestCounts = new HashMap<>();
    private static final int LIMIT = 10; // Max requests per minute
    private static final long TIME_WINDOW = 60000; // 1 minute in milliseconds
    private final Map<String, Long> lastRequestTime = new HashMap<>();

    @Override
    public HttpResponse handle(HttpRequest request, MiddlewareChain chain) {
        String ip = request.getHeaders().getOrDefault("X-Forwarded-For", "unknown");

        long currentTime = System.currentTimeMillis();
        long lastRequest = lastRequestTime.getOrDefault(ip, 0L);

        if (currentTime - lastRequest > TIME_WINDOW) {
            requestCounts.put(ip, 0);
        }

        if (requestCounts.getOrDefault(ip, 0) >= LIMIT) {
            return new HttpResponse(429, "Too Many Requests", "{\"error\": \"Rate limit exceeded\"}");
        }

        requestCounts.put(ip, requestCounts.getOrDefault(ip, 0) + 1);
        lastRequestTime.put(ip, currentTime);

        return chain.next(request);
    }
}

