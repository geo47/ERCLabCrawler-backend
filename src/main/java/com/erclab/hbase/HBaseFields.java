package com.erclab.hbase;

public class HBaseFields {

    /**
     * Drop tables if this value is set true.
     */
    public static boolean INITIALIZE_AT_FIRST = true;

    /**
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
     */
//    private final TableName table1 = TableName.valueOf("Table1");
    public static final String CF_IDENTIFIER = "identifier";
    public static final String CF_INFO = "info";
    public static final String CF_MENU = "menus";
    public static final String CF_REVIEW = "reviews";
    public static final String CF_CRAWL_INFO = "crawl_info";

    public static final String Q_ID = "_id";
    public static final String Q_SOURCE = "source";

    public static final String Q_REST_NAME = "rest_name";
    public static final String Q_PHONE_NO = "phone_no";
    public static final String Q_ADDRESS = "address";
    public static final String Q_WEBSITE = "website";
    public static final String Q_RATING = "rating";
    public static final String Q_URL = "url";

    public static final String Q_MENU = "menus";

    public static final String Q_REVIEW = "reviews";
    public static final String Q_BLOG_REVIEW = "blog_reviews";

    public static final String Q_LAST_UPDATE = "last_update";
}
