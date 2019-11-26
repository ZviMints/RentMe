package com.example.rentme.model;

import android.widget.ImageView;

public class Category {
    private String name;
    private ImageView pic;

    public void setName(String name) {
        this.name = name;
    }

    public void setPic(ImageView pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public ImageView getPic() {
        return pic;
    }
}
