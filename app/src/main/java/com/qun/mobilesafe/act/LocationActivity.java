package com.qun.mobilesafe.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.db.LocationDao;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mLocationBtn;
    private EditText mLocationEtNumber;
    private TextView mLocationTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initView();
    }

    private void initView() {
        mLocationBtn = (Button) findViewById(R.id.location_btn);
        mLocationEtNumber = (EditText) findViewById(R.id.location_et_number);
        mLocationTv = (TextView) findViewById(R.id.location_tv);
        mLocationBtn.setOnClickListener(this);
        //通过文字改变监听实现实时查询功能(本地数据库查询),客户端里面网络请求不做实时查询操作，本地数据库则可以
        mLocationEtNumber.addTextChangedListener(new TextWatcher() {
            //当文字改变时
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //当文字改变前
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //当文字改变后
            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString();
                //将号码从数据库进行查询
                String location = LocationDao.queryLocation(getApplicationContext(), number);
                mLocationTv.setText(location);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location_btn:
                String number = mLocationEtNumber.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(getApplicationContext(), "号码不能为空", Toast.LENGTH_SHORT).show();
                    //AnimationUtils动画工具类：能够加载xml动画文件生成动画对象（用于动画效果的抽取）
                    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                    shake.setInterpolator(new CycleInterpolator(7));
                    mLocationEtNumber.startAnimation(shake);
                    return;
                }
                //将号码从数据库进行查询
                String location = LocationDao.queryLocation(getApplicationContext(), number);
                mLocationTv.setText(location);
                break;
            default:
                break;
        }
    }
}
