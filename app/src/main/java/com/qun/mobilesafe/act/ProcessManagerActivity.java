package com.qun.mobilesafe.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.engine.ProcessInfoProvider;
import com.qun.mobilesafe.view.ProgressDescView;

public class ProcessManagerActivity extends AppCompatActivity {

    private ProgressDescView mPdvProcessNum;
    private ProgressDescView mPdvProcessMemory;

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
