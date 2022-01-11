package com.erclab.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RestaurantBean implements Serializable {

    private String source;
    private String _id;
    @SerializedName("rest_name")
    private String restName;
    @SerializedName("phone_no")
    private String phoneNo;
    private String address;
    private String website;
    private String rating;
    private Rating ratings;
    private String url;
    private List<URLs> urls;
    @SerializedName("last_update")
    private String lastUpdate;
    private String credibilityScore;
    private List<Menu> menus;
    private List<Review> reviews;
    @SerializedName("blog_reviews")
    private List<BlogReview> blogReview;

    public RestaurantBean(String source, String _id, String restName, String phoneNo,
                          String address, String website, String rating, String url,
                          String lastUpdate, List<Menu> menus, List<Review> reviews,
                          List<BlogReview> blogReview) {
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

    public void setSource(String source) {
        this.source = source;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Rating getRatings() {
        return ratings;
    }

    public void setRatings(Rating ratings) {
        this.ratings = ratings;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<URLs> getUrls() {
        return urls;
    }

    public void setUrls(List<URLs> urls) {
        this.urls = urls;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCredibilityScore() {
        return credibilityScore;
    }

    public void setCredibilityScore(String credibilityScore) {
        this.credibilityScore = credibilityScore;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<BlogReview> getBlogReview() {
        return blogReview;
    }

    public void setBlogReview(List<BlogReview> blogReview) {
        this.blogReview = blogReview;
    }
}
