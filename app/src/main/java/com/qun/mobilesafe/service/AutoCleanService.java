package com.qun.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.qun.mobilesafe.engine.ProcessInfoProvider;

/**
 * Created by Qun on 2017/6/28.
 */

public class AutoCleanService extends Service {

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //清理正在执行的进程即可
            ProcessInfoProvider.cleanAllProcess(getApplicationContext());
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("自动清理服务开启了");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);//锁屏动作
        //监听锁屏操作，如果锁屏，将后台进程自动清理
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("自动清理服务关闭了");
        unregisterReceiver(mReceiver);
    }
}
