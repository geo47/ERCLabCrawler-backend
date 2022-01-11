package com.erclab.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BlogReview implements Serializable {

    private String link;

    public BlogReview(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
