package com.example.stick.DB;

public class DBConstants {
    public static final String DB_NAME = "Stick.db";
    public static final int DB_VERSION = 1;

    //TABLE1 COLUMNS
    public static final String T1_NAME = "notes";
    public static final String T1_ID = "ID";
    public static final String T1_TITLE = "Title";
    public static final String T1_COLOR = "Color";
    public static final String T1_DATE = "Date";
    //public static final String T1_TYPE = "TYPE";
    //public static final String T1_STATUS = "STATUS";
    public static final String T1_QUERY = "CREATE TABLE " + T1_NAME + " ("
            + T1_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + T1_TITLE + " TEXT, "
            + T1_COLOR + " TEXT, "
            + T1_DATE + " INTEGER)";
    //TABLE2 COLUMNS
    public static final String T2_NAME = "tasks";
    public static final String T2_ID = "ID";
    public static final String T2_CONTENT = "Content";
    public static final String T2_STATUS = "Status";
    public static final String T2_DATE = "Date";
    public static final String T2_PARENTID = "ParentID";
    //public static final String T1_TYPE = "TYPE";
    //public static final String T1_STATUS = "STATUS";
    public static final String T2_QUERY = "CREATE TABLE " + T2_NAME + " ("
            + T2_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + T2_CONTENT + " TEXT, "
            + T2_STATUS + " INTEGER, "
            + T2_DATE + " INTEGER, "
            + T2_PARENTID + " INTEGER)";
}
