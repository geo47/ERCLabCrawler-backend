package com.erclab.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class SparkManager {

    private static Long STREAM_DURATION = 2000L;

    private SparkSession sparkSession;
    private JavaSparkContext sparkContext;
    private JavaStreamingContext sparkStreamingContext;

    public SparkManager(){

        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);
        Logger.getRootLogger().setLevel(Level.ERROR);

        /*SparkConf sparkConf = new SparkConf()
                .setAppName("SparkManager")
                .setMaster("local[*]");*/

        sparkSession = SparkSession.builder()
                .appName("SparkManager")
                .master("local[*]")
//                .config("spark.mongodb.input.uri", "mongodb://localhost:27017/erclab_restaurant_db.restaurant")
//                .config("spark.mongodb.output.uri", "mongodb://localhost:27017/erclab_restaurant_db.restaurant")
                .getOrCreate();

        sparkContext = new JavaSparkContext(sparkSession.sparkContext());
        sparkStreamingContext = new JavaStreamingContext(sparkContext, new Duration(STREAM_DURATION));

    }

    public JavaSparkContext getSparkContext() {
        return sparkContext;
    }

    public JavaStreamingContext getSparkStreamingContext() {
        return sparkStreamingContext;
    }

    public SparkSession getSparkSession() {
        return sparkSession;
    }
}
