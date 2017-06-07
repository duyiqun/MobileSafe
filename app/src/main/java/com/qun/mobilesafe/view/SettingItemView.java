package com.qun.mobilesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qun.mobilesafe.R;

/**
 * Created by Qun on 2017/6/1.
 */

public class SettingItemView extends RelativeLayout {

    private TextView mSivItemTitle;
    private ImageView mSivItemIcon;
    private boolean flag = false;//用来记录开关的状态

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //方式一：
        View view = View.inflate(context, R.layout.view_setting_item, null);
        this.addView(view);
        //方式二：
//		View.inflate(context, R.layout.view_setting_item, this);

        mSivItemTitle = (TextView) view.findViewById(R.id.siv_item_title);

        //通过AttributeSet获取xml中配置的title值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        String title = typedArray.getString(R.styleable.SettingItemView_title);
        mSivItemTitle.setText(title);

        //获取背景类型值，设置对应背景图片
        int backgroundType = typedArray.getInt(R.styleable.SettingItemView_backgroundType, -1);
        if (backgroundType == -1) {
            throw new RuntimeException("请设置backgroundType属性！");
        }
        switch (backgroundType) {
            case 0:
                this.setBackgroundResource(R.drawable.selector_siv_first);
                break;
            case 1:
                this.setBackgroundResource(R.drawable.selector_siv_middle);
                break;
            case 2:
                this.setBackgroundResource(R.drawable.selector_siv_last);
                break;
            default:
                break;
        }
        //获取图片控件
        mSivItemIcon = (ImageView) view.findViewById(R.id.siv_item_icon);

        //获取属性值判断是否要显示开关图片即可
        boolean enable = typedArray.getBoolean(R.styleable.SettingItemView_enable, true);
        if (enable) {
            mSivItemIcon.setVisibility(View.VISIBLE);
        }else{
            mSivItemIcon.setVisibility(View.INVISIBLE);
        }
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public void setTitle(String title) {
        mSivItemTitle.setText(title);
    }

    //设置开关的图片切换
    public void setToggle(boolean isOpen) {
        if (!isOpen) {
            mSivItemIcon.setImageResource(R.mipmap.on);
        } else {
            mSivItemIcon.setImageResource(R.mipmap.off);
        }
        this.flag = isOpen;
    }

    //返回当前的开关的状态
    public boolean isToggle() {
        return this.flag;
    }

    //让开关进行切换即可
    public void toggle() {
        setToggle(!this.flag);
    }
}
