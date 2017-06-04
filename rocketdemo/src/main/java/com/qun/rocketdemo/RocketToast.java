package com.qun.rocketdemo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Qun on 2017/6/4.
 */

public class RocketToast implements View.OnTouchListener {

    WindowManager mWM;
    View mView;
    Context mContext;
    private WindowManager.LayoutParams mParams;//火箭的布局参数
    private WindowManager.LayoutParams mTipParams;//提示框的布局参数
    private int startX;
    private int startY;
    private ImageView mTipView;

    public RocketToast(Context context) {
        super();
        this.mContext = context;
        //WindowManager:窗口管理器，添加，删除，修改窗口
        //window：是android中最顶层的界面元素。在android看不见的一个框，将view放入到window里面才能在屏幕上进行显示（activity，dialog，toast都是通过窗口来显示的）
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //给窗口设置一些属性(布局参数)
        //在xml中部分属性是以layout_开头，这些属性不能由控件自身决定，必须与父控件进行“商量”才能有特定效果
        //在xml中部分属性不是以layout_开头，这些属性能由控件自身决定，不需要与父控件进行“商量”
        //在xml中部分属性不是以layout_开头,在代码中可以通过控件自身的set方法来实现设置属性
        //在xml中部分属性是以layout_开头,在代码中必须通过布局参数layoutparams来设置属性
        //在使用布局参数时，注意必须使用当前控件的父控件类型的布局参数
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;//以像素为单位显示界面
        mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;//设置窗口的类型(如果是toast类型的窗口默认没有点击事件与触摸操作)
        mParams.setTitle("Toast");
        //如果不设置FLAG_NOT_FOCUSABLE，会抢占其他窗口的焦点，点击操作就失效
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mParams.gravity = Gravity.LEFT | Gravity.TOP;// 设置火箭在左上角

        // 创建提示框的布局参数对象
        mTipParams = new WindowManager.LayoutParams();
        mTipParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mTipParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mTipParams.format = PixelFormat.TRANSLUCENT;// 以像素为单位显示界面
        mTipParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mTipParams.setTitle("Toast");
        mTipParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mTipParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;// 设置提示框在底部，水平居中
    }

    // 显示
    public void showRocketToast() {
        mView = View.inflate(mContext, R.layout.view_rocket, null);
        ImageView ivRocket = (ImageView) mView.findViewById(R.id.iv_rocket);
        AnimationDrawable rocketAnimation = (AnimationDrawable) ivRocket.getDrawable();
        rocketAnimation.start();
        mView.setOnTouchListener(this);

        //创建出一个窗口，使用mParams设置该窗口的一些属性，将mView放入窗口再进行显示
        mWM.addView(mView, mParams);
    }

    public void hideRocketToast() {
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

    //手指触摸view的时候，实时调用该方法
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //触摸事件一次按下n次移动一次抬起即可
//        System.out.println("onTouch...");
        int action = event.getAction();
        System.out.println("action:" + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN://手指按下
                //1.记录起始点
                startX = (int) event.getRawX();//在屏幕上的最小单位，像素点
                startY = (int) event.getRawY();

                // 在底部显示出提示框
                showTipView();
                break;
            case MotionEvent.ACTION_MOVE://手指移动
                //2.记录移动后结束点
                int endX = (int) event.getRawX();
                int endY = (int) event.getRawY();
                //3.计算出间距
                int diffX = endX - startX;
                int diffY = endY - startY;
                //4.让窗口动起来
                mParams.x = mParams.x + diffX;
                mParams.y = mParams.y + diffY;
                mWM.updateViewLayout(mView, mParams);
                //5.初始化起始点
                startX = endX;
                startY = endY;

                // 手指移动时，让提示框进行闪烁动画
                startTipAnimation();
                // 实时获取火箭与提示框的位置关系，如果火箭进入提示框，展示发射状态

                break;
            case MotionEvent.ACTION_UP://手指抬起
                // 如果手指松开时，时发射状态，则执行发射动画

                // 手指抬起时，隐藏火箭
                hideTipView();
                break;
            default:
                break;
        }
        return true;//由我们自己处理触摸事件
    }

    // 与显示火箭功能一致
    private void showTipView() {
        mTipView = new ImageView(mContext);
        mTipView.setImageResource(R.mipmap.desktop_bg_tips_1);


        //创建出一个窗口，使用mParams设置该窗口的一些属性，将mView放入窗口再进行显示
        mWM.addView(mTipView, mTipParams);
    }

    private void startTipAnimation() {

    }

    //与火箭隐藏一致
    private void hideTipView() {
        if (mTipView != null) {
            // note: checking parent() just to make sure the view has
            // been added...  i have seen cases where we get here when
            // the view isn't yet added, so let's try not to crash.
            if (mTipView.getParent() != null) {
                mWM.removeView(mTipView);
            }
            mTipView = null;
        }
    }
}
