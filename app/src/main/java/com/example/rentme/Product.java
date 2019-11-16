package com.example.rentme;

public class Product {
    private String title;
    private String category;
    private String details;

    public Product(String title, String category, String details){
        this.title = title;
        this.category = category;
        this.details = details;
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
}
