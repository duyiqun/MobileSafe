package com.qun.mobilesafe.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.act.AppLockActivity;
import com.qun.mobilesafe.bean.AppInfoBean;
import com.qun.mobilesafe.db.AppLockDao;

import java.util.List;

/**
 * Created by Qun on 2017/6/29.
 */

public class AppLockAdapter extends BaseAdapter {

    private final List<AppInfoBean> mData;
    private final boolean mIsLock;//用来区分未加锁与已加锁操作，true，已加锁
    private final List<AppInfoBean> mOtherData;
    private Context mContext;
    private AppLockDao mAppLockDao;


    //如果isLock为true，data是已加锁的数据集合，otherData是未加锁的数据集合，如果isLock为false，data是未加锁的数据集合，otherData是已加锁的数据集合
    public AppLockAdapter(Context context, List<AppInfoBean> unlockData, List<AppInfoBean> otherData, boolean isLock) {
        this.mContext = context;
        this.mData = unlockData;//址传递（activity中数据删除掉后，适配器中的 数据也删除了）
        this.mIsLock = isLock;
        this.mOtherData = otherData;
        mAppLockDao = new AppLockDao(mContext);
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
        final View itemView = convertView;
        final AppInfoBean appInfoBean = mData.get(position);
        holder.tvName.setText(appInfoBean.appName);
        holder.ivIcon.setImageDrawable(appInfoBean.appIcon);
        if (mIsLock) {
            holder.ivApplock.setImageResource(R.drawable.selector_iv_unlock);
        } else {
            holder.ivApplock.setImageResource(R.drawable.selector_iv_lock);
        }

        // 给加锁解锁按钮设置点击事件
        if (mIsLock) {// 解锁操作
            holder.ivApplock.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //从数据库删除，如果成功，则从当前列表中删除
                    if (mAppLockDao.delete(appInfoBean.appPackageName)) {

                        Animation unlockAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_applock_unlock);
                        unlockAnimation.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mData.remove(appInfoBean);
                                notifyDataSetChanged();

                                //调用AppLockActivity里面的updateNum
                                ((AppLockActivity) mContext).updateNum(mIsLock);
                                mOtherData.add(appInfoBean);
                            }
                        });
                        itemView.startAnimation(unlockAnimation);
                    }
                }
            });
        } else {// 加锁操作
            holder.ivApplock.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //插入数据库，如果成功，则从当前列表中删除,更新界面上的数量，将删除的数据传递给另一个列表进行展示
                    if (mAppLockDao.insert(appInfoBean.appPackageName)) {
                        //让条目执行右移动画
                        Animation lockAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_applock_lock);
                        lockAnimation.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mData.remove(appInfoBean);
                                notifyDataSetChanged();

                                //调用AppLockActivity里面的updateNum
                                ((AppLockActivity) mContext).updateNum(mIsLock);
                                mOtherData.add(appInfoBean);
                            }
                        });
                        itemView.startAnimation(lockAnimation);
                    }
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView ivIcon;
        ImageView ivApplock;
        TextView tvName;
    }
}
