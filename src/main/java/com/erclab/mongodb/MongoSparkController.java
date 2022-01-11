package com.erclab.mongodb;

import com.erclab.bean.RestaurantBean;
import com.erclab.spark.SparkManager;
import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.bson.Document;

public class MongoSparkController {

    public static void main(String[] args) {
        SparkManager sparkManager = new SparkManager();

        SparkSession sparkSession = sparkManager.getSparkSession();
        JavaSparkContext javaSparkContext = sparkManager.getSparkContext();

        JavaMongoRDD<Document> rdd = MongoSpark.load(javaSparkContext);
        System.out.println(rdd.count());
        System.out.println(rdd.first().toJson());

        Dataset<Row> documentDataset = rdd.toDF();
                sparkSession.createDataset(rdd.rdd(), Encoders.bean(Document.class));
        documentDataset.printSchema();
        // Load dataset with implicit dataset
        /*Dataset<Row> dataset = MongoSpark.load(javaSparkContext).toDF();

        dataset.printSchema();
//        dataset.show();
        dataset.createOrReplaceTempView("restaurant");

        Dataset<Row> row = sparkSession.sql("SELECT rest_name FROM restaurant WHERE _id = 1");
        row.show();*/



    }
}
