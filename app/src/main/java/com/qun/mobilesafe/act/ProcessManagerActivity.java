package com.qun.mobilesafe.act;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.adapter.ProcessAdapter;
import com.qun.mobilesafe.bean.ProcessInfoBean;
import com.qun.mobilesafe.engine.ProcessInfoProvider;
import com.qun.mobilesafe.view.ProgressDescView;

import java.util.ArrayList;
import java.util.List;

public class ProcessManagerActivity extends AppCompatActivity {

    private ProgressDescView mPdvProcessNum;
    private ProgressDescView mPdvProcessMemory;
    private ListView mLvProcessManager;
    private List<ProcessInfoBean> mData;
    private View mLlLoading;// 加载进度圈
    private TextView mTvTitle;
    private LinearLayout mLlProcessTitle;
    private List<ProcessInfoBean> userData = new ArrayList<>();// 用户进程数据
    private List<ProcessInfoBean> systemData = new ArrayList<>();// 系统进程数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);

        initView();
        initData();
    }

    private void initView() {
        mPdvProcessNum = (ProgressDescView) findViewById(R.id.pdv_process_num);
        mPdvProcessMemory = (ProgressDescView) findViewById(R.id.pdv_process_memory);
        mLvProcessManager = (ListView) findViewById(R.id.lv_process_manager);

        mLlLoading = findViewById(R.id.ll_loading);
        //获取标题型的布局的文本控件
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mLlProcessTitle = (LinearLayout) findViewById(R.id.ll_process_title);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                List<ProcessInfoBean> runningProcessInfos = ProcessInfoProvider.getRunningProcessInfos(getApplicationContext());
                for (ProcessInfoBean processInfoBean : runningProcessInfos) {
                    if (processInfoBean.isSystem) {
                        systemData.add(processInfoBean);
                    } else {
                        userData.add(processInfoBean);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLlLoading.setVisibility(View.INVISIBLE);
                        ProcessAdapter processAdapter = new ProcessAdapter(ProcessManagerActivity.this, userData, systemData);
                        mLvProcessManager.setAdapter(processAdapter);
                        mLlProcessTitle.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

    private void initData() {
        // 获取进程数并设置界面
        initProcessNum();
        // 获取内存数并设置
        initMemory();
    }

    private void initProcessNum() {
        //获取进程数并设置界面
        mPdvProcessNum.setTitle("进程数：");
        int runningProcessNum = ProcessInfoProvider.getRunningProcessNum(getApplicationContext());
        int allProcessNum = ProcessInfoProvider.getAllProcessNum(getApplicationContext());
        mPdvProcessNum.setLeftText("正在运行(" + runningProcessNum + ")个");
        mPdvProcessNum.setRightText("总进程(" + allProcessNum + ")个");
        //通过+0.5f进行四舍五入
        mPdvProcessNum.setProgress((int) (runningProcessNum * 100f / allProcessNum + 0.5f));
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
}
