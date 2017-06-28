package com.qun.mobilesafe.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.bean.ProcessInfoBean;
import com.qun.mobilesafe.engine.ProcessInfoProvider;
import com.qun.mobilesafe.service.AutoCleanService;
import com.qun.mobilesafe.utils.Contants;
import com.qun.mobilesafe.utils.ServiceStateUtil;
import com.qun.mobilesafe.utils.SpUtil;
import com.qun.mobilesafe.view.ProgressDescView;
import com.qun.mobilesafe.view.SettingItemView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ProcessManagerActivity2 extends AppCompatActivity implements View.OnClickListener {

    private ProgressDescView mPdvProcessNum;
    private ProgressDescView mPdvProcessMemory;
    private StickyListHeadersListView mSlhLvProcessManager;
    private List<ProcessInfoBean> mData = new ArrayList<>();
    private View mLlLoading;//加载进度圈
    private Button mBtnProcessAll;
    private Button mBtnProcessReverse;
    private List<ProcessInfoBean> mUserData = new ArrayList<>();//用户进程数据
    private List<ProcessInfoBean> mSystemData = new ArrayList<>();//系统进程数据
    private ProcessAdapter2 mProcessAdapter;
    private ImageButton mIbProcessClean;
    private int mRunningProcessNum;
    private ImageView mIvProcessArrow1;
    private ImageView mIvProcessArrow2;
    private SlidingDrawer mSlidingDrawer;
    private SettingItemView mSivProcessShow;
    private SettingItemView mSivProcessClean;
    private boolean mIsSystemShow;// 记录当前界面是否要显示系统进程

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager2);

        initView();
        initData();
    }

    private void initView() {
        mPdvProcessNum = (ProgressDescView) findViewById(R.id.pdv_process_num);
        mPdvProcessMemory = (ProgressDescView) findViewById(R.id.pdv_process_memory);
        mSlhLvProcessManager = (StickyListHeadersListView) findViewById(R.id.slhlv_process_manager);

        mLlLoading = findViewById(R.id.ll_loading);

        //获取全选反选按钮
        mBtnProcessAll = (Button) findViewById(R.id.btn_process_all);
        mBtnProcessReverse = (Button) findViewById(R.id.btn_process_reverse);
        mBtnProcessAll.setOnClickListener(this);
        mBtnProcessReverse.setOnClickListener(this);

        //清理按钮
        mIbProcessClean = (ImageButton) findViewById(R.id.ib_process_clean);
        mIbProcessClean.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                List<ProcessInfoBean> runningProcessInfos = ProcessInfoProvider.getRunningProcessInfos(getApplicationContext());
                for (ProcessInfoBean processInfoBean : runningProcessInfos) {
                    if (processInfoBean.isSystem) {
                        mSystemData.add(processInfoBean);
                    } else {
                        mUserData.add(processInfoBean);
                    }
                }
                mData.addAll(mUserData);
                mData.addAll(mSystemData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLlLoading.setVisibility(View.INVISIBLE);
//                        ProcessAdapter processAdapter = new ProcessAdapter(ProcessManagerActivity2.this, userData, systemData);
//                        mLvProcessManager.setAdapter(processAdapter);
//                        mLlProcessTitle.setVisibility(View.VISIBLE);

                        mProcessAdapter = new ProcessAdapter2();
                        mSlhLvProcessManager.setAdapter(mProcessAdapter);
                    }
                });
            }
        }).start();

        //设置箭头动画
        mIvProcessArrow1 = (ImageView) findViewById(R.id.iv_process_arrow1);
        mIvProcessArrow2 = (ImageView) findViewById(R.id.iv_process_arrow2);
        startArrowAnimation();

        //给SlidingDrawer设置打开关闭监听
        mSlidingDrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);
        mSlidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {

            @Override
            public void onDrawerClosed() {
                startArrowAnimation();
            }
        });
        mSlidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {

            @Override
            public void onDrawerOpened() {
                stopArrowAnimation();
            }
        });

        //获取显示系统进程与锁屏自动清理
        mSivProcessShow = (SettingItemView) findViewById(R.id.siv_process_show);
        mSivProcessClean = (SettingItemView) findViewById(R.id.siv_process_clean);

        mSivProcessShow.setOnClickListener(this);
        mSivProcessClean.setOnClickListener(this);

        //在界面展示时，回显系统显示安装的状态
        mIsSystemShow = SpUtil.getBoolean(getApplicationContext(),
                Contants.KEY_SYSTEM_SHOW, true);
        mSivProcessShow.setToggle(mIsSystemShow);
    }

    private void stopArrowAnimation() {
        mIvProcessArrow1.clearAnimation();
        mIvProcessArrow2.clearAnimation();
        mIvProcessArrow1.setImageResource(R.mipmap.drawer_arrow_down);
        mIvProcessArrow2.setImageResource(R.mipmap.drawer_arrow_down);
    }

    private void startArrowAnimation() {
        mIvProcessArrow1.setImageResource(R.mipmap.drawer_arrow_up);
        mIvProcessArrow2.setImageResource(R.mipmap.drawer_arrow_up);

        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation1.setDuration(500);
        alphaAnimation1.setRepeatCount(Animation.INFINITE);
        alphaAnimation1.setRepeatMode(Animation.REVERSE);// 重复模式
        mIvProcessArrow1.startAnimation(alphaAnimation1);
        AlphaAnimation alphaAnimation2 = new AlphaAnimation(1.0f, 0.2f);
        alphaAnimation2.setDuration(500);
        alphaAnimation2.setRepeatCount(Animation.INFINITE);
        alphaAnimation2.setRepeatMode(Animation.REVERSE);// 重复模式
        mIvProcessArrow2.startAnimation(alphaAnimation2);
    }

    private void initData() {
        //获取进程数并设置界面
        initProcessNum();
        //获取内存数并设置
        initMemory();
    }

    private void initProcessNum() {
        mRunningProcessNum = ProcessInfoProvider.getRunningProcessNum(getApplicationContext());
        initCleanProcessNum();
    }

    private void initCleanProcessNum() {
        mPdvProcessNum.setTitle("进程数：");
        int allProcessNum = ProcessInfoProvider.getAllProcessNum(getApplicationContext());
        mPdvProcessNum.setLeftText("正在运行(" + mRunningProcessNum + ")个");
        mPdvProcessNum.setRightText("总进程(" + allProcessNum + ")个");
        //通过+0.5f进行四舍五入
        mPdvProcessNum.setProgress((int) (mRunningProcessNum * 100f / allProcessNum + 0.5f));
    }

    private void initMemory() {
        mPdvProcessMemory.setTitle("内存：");
        long availMemory = ProcessInfoProvider.getAvailMemory(getApplicationContext());
        long totalMemory = ProcessInfoProvider.getTotalMemory(getApplicationContext());
        long usedMemory = totalMemory - availMemory;
        mPdvProcessMemory.setLeftText("占用内存：" + Formatter.formatFileSize(getApplicationContext(), usedMemory));
        mPdvProcessMemory.setRightText("可用内存：" + Formatter.formatFileSize(getApplicationContext(), availMemory));
        mPdvProcessMemory.setProgress((int) (usedMemory * 100f / totalMemory + 0.5f));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_process_all:// 全选
                //遍历所有的数据，将对应列表的javabean的字段设置为选中并刷新即可
                List<ProcessInfoBean> allData;
                if (mIsSystemShow) {
                    allData = mData;
                } else {
                    allData = mUserData;
                }
                for (ProcessInfoBean bean : allData) {
                    //如果遇到本应用的包名时，不做任务操作
                    if (TextUtils.equals(bean.appPackageName, getPackageName())) {
                        continue;
                    }
                    bean.isSelected = true;
                }
                mProcessAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_process_reverse:// 反选
                //遍历所有的数据，将对应列表的javabean的字段取反并刷新即可
                List<ProcessInfoBean> reverseData;
                if (mIsSystemShow) {
                    reverseData = mData;
                } else {
                    reverseData = mUserData;
                }
                for (ProcessInfoBean bean : reverseData) {
                    // 如果遇到本应用的包名时，不做任务操作
                    if (TextUtils.equals(bean.appPackageName, getPackageName())) {
                        continue;
                    }
                    bean.isSelected = !bean.isSelected;
                }
                mProcessAdapter.notifyDataSetChanged();
                break;
            case R.id.ib_process_clean:// 清理
                //遍历所有的数据，如果是选中的则，清理掉进程，并从列表中删除
                //增强for循环不能遍遍历遍删除，因此应该用迭代器
//                for (ProcessInfoBean bean : mData) {
//                    if (bean.isSelected) {
//                        ProcessInfoProvider.cleanProcess(getApplicationContext(), bean.appPackageName);
//                        mData.remove(bean);
//                    }
//                }

                List<ProcessInfoBean> cleanData;
                if (mIsSystemShow) {
                    cleanData = mData;
                } else {
                    cleanData = mUserData;
                }

                ListIterator<ProcessInfoBean> listIterator = cleanData.listIterator();
                while (listIterator.hasNext()) {
                    ProcessInfoBean processInfoBean = (ProcessInfoBean) listIterator.next();
                    if (processInfoBean.isSelected) {
                        ProcessInfoProvider.cleanProcess(getApplicationContext(), processInfoBean.appPackageName);
                        listIterator.remove();

                        if (mIsSystemShow) {
                            //mData里面要删除时，必须从系统与用户集合中同时删除，保证数据统一
                            if (processInfoBean.isSystem) {
                                mSystemData.remove(processInfoBean);
                            } else {
                                mUserData.remove(processInfoBean);
                            }
                        } else {
                            //如果用户集合中数据删除了，同样在mData里面也要删除，保证数据的统一
                            mData.remove(processInfoBean);
                        }
                    }
                }
                mProcessAdapter.notifyDataSetChanged();

                //重新计算进程数
                mRunningProcessNum = mData.size();
                initCleanProcessNum();

                //重新计算占用内存数
                long usedMemory = 0;
                for (ProcessInfoBean bean : mData) {
                    usedMemory += bean.appMemory;
                }
                initCleanMemory(usedMemory);
                break;
            case R.id.siv_process_show://显示系统进程
                mSivProcessShow.toggle();
                SpUtil.saveBoolean(getApplicationContext(),
                        mSivProcessShow.isToggle(), Contants.KEY_SYSTEM_SHOW);
                mIsSystemShow = mSivProcessShow.isToggle();
                mProcessAdapter.notifyDataSetChanged();
                break;
            case R.id.siv_process_clean://锁屏清理
                mSivProcessClean.toggle();
                //根据服务的状态来启动或关闭操作
                if (ServiceStateUtil.isServiceRunning(ProcessManagerActivity2.this, AutoCleanService.class)) {
                    stopService(new Intent(ProcessManagerActivity2.this, AutoCleanService.class));
                } else {
                    startService(new Intent(ProcessManagerActivity2.this, AutoCleanService.class));
                }
                break;
            default:
                break;
        }
    }

    private void initCleanMemory(long usedMemory) {
        long totalMemory = ProcessInfoProvider.getTotalMemory(getApplicationContext());
        long availMemory = totalMemory - usedMemory;

        mPdvProcessMemory.setTitle("内存：");
        mPdvProcessMemory.setLeftText("占用内存：" + Formatter.formatFileSize(getApplicationContext(), usedMemory));
        mPdvProcessMemory.setRightText("可用内存：" + Formatter.formatFileSize(getApplicationContext(), availMemory));
        mPdvProcessMemory.setProgress((int) (usedMemory * 100f / totalMemory + 0.5f));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ServiceStateUtil.isServiceRunning(ProcessManagerActivity2.this, AutoCleanService.class)) {
            mSivProcessClean.setToggle(true);
        } else {
            mSivProcessClean.setToggle(false);
        }
    }

    private class ProcessAdapter2 extends BaseAdapter implements StickyListHeadersAdapter {

        @Override
        public int getCount() {
            if (mIsSystemShow) {
                return mData.size();
            } else {
                return mUserData.size();
            }
        }

        @Override
        public ProcessInfoBean getItem(int position) {
            if (mIsSystemShow) {
                return mData.get(position);
            } else {
                return mUserData.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(ProcessManagerActivity2.this, R.layout.view_item_processmanager, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_process_manager_icon);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_process_manager_name);
                holder.tvMemory = (TextView) convertView.findViewById(R.id.tv_process_manager_memory);
                holder.cb = (CheckBox) convertView.findViewById(R.id.cb_process_manager);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final ProcessInfoBean processInfoBean = getItem(position);
            holder.ivIcon.setImageDrawable(processInfoBean.appIcon);
            //根据条目javabean动态设置选中状态
//            if (processInfoBean.isSelected) {
//                holder.cb.setChecked(true);
//            } else {
//                holder.cb.setChecked(false);
//            }
            holder.cb.setChecked(processInfoBean.isSelected);
            // 勾选功能，记录数据
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    processInfoBean.isSelected = isChecked;
                }
            });
            holder.tvName.setText(processInfoBean.appName);
            holder.tvMemory.setText(Formatter.formatFileSize(ProcessManagerActivity2.this, processInfoBean.appMemory));

            // 判断当前条目是否与应用包名一致，如果一致，将选择框隐藏
            if (TextUtils.equals(processInfoBean.appPackageName, getPackageName())) {
                holder.cb.setVisibility(View.INVISIBLE);
            } else {
                holder.cb.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

        //根据头视图的id创建出不同头视图界面对象
        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            int headerId = (int) getHeaderId(position);
            if (convertView == null) {
                convertView = View.inflate(ProcessManagerActivity2.this, R.layout.view_item_processmanager_title, null);
            }
            TextView mTvProcessTitle = (TextView) convertView.findViewById(R.id.tv_process_title);
            switch (headerId) {
                case 0:
                    mTvProcessTitle.setText("系统进程(" + mSystemData.size() + ")个");
                    break;
                case 1:
                    mTvProcessTitle.setText("用户进程(" + mUserData.size() + ")个");
                    break;
                default:
                    break;
            }
            return convertView;
        }

        /**
         * 根据索引返回头视图的id
         * position 是listview的数据的原本的索引
         */
        @Override
        public long getHeaderId(int position) {
            ProcessInfoBean processInfoBean = getItem(position);
            if (processInfoBean.isSystem) {
                return 0;// 系统进程的头id是0
            } else {
                return 1;// 用户进程的头id是1
            }
        }

        class ViewHolder {
            ImageView ivIcon;
            TextView tvName;
            TextView tvMemory;
            CheckBox cb;
        }
    }
}