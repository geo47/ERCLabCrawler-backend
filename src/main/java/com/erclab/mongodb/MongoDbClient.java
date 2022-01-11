package com.erclab.mongodb;

import com.erclab.bean.Menu;
import com.erclab.bean.RestaurantBean;
import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MongoDbClient {

    private static final String HOST = "localhost";
    private static final Integer PORT = 27017;
    private static final String CONNECTION_URI = "mongodb://"+HOST+":"+PORT;

    private static final String DB_NAME = "erclab_restaurant_db";
    private static final String COLLECTION_RESTAURANT = "restaurant";

    private static MongoClient mongoClient;

    private MongoClient getMongoClient(){
        if(mongoClient == null){
//            mongoClient = new MongoClient(new MongoClientURI(CONNECTION_URI));
            mongoClient = new MongoClient(HOST, PORT);
        }
        return mongoClient;
    }

    private DB getDB(){
        return getMongoClient().getDB(DB_NAME);
    }

    private DBCollection getRestaurantCollection(){
        return getDB().getCollection(COLLECTION_RESTAURANT);
    }

    private void createIndexNMA(){
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.append("rest_name", "text");
        basicDBObject.append("address", "text");
        basicDBObject.append("menus.title", "text");
        getRestaurantCollection().createIndex(basicDBObject);
    }

    public void insertCollection(BasicDBObject dbObject){
        getRestaurantCollection().insert(dbObject);
    }

    public void updateCollection(String id, BasicDBObject dbObject){
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("_id", id);
        getRestaurantCollection().update(basicDBObject, dbObject);
    }

    /*public void updateCollectionForRatingType(){
        DBCollection dbCollection = getRestaurantCollection();

        BasicDBObject basicDBObject = new BasicDBObject();
        DBCursor cursor = dbCollection.find(basicDBObject);

        while (cursor.hasNext()){
            DBObject cur = cursor.next();
            String id = cur.get("_id").toString();
            String rating = cur.get("menus.rating").toString();

            float updateRating = Float.parseFloat(rating)


        }
    }*/

    public List<RestaurantBean> getAllRestaurants(){
        List<RestaurantBean> restaurantBeanList = new ArrayList<>();
        DBCursor cursor = getRestaurantCollection().find(new BasicDBObject());
        if(cursor.count() > 0){
            while (cursor.hasNext()){
                String object = cursor.next().toString();
//                System.out.println(object);
                restaurantBeanList.add(new Gson().fromJson(object, RestaurantBean.class));
            }
        }
        return restaurantBeanList;
    }

    public RestaurantBean getRestaurantByObjectId(String id){
        RestaurantBean restaurantBean = null;
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", id);

        /*BasicDBObject projection = new BasicDBObject();
        projection.put("rest_name", 1);
        projection.put("website", 1);
        projection.put("phone_no", 1);
        projection.put("rating", 1);
        projection.put("menus", 1);*/

        DBCursor cursor = getRestaurantCollection().find(searchQuery/*, projection*/);

        if(cursor.count() > 0){
            while (cursor.hasNext()){
                String object = cursor.next().toString();
//                System.out.println(object);
                restaurantBean =  new Gson().fromJson(object, RestaurantBean.class);
                return restaurantBean;
            }
        }
        return null;
    }

    public List<RestaurantBean> getRestaurantByMenu(String menu){

        List<RestaurantBean> restaurantBeanList = new ArrayList<>();
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("menus.title", new BasicDBObject("$regex", String.format(".*%s.*", menu)));

        BasicDBObject projection = new BasicDBObject();
        projection.put("rest_name", 1);
        projection.put("address", 1);
        projection.put("website", 1);
        projection.put("phoneNo", 1);
        projection.put("rating", 1);
        projection.put("menus", 1);

//        DBObject sortByMenuRating = new BasicDBObject("menus.$.rating", -1);

        DBCursor cursor = getRestaurantCollection().find(searchQuery, projection);//.sort(sortByMenuRating);

        if(cursor.count() > 0){
            while (cursor.hasNext()){
                String object = cursor.next().toString();
//                System.out.println(object);
                restaurantBeanList.add(new Gson().fromJson(object, RestaurantBean.class));
            }
        }

        return restaurantBeanList;
    }

//    흑석로
    public List<RestaurantBean> getRestaurantByAddress(String address){

        List<RestaurantBean> restaurantBeanList = new ArrayList<>();
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("address", new BasicDBObject("$regex", String.format(".*%s.*", address)));

        BasicDBObject projection = new BasicDBObject();
        projection.put("rest_name", 1);
        projection.put("address", 1);
        projection.put("website", 1);
        projection.put("phoneNo", 1);
        projection.put("rating", 1);
        projection.put("menus", 1);

        DBCursor cursor = getRestaurantCollection().find(searchQuery, projection);

        if(cursor.count() > 0){
            while (cursor.hasNext()){
                String object = cursor.next().toString();
//                System.out.println(object);
                restaurantBeanList.add(new Gson().fromJson(object, RestaurantBean.class));
            }
        }
        return restaurantBeanList;
    }

    public List<RestaurantBean> getRestaurantByTextSearch(String query){

        List<RestaurantBean> restaurantBeanList = new ArrayList<>();
        DBObject search = new BasicDBObject("$search", query);
        DBObject textSearch = new BasicDBObject("$text", search);
        DBObject scoreSort = new BasicDBObject("score",new BasicDBObject("$meta", "textScore"));

        DBCursor cursor = getRestaurantCollection().find(textSearch, scoreSort).sort(scoreSort);
        System.out.println("Found search matches: "+cursor.count());

        if(cursor.count() > 0){
            while (cursor.hasNext()){
                String object = cursor.next().toString();
//                System.out.println(object);
                restaurantBeanList.add(new Gson().fromJson(object, RestaurantBean.class));
            }
        }
        return restaurantBeanList;
    }

    public List<RestaurantBean> getRestaurantByMenuAndAddress(String menu, String address){

        List<RestaurantBean> restaurantBeanList = new ArrayList<>();
        DBObject qMenu = new BasicDBObject("menus.title", new BasicDBObject("$regex", String.format(".*%s.*", menu)));
        DBObject qAddress = new BasicDBObject("address", new BasicDBObject("$regex", String.format(".*%s.*", address)));
        BasicDBList basicDBList = new BasicDBList();
        basicDBList.add(qMenu);
        basicDBList.add(qAddress);

        DBObject query = new BasicDBObject("$and", basicDBList);
        DBCursor cursor = getRestaurantCollection().find(query);
        System.out.println("Found search matches: "+cursor.count());

        if(cursor.count() > 0){
            while (cursor.hasNext()){
                String object = cursor.next().toString();
//                System.out.println(object);
                restaurantBeanList.add(new Gson().fromJson(object, RestaurantBean.class));
            }
        }
        return restaurantBeanList;
    }

    public void emptyRestaurantCollection(){
        getRestaurantCollection().remove(new BasicDBObject());
    }

    public void deleteRestaurantByObjectId(String id){
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("_id", id);
        WriteResult result = getRestaurantCollection().remove(searchQuery);
    }

    public static void main(String[] args) {
        MongoDbClient mongoDbClient = new MongoDbClient();
        // print existing collection
        mongoDbClient.getDB().getCollectionNames().forEach(System.out::println);
//        mongoDbClient.createIndexNMA();
        RestaurantBean bean = mongoDbClient.getRestaurantByObjectId("13");
        System.out.println(new Gson().toJson(bean));
//        int totalRecordsInDB = mongoDbClient.getAllRestaurants();
//        System.out.println("totalRecordsInDB: "+totalRecordsInDB);
//        mongoDbClient.emptyRestaurantCollection();
//        mongoDbClient.deleteRestaurantByObjectId("1");
//        List<RestaurantBean> restaurantBeanList = mongoDbClient.getRestaurantByMenu("짬뽕");
//        for(RestaurantBean restaurantBean:restaurantBeanList){
//            System.out.println("menus: "+new Gson().toJson(restaurantBean.getMenus()));
//        }
//        List<RestaurantBean> restaurantBeanList = mongoDbClient.getRestaurantByAddress("흑석로");
//        List<RestaurantBean> restaurantBeanList = mongoDbClient.getRestaurantByTextSearch("흑석로 짬뽕");
        /*List<RestaurantBean> restaurantBeanList = mongoDbClient.getRestaurantByMenuAndAddress("짬뽕", "흑석로");
        for(RestaurantBean restaurantBean:restaurantBeanList){
            System.out.println("name: "+restaurantBean.getRestName()+
                    " - address: "+restaurantBean.getAddress()+
                    " - menus: "+new Gson().toJson(restaurantBean.getMenus()));
        }
        System.out.println("RecordsFoundInDB: "+restaurantBeanList.size());*/

    }
}
