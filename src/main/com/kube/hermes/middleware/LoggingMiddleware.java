package com.kube.hermes.middleware;

import com.kube.hermes.util.Logger;
import com.kube.hermes.http.HttpRequest;
import com.kube.hermes.http.HttpResponse;

public class LoggingMiddleware implements Middleware {
    @Override
    public HttpResponse handle(HttpRequest request, MiddlewareChain chain) {
        Logger.log("INFO", "Request: " + request.getMethod() + " " + request.getPath());
        return chain.next(request);
    }
}

