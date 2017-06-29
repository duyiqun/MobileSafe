package com.qun.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Qun on 2017/6/29.
 */

public class AppLockOpenHelper extends SQLiteOpenHelper {

    public AppLockOpenHelper(Context context) {
        super(context, AppLockContants.DB_NAME, null, AppLockContants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AppLockContants.SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
