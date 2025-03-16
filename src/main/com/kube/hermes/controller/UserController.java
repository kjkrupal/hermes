package com.kube.hermes.controller;

import com.kube.hermes.http.HttpRequest;
import com.kube.hermes.http.HttpResponse;
import com.kube.hermes.util.JsonParser;
import com.kube.hermes.util.JsonSerializer;
import com.kube.hermes.auth.Authenticator;
import com.kube.hermes.http.Controller;
import com.kube.hermes.model.Address;
import com.kube.hermes.model.User;

import java.util.Map;

public class UserController implements Controller {
    @Override
    public HttpResponse handle(HttpRequest request) {
        if (!Authenticator.isAuthenticated(request)) {
            return new HttpResponse(401, "Unauthorized", JsonSerializer.serialize(Map.of("error", "Invalid API token")));
        }

        switch (request.getMethod()) {
            case "GET":
                return handleGet(request);
            case "POST":
                return handlePost(request);
            default:
                return new HttpResponse(405, "Method Not Allowed", JsonSerializer.serialize(Map.of("error", "Method not supported")));
        }
    }

    private HttpResponse handleGet(HttpRequest request) {
        String userId = request.getQueryParam("id");

        if (userId == null) {
            return new HttpResponse(400, "Bad Request", JsonSerializer.serialize(Map.of("error", "User ID is required")));
        }

        User user = new User(userId, "Alice", 30, new Address("123 Main St", "New York", 10001));
        return new HttpResponse(200, "OK", JsonSerializer.serialize(user));
    }

    private HttpResponse handlePost(HttpRequest request) {
        try {
            User user = JsonParser.parseJson(request.getBody(), User.class);
            return new HttpResponse(201, "Created", JsonSerializer.serialize(Map.of("message", "User created", "user", user)));
        } catch (Exception e) {
            return new HttpResponse(400, "Bad Request", JsonSerializer.serialize(Map.of("error", "Invalid JSON format")));
        }
    }
}
