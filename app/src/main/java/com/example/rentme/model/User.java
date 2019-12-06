package com.example.rentme.model;

import com.example.rentme.R;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String lastname;
    private String area;
    private String number;
    private String email;
    private List<Relation> posts = new ArrayList<>();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public List<Relation> getPosts() {
        return posts;
    }
    // In SignUp
    public User(String name, String lastname, String area, String number, String email) {
        this.name = name;
        this.email = email;
        this.lastname = lastname;
        this.area = area;
        this.number = number;
    }

    public void setPosts(List<Relation> posts) {
        this.posts = posts;
    }

    // From Database
    public User(String name, String lastname, String area, String number, String email, List<Relation> posts) {
        this.name = name;
        this.email = email;
        this.lastname = lastname;
        this.area = area;
        this.number = number;
        this.posts = posts;
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
}
