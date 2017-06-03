package com.qun.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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
                    break;
                case TelephonyManager.CALL_STATE_IDLE://停滞
                    break;
                default:
                    break;
            }
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("归属地服务关闭");
        //服务关闭时，将电话监听停止
        mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
    }
}
