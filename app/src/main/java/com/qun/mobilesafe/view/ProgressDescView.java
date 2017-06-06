package com.qun.mobilesafe.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qun.mobilesafe.R;

/**
 * Created by Qun on 2017/6/6.
 */

public class ProgressDescView extends LinearLayout {

    private TextView mTvPdvTitle;
    private TextView mTvPdvLeft;
    private TextView mTvPdvRight;
    private ProgressBar mPbPdv;

    public ProgressDescView(Context context) {
        this(context, null);
    }

    public ProgressDescView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //将抽取的布局视图直接填充到容器中
        View view = View.inflate(context, R.layout.view_pdv, null);
        this.addView(view);

        //获取控件，并设置set方法，供外部动态设置内容
        mTvPdvTitle = (TextView) view.findViewById(R.id.tv_pdv_title);
        mTvPdvLeft = (TextView) view.findViewById(R.id.tv_pdv_left);
        mTvPdvRight = (TextView) view.findViewById(R.id.tv_pdv_right);
        mPbPdv = (ProgressBar) view.findViewById(R.id.pb_pdv);
        mPbPdv.setMax(100);
    }

    public ProgressDescView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public ProgressDescView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    //设置标题
    public void setTitle(String title) {
        mTvPdvTitle.setText(title);
    }

    //设置左边的文本
    public void setLeftText(String text) {
        mTvPdvLeft.setText(text);
    }

    //设置右边的文本
    public void setRightText(String text) {
        mTvPdvRight.setText(text);
    }

    /**
     * 设置进度值
     * @param progress 进度值，（注意：最大值为100）
     */
    public void setProgress(int progress) {
        mPbPdv.setProgress(progress);
    }
}
