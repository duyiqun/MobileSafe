package com.qun.mobilesafe.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Qun on 2017/6/4.
 */

public class LocationToast {

    WindowManager mWM;
    TextView mView;
    Context mContext;
    private final WindowManager.LayoutParams mParams;

    public LocationToast(Context context) {
        super();
        this.mContext = context;
        //WindowManager:窗口管理器，添加，删除，修改窗口
        //window：是android中最顶层的界面元素。在android看不见的一个框，将view放入到window里面才能在屏幕上进行显示（activity，dialog，toast都是通过窗口来显示的）
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //布局参数，设置窗口的一些属性
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;//以像素为单位显示界面
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;//设置窗口的类型
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
    }

    // 显示
    public void showLocationToast(String location) {
        mView = new TextView(mContext.getApplicationContext());
        mView.setText(location);
        mView.setTextColor(Color.RED);
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
}
