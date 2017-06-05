package com.qun.mobilesafe.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.adapter.LocationDialogAdapter;
import com.qun.mobilesafe.bean.LocationBean;
import com.qun.mobilesafe.utils.Contants;
import com.qun.mobilesafe.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qun on 2017/6/5.
 */

public class LocationDialog extends Dialog implements AdapterView.OnItemClickListener {

    private String[] mTitles = new String[]{"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};
    private int[] mImages = new int[]{R.drawable.shape_location_normal, R.drawable.shape_location_orange, R.drawable.shape_location_blue, R.drawable.shape_location_gray, R.drawable.shape_location_green};
    private ListView mLvLocaitonDialog;
    private LocationDialogAdapter mAdapter;
    private List<LocationBean> mData;

    public LocationDialog(@NonNull Context context) {
        super(context, R.style.style_locationdialog);
        //获取用于显示对话框的窗口对象设置显示在底部即可
        //方式一：
//        Window window = getWindow();
//        window.setGravity(Gravity.BOTTOM);
        //方式二：
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
    }

    public LocationDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected LocationDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_location_dialog);

        mLvLocaitonDialog = (ListView) findViewById(R.id.lv_locaiton_dialog);
        mLvLocaitonDialog.setOnItemClickListener(this);
        initData();
        mAdapter = new LocationDialogAdapter(getContext(), mData);
        mLvLocaitonDialog.setAdapter(mAdapter);
    }

    private void initData() {
        mData = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            LocationBean locationBean = new LocationBean();
            locationBean.title = mTitles[i];
            locationBean.imageId = mImages[i];
            mData.add(locationBean);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击时，记录选择的颜色，并让条目的图片设置为选中
        SpUtil.saveInt(getContext(), mImages[position], Contants.KEY_LOCATION_IMAGEID);
        //在一个界面中适配器只会被创建一次，并且setAdapter也只会被调用一次，如果要刷新则修改数据，并调用刷新方法notifyDataSetChanged即可
//        mLvLocaitonDialog.setAdapter(adapter);
        mAdapter.notifyDataSetChanged();//让适配器根据最新的数据重新显示，不会修改当前lsitview的索引状态
    }
}
