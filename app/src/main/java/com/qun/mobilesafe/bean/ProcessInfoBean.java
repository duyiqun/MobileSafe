package com.qun.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Qun on 2017/6/7.
 */

public class ProcessInfoBean {

    public Drawable appIcon;
    public String appName;
    public long appMemory;
    public boolean isSystem;
    public boolean isSelected = false;//用来记录当前条目的选中状态
    public String appPackageName;
}
