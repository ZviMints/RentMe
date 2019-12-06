package com.example.rentme.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Comment implements Serializable {
    Author author;
    String msg;

    public Comment(Author author, String msg) {
        this.author = author;
        this.msg = msg;
    }
    public Author getAuthor() {
        return author;
    }
    public String getMsg() {
        return msg;
    }


    @NonNull
    @Override
    public String toString() {
        return "[" + author + "] : " + msg;
    }

}
