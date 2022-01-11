package com.erclab.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Menu implements Serializable {

    private String title;
    private String price;
    private String rating;

    public Menu() {}

    public Menu(String title, String price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
