package com.kube.hermes.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final String body;
    private final Map<String, String> queryParams = new HashMap<>();

    public HttpRequest(String method, String fullPath, Map<String, String> headers, String body) {
        this.method = method;
        String[] pathParts = fullPath.split("\\?", 2);
        this.path = pathParts[0]; // Extract path (excluding query params)
        this.headers = headers;
        this.body = body;

        if (pathParts.length > 1) {
            parseQueryParams(pathParts[1]);
        }
    }

    private void parseQueryParams(String query) {
        for (String param : query.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
    }

    public static HttpRequest parse(String rawRequest) {
        String[] lines = rawRequest.split("\r\n");

        if (lines.length < 1) {
            throw new IllegalArgumentException("Invalid HTTP request format.");
        }

        // Extract method, path, and HTTP version (e.g., "GET /users?id=123 HTTP/1.1")
        String[] requestLineParts = lines[0].split(" ");
        if (requestLineParts.length < 2) {
            throw new IllegalArgumentException("Invalid request line.");
        }

        String method = requestLineParts[0];
        String fullPath = requestLineParts[1];

        // Parse headers
        Map<String, String> headers = new HashMap<>();
        int i = 1;
        while (i < lines.length && !lines[i].isEmpty()) {
            String[] headerParts = lines[i].split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
            i++;
        }

        // Read body (if exists)
        StringBuilder body = new StringBuilder();
        i++; // Move past empty line after headers
        while (i < lines.length) {
            body.append(lines[i]).append("\n");
            i++;
        }

        return new HttpRequest(method, fullPath, headers, body.toString().trim());
    }

    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getBody() { return body; }
    public Map<String, String> getHeaders() { return headers; }
    public Map<String, String> getQueryParams() { return queryParams; }
    public String getQueryParam(String key) { return queryParams.getOrDefault(key, null); }
}
