package com.qun.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.qun.mobilesafe.R;

/**
 * Created by Qun on 2017/6/1.
 */

public class SettingItemView extends RelativeLayout {

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.view_setting_item, null);
        this.addView(view);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }
}
