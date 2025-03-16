package com.kube.hermes.model;

public class User {
    private String id;
    private String name;
    private int age;

    public User(String id, String name, int age, Address newYork) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
