package com.qun.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Qun on 2017/6/3.
 */

public class LocationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("归属地服务打开");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("归属地服务关闭");
    }
}
