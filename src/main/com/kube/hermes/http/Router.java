package com.kube.hermes.http;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Controller> routes = new HashMap<>();

    public void register(String path, Controller controller) {
        routes.put(path, controller);
    }

    public Controller getController(String path) {
        return routes.get(path);
    }
}
