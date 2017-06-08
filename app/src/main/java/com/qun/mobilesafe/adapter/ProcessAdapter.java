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
    private List<ProcessInfoBean> mUserData;
    private List<ProcessInfoBean> mSystemData;

    public ProcessAdapter(Context context, List<ProcessInfoBean> userData, List<ProcessInfoBean> systemData) {
        this.mContext = context;
        this.mUserData = userData;
        this.mSystemData = systemData;
    }

    @Override
    public int getCount() {
        return mUserData.size() + mSystemData.size() + 2;
    }

    @Override
    public ProcessInfoBean getItem(int position) {
//        if (position < mUserData.size()) {
//            return mUserData.get(position);
//        } else {
//            return mSystemData.get(position - mUserData.size());
//        }

//        if (position == 0) {
//            return null;
//        }
//        if (position < mUserData.size() + 1) {
//            return mUserData.get(position - 1);
//        }
//        if (position == mUserData.size() + 1) {
//            return null;
//        }
//        if (position >= mUserData.size() + 2) {
//            return mSystemData.get(position - (mUserData.size() + 2));
//        }
//        return null;

        if (position == 0 || position == mUserData.size() + 1) {
            return null;
        } else if (position < mUserData.size() + 1) {
            return mUserData.get(position - 1);
        } else {
            return mSystemData.get(position - (mUserData.size() + 2));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //根据索引返回当前列表的类型值
    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mUserData.size() + 1) {
            return 0;//标题型，类型值必须从0开始
        } else {
            return 1;//列表型
        }
    }

    //设置当前listview视图类型数量
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //根据索引，获取对应条目的类型值，根据类型值，返回对应条目的视图界面对象
        int itemViewType = getItemViewType(position);
        ViewHolder holder = null;
        switch (itemViewType) {
            case 0://标题型
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.view_item_processmanager_title, null);
                }
                TextView tvProcessItemTitle = (TextView) convertView.findViewById(R.id.tv_process_title);
                if (position == 0) {
                    tvProcessItemTitle.setText("用户进程(" + mUserData.size() + ")个");
                } else {
                    tvProcessItemTitle.setText("系统进程(" + mSystemData.size() + ")个");
                }
                break;
            case 1://列表型
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
                break;
            default:
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvMemory;
        CheckBox cb;
    }
}
