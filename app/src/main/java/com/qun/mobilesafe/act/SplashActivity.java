package com.qun.mobilesafe.act;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.bean.UpdateBean;
import com.qun.mobilesafe.utils.PackageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private TextView mSplashTvVersion;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
        initData();
    }

    private void initView() {
        mSplashTvVersion = (TextView) findViewById(R.id.splash_tv_version);
    }

    private void initData() {
        //获取版本名展示
        initVersionName();

        //自动更新功能
        upDateVersion();
    }

    private void initVersionName() {
//        PackageManager manager = getPackageManager();
//        //参数一：要查询的应用包信息对应包名，参数二：标记，想获取什么信息，就设置什么标记（0：基本信息）
//        try {
//            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
//            String versionName = packageInfo.versionName;
//            mSplashTvVersion.setText(versionName);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        String versionName = PackageUtil.getVersionName(SplashActivity.this, getPackageName());
        mSplashTvVersion.setText(versionName);
    }

    private void upDateVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //1.访问服务器获取版本信息
                // 01. 定义okhttp
                OkHttpClient okHttpClient_get = new OkHttpClient();
//        OkHttpClient okHttpClient_get = new Builder().connectTimeout(1, TimeUnit.SECONDS).build();

                // 02.请求体
                Request request = new Request.Builder()
                        .get()//get请求方式
                        .url("http://192.168.1.106:8080/update.txt")//网址
                        .build();

                // 03.执行okhttp
                Response response = null;
                try {
                    //同步请求(网络请求在哪个线程中调用，那么就在该线程中进行网络请求，线程阻塞)
                    response = okHttpClient_get.newCall(request).execute();
//                    // 打印数据
//                    System.out.println(response.body().string());
                    //一次请求一次响应

                    String json = response.body().string();
                    JSONObject jsonObject = new JSONObject(json);
                    int remoteVersion = jsonObject.getInt("remoteVersion");
                    String desc = jsonObject.getString("desc");
                    String apkUrl = jsonObject.getString("apkUrl");

                    UpdateBean bean= new UpdateBean();
                    bean.desc = desc;
                    bean.versionCode = remoteVersion;
                    bean.apkUrl = apkUrl;

                    int versionCode = PackageUtil.getVersionCode(SplashActivity.this, getPackageName());

                    //2.比对版本弹出dialog提示
                    if (versionCode < remoteVersion) {
                        //弹出对话框
                        initUpdateDialog(bean);
                    }else {
                        enterHome();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initUpdateDialog(final UpdateBean bean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setTitle("版本更新提示");
                builder.setMessage(bean.desc);
                builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterHome();
                    }
                });
                builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressDialog = new ProgressDialog(SplashActivity.this);
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.show();

                        //3.是否升级，如果升级下apk
                        downLoadApk(bean);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        //4.下载成功，提示用户安装
        //5.判断是否安装
        //6.安装成功
    }

    private void downLoadApk(UpdateBean bean) {

    }

    private void enterHome() {
//        mHandler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                startActivity( new Intent(SplashActivity.this,HomeActivity.class));
//                finish();
//            }
//        }, 2000);
    }
}
