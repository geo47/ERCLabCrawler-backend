package com.erclab.hbase;

import com.erclab.bean.RestaurantBeanStr;
import com.erclab.spark.SparkManager;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class HBaseClientOnSparkOperations implements Serializable {

    private void get(Table table) throws IOException {

        SparkManager sparkManager = new SparkManager();

        System.out.println("TableName: "+table.getName().getNameAsString());
        Configuration hBaseConfig = HBaseClientApp.getHBaseConfiguration();
        hBaseConfig.set(TableInputFormat.INPUT_TABLE, table.getName().getNameAsString());

        JavaPairRDD<ImmutableBytesWritable, Result> hbasePairRDD =
                sparkManager.getSparkContext().newAPIHadoopRDD(
                        hBaseConfig,
                        TableInputFormat.class,
                        ImmutableBytesWritable.class,
                        Result.class);

        JavaRDD<RestaurantBeanStr> tableRDD = hbasePairRDD
                .flatMap(new FlatMapFunction<Tuple2<ImmutableBytesWritable, Result>, RestaurantBeanStr>() {
                    @Override
                    public Iterator<RestaurantBeanStr> call(Tuple2<ImmutableBytesWritable, Result>
                                                                 immutableBytesWritableResultTuple2) throws Exception {

                        ArrayList<RestaurantBeanStr> restaurantList = new ArrayList<>();

                        Result result = immutableBytesWritableResultTuple2._2;

                        String id = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_IDENTIFIER),
                                Bytes.toBytes(HBaseFields.Q_ID)));
                        String source = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_IDENTIFIER),
                                Bytes.toBytes(HBaseFields.Q_SOURCE)));

                        String restName = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_INFO),
                                Bytes.toBytes(HBaseFields.Q_REST_NAME)));
                        String phoneNo = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_INFO),
                                Bytes.toBytes(HBaseFields.Q_PHONE_NO)));
                        String address = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_INFO),
                                Bytes.toBytes(HBaseFields.Q_ADDRESS)));
                        String website = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_INFO),
                                Bytes.toBytes(HBaseFields.Q_WEBSITE)));
                        String rating = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_INFO),
                                Bytes.toBytes(HBaseFields.Q_RATING)));
                        String url = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_INFO),
                                Bytes.toBytes(HBaseFields.Q_URL)));

                        String menus = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_MENU),
                                Bytes.toBytes(HBaseFields.Q_MENU)));

                        String reviews = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_REVIEW),
                                Bytes.toBytes(HBaseFields.Q_REVIEW)));
                        String blogReviews = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_REVIEW),
                                Bytes.toBytes(HBaseFields.Q_BLOG_REVIEW)));

                        String lastUpdate = Bytes.toString(result.getValue(Bytes.toBytes(HBaseFields.CF_CRAWL_INFO),
                                Bytes.toBytes(HBaseFields.Q_LAST_UPDATE)));

                        System.out.println(
                                "id: "+id+"\n"+
                                "source: "+source+"\n"+
                                "restName: "+restName+"\n"+
                                "phoneNo: "+phoneNo+"\n"+
                                "address: "+address+"\n"+
                                "website: "+website+"\n"+
                                "rating: "+rating+"\n"+
                                "url: "+url+"\n"+
                                "menu: "+menus+"\n"+
                                "review: "+reviews+"\n"+
                                "blogReview: "+blogReviews+"\n"+
                                "lastUpdate: "+lastUpdate+"\n");

                        /*ArrayList<Menu> menuList = new Gson().fromJson(menus, new TypeToken<List<Menu>>(){}.getType());
                        ArrayList<Review> reviewList = new Gson().fromJson(reviews,
                                new TypeToken<List<Review>>(){}.getType());
                        ArrayList<BlogReview> blogReviewList = new Gson().fromJson(blogReviews,
                                new TypeToken<List<BlogReview>>(){}.getType());*/

                        restaurantList.add(new RestaurantBeanStr(
                                source, id, restName, phoneNo, address, website, rating, url, lastUpdate,
                                menus, reviews, blogReviews));

                        return restaurantList.iterator();
                    }
                });

        System.out.println("Count of RDD: "+tableRDD.count());

        // Encoders are created for Java beans
//        Encoder<RestaurantBeanStr> restaurantEncoder = Encoders.bean(RestaurantBeanStr.class);

        // Apply a schema to an RDD of JavaBeans to get DataFrame
        Dataset<Row> restaurantDF = sparkManager.getSparkSession()
                .createDataFrame(tableRDD, RestaurantBeanStr.class);
        // Register the DataFrame as a temporary view
        restaurantDF.createOrReplaceTempView("restaurant");
        restaurantDF.printSchema();

        // SQL statements can be run by using the sql methods provided by spark
        Dataset<Row> query = sparkManager.getSparkSession().sql("SELECT * FROM restaurant");

        // The columns of a row can be run by using the sql methods provided by spark
        Encoder<String> stringEncoder = Encoders.STRING();
//        Dataset<String> getResultByIndex = query.map((MapFunction<Row, String>) row -> "Name: "+ row.get(2),
//                stringEncoder);
//        getResultByIndex.show();

        // Or by field name
        Dataset<String> getResultByField = query.map((MapFunction<Row, String>) row -> "Name: "+
                        row.<String>getAs("restName"),
                stringEncoder);
        getResultByField.show();

        System.out.println("Done. ");
    }

    public void run(Table table) {

        if(null != table){
            try {
                get(table);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Operation can not performed...");
            System.out.println("Table is Null!");
        }

    }
}
