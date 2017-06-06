package com.qun.mobilesafe.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by Qun on 2017/6/5.
 */

public class CommonNumberDb {

    // 获取组的数量
    public static int getGroupCount(Context context) {

        // select count(*) from classlist;
        File dbFile = new File(context.getFilesDir(), "commonnum.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("select count(*) from classlist", null);
        int groupCount = 0;
        if (cursor != null && cursor.moveToNext()) {
            groupCount = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return groupCount;
    }

    // 获取对应组的子条目数量
    public static int getChildrenCount(Context context, int groupPosition) {
        // select count(*) from table2
        File dbFile = new File(context.getFilesDir(), "commonnum.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("select count(*) from table" + String.valueOf(groupPosition + 1), null);
        int childCount = 0;
        if (cursor != null && cursor.moveToNext()) {
            childCount = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return childCount;
    }

    // 获取对应组的文本数据
    public static String getGroupText(Context context, int groupPosition) {
        // select name from classlist where idx = 1
        File dbFile = new File(context.getFilesDir(), "commonnum.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("select name from classlist where idx = ?", new String[]{String.valueOf(groupPosition + 1)});
        String groupText = "";
        if (cursor != null && cursor.moveToNext()) {
            groupText = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return groupText;
    }

    // 获取对应组的对应子条目的文本数据
    public static String[] getChildText(Context context, int groupPosition, int childPosition) {
        // select name , number from table1 where _id = 1
        File dbFile = new File(context.getFilesDir(), "commonnum.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery("select name , number from table" + (groupPosition + 1) + " where _id = ?", new String[]{String.valueOf(childPosition + 1)});
        String[] childText = new String[2];
        if (cursor != null && cursor.moveToNext()) {
            String name = cursor.getString(0);
            String number = cursor.getString(1);
            childText[0] = name;
            childText[1] = number;
        }
        cursor.close();
        database.close();
        return childText;
    }
}
