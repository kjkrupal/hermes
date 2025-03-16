package com.kube.hermes.util;

import java.lang.reflect.Field;

public class JsonSerializer {
    public static String serialize(Object obj) {
        if (obj == null) return "null";
        StringBuilder json = new StringBuilder("{");

        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            try {
                json.append("\"").append(fields[i].getName()).append("\":\"");
                json.append(fields[i].get(obj)).append("\"");
                if (i < fields.length - 1) json.append(",");
            } catch (IllegalAccessException ignored) {}
        }

        json.append("}");
        return json.toString();
    }
}

