package com.erclab.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RestaurantBeanStr implements Serializable {

    private String source;
    private String _id;
    @SerializedName("rest_name")
    private String restName;
    @SerializedName("phone_no")
    private String phoneNo;
    private String address;
    private String website;
    private String rating;
    private String url;
    @SerializedName("last_update")
    private String lastUpdate;
    private String credibilityScore;
    private String menus;
    private String reviews;
    @SerializedName("blog_reviews")
    private String blogReview;

    public RestaurantBeanStr(String source, String _id, String restName, String phoneNo,
                          String address, String website, String rating, String url,
                          String lastUpdate, String menus, String reviews,
                          String blogReview) {
        this.source = source;
        this._id = _id;
        this.restName = restName;
        this.phoneNo = phoneNo;
        this.address = address;
        this.website = website;
        this.rating = rating;
        this.url = url;
        this.lastUpdate = lastUpdate;
        this.menus = menus;
        this.reviews = reviews;
        this.blogReview = blogReview;
    }

    public String getSource() {
        return source;
    }

    public String get_id() {
        return _id;
    }

    public String getRestName() {
        return restName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public String getRating() {
        return rating;
    }

    public String getUrl() {
        return url;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getCredibilityScore() {
        return credibilityScore;
    }

    public void setCredibilityScore(String credibilityScore) {
        this.credibilityScore = credibilityScore;
    }

    public String getMenus() {
        return menus;
    }

    public String getReviews() {
        return reviews;
    }

    public String getBlogReview() {
        return blogReview;
    }
}
