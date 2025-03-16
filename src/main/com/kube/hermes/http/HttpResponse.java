package com.kube.hermes.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final int statusCode;
    private final String statusText;
    private final String body;
    private final Map<String, String> headers = new HashMap<>();

    public HttpResponse(int statusCode, String statusText, String body) {
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.body = body;
        this.headers.put("Content-Type", "application/json");
        this.headers.put("Content-Length", String.valueOf(body.length()));
        this.headers.put("Connection", "keep-alive");
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String formatResponse() {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(statusCode).append(" ").append(statusText).append("\r\n");
        headers.forEach((key, value) -> response.append(key).append(": ").append(value).append("\r\n"));
        response.append("\r\n").append(body);
        return response.toString();
    }
}
