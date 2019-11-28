package com.example.rentme.model;

public class Product {
    private String title;
    private String category;
    private String details;
    private String image;
    private String condition;
    private String price;
    private String rentPeriod;
    private final String DEF_IMAGE = "https://www.bitsinc.com/sca-dev-montblanc/img/no_image_available.jpeg";


    public Product(String title, String category, String details,String condition,  String price, String rentPeriod){
        this.title = title;
        this.category = category;
        this.condition = condition;
        this.details = details;
        this.price = price;
        this.rentPeriod = rentPeriod;
        this.image = DEF_IMAGE;
    }

    public Product(String title, String category, String details, String condition,String price, String rentPeriod,String image){
        this.title = title;
        this.category = category;
        this.details = details;
        if (image=="")
            this.image = DEF_IMAGE;
        else
            this.image = image;
        this.rentPeriod = rentPeriod;
        this.condition = condition;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getRentPeriod() {
        return rentPeriod;
    }

    public String getDetails() {
        return details;
    }

    public String getImage() {
        return image;
    }

    public String getCondition() {
        return condition;
    }

    public String getPrice() {
        return price;
    }
}