package com.qun.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Qun on 2017/6/2.
 */

public class SpUtil {

    private static SharedPreferences sSharedPreferences;

    //保存boolean
    public static void saveBoolean(Context context, boolean value, String key) {
        if (sSharedPreferences == null) {
            sSharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sSharedPreferences.edit().putBoolean(key, value).commit();
    }

    //获取boolean
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sSharedPreferences == null) {
            sSharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sSharedPreferences.getBoolean(key, defValue);
    }

    //保存int
    public static void saveInt(Context context, int value, String key) {
        if (sSharedPreferences == null) {
            sSharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sSharedPreferences.edit().putInt(key, value).commit();
    }

    //获取int
    public static int getInt(Context context, String key, int defValue) {
        if (sSharedPreferences == null) {
            sSharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sSharedPreferences.getInt(key, defValue);
    }
}
