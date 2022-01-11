package com.erclab.data.pipeline;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class KafkaProducerFile {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);
        Logger.getLogger("warn").setLevel(Level.OFF);

        final String fileName = "/home/muzamil/IdeaProjects/apache-spark-kafka/src/main/resources/spark_example.txt";
        String line;
//        String topicName = "ERCLabCrawler";
        String topicName = "test";

        final KafkaProducer<String, String> kafkaProducer;
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("client.id", "KafkaFileProducer");

        kafkaProducer = new KafkaProducer<String, String>(properties);
        int count = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter something: ");
        System.out.println("Line: "+scanner.nextLine());
        while (!scanner.nextLine().equals("exit")){
            count++;
            System.out.println("Yes here");
            kafkaProducer.send(new ProducerRecord<String, String>(topicName, Integer.toString(count), scanner.nextLine()));
        }
//        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
//            while ((line = bufferedReader.readLine()) != null){
//                count++;
//                kafkaProducer.send(new ProducerRecord<String, String>(topicName, Integer.toString(count), "scanner.nextLine()"));
//                kafkaProducer.send(new ProducerRecord<String, String>(topicName, Integer.toString(count), line));
//            }
//        } catch (IOException e){
//            e.printStackTrace();
//        }

    }
}
