package com.qun.mobilesafe.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private SettingItemView mSivAutoupdate;
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
        mSivAutoupdate = (SettingItemView) findViewById(R.id.siv_autoupdate);
        mSivLocation = (SettingItemView) findViewById(R.id.siv_location);
        mSivLocationStyle = (SettingItemView) findViewById(R.id.siv_location_style);
        mSivAutoupdate.setOnClickListener(this);
        mSivLocation.setOnClickListener(this);
        mSivLocationStyle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.siv_autoupdate://自动更新
//                if (flag) {
//                    mSivAutoupdate.setToggle(false);
//                    flag = false;
//                }else{
//                    mSivAutoupdate.setToggle(true);
//                    flag = true;
//                }

//                mSivAutoupdate.setToggle(!flag);
//                flag = !flag;

//                mSivAutoupdate.setToggle(!mSivAutoupdate.isToggle());
//                flag = !flag;

                mSivAutoupdate.toggle();
                break;
            case R.id.siv_location://归属地

                break;
            case R.id.siv_location_style://风格设置

                break;
            default:
                break;
        }
    }
}
