package com.example.rentme.model;

public class Product {
    private String title;
    private String category;
    private String details;
    private String image;
    private String condition;
    private int pricePerDay;
    private int pricePerhour;
    private final String DEF_IMAGE = "https://www.bitsinc.com/sca-dev-montblanc/img/no_image_available.jpeg";


    public Product(String title, String category, String details,String condition,  int pricePerDay ,int pricePerhour){
        this.title = title;
        this.category = category;
        this.condition = condition;
        this.details = details;
        this.pricePerDay = pricePerDay;
        this.pricePerhour = pricePerhour;
        this.image = DEF_IMAGE;
    }

    public Product(String title, String category, String details, String condition, int pricePerDay ,int pricePerhour, String image){
        this.title = title;
        this.category = category;
        this.details = details;
        if (image=="")
            this.image = DEF_IMAGE;
        else
            this.image = image;

        this.condition = condition;
        this.pricePerDay = pricePerDay;
        this.pricePerhour = pricePerhour;
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