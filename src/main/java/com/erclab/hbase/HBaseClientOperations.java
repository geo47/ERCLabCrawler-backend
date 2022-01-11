package com.erclab.hbase;

import com.erclab.bean.BlogReview;
import com.erclab.bean.Menu;
import com.erclab.bean.RestaurantBean;
import com.erclab.bean.Review;
import com.erclab.spark.SparkManager;
import com.erclab.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
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
import scala.Tuple2;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class HBaseClientOperations implements Serializable {

    /**
     * Drop tables if this value is set true.
     *//*
    static boolean INITIALIZE_AT_FIRST = true;

    *//**
     * <table>
     * <tr>
     * <tb>Row1</tb> <tb>Family1:Qualifier1</tb> <tb>Family1:Qualifier2</tb>
     * </tr>
     * <tr>
     * <tb>Row2</tb> <tb>Family1:Qualifier1</tb> <tb>Family2:Qualifier3</tb>
     * </tr>
     * <tr>
     * <tb>Row3</tb> <tb>Family1:Qualifier1</tb> <tb>Family2:Qualifier3</tb>
     * </tr>
     * </table>
     *//*
//    private final TableName table1 = TableName.valueOf("Table1");
    private final String fIdentifier = "identifier";
    private final String fInfo = "info";
    private final String fMenu = "menus";
    private final String fReview = "reviews";
    private final String fCrawlInfo = "crawl_info";

//    private final byte[] row1 = Bytes.toBytes("Row1");
//    private final byte[] row2 = Bytes.toBytes("Row2");
//    private final byte[] row3 = Bytes.toBytes("Row3");
    private final String qId = "_id";
    private final String qSource = "source";

    private final String qRestName = "rest_name";
    private final String qPhoneNo = "phone_no";
    private final String qAddress = "address";
    private final String qWebsite = "website";
    private final String qRating = "rating";
    private final String qUrl = "url";

    private final String qMenu = "menus";

    private final String qReview = "reviews";
    private final String qBlogReview = "blog_reviews";

    private final String qLastUpdate = "last_update";*/

    private RestaurantBean restaurantBean;

    public HBaseClientOperations(){}

    public HBaseClientOperations(RestaurantBean restaurantBean){
        this.restaurantBean = restaurantBean;
    }

    /*private void createTable(Admin admin) throws IOException {
        HTableDescriptor desc = new HTableDescriptor(table1);
        desc.addFamily(new HColumnDescriptor(family1));
        desc.addFamily(new HColumnDescriptor(family2));
        admin.createTable(desc);
    }*/

    /*private void delete(Table table) throws IOException {
        final byte[] rowToBeDeleted =  Bytes.toBytes("RowToBeDeleted");
        System.out.println("\n*** DELETE ~Insert data and then delete it~ ***");

        System.out.println("Inserting a data to be deleted later.");
        Put put = new Put(rowToBeDeleted);
        put.addColumn(family1.getBytes(), qualifier1, cellData);
        table.put(put);

        Get get = new Get(rowToBeDeleted);
        Result result = table.get(get);
        byte[] value = result.getValue(family1.getBytes(), qualifier1);
        System.out.println("Fetch the data: " + Bytes.toString(value));
        assert Arrays.equals(cellData, value);

        System.out.println("Deleting");
        Delete delete = new Delete(rowToBeDeleted);
        delete.addColumn(family1.getBytes(), qualifier1);
        table.delete(delete);

        result = table.get(get);
        value = result.getValue(family1.getBytes(), qualifier1);
        System.out.println("Fetch the data: " + Bytes.toString(value));
        assert Arrays.equals(null, value);

        System.out.println("Done. ");
    }*/

    /*private void deleteTable(Admin admin) throws IOException {
        if (admin.tableExists(table1)) {
            admin.disableTable(table1);
            admin.deleteTable(table1);
        }
    }*/

    /*private void filters(Table table) throws IOException {
        System.out.println("\n*** FILTERS ~ scanning with filters to fetch a row of which key is larget than \"Row1\"~ ***");
        Filter filter1 = new PrefixFilter(row1);
        Filter filter2 = new QualifierFilter(CompareOp.GREATER_OR_EQUAL, new BinaryComparator(
                qualifier1));

        List<Filter> filters = Arrays.asList(filter1, filter2);

        Scan scan = new Scan();
        scan.setFilter(new FilterList(Operator.MUST_PASS_ALL, filters));

        try (ResultScanner scanner = table.getScanner(scan)) {
            int i = 0;
            for (Result result : scanner) {
                System.out.println("Filter " + scan.getFilter() + " matched row: " + result);
                i++;
            }
            assert i == 2 : "This filtering sample should return 1 row but was " + i + ".";
        }
        System.out.println("Done. ");
    }*/

    private void get(Table table) throws IOException{

        Get g = new Get(Bytes.toBytes("row_null"));
        Result r = table.get(g);
        byte[] value = r.getValue(Bytes.toBytes(HBaseFields.CF_REVIEW), Bytes.toBytes(HBaseFields.Q_REVIEW));
        System.out.println("Fetched value: "+Bytes.toString(value));
    }

    private void put(Table table) throws IOException {

        // Instantiate Put class
        Put p = new Put(Bytes.toBytes("rest1"));

        // add values using addColumn() methods
        // Row1 => Family1:Qualifier1, Family1:Qualifier2
        System.out.println("ID: "+this.restaurantBean.get_id());
        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_IDENTIFIER),
                Bytes.toBytes(HBaseFields.Q_ID),
                Bytes.toBytes("row_"+this.restaurantBean.get_id()));
        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_IDENTIFIER),
                Bytes.toBytes(HBaseFields.Q_SOURCE),
                Bytes.toBytes(this.restaurantBean.getSource()));

        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_INFO),
                Bytes.toBytes(HBaseFields.Q_REST_NAME),
                Bytes.toBytes(this.restaurantBean.getRestName()));
        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_INFO),
                Bytes.toBytes(HBaseFields.Q_PHONE_NO),
                Bytes.toBytes(this.restaurantBean.getPhoneNo()));
        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_INFO),
                Bytes.toBytes(HBaseFields.Q_ADDRESS),
                Bytes.toBytes(this.restaurantBean.getAddress()));
        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_INFO),
                Bytes.toBytes(HBaseFields.Q_WEBSITE),
                Bytes.toBytes(this.restaurantBean.getWebsite()));
        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_INFO),
                Bytes.toBytes(HBaseFields.Q_RATING),
                Bytes.toBytes(this.restaurantBean.getRating()));
        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_INFO),
                Bytes.toBytes(HBaseFields.Q_URL),
                Bytes.toBytes(this.restaurantBean.getUrl()));

        String jsonFromMenuArray = new GsonBuilder().create().toJson(this.restaurantBean.getMenus());
        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_MENU),
                Bytes.toBytes(HBaseFields.Q_MENU),
                Bytes.toBytes(jsonFromMenuArray));
        System.out.println("jsonFromMenuArray: "+jsonFromMenuArray);

        String jsonFromReviewList = new GsonBuilder().create().toJson(this.restaurantBean.getReviews());
        String jsonFromBlogReviewList = new GsonBuilder().create().toJson(this.restaurantBean.getBlogReview());
        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_REVIEW),
                Bytes.toBytes(HBaseFields.Q_REVIEW),
                Bytes.toBytes(jsonFromReviewList));
        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_REVIEW),
                Bytes.toBytes(HBaseFields.Q_BLOG_REVIEW),
                Bytes.toBytes(jsonFromBlogReviewList));
        System.out.println("jsonFromReviewList: "+jsonFromReviewList);
        System.out.println("jsonFromBlogReviewList: "+jsonFromBlogReviewList);

        p.addColumn(
                Bytes.toBytes(HBaseFields.CF_CRAWL_INFO),
                Bytes.toBytes(HBaseFields.Q_LAST_UPDATE),
                Bytes.toBytes((this.restaurantBean.getLastUpdate() == null) ?
                        DateUtil.getFormattedDate(new Date()) :
                        this.restaurantBean.getLastUpdate()));

        table.put(p);

        System.out.println("Data inserted successfully...");
        table.close();
    }

    public void run(Table table) {

        if(null != table){
            try {
//                put(table);
                get(table);
//                delete(table);
            } catch (IOException e) {
                e.printStackTrace();
            }
//        get(table);
//        scan(table);
//        filters(table);
//        delete(table);
        } else {
            System.out.println("Operation can not performed...");
            System.out.println("Table is Null!");
        }

    }

    /*private void scan(Table table) throws IOException {
        System.out.println("\n*** SCAN example ~fetching data in Family1:Qualifier1 ~ ***");

        Scan scan = new Scan();
        scan.addColumn(family1.getBytes(), qualifier1);

        try (ResultScanner scanner = table.getScanner(scan)) {
            for (Result result : scanner)
                System.out.println("Found row: " + result);
        }
        System.out.println("Done.");
    }*/
}
