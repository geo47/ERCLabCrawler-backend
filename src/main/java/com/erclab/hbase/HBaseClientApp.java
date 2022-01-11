package com.erclab.hbase;

import com.erclab.bean.RestaurantBean;
import com.google.protobuf.ServiceException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HBaseClientApp {

    /**
     * The identifier for the application table.
     * */
    private static final TableName TABLE_NAME = TableName.valueOf("REST_KAKAO");

    /**
     * The name of the column family used by the application.
     * */
    private static final byte[] CF = Bytes.toBytes("cf");

    public static void main(String[] args) throws IOException {
        Table table = new HBaseClientApp().connect(TABLE_NAME);

        HBaseClientOperations HBaseClientOperations = new HBaseClientOperations();
        HBaseClientOperations.run(table);

//        HBaseClientOnSparkOperations HBaseClientOnSparkOperations = new HBaseClientOnSparkOperations();
//        HBaseClientOnSparkOperations.run(table);
    }

    public int run(RestaurantBean restaurantBean) throws Exception {
        Table table = new HBaseClientApp().connect(getTableName(restaurantBean.getSource()));

        HBaseClientOperations HBaseClientOperations = new HBaseClientOperations(restaurantBean);
        HBaseClientOperations.run(table);
        return 0;
    }

    private Table connect(TableName tableName) throws IOException {

        /**
         * Connection to the cluster. A single connection shared by all
         * application threads.
         * */
        Connection connection = ConnectionFactory.createConnection(getHBaseConfiguration());

        /**
         * A lightweight handle to a specific table. Used for a single thread.
         * */
        Table table = connection.getTable(tableName);
        return table;
    }

    private TableName getTableName(String source){
        if(source.equals("KAKAO"))
            return TableName.valueOf("REST_KAKAO");
        else if(source.equals("NAVER"))
            return TableName.valueOf("REST_NAVER");
        else{
            System.out.println("Table can not be fetched...!");
            System.out.println("Sourcein the RestaurantBean is null.");
            return null;
        }
    }

    public static Configuration getHBaseConfiguration(){
        Configuration config = HBaseConfiguration.create();
        config.addResource(new Path("/usr/local/HBase/HBase-2.2.3/conf/hbase-default.xml"));
        config.addResource(new Path("/usr/local/HBase/HBase-2.2.3/conf/hbase-site.xml"));
        return config;
    }
    private String fixJSON(String line) {
        return line.replace("][", ",\n").replace("[", "").replace("]", "").replace("}},", "}}");
    }
}
