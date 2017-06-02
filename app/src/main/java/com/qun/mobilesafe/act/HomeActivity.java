package com.qun.mobilesafe.act;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.adapter.HomeAdapter;
import com.qun.mobilesafe.bean.HomeBean;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final static String[] TITLES = new String[]{"常用工具", "进程管理", "手机杀毒", "功能设置"};

    private final static String[] DESCS = new String[]{"工具大全", "管理运行进程", "病毒无处藏身", "管理您的软件"};

    private final static int[] ICONS = new int[]{R.mipmap.cygj, R.mipmap.jcgl, R.mipmap.sjsd, R.mipmap.szzx};
    private ImageView mHomeIvLogo;
    private GridView mHomeGv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initData();
    }

    private void initView() {
        mHomeIvLogo = (ImageView) findViewById(R.id.home_iv_logo);
        mHomeGv = (GridView) findViewById(R.id.home_gv);
        mHomeGv.setOnItemClickListener(this);

//        //修改字体
//        TextView homeTvTitle = (TextView) findViewById(R.id.home_tv_title);
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/xxx.ttf");
//        homeTvTitle.setTypeface(typeface);
    }

    private void initData() {
        // logo动画
        // mHomeIvLogo.setRotation(rotation);//类似一张纸绕着z轴旋转
        // mHomeIvLogo.setRotationX(rotationX);//类似笔记本的开关
        // mHomeIvLogo.setRotationY(rotationY);//类似开门

        ObjectAnimator oa = ObjectAnimator.ofFloat(mHomeIvLogo, "rotationY", 0, 45, 90, 180, 270, 360);
        oa.setDuration(1500);
        oa.setRepeatCount(ValueAnimator.INFINITE);// 设置动画重复次数
        oa.setRepeatMode(ValueAnimator.REVERSE);// 设置重复模式
        oa.start();

        //封装数据
        List<HomeBean> data = new ArrayList<>();
        for (int i = 0; i < TITLES.length; i++) {
            HomeBean bean = new HomeBean();
            bean.desc = DESCS[i];
            bean.imageId = ICONS[i];
            bean.title = TITLES[i];
            data.add(bean);
        }

        HomeAdapter adapter = new HomeAdapter(HomeActivity.this, data);
        mHomeGv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                System.out.println("常用工具");
                break;
            case 1:
                System.out.println("进程管理");
                break;
            case 2:
                System.out.println("手机杀毒");
                break;
            case 3:
                System.out.println("功能设置");
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));
                break;
            default:
                break;
        }
    }
}
