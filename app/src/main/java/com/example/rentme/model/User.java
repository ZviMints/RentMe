package com.example.rentme.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String lastname;
    private String area;
    private String number;
    private String email;
    private List<RelationCategoryProduct> products;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String lastname, String area, String number, String email) {
        this.name = name;
        this.email = email;
        this.lastname = lastname;
        this.area = area;
        this.number = number;
        this.products = products;
        this.products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<RelationCategoryProduct> getProducts() {
        return products;
    }

    public void setProducts(List<RelationCategoryProduct> products) {
        this.products = products;
    }
}
