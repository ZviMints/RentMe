package com.example.rentme.model;

public class Category {
    String title;
    String img;
    private static final String DEFAULT_IMAGE = "https://i.ytimg.com/vi/8tPnX7OPo0Q/maxresdefault.jpg";
    public Category() {}

    public Category(String title, String img) {
        this.title = title;
        this.img = (img == null) ? DEFAULT_IMAGE : img;
    }

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }
}
