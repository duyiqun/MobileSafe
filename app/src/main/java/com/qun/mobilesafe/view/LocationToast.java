package com.qun.mobilesafe.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.qun.mobilesafe.R;

/**
 * Created by Qun on 2017/6/4.
 */

public class LocationToast implements View.OnTouchListener {

    WindowManager mWM;
    View mView;
    Context mContext;
    private final WindowManager.LayoutParams mParams;
    private int startX;
    private int startY;
    private TextView mTvToastTitle;

    public LocationToast(Context context) {
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
    }

    // 显示
    public void showLocationToast(String location) {
        mView = View.inflate(mContext, R.layout.view_location_toast, null);
        mTvToastTitle = (TextView) mView.findViewById(R.id.tv_toast_title);
        mTvToastTitle.setText(location);
        mTvToastTitle.setTextColor(Color.RED);
        mView.setOnTouchListener(this);

        //创建出一个窗口，使用mParams设置该窗口的一些属性，将mView放入窗口再进行显示
        mWM.addView(mView, mParams);
    }

    public void hideLocationToast() {
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
                break;
            case MotionEvent.ACTION_UP://手指抬起
                break;
            default:
                break;
        }
        return true;//由我们自己处理触摸事件
    }
}
