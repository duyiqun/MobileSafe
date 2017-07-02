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

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.qun.mobilesafe.R;
import com.qun.mobilesafe.adapter.AntiVirusAdapter;
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
    private AntiVirusAdapter mAdapter;
    private LinearLayout mLlAntiVirusScan;
    private ArcProgress mArcProgress;
    private TextView mTvAntiVirusScanName;
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
        mArcProgress = (ArcProgress) findViewById(R.id.arc_progress);
        mTvAntiVirusScanName = (TextView) findViewById(R.id.tv_anti_virus_scan_name);
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

    /**
     * 泛型一：决定doInBackground的参数类型，并且决定了execute的参数类型，execute的参数传递给了doInBackground方法作为参数
     * 泛型二：设置了publishProgress方法与onProgressUpdate方法的参数类型，并且publishProgress方法执行一次会导致onProgressUpdate方法会执行一次
     * 泛型三：决定doInBackground的返回值类型，并且决定了onPostExecute的参数类型，doInBackground的返回值就是传递给onPostExecute的参数
     */

    private class AntiVirusTask extends AsyncTask<String, AntiVirusBean, String> {

        private int max;

        //在子线程执行之前的主线程操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mAdapter = new AntiVirusAdapter(AntiVirusActivity.this, mData);
            mLvAntiVirus.setAdapter(mAdapter);
        }

        //唯一一个运行在子线程中的方法
        @Override
        protected String doInBackground(String... params) {
            List<AppInfoBean> allAppInfo = AppInfoProvider.getAllAppInfo(getApplicationContext());
            max = allAppInfo.size();
            for (AppInfoBean appInfoBean : allAppInfo) {
                String fileMd5 = MD5Utils.getFileMd5(appInfoBean.appPath);
                boolean isAntiVirus = AntiVirusDao.queryAntiVirus(getApplicationContext(), fileMd5);
                System.out.println(appInfoBean.appName + ": " + fileMd5 + " " + isAntiVirus);
                AntiVirusBean antiVirusBean = new AntiVirusBean();
                antiVirusBean.appIcon = appInfoBean.appIcon;
                antiVirusBean.appName = appInfoBean.appName;
                antiVirusBean.isAntiVirus = isAntiVirus;

                //在doInBackground中mData里面获取一个数据，就应该在主线程中刷新一次
                //该方法执行，能够导致AsyncTask里面的一个运行在主线程的方法执行一次onProgressUpdate;
                publishProgress(antiVirusBean);

                SystemClock.sleep(50);
            }
            return null;
        }

        // The content of the adapter has changed but ListView did not receive a
        // notification. Make sure the content of your adapter is not modified
        // from a background thread, but only from the UI thread. [in
        // ListView(2131034115, class android.widget.ListView) with
        // Adapter(class com.qun.mobilesafe.adapter.AntiVirusAdapter)]

        //运行在主线程
        @Override
        protected void onProgressUpdate(AntiVirusBean... values) {
            super.onProgressUpdate(values);
            AntiVirusBean antiVirusBean = values[0];

            //确保数据集合的修改在主线程中
            if (antiVirusBean.isAntiVirus) {
                mData.add(0, antiVirusBean);
            } else {
                mData.add(antiVirusBean);
            }

            mAdapter.notifyDataSetChanged();
            // 滑动到底部实现自动滚动功能
            // mLvAntiVirus.setSelection(mData.size() -1);//直接跳转到对应索引，不带有滚动效果
            mLvAntiVirus.smoothScrollToPosition(mData.size() - 1);//滚动到对应的索引，带有滚动效果

            mArcProgress.setProgress((int) (mData.size() * 100f / max + 0.5f));

            // 扫描时，实时显示当前扫描的应用名
            mTvAntiVirusScanName.setText(antiVirusBean.appName);
        }

        //doInBackground执行完毕之后，回到主线程执行的方法
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //扫描完毕后，将listview滚动到第一行
            mLvAntiVirus.smoothScrollToPosition(0);
        }
    }
}
