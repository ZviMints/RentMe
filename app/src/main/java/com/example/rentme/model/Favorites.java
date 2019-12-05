package com.example.rentme.model;

import java.io.Serializable;

public class Favorites implements Serializable {
    private String father;
    private String category;

    public Favorites(String father, String category) {
        this.father = father;
        this.category = category;
    }

    public String getFather() {
        return father;
    }

    public String getCategory() {
        return category;
    }
}
