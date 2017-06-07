package com.qun.mobilesafe.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Qun on 2017/6/7.
 */

public class ProcessInfoProvider {

    // 获取正在运行的进程数
    public static int getRunningProcessNum(Context context) {
        // 通过任务管理器类直接获取正在运行的进程数即可
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        return runningAppProcesses.size();
    }

    // 获取设备上的总进程数
    public static int getAllProcessNum(Context context) {
        // 获取所有安装在设备上的包对象，获取每个应用的application以及4大组件的process属性的值，并去重
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_PROVIDERS | PackageManager.GET_RECEIVERS | PackageManager.GET_SERVICES);
        Set<String> processes = new HashSet<>();//使用set集合来进行去重操作
        for (PackageInfo packageInfo : installedPackages) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            String processName = applicationInfo.processName;//包名
            processes.add(processName);
            //获取4大组件所有的进程名进行去重，来获取总进程数
            ActivityInfo[] activities = packageInfo.activities;
            if (activities != null) {
                for (ActivityInfo activityInfo : activities) {
                    processes.add(activityInfo.processName);
                }
            }
            ProviderInfo[] providers = packageInfo.providers;
            if (providers != null) {
                for (ProviderInfo providerInfo : providers) {
                    processes.add(providerInfo.processName);
                }
            }
            ActivityInfo[] receivers = packageInfo.receivers;
            if (receivers != null) {
                for (ActivityInfo activityInfo : receivers) {
                    processes.add(activityInfo.processName);
                }
            }
            ServiceInfo[] services = packageInfo.services;
            if (services != null) {
                for (ServiceInfo serviceInfo : services) {
                    processes.add(serviceInfo.processName);
                }
            }
        }
        return processes.size();
    }
}
