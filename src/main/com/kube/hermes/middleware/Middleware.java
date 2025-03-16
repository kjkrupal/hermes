package com.kube.hermes.middleware;

import com.kube.hermes.http.HttpRequest;
import com.kube.hermes.http.HttpResponse;

public interface Middleware {
    HttpResponse handle(HttpRequest request, MiddlewareChain chain);
}

