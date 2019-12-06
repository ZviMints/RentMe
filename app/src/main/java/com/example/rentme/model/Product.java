package com.example.rentme.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product implements Serializable {

    private ProductDetails productDetails;
    private Author author;
    private String PRODUCT_UID;
    private List<Comment> comments_list = new ArrayList<>();

    public Product() { comments_list = new ArrayList<>(); }

    // From Publish
    public Product(ProductDetails productDetails, Author author){
     this.productDetails = productDetails;
     this.author = author;
     this.PRODUCT_UID = getPRODUCT_UID();
    }
    // Reslove
    public Product(ProductDetails productDetails, Author author, List<Comment> comments_list){
        this.productDetails = productDetails;
        this.author = author;
        this.PRODUCT_UID = getPRODUCT_UID();
        this.comments_list = comments_list;
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public Author getAuthor() {
        return author;
    }

    public String getPRODUCT_UID() {

        return getProductDetails().getUtc() + ": " + getProductDetails().getTitle();
    }

    public List<Comment> getCommentsList() {
        return (comments_list == null) ?  new ArrayList<Comment>() : comments_list;
    }

    @NonNull
    @Override
    public String toString() {
        return "\n" + "productDetails: " + productDetails + "\n" +
                "author: " + author + "\n" +
                "Product_UID: " + PRODUCT_UID + "\n" +
                "comments: " + comments_list.toString();
    }
}