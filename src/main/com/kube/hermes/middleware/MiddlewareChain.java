package com.kube.hermes.middleware;

import com.kube.hermes.http.HttpRequest;
import com.kube.hermes.http.HttpResponse;

import java.util.List;

public class MiddlewareChain {
    private final List<Middleware> middlewares;
    private int index = 0;

    public MiddlewareChain(List<Middleware> middlewares) {
        this.middlewares = middlewares;
    }

    public HttpResponse next(HttpRequest request) {
        if (index < middlewares.size()) {
            return middlewares.get(index++).handle(request, this);
        }
        return null;
    }
}

