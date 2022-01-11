package com.erclab.data.pipeline;

import com.erclab.bean.Rating;
import com.erclab.bean.RestaurantBean;
import com.erclab.controller.ProcessStream;
import com.erclab.mongodb.MongoDbClient;
import com.erclab.spark.SparkManager;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;

import java.util.*;

public class KafkaSparkStream {

    static String myObj = null;
    public static void main(String[] args) throws InterruptedException {

        SparkManager sparkManager = new SparkManager();
        /*SparkConf sparkConf = new SparkConf().setAppName("kafkaSparkStream")
                .setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(10000));*/

        Map<String, Object> kafkaParams = new HashMap<String, Object>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", "1");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList("ERCLabCrawler");
//        Collection<String> topics = Arrays.asList("test");

        JavaInputDStream<ConsumerRecord<String, String>> kafkaSparkInputDStream = KafkaUtils
                .createDirectStream(sparkManager.getSparkStreamingContext(),
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, String> Subscribe(topics, kafkaParams));

        JavaPairDStream<String, String> results = kafkaSparkInputDStream.mapToPair(record -> new Tuple2<>(record.key(), record.value()));
        results.print();
        results.foreachRDD(rdd -> rdd.collect().forEach(
                restObj -> {
                    myObj = restObj._2;
                    streamReceived(myObj);
                }
        ));
        sparkManager.getSparkStreamingContext().start();
        sparkManager.getSparkStreamingContext().awaitTermination();
    }

    private static void streamReceived(String streamData){
        if(null != streamData){
            System.out.println("myObj: "+streamData);

            Gson gson = new Gson();
            RestaurantBean restaurantBean = gson.fromJson(streamData, RestaurantBean.class);
            new ProcessStream().process(restaurantBean);

            /*try {

//                new HBaseClientApp().run(restaurantBean);
                new MongoDbClient().insertCollection((BasicDBObject) JSON.parse(gson.toJson(restaurantBean)));
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }
}
