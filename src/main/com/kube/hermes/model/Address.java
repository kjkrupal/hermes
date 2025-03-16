package com.kube.hermes.model;

public class Address {
    private String street;
    private String city;
    private int zipCode;

    public Address(String street, String city, int zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    public String getStreet() { return street; }
    public String getCity() { return city; }
    public int getZipCode() { return zipCode; }

    @Override
    public String toString() {
        return "Address{street='" + street + "', city='" + city + "', zipCode=" + zipCode + "}";
    }
}
