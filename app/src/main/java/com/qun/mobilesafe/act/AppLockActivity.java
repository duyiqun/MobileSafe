package com.qun.mobilesafe.act;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;

public class AppLockActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtApplockLocked;
    private Button mBtApplockUnlock;
    private TextView mTvApplock;
    private ListView mLvApplockLocked;
    private ListView mLvApplockUnlock;
    private View mLlLoading;
    //    private AppLockDao appLockDao;
    private List<AppInfoBean> mUnlockData = new ArrayList<>();// 未加锁的数据
    private List<AppInfoBean> mLockData = new ArrayList<>();// 已加锁的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);

        initView();
        initData();
    }

    private void initView() {
        mBtApplockLocked = (Button) findViewById(R.id.bt_applock_locked);
        mBtApplockUnlock = (Button) findViewById(R.id.bt_applock_unlock);

        mTvApplock = (TextView) findViewById(R.id.tv_applock);

        mLvApplockLocked = (ListView) findViewById(R.id.lv_applock_locked);
        mLvApplockUnlock = (ListView) findViewById(R.id.lv_applock_unlock);

        mBtApplockLocked.setOnClickListener(this);
        mBtApplockUnlock.setOnClickListener(this);

        mLlLoading = findViewById(R.id.ll_loading);

//        appLockDao = new AppLockDao(getApplicationContext());
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_applock_locked:// 已加锁按钮
                // 修改文字颜色
                mBtApplockLocked.setTextColor(Color.WHITE);
                mBtApplockUnlock.setTextColor(getResources()
                        .getColor(R.color.blue));
                // 修改文字背景
                mBtApplockLocked
                        .setBackgroundResource(R.drawable.applock_tabright_pressed_shape);
                mBtApplockUnlock
                        .setBackgroundResource(R.drawable.applock_tableft_normal_shape);

                mLvApplockLocked.setVisibility(View.VISIBLE);
                mLvApplockUnlock.setVisibility(View.GONE);
                mTvApplock.setText("已加锁(" + mLockData.size() + ")个");

                break;
            case R.id.bt_applock_unlock:// 未加锁按钮
                // 修改文字颜色
                mBtApplockUnlock.setTextColor(Color.WHITE);
                mBtApplockLocked.setTextColor(getResources()
                        .getColor(R.color.blue));
                // 修改文字背景
                mBtApplockLocked
                        .setBackgroundResource(R.drawable.applock_tabright_normal_shape);
                mBtApplockUnlock
                        .setBackgroundResource(R.drawable.applock_tableft_pressed_shape);

                mLvApplockLocked.setVisibility(View.GONE);
                mLvApplockUnlock.setVisibility(View.VISIBLE);
                mTvApplock.setText("未加锁(" + mUnlockData.size() + ")个");

                break;
            default:
                break;
        }
    }
}