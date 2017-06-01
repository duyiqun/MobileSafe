package com.qun.mobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.bean.HomeBean;

import java.util.List;

/**
 * Created by Qun on 2017/6/1.
 */

public class HomeAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomeBean> mData;

    public HomeAdapter(Context context, List<HomeBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = View.inflate(parent.getContext(), R.layout.home_item_gv, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            holder.title = (TextView) convertView.findViewById(R.id.tv_item_title);
            holder.desc = (TextView) convertView.findViewById(R.id.tv_item_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeBean homeBean = mData.get(position);
        holder.iv.setImageResource(homeBean.imageId);//setImageResource与xml中src属性一一对应
//        holder.iv.setBackgroundResource(homeBean.imageId);//setBackgroundResource与xml中backgroud属性一一对应
        holder.title.setText(homeBean.title);
        holder.desc.setText(homeBean.desc);

        return convertView;
    }

    static class ViewHolder {
        ImageView iv;
        TextView title;
        TextView desc;
    }
}
