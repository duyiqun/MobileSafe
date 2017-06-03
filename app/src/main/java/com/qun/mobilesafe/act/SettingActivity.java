package com.qun.mobilesafe.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.service.LocationService;
import com.qun.mobilesafe.utils.Contants;
import com.qun.mobilesafe.utils.ServiceStateUtil;
import com.qun.mobilesafe.utils.SpUtil;
import com.qun.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private SettingItemView mSivAutoUpdate;
    private SettingItemView mSivLocation;
    private SettingItemView mSivLocationStyle;

//    private boolean flag = false;//用来记录自动更新的开关的状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        mSivAutoUpdate = (SettingItemView) findViewById(R.id.siv_autoupdate);
        mSivLocation = (SettingItemView) findViewById(R.id.siv_location);
        mSivLocationStyle = (SettingItemView) findViewById(R.id.siv_location_style);
        mSivAutoUpdate.setOnClickListener(this);
        mSivLocation.setOnClickListener(this);
        mSivLocationStyle.setOnClickListener(this);

        //界面展示时，根据记录的状态值，显示开关
//		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
//		boolean flag = sp.getBoolean("autoUpdate", true);
        boolean flag = SpUtil.getBoolean(getApplicationContext(), Contants.KEY_AUTO_UPDATE, true);
        mSivAutoUpdate.setToggle(flag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.siv_autoupdate://自动更新
//                if (flag) {
//                    mSivAutoUpdate.setToggle(false);
//                    flag = false;
//                }else{
//                    mSivAutoUpdate.setToggle(true);
//                    flag = true;
//                }

//                mSivAutoUpdate.setToggle(!flag);
//                flag = !flag;

//                mSivAutoUpdate.setToggle(!mSivAutoUpdate.isToggle());
//                flag = !flag;

                mSivAutoUpdate.toggle();
                //记录自动更新的状态值
//                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
//                sp.edit().putBoolean("autoUpdate", mSivAutoUpdate.isToggle()).commit();
                SpUtil.saveBoolean(getApplicationContext(), mSivAutoUpdate.isToggle(), Contants.KEY_AUTO_UPDATE);
                break;
            case R.id.siv_location://归属地
                //点击时，切换开关，将服务根据当前的状态进行关闭与打开即可
                mSivLocation.toggle();
                if (ServiceStateUtil.isServiceRunning(getApplicationContext(), LocationService.class)) {
                    stopService(new Intent(SettingActivity.this, LocationService.class));
                } else {
                    startService(new Intent(SettingActivity.this, LocationService.class));
                }
                break;
            case R.id.siv_location_style://风格设置
                mSivLocationStyle.toggle();
                break;
            default:
                break;
        }
    }
}
