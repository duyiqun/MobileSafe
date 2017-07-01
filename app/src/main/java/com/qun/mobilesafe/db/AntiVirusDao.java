package com.qun.mobilesafe.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by Qun on 2017/7/1.
 */

public class AntiVirusDao {

    //查询
    public static boolean queryAntiVirus(Context context, String md5) {
        //select md5 from datable where md5 ='ac365eeb5595554d67975ad61003e48e'
        File dbFile = new File(context.getFilesDir(), "antivirus.db");
        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        String sql = "select md5 from datable where md5 = ?";
        String[] selectionArgs = new String[]{md5};
        Cursor cursor = database.rawQuery(sql, selectionArgs);
        boolean result = false;
        if (cursor != null && cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        database.close();
        return result;
    }
}
