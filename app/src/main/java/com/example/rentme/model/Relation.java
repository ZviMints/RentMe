package com.example.rentme.model;

import java.io.Serializable;

public class Relation implements Serializable {
    private String categoryName;
    private String productUid;

    public Relation() {}

    public String getCategoryName() {
        return categoryName;
    }

    public String getProductUid() {
        return productUid;
    }

    public Relation(String categoryName, String productUid) {
        this.categoryName = categoryName;
        this.productUid = productUid;
    }
}
