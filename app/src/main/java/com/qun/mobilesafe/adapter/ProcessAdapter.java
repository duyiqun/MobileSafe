package com.qun.mobilesafe.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.bean.ProcessInfoBean;

import java.util.List;

/**
 * Created by Qun on 2017/6/7.
 */

public class ProcessAdapter extends BaseAdapter {

    private Context mContext;
    private List<ProcessInfoBean> mData;

    public ProcessAdapter(Context context, List<ProcessInfoBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ProcessInfoBean getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.view_item_processmanager, null);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_process_manager_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_process_manager_name);
            holder.tvMemory = (TextView) convertView.findViewById(R.id.tv_process_manager_memory);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb_process_manager);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProcessInfoBean processInfoBean = getItem(position);
        holder.ivIcon.setImageDrawable(processInfoBean.appIcon);
        holder.tvName.setText(processInfoBean.appName);
        holder.tvMemory.setText(Formatter.formatFileSize(mContext, processInfoBean.appMemory));

        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvMemory;
        CheckBox cb;
    }
}
