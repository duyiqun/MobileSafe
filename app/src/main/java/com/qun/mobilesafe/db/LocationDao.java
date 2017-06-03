package com.qun.mobilesafe.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Qun on 2017/6/2.
 */

public class LocationDao {

    // 获取已经存在的数据库，进行查询操作
    public static String queryLocation(Context context, String number) {
        File dbFile = new File(context.getFilesDir(), "address.db");
        // 直接打开一个已经存在的数据库，并获取该对象
        // 参数一：已经存在数据库文件的路径，参数二：工厂，参数三：数据的模式
        SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        // 先判断是否是手机号码如果是：否则以号码的长度进行区分
        // 1 1个3-8的数字 9位 0-9 的数字
        String reg = "^1[3-8]\\d{9}$";
        String location = "未知";
        if (number.matches(reg)) {
            String mobilePrefix = number.substring(0, 7);
            String[] selectionArgs = new String[]{mobilePrefix};
//            select cardtype from info where mobileprefix = '1368314';
            Cursor cardtypeCursor = database.rawQuery("select cardtype from info where mobileprefix = ?", selectionArgs);
            if (cardtypeCursor != null) {
                if (cardtypeCursor.moveToNext()) {
                    location = cardtypeCursor.getString(0);
                }
            }
            cardtypeCursor.close();
            database.close();
        } else {// 否则以号码的长度进行区分
            int length = number.length();
            switch (length) {
                case 3:
                    location = "紧急号码";
                    break;
                case 4:
                    location = "模拟器";
                    break;
                case 5:
                    location = "服务号码";
                    break;
                case 7:
                case 8:
                    location = "本地座机";
                    break;
                // 3+8 、4+8、4+7、3+7
                case 10:
                case 11:
                case 12:
                    // 获取前3或者4位查询
                    // select distinct city from info where area = '0519';
                    Cursor cursor3 = database.rawQuery("select distinct city from info where area = ?", new String[]{number.substring(0, 3)});
                    if (cursor3 != null) {
                        if (cursor3.moveToNext()) {
                            location = cursor3.getString(0);
                        }
                    }
                    cursor3.close();
                    if (TextUtils.equals(location, "未知")) {
                        Cursor cursor4 = database.rawQuery("select distinct city from info where area = ?", new String[]{number.substring(0, 4)});
                        if (cursor4 != null) {
                            if (cursor4.moveToNext()) {
                                location = cursor4.getString(0);
                            }
                        }
                        cursor4.close();
                    }
                    database.close();
                    break;
                default:
                    break;
            }
        }
        return location;
    }
}
