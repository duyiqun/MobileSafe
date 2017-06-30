package com.qun.mobilesafe.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.service.WatchDogService;
import com.qun.mobilesafe.utils.ServiceStateUtil;
import com.qun.mobilesafe.view.SettingItemView;

public class CommonToolActivity extends AppCompatActivity implements View.OnClickListener {

    private SettingItemView mCommonToolSivApplock;
    private SettingItemView mCommonToolSivDog;
    private SettingItemView mCommonToolSivLocation;
    private SettingItemView mCommonToolSivNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_tool);

        initView();
    }

    private void initView() {
        mCommonToolSivApplock = (SettingItemView) findViewById(R.id.common_tool_siv_applock);
        mCommonToolSivDog = (SettingItemView) findViewById(R.id.common_tool_siv_dog);
        mCommonToolSivLocation = (SettingItemView) findViewById(R.id.common_tool_siv_location);
        mCommonToolSivNumber = (SettingItemView) findViewById(R.id.common_tool_siv_number);
        mCommonToolSivApplock.setOnClickListener(this);
        mCommonToolSivDog.setOnClickListener(this);
        mCommonToolSivLocation.setOnClickListener(this);
        mCommonToolSivNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_tool_siv_location://归属地
                startActivity(new Intent(CommonToolActivity.this, LocationActivity.class));
                break;
            case R.id.common_tool_siv_number://常用号码
                startActivity(new Intent(CommonToolActivity.this, CommonNumberActivity.class));
                break;
            case R.id.common_tool_siv_applock://程序锁管理
                startActivity(new Intent(CommonToolActivity.this, AppLockActivity.class));
                break;
            case R.id.common_tool_siv_dog://电子狗
                mCommonToolSivDog.toggle();
                if (ServiceStateUtil.isServiceRunning(getApplicationContext(), WatchDogService.class)) {
                    stopService(new Intent(CommonToolActivity.this, WatchDogService.class));
                } else {
                    startService(new Intent(CommonToolActivity.this, WatchDogService.class));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ServiceStateUtil.isServiceRunning(getApplicationContext(), WatchDogService.class)) {
            mCommonToolSivDog.setToggle(true);
        } else {
            mCommonToolSivDog.setToggle(false);
        }
    }
}
