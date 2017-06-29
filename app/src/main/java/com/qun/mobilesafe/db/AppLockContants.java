package com.qun.mobilesafe.db;

/**
 * Created by Qun on 2017/6/29.
 */

public interface AppLockContants {
    String DB_NAME = "applock";//数据库名
    int DB_VERSION = 1;
    String TABLE_NAME = "applock";//表名
    String COLUMN_PACKAGE_NAME = "package_name";//包名字段

    String SQL = "create table " + TABLE_NAME + " (_id integer primary key autoincrement ," + COLUMN_PACKAGE_NAME + " varchar(200));";
}
