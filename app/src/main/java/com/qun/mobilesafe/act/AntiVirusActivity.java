package com.qun.mobilesafe.act;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.bean.AntiVirusBean;
import com.qun.mobilesafe.bean.AppInfoBean;
import com.qun.mobilesafe.db.AntiVirusDao;
import com.qun.mobilesafe.engine.AppInfoProvider;
import com.qun.mobilesafe.utils.MD5Utils;

import java.util.ArrayList;
import java.util.List;

public class AntiVirusActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mLvAntiVirus;
    private List<AntiVirusBean> mData = new ArrayList<>();
    //    private AntiVirusAdapter mAdapter;
    private LinearLayout mLlAntiVirusScan;
    //    private ArcProgress mArcProgress;
    private TextView mTvAntvirusScanName;
    private LinearLayout mLlAntiVirusScanResult;
    private TextView mTvAntiVirusScanResult;
    private Button mBtAntiVirusScanResult;
    private LinearLayout mLlAntiVirusAnimation;
    private ImageView mIvAntiVirusAnimationLeft;
    private ImageView mIvAntiVirusAnimationRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);

        initView();
        initData();
    }

    private void initData() {
        new AntiVirusTask().execute();
    }

    private void initView() {
        mLvAntiVirus = (ListView) findViewById(R.id.lv_anti_virus);
        //扫描界面
        mLlAntiVirusScan = (LinearLayout) findViewById(R.id.ll_anti_virus_scan);
//        mArcProgress = (ArcProgress) findViewById(R.id.arc_progress);
        mTvAntvirusScanName = (TextView) findViewById(R.id.tv_anti_virus_scan_name);
        //扫描结果界面
        mLlAntiVirusScanResult = (LinearLayout) findViewById(R.id.ll_anti_virus_scan_result);
        mTvAntiVirusScanResult = (TextView) findViewById(R.id.tv_anti_virus_scan_result);
        mBtAntiVirusScanResult = (Button) findViewById(R.id.bt_anti_virus_scan_result);
        mBtAntiVirusScanResult.setOnClickListener(this);

        //动画界面
        mLlAntiVirusAnimation = (LinearLayout) findViewById(R.id.ll_anti_virus_animation);
        mIvAntiVirusAnimationLeft = (ImageView) findViewById(R.id.iv_anti_virus_animation_left);
        mIvAntiVirusAnimationRight = (ImageView) findViewById(R.id.iv_anti_virus_animation_right);
    }

    @Override
    public void onClick(View v) {

    }

    private class AntiVirusTask extends AsyncTask<String, AntiVirusBean, String> {

        //唯一一个运行在子线程中的方法
        @Override
        protected String doInBackground(String... params) {
            List<AppInfoBean> allAppInfo = AppInfoProvider.getAllAppInfo(getApplicationContext());
            for (AppInfoBean appInfoBean : allAppInfo) {
                String fileMd5 = MD5Utils.getFileMd5(appInfoBean.appPath);
                boolean isAntiVirus = AntiVirusDao.queryAntiVirus(getApplicationContext(), fileMd5);
                System.out.println(appInfoBean.appName + ": " + fileMd5 + " " + isAntiVirus);
                AntiVirusBean antiVirusBean = new AntiVirusBean();
                antiVirusBean.appIcon = appInfoBean.appIcon;
                antiVirusBean.appName = appInfoBean.appName;
                antiVirusBean.isAnitVirus = isAntiVirus;

                //在doInBackground中mData里面获取一个数据，就应该在主线程中刷新一次
                //该方法执行，能够导致AsyncTask里面的一个运行在主线程的方法执行一次onProgressUpdate;
                publishProgress(antiVirusBean);

                SystemClock.sleep(50);
            }
            return null;
        }
    }
}
