package com.example.rentme.model;
import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    String fullname;
    String msg;

    public Comment() {}

    public Comment(String fullname, String msg) {
        this.fullname = fullname;
        this.msg = msg;
    }

    public String getFullname() {
        return fullname;
    }

    public String getMsg() {
        return this.msg;
    }
}
