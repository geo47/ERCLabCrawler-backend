package com.erclab.bean;

public class Rating {

    private String kakaoRating;
    private String naverRating;
    private String googleRating;
    private String siksinhotRating;
    private String mangoplateRating;
    private String diningCodeRating;

    public Rating() {
        kakaoRating = "-";
        naverRating = "-";
        googleRating = "-";
        siksinhotRating = "-";
        mangoplateRating = "-";
        diningCodeRating = "-";
    }

    public String getKakaoRating() {
        return kakaoRating;
    }

    public void setKakaoRating(String kakaoRating) {
        this.kakaoRating = kakaoRating;
    }

    public String getNaverRating() {
        return naverRating;
    }

    public void setNaverRating(String naverRating) {
        this.naverRating = naverRating;
    }

    public String getGoogleRating() {
        return googleRating;
    }

    public void setGoogleRating(String googleRating) {
        this.googleRating = googleRating;
    }

    public String getSiksinhotRating() {
        return siksinhotRating;
    }

    public void setSiksinhotRating(String siksinhotRating) {
        this.siksinhotRating = siksinhotRating;
    }

    public String getMangoplateRating() {
        return mangoplateRating;
    }

    public void setMangoplateRating(String mangoplateRating) {
        this.mangoplateRating = mangoplateRating;
    }

    public String getDiningCodeRating() {
        return diningCodeRating;
    }

    public void setDiningCodeRating(String diningCodeRating) {
        this.diningCodeRating = diningCodeRating;
    }
}
