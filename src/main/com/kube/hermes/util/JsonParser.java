package com.kube.hermes.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {
    public static <T> T parseJson(String json, Class<T> clazz) {
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON format.");
        }

        Map<String, Object> jsonMap = new HashMap<>();
        String[] keyValuePairs = json.substring(1, json.length() - 1).split(",");

        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim().replace("\"", "");
                jsonMap.put(key, value);
            }
        }

        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (jsonMap.containsKey(field.getName())) {
                    field.set(obj, jsonMap.get(field.getName()));
                }
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}

