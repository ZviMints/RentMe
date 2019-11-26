package com.example.rentme.model;

public class Product {
    private String title;
    private String category;
    private String details;
    private String image;

    public Product(String title, String category, String details, String image){
        this.title = title;
        this.category = category;
        this.details = details;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDetails() {
        return details;
    }

    public String getImage() { return image; }

}
