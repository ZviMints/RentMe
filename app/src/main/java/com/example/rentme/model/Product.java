package com.example.rentme.model;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {
    private String title;
    private String category;
    private String details;
    private String image;
    private String condition;
    private String price;
    private String rentPeriod;
    private String uploadTime;


    private final String DEF_IMAGE = "https://firebasestorage.googleapis.com/v0/b/rentme-cdf84.appspot.com/o/1575107154720.jpg?alt=media&token=349bb82d-a50f-4736-b5a0-6470031bad0e";
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Product(String title, String category, String details,String condition,  String price, String rentPeriod){
        this.title = title;
        this.category = category;
        this.condition = condition;
        this.details = details;
        this.price = price;
        this.rentPeriod = rentPeriod;

        this.uploadTime = formatter.format(new Date());
        this.image = DEF_IMAGE;
    }

    public Product(String title, String category, String details, String condition,String price, String rentPeriod, String image){
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
        this.uploadTime = formatter.format(new Date());


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

    public String getUploadTime() {
        return uploadTime;
    }

}