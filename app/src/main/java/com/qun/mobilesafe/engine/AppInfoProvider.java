package com.qun.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.qun.mobilesafe.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qun on 2017/6/29.
 */

public class AppInfoProvider {

    //获取安装在设备上的所有的应用数据
    public static List<AppInfoBean> getAllAppInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        List<AppInfoBean> data = new ArrayList<AppInfoBean>();
        for (PackageInfo packageInfo : installedPackages) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            Drawable appIcon = applicationInfo.loadIcon(packageManager);
            String appName = applicationInfo.loadLabel(packageManager).toString();
            AppInfoBean bean = new AppInfoBean();
            bean.appIcon = appIcon;
            bean.appName = appName;

            bean.appPackageName = packageInfo.packageName;
            data.add(bean);
        }
        return data;
    }
}