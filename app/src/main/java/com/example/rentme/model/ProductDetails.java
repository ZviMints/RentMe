package com.example.rentme.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProductDetails implements Serializable {

    private final String DEF_IMAGE = "https://firebasestorage.googleapis.com/v0/b/rentme-cdf84.appspot.com/o/1575107154720.jpg?alt=media&token=349bb82d-a50f-4736-b5a0-6470031bad0e";
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private String title;
    private String category;
    private String details;
    private String image;
    private String condition;
    private String price;
    private String rentPeriod;
    private String uploadTime;
    private long utc;

    public ProductDetails() {
    }

    public ProductDetails(String title, String category, String details, String condition, String price, String rentPeriod, String uploadTime, String image, long utc) {
        this.title = title;
        this.category = category;
        this.details = details;
        if (image == "") this.image = DEF_IMAGE;
        else this.image = image;
        this.rentPeriod = rentPeriod;
        this.condition = condition;
        this.price = price;
        this.uploadTime = uploadTime;
        this.utc = utc;

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

    public long getUtc() {
        return utc;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "[" + title
                + "," + category
                + "," + details
                + "," + image
                + "," + condition
                + "," + price
                + "," + rentPeriod
                + "," + uploadTime
                + "," + utc + "]";
    }
}
