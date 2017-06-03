package com.qun.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Qun on 2017/6/3.
 */

public class ServiceStateUtil {

    //判断服务是否是运行状态即可
    public static boolean isServiceRunning(Context context, Class<? extends Service> clazz) {
        //类似与windows中的任务管理器
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //假设当前设备有1000个服务在运行，将前100个服务进行返回，假设当前设备只有20个服务在运行，将所有的20个服务全部返回即可
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        //遍历正在运行的服务的数据集合，判断clazz是否在集合中如果在，当前服务正在运行
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            ComponentName service = runningServiceInfo.service;
            if (TextUtils.equals(service.getClassName(), clazz.getName())) {
                return true;
            }
        }
        return false;
    }
}
