package com.kube.hermes.middleware;

import com.kube.hermes.http.HttpRequest;
import com.kube.hermes.http.HttpResponse;

public class CorsMiddleware implements Middleware {
    @Override
    public HttpResponse handle(HttpRequest request, MiddlewareChain chain) {
        HttpResponse response = chain.next(request);
        if (response != null) {
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        }
        return response;
    }
}

