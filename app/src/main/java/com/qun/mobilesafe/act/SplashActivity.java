package com.qun.mobilesafe.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.utils.PackageUtil;

public class SplashActivity extends AppCompatActivity {

    private TextView mSplashTvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initData();
    }

    private void initView() {
        mSplashTvVersion = (TextView) findViewById(R.id.splash_tv_version);
    }

    private void initData() {
        //获取版本名展示
        initVersionName();

        //自动更新功能
        upDateVersion();
    }

    private void initVersionName() {
//        PackageManager manager = getPackageManager();
//        //参数一：要查询的应用包信息对应包名，参数二：标记，想获取什么信息，就设置什么标记（0：基本信息）
//        try {
//            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
//            String versionName = packageInfo.versionName;
//            mSplashTvVersion.setText(versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        String versionName = PackageUtil.getVersionName(SplashActivity.this, getPackageName());
        mSplashTvVersion.setText(versionName);
    }

    private void upDateVersion() {

    }
}
