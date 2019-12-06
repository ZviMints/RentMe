package com.example.rentme.model;
import androidx.annotation.NonNull;

import java.io.Serializable;

public class RelationCategoryProduct implements Serializable {

    private String CategoryName;
    private String ProductUID;

    public RelationCategoryProduct(){};

    public RelationCategoryProduct(String CategoryName, String ProductUID) {
        this.CategoryName = CategoryName;
        this.ProductUID = ProductUID;
    }

    public String getCategoryName() {
        return CategoryName;
    }
    public String getProductUID() {
        return ProductUID;
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + CategoryName + "," + ProductUID + ")";
    }
}
