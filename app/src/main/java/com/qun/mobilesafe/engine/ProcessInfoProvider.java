package com.qun.mobilesafe.engine;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    // 获取可用的内存数
    public static long getAvailMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);// 赋值函数
        long availMem = outInfo.availMem;
        return availMem;
    }

    // 获取总的内存数
    @SuppressLint("NewApi")
    public static long getTotalMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);// 赋值函数
        // 如果高于16版本直接获取字段，如果低于16做对应的处理
        // Build,系统运行时，产生的文件，携带系统的信息
        long totalMem = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            totalMem = outInfo.totalMem;
        } else {
            // 在低版本中，读取proc/meminfo的第一行MemTotal: 513492 kB
            File file = new File("proc/meminfo");
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String readLine = br.readLine();
                readLine = readLine.replace("MemTotal:", "").trim();
                readLine = readLine.replace("kB", "").trim();
                totalMem = Integer.valueOf(readLine) * 1024;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return totalMem;
    }
}
