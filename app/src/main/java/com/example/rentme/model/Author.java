package com.example.rentme.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Author implements Serializable {

    String userUid;
    String firstname;
    String lastname;
    String area;

    public Author() {}

    public Author(String userUid, String firstname, String lastname, String area) {
        this.userUid = userUid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.area = area;
    }


    public String getUserUid() {
        return userUid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
    public String getFullName() {
        return firstname + " " + lastname;
    }

    public String getArea() {
        return area;
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + userUid + "," + firstname + "," + lastname + ")";
    }
}
