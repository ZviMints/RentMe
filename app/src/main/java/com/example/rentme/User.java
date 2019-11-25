package com.example.rentme;

public class User {
    private String name;
    private String lastname;
    private String area;
    private String number;

    public User(String name, String lastname, String area, String number) {
        this.name = name;
        this.lastname = lastname;
        this.area = area;
        this.number = number;
    }


    public String getName() {
        return name;
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
