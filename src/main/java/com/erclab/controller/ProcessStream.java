package com.erclab.controller;

import com.erclab.bean.*;
import com.erclab.mongodb.MongoDbClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import java.util.ArrayList;
import java.util.List;

public class ProcessStream {

    private static int duplicateDoc = 0;
    boolean duplicateBlogReview = false;

    public void process(RestaurantBean restaurantBean){

        MongoDbClient mongoDbClient = new MongoDbClient();

        System.out.println(restaurantBean.getRestName());

        String _id = restaurantBean.get_id();
        String source = restaurantBean.getSource();
        String rating = restaurantBean.getRating();

        RestaurantBean dbRestaurant = mongoDbClient.getRestaurantByObjectId(_id);

        if(null == dbRestaurant){

            System.out.println("No restaurant found");

            Rating ratings = new Rating();
            if(!rating.equals("")){
                if(source.equals("KAKAO")){
                    ratings.setKakaoRating(rating);
                } else if(source.equals("NAVER")){
                    ratings.setNaverRating(rating);
                } else if(source.equals("GOOGLE")){
                    ratings.setGoogleRating(rating);
                } else if(source.equals("MANGOPLATE")){
                    ratings.setMangoplateRating(rating);
                }
            }
            restaurantBean.setRatings(ratings);

            // Add URL of the blog/website
            List<URLs> urls = new ArrayList<>();
            URLs urLs = new URLs(source, restaurantBean.getUrl());
            urls.add(urLs);
            restaurantBean.setUrls(urls);

            // Add Menus
            List<Menu> menuList = restaurantBean.getMenus();
            menuList.forEach(menu -> {
                menu.setRating(rating);
                menu.setPrice(menu.getPrice().replaceAll("[^\\d]", ""));
            });

            // Add Reviews
            if(null != restaurantBean.getReviews()){
                List<Review> reviewList = restaurantBean.getReviews();
                reviewList.forEach(review -> {review.setSource(source);});
            } else {
                restaurantBean.setReviews(new ArrayList<>());
            }

            if(null == restaurantBean.getBlogReview()){
                restaurantBean.setBlogReview(new ArrayList<>());
            }

            String restaurantObjStr = new Gson().toJson(restaurantBean);
            JsonObject jsonObject = new JsonParser().parse(restaurantObjStr).getAsJsonObject();
            jsonObject.remove("url");
            jsonObject.remove("source");

            System.out.println("Inserting......");
            mongoDbClient.insertCollection((BasicDBObject) JSON.parse(jsonObject.toString()));
            System.out.println("Inserted Successfully.");
            System.out.println("Fetching ......");
            RestaurantBean bean = mongoDbClient.getRestaurantByObjectId(restaurantBean.get_id());
            System.out.println("menus length (add):"+bean.getMenus().size());

        } else {
            System.out.println("restaurant found");
            System.out.println(new Gson().toJson(dbRestaurant));
            duplicateDoc++;

            if(dbRestaurant.getPhoneNo().equals("")){
                dbRestaurant.setPhoneNo(restaurantBean.getPhoneNo());
            }

            if(dbRestaurant.getWebsite().equals("")){
                dbRestaurant.setWebsite(restaurantBean.getWebsite());
            }

            Rating ratings = dbRestaurant.getRatings();
            if(!rating.equals("")){
                if(source.equals("KAKAO")){
                    ratings.setKakaoRating(rating);
                } else if(source.equals("NAVER")){
                    ratings.setNaverRating(rating);
                } else if(source.equals("GOOGLE")){
                    ratings.setGoogleRating(rating);
                } else if(source.equals("MANGOPLATE")){
                    ratings.setMangoplateRating(rating);
                }
            }
            restaurantBean.setRatings(ratings);


            // Update URL of the blog/website
            dbRestaurant.getUrls().add(new URLs(source, restaurantBean.getUrl()));

            if(dbRestaurant.getMenus().size() == 0){
                dbRestaurant.setMenus(restaurantBean.getMenus());
            }

            float totalMenuRating = 0;
            int ratingSourceCount = 0;
            if(!ratings.getGoogleRating().equals("-")){
                totalMenuRating += Float.parseFloat(ratings.getGoogleRating());
                ratingSourceCount++;
            }

            if(!ratings.getKakaoRating().equals("-")){
                totalMenuRating += Float.parseFloat(ratings.getKakaoRating());
                ratingSourceCount++;
            }

            if(!ratings.getNaverRating().equals("-")){
                totalMenuRating += Float.parseFloat(ratings.getNaverRating());
                ratingSourceCount++;
            }

            if(!ratings.getMangoplateRating().equals("-")){
                totalMenuRating += Float.parseFloat(ratings.getMangoplateRating());
                ratingSourceCount++;
            }

            if(!ratings.getSiksinhotRating().equals("-")){
                totalMenuRating += Float.parseFloat(ratings.getSiksinhotRating());
                ratingSourceCount++;
            }

            if(!ratings.getDiningCodeRating().equals("-")){
                totalMenuRating += Float.parseFloat(ratings.getDiningCodeRating());
                ratingSourceCount++;
            }

            String avgMenuRating = String.valueOf((totalMenuRating/ratingSourceCount));
            dbRestaurant.getMenus().forEach(menu -> {menu.setRating(avgMenuRating);});
            dbRestaurant.setRating(avgMenuRating);

            System.out.println("menus length (update):"+dbRestaurant.getMenus().size());

            // Add Reviews
            System.out.println("review length (current):"+restaurantBean.getReviews().size());

            restaurantBean.getReviews().forEach(review -> {
                review.setSource(source);
                dbRestaurant.getReviews().add(review);
            });


            restaurantBean.getBlogReview().forEach(blogReview -> {
                duplicateBlogReview = false;
                for(BlogReview dbBlogReview:dbRestaurant.getBlogReview()){
                    if(blogReview.getLink().equalsIgnoreCase(dbBlogReview.getLink())){
                        duplicateBlogReview = true;
                        break;
                    }
                }

                if (!duplicateBlogReview)
                    dbRestaurant.getBlogReview().add(blogReview);
            });

            String restaurantObjStr = new Gson().toJson(dbRestaurant);
            JsonObject jsonObject = new JsonParser().parse(restaurantObjStr).getAsJsonObject();

            System.out.println("Updating......");
            mongoDbClient.updateCollection(_id, (BasicDBObject) JSON.parse(jsonObject.toString()));
            System.out.println("Updated Successfully.");
            System.out.println("Fetching ......");
            RestaurantBean bean = mongoDbClient.getRestaurantByObjectId(restaurantBean.get_id());
            System.out.println("review length (after):"+bean.getReviews().size());
            System.out.println("duplicateDoc: "+duplicateDoc);

        }
    }
}
