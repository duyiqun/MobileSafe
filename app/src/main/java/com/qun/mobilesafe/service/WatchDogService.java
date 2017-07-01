package com.qun.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.qun.mobilesafe.act.PassWordEnterActivity;
import com.qun.mobilesafe.db.AppLockDao;
import com.qun.mobilesafe.utils.Contants;

import java.util.List;

/**
 * Created by Qun on 2017/6/30.
 */

public class WatchDogService extends Service {

    private ActivityManager mActivityManager;
    private AppLockDao mAppLockDao;
    private String mSkipPackageName = "";//需要不拦截的包名
    private boolean mIsRunning;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            mSkipPackageName = intent.getStringExtra(Contants.KEY_PACKAGE_NAME);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        mAppLockDao = new AppLockDao(this);
        System.out.println("电子狗服务启动了");

        IntentFilter filter = new IntentFilter();
        filter.addAction(Contants.ACTION_SKIP_PACKAGE);
        registerReceiver(mReceiver, filter);

        // 有一个电子狗实时观察者用户打开了哪一个应用，进行数据库的查询，如果是已加锁应用，那么就拦截
        startWatchDog();
    }

    private void startWatchDog() {
        mIsRunning = true;
        new Thread() {
            public void run() {
                while (mIsRunning) {
                    List<ActivityManager.RunningTaskInfo> runningTasks = mActivityManager.getRunningTasks(1);
                    ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                    ComponentName topActivity = runningTaskInfo.topActivity;
                    String packageName = topActivity.getPackageName();
                    // 将输入正确密码的拦截的应用包名传递过来，进行判断不进行拦截操作
                    if (TextUtils.equals(mSkipPackageName, packageName)) {
                        continue;
                    }
                    if (mAppLockDao.query(packageName)) {
                        System.out.println("拦截应用：" + packageName);

                        // 展示一个界面拦截一下即可
                        Intent intent = new Intent(WatchDogService.this, PassWordEnterActivity.class);
                        intent.putExtra(Contants.KEY_PACKAGE_NAME, packageName);
                        //FLAG_ACTIVITY_NEW_TASK如果本应用有任务栈直接将PassWordEnterActivity放入，如果没有直接创建新的本应用的任务栈并放入
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    SystemClock.sleep(10);
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("电子狗服务销毁了");

        unregisterReceiver(mReceiver);
        mIsRunning = false;
    }
}
