package com.qun.mobilesafe.act;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.bean.UpdateBean;
import com.qun.mobilesafe.utils.Contants;
import com.qun.mobilesafe.utils.GzipUtil;
import com.qun.mobilesafe.utils.HttpUtil;
import com.qun.mobilesafe.utils.PackageUtil;
import com.qun.mobilesafe.utils.SpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 实现闪屏界面
 */
public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;//安装请求码
    private TextView mSplashTvVersion;
    private ProgressDialog mProgressDialog;

    private Handler mHandler = new Handler();

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
        boolean flag = SpUtil.getBoolean(getApplicationContext(), Contants.KEY_AUTO_UPDATE, true);
        if (flag) {
            upDateVersion();
        } else {
            enterHome();
        }

        //数据初始化操作（数据库的复制与sp的创建）
        copyCommonDb();

        //复制常用号码数据库
        copyCommonNumDb();
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
//                OkHttpClient okHttpClient_get = new OkHttpClient();
                OkHttpClient okHttpClient_get = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS).build();

                // 02.请求体
                Request request = new Request.Builder().get()//get请求方式
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

                    UpdateBean bean = new UpdateBean();
                    bean.desc = desc;
                    bean.versionCode = remoteVersion;
                    bean.apkUrl = apkUrl;

                    int versionCode = PackageUtil.getVersionCode(SplashActivity.this, getPackageName());

                    //2.比对版本弹出dialog提示
                    if (versionCode < remoteVersion) {
                        //弹出对话框
                        initUpdateDialog(bean);
                    } else {
                        enterHome();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    enterHome();
                } catch (JSONException e) {
                    e.printStackTrace();
                    enterHome();
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
    }

    //下载apk
    private void downLoadApk(UpdateBean bean) {
        new Thread(new DownLoadApkTask(bean)).start();
    }

    private class DownLoadApkTask implements Runnable {

        private UpdateBean mBean;

        public DownLoadApkTask(UpdateBean bean) {
            this.mBean = bean;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            FileOutputStream fos = null;
            try {
                Response response = HttpUtil.httpGet(mBean.apkUrl);
                long contentLength = response.body().contentLength();
                mProgressDialog.setMax((int) contentLength);
                inputStream = response.body().byteStream();
                File apkFile = new File(Environment.getExternalStorageDirectory(), "MobileSafe.apk");
                fos = new FileOutputStream(apkFile);
                int len = -1;
                int progress = 0;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    progress += len;
                    mProgressDialog.setProgress(progress);
//					SystemClock.sleep(10);
                }
                fos.flush();
                mProgressDialog.dismiss();

                //安装apk
                //4.下载成功，提示用户安装
                installApk(apkFile);
            } catch (IOException e) {
                e.printStackTrace();
                enterHome();
            } finally {
//				closeIO(inputStream);
//				closeIO(fos);
                closeIOs(inputStream, fos);
            }
        }
    }

    //安装apk
    private void installApk(File apkFile) {
//		 <intent-filter>
//         <action android:name="android.intent.action.VIEW" />
//         <category android:name="android.intent.category.DEFAULT" />
//         <data android:scheme="content" />
//         <data android:scheme="file" />
//         <data android:mimeType="application/vnd.android.package-archive" />
//       </intent-filter>
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri data = Uri.fromFile(apkFile);
        intent.setDataAndType(data, "application/vnd.android.package-archive");
//		startActivity(intent);
        startActivityForResult(intent, REQUEST_CODE);
        //5.判断是否安装
        //6.安装成功
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    //确定
                    break;
                case RESULT_CANCELED:
                    //取消
                    System.out.println("RESULT_CANCELED");
                    enterHome();
                    break;
                default:
                    break;
            }
        }
    }

    private void closeIOs(Closeable... ios) {
        for (int i = 0; i < ios.length; i++) {
            Closeable closeable = ios[i];
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    enterHome();
                }
            }
        }
    }

    private void enterHome() {
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }, 2000);
    }

    private void copyCommonDb() {
        final File targetFile = new File(getFilesDir(), "address.db");
        if (targetFile.exists()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                //将assets里面的zip解压到data/data/包名/file内部
                AssetManager assetManager = getAssets();//用于管理assets的管理类
                try {
                    InputStream inputStream = assetManager.open("address.zip");
                    GzipUtil.unZip(inputStream, targetFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void copyCommonNumDb() {
        final File dbFile = new File(getFilesDir(), "commonnum.db");
        if (dbFile.exists()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                AssetManager assetManager = getAssets();
                InputStream inputStream = null;
                FileOutputStream fos = null;
                try {
                    inputStream = assetManager.open("commonnum.db");
                    fos = new FileOutputStream(dbFile);
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeIOs(inputStream, fos);
                }
            }
        }).start();
    }
}
