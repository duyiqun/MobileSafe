package com.qun.rocketdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Qun on 2017/6/4.
 */

public class RocketService extends Service {

    private RocketToast mRocketToast;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("火箭服务启动了");
        mRocketToast = new RocketToast(this);
        mRocketToast.showRocketToast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("火箭服务关闭了");
        mRocketToast.hideRocketToast();
    }
}
