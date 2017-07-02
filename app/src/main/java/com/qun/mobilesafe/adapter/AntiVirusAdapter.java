package com.qun.mobilesafe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.bean.AntiVirusBean;

import java.util.List;

/**
 * Created by Qun on 2017/7/2.
 */

public class AntiVirusAdapter extends BaseAdapter {

    private Context mContext;
    private List<AntiVirusBean> mData;

    public AntiVirusAdapter(Context context, List<AntiVirusBean> data) {
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
            convertView = View.inflate(mContext, R.layout.view_item_antivirus, null);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_item_anti_virus_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_anti_virus_name);
            holder.tvIsAntiVirus = (TextView) convertView.findViewById(R.id.tv_item_anti_virus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AntiVirusBean antiVirusBean = (AntiVirusBean) getItem(position);
        holder.ivIcon.setImageDrawable(antiVirusBean.appIcon);
        holder.tvName.setText(antiVirusBean.appName);
        holder.tvIsAntiVirus.setText(antiVirusBean.isAntiVirus ? "病毒" : "安全");
        holder.tvIsAntiVirus.setTextColor(antiVirusBean.isAntiVirus ? Color.RED : Color.GREEN);

        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvIsAntiVirus;
    }
}
