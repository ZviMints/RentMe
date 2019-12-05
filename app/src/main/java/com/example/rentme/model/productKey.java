package com.example.rentme.model;

import java.io.Serializable;

public class productKey implements Serializable {
    private String father;
    private String category;

    public productKey(){};

    public productKey(String father, String category) {
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
