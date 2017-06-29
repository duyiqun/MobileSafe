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
    private Context mContext;

    public AppLockAdapter(Context context, List<AppInfoBean> unlockData) {
        this.mContext = context;
        this.mData = unlockData;
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
        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        ImageView ivApplock;
        TextView tvName;
    }
}
