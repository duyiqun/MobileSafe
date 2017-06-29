package com.qun.mobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.bean.AppInfoBean;

import java.util.List;

/**
 * Created by Qun on 2017/6/29.
 */

public class AppLockAdapter extends BaseAdapter {

    private final List<AppInfoBean> mData;
    private final boolean mIsLock;//用来区分未加锁与已加锁操作，true，已加锁
    private final List<AppInfoBean> mOtherData;
    private Context mContext;

    //如果isLock为true，data是已加锁的数据集合，otherData是未加锁的数据集合，如果isLock为false，data是未加锁的数据集合，otherData是已加锁的数据集合
    public AppLockAdapter(Context context, List<AppInfoBean> unlockData, List<AppInfoBean> otherData, boolean isLock) {
        this.mContext = context;
        this.mData = unlockData;
        this.mIsLock = isLock;
        this.mOtherData = otherData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public AppInfoBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.view_item_applock, null);
            holder = new ViewHolder();
            holder.ivApplock = (ImageView) convertView.findViewById(R.id.iv_item_applock);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_item_applock_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_applock_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AppInfoBean appInfoBean = mData.get(position);
        holder.tvName.setText(appInfoBean.appName);
        holder.ivIcon.setImageDrawable(appInfoBean.appIcon);
        if (mIsLock) {
            holder.ivApplock.setImageResource(R.drawable.selector_iv_unlock);
        } else {
            holder.ivApplock.setImageResource(R.drawable.selector_iv_lock);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        ImageView ivApplock;
        TextView tvName;
    }
}
