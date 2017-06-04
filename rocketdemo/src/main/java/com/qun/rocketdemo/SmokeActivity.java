package com.qun.rocketdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class SmokeActivity extends AppCompatActivity {

    private ImageView ivBottom;
    private ImageView ivTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoke);

        ivBottom = (ImageView) findViewById(R.id.iv_bottom);
        ivTop = (ImageView) findViewById(R.id.iv_top);
        //设置透明度动画
        final AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(400);
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            //动画监听，当动画结束进行对应的操作必须在动画监听中来进行
            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }
        });
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ivBottom.setVisibility(View.VISIBLE);
                ivTop.setVisibility(View.VISIBLE);
                ivBottom.startAnimation(aa);
                ivTop.startAnimation(aa);
            }
        }, 400);
    }
}
