package com.qun.mobilesafe.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewDebug;

/**
 * Created by Qun on 2017/6/1.
 */

public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {

    //在代码通过new方式创建时的，调用该构造方法
    public MarqueeTextView(Context context) {
        super(context);
    }

    //在xml中引用该控件时，调用该构造方法
    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //带有主题样式的构造方法
    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //欺骗系统，该控件获取到焦点
    @Override
    @ViewDebug.ExportedProperty(category = "focus")
    public boolean isFocused() {
        // TODO Auto-generated method stub
        return true;
    }

    //当焦点被抢占时，停止了跑马灯效果
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        System.out.println("focused:" + focused);
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    //当本窗口的焦点被上层窗口抢占时，执行该方法
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        System.out.println("hasWindowFocus:" + hasWindowFocus);
        if (hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }
}
