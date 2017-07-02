package com.qun.mobilesafe.application;

import android.app.Application;
import android.os.Environment;
import android.os.Process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Created by Qun on 2017/7/2.
 */

public class MyApplication extends Application {

    public String data = "test";

    //Application只有一个生命周期方法onCreate，application应用一经开启，优先于4大组件被创建出来并贯穿整个应用的上下文对象
    //Application是全局唯一的上下文对象，定义出全局的成员变量与方法
    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("MyApplication被创建了");
        //做三方sdk初始化操作（百度地图，推送，友盟统计）

        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
    }

    private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable ex) {
            //3.在应用打开，将错误信息判断，如果存在后台上传服务器

            //sd卡
            PrintStream err;
            try {
                err = new PrintStream(new File(Environment.getExternalStorageDirectory(), "err.log"));
                //2.收集错误信息
                ex.printStackTrace(err);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //1.一定不能让用户看到错误信息,直接关闭应用
            Process.killProcess(Process.myPid());
        }
    }

    public void doSometing() {
        System.out.println("doSometing");
    }
}
