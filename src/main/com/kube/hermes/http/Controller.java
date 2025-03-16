package com.kube.hermes.http;

public interface Controller {
    HttpResponse handle(HttpRequest request);
}

