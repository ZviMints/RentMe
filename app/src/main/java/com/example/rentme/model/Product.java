package com.example.rentme.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private String title;
    private String category;
    private String details;
    private String image;
    private String condition;
    private String price;
    private String rentPeriod;
    private String uploadTime;
    private String userUid;
    private String utc;
    private ArrayList<String> comments;

    private final String DEF_IMAGE = "https://firebasestorage.googleapis.com/v0/b/rentme-cdf84.appspot.com/o/1575107154720.jpg?alt=media&token=349bb82d-a50f-4736-b5a0-6470031bad0e";
    

    public Product() {}

    public Product(String title, String category, String details, String condition,String price, String rentPeriod, String uploadTime,String userUid, String utc, String image){
        this.title = title;
        this.category = category;
        this.details = details;
        if (image== "") this.image = DEF_IMAGE; else  this.image = image;
        this.rentPeriod = rentPeriod;
        this.condition = condition;
        this.price = price;
        this.uploadTime = uploadTime;
        this.userUid = userUid;
        this.utc = utc;
        this.comments = new ArrayList<String>();
        //this.comments.add("0");

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

    public String getUserUid() {
        return userUid;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public String getUtc() {
        return utc;
    }

    public String getFatherId() {
        return utc+": "+title;
    }
}