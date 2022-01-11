package com.erclab.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Review implements Serializable {

    private String rating;
    private String review;
    private String reviewer;
    @SerializedName("review_date")
    private String reviewDate;
    private String spamScore;
    private String source;

    public Review() {}

    public Review(String rating, String review, String reviewer, String reviewDate) {
        this.rating = rating;
        this.review = review;
        this.reviewer = reviewer;
        this.reviewDate = reviewDate;
    }

    public String getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public String getReviewer() {
        return reviewer;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getSpamScore() {
        return spamScore;
    }

    public void setSpamScore(String spamScore) {
        this.spamScore = spamScore;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
