package com.qun.mobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.bean.LocationBean;
import com.qun.mobilesafe.utils.Contants;
import com.qun.mobilesafe.utils.SpUtil;

import java.util.List;

/**
 * Created by Qun on 2017/6/5.
 */

public class LocationDialogAdapter extends BaseAdapter {

    private Context mContext;
    private List<LocationBean> mData;

    public LocationDialogAdapter(Context context, List<LocationBean> data) {
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
            convertView = View.inflate(mContext, R.layout.view_item_location_dialog, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_item_dialog);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_dialog);
            holder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LocationBean locationBean = mData.get(position);
        holder.iv.setImageResource(locationBean.imageId);
        holder.tvTitle.setText(locationBean.title);

        //当对应的条目重新显示时，根据sp记录的颜色值来判断当前的条目是否该选中
        int imageId = SpUtil.getInt(mContext, Contants.KEY_LOCATION_IMAGEID, R.drawable.shape_location_normal);
        if (imageId == locationBean.imageId) {
            holder.ivSelected.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelected.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView iv;
        TextView tvTitle;
        ImageView ivSelected;
    }
}
