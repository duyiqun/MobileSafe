package com.qun.mobilesafe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qun on 2017/6/29.
 */

public class AppLockDao {

    private AppLockOpenHelper appLockOpenHelper;

    public AppLockDao(Context context) {
        appLockOpenHelper = new AppLockOpenHelper(context);
    }

    //添加操作
    public boolean insert(String packageName) {
        SQLiteDatabase database = appLockOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppLockContants.COLUMN_PACKAGE_NAME, packageName);
        long insert = database.insert(AppLockContants.TABLE_NAME, null, values);
//        if (insert == -1) {
//            return false;
//        } else {
//            return true;
//        }
        database.close();
        return insert != -1;
    }

    //删除操作
    public boolean delete(String packageName) {
        // delete from applock where package_name = 'com.qun.mobilesafe'
        SQLiteDatabase database = appLockOpenHelper.getWritableDatabase();
        int delete = database.delete(AppLockContants.TABLE_NAME, AppLockContants.COLUMN_PACKAGE_NAME + " = ?", new String[]{packageName});
        database.close();
        return delete != 0;
    }

    //查询数据
    public boolean query(String packageName) {
        // select package_name from applock where package_name = 'com.qun.mobilesafe'
        SQLiteDatabase database = appLockOpenHelper.getWritableDatabase();
        Cursor cursor = database.query(AppLockContants.TABLE_NAME, new String[]{AppLockContants.COLUMN_PACKAGE_NAME}, AppLockContants.COLUMN_PACKAGE_NAME + " = ?", new String[]{packageName}, null, null, null);
        boolean result = false;
        if (cursor != null && cursor.moveToNext()) {
            result = true;
        }
        database.close();
        return result;
    }

    //查询所有的加锁的应用数据
    public List<String> getAllData() {
        // select package_name from applock
        SQLiteDatabase database = appLockOpenHelper.getWritableDatabase();
        Cursor cursor = database.query(AppLockContants.TABLE_NAME, new String[]{AppLockContants.COLUMN_PACKAGE_NAME}, null, null, null, null, null);
        List<String> data = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String packageName = cursor.getString(0);
                data.add(packageName);
            }
        }
        cursor.close();
        database.close();
        return data;
    }
}
