package com.qun.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.qun.mobilesafe.db.LocationDao;

/**
 * Created by Qun on 2017/6/3.
 */

public class LocationService extends Service {

    private TelephonyManager mTelephonyManager;
    private PhoneStateListener mListener = new PhoneStateListener() {

        //参数一：电话状态，参数二：来电的状态
        public void onCallStateChanged(int state, String incomingNumber) {
            //响铃时，显示归属地，挂断时，隐藏归属地
            String location = LocationDao.queryLocation(getApplicationContext(), incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://响铃
                    Toast.makeText(getApplicationContext(), location, Toast.LENGTH_SHORT).show();
                    //仿照toast的源码实现在电话界面悬浮的效果
                    showLocationToast(location);
                    break;
                case TelephonyManager.CALL_STATE_IDLE://停滞
                    //仿照toast的源码实现悬浮的效果隐藏功能
                    hideLocationToast();
                    break;
                default:
                    break;
            }
        }
    };

    WindowManager mWM;
    TextView mView;

    private void showLocationToast(String location) {

        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mView = new TextView(getApplicationContext());
        mView.setText(location);
        mView.setTextColor(Color.RED);
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;//以像素为单位显示界面
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;//设置窗口的类型
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWM.addView(mView, mParams);
    }

    private void hideLocationToast() {
        if (mView != null) {
            // note: checking parent() just to make sure the view has
            // been added...  i have seen cases where we get here when
            // the view isn't yet added, so let's try not to crash.
            if (mView.getParent() != null) {
                mWM.removeView(mView);
            }
            mView = null;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //监听到去电广播后，获取号码，查询归属地
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            String location = LocationDao.queryLocation(getApplicationContext(), number);
            Toast.makeText(getApplicationContext(), location, Toast.LENGTH_SHORT).show();
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
        System.out.println("归属地服务打开");

        //监听来去电
        //通过电话管理器监听来电
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);

        // 监听去电
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("归属地服务关闭");
        //服务关闭时，将电话监听停止
        mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
        unregisterReceiver(receiver);
    }
}
