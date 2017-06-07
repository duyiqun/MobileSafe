package com.qun.mobilesafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Qun on 2017/5/31.
 */

public class PackageUtil {

    //获取版本名
    public static String getVersionName(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        //参数一：要查询的应用包信息对应包名，参数二：标记，想获取什么信息，就设置什么标记（0：基本信息）
        String versionName = "未知";
        try {
            PackageInfo packageInfo = manager.getPackageInfo(packageName, 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //获取版本号
    public static int getVersionCode(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        //参数一：要查询的应用包信息对应包名，参数二：标记，想获取什么信息，就设置什么标记（0：基本信息）
        int versionCode = 1;
        try {
            PackageInfo packageInfo = manager.getPackageInfo(packageName, 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
