package com.qun.mobilesafe.act;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
        switch (v.getId()) {
            case R.id.bt_anti_virus_scan_result:
                // 关门动画
                closeDoorAnimation();
                break;
            default:
                break;
        }
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

            //扫描结束显示出结果界面
            mLlAntiVirusScanResult.setVisibility(View.VISIBLE);
            AntiVirusBean antiVirusBean = mData.get(0);
            mTvAntiVirusScanResult.setText(antiVirusBean.isAntiVirus ? "你的手机很危险，请注意" : "你的手机很安全，请放心");

            //将扫描结果界面的百分之百的图片获取一下
            mLlAntiVirusScan.setDrawingCacheEnabled(true);
            Bitmap drawingCache = mLlAntiVirusScan.getDrawingCache();

            mLlAntiVirusAnimation.setVisibility(View.VISIBLE);

            //获取百分百界面的左边的图片
            Bitmap leftBitmap = getLeftDrawingCache(drawingCache);
            mIvAntiVirusAnimationLeft.setImageBitmap(leftBitmap);
            //获取百分百界面的右边的图片
            Bitmap rightBitmap = getRightDrawingCache(drawingCache);
            mIvAntiVirusAnimationRight.setImageBitmap(rightBitmap);

            //将扫描界面隐藏
            mLlAntiVirusScan.setVisibility(View.INVISIBLE);

            //启动开门动画
            startOpenDoorAnimation();
        }

        public Bitmap getLeftDrawingCache(Bitmap drawingCache) {
            // 创建出一个与左半边一样大小的bitmap对象出来
            int width = (int) (drawingCache.getWidth() / 2.0f + 0.5f);
            int height = drawingCache.getHeight();
            Bitmap.Config config = drawingCache.getConfig();
            Bitmap newBitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(newBitmap);
            Matrix matrix = new Matrix();// 矩阵设置图片以什么形式(平移，缩放，旋转)绘制到画布
            canvas.drawBitmap(drawingCache, matrix, null);
            return newBitmap;
        }

        public Bitmap getRightDrawingCache(Bitmap drawingCache) {
            // 创建出一个与左半边一样大小的bitmap对象出来
            int width = (int) (drawingCache.getWidth() / 2.0f + 0.5f);
            int height = drawingCache.getHeight();
            Bitmap.Config config = drawingCache.getConfig();
            Bitmap newBitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(newBitmap);
            Matrix matrix = new Matrix();//矩阵设置图片以什么形式(平移，缩放，旋转)绘制到画布
            // matrix.setTranslate(-width, 0);//让图片左移一半，将右半边的图片放置到画布上，只能设置一次操作
            matrix.postTranslate(-width, 0);//能设置多次操作
            canvas.drawBitmap(drawingCache, matrix, null);
            return newBitmap;
        }

        //启动开门动画
        public void startOpenDoorAnimation() {
            //左图左移 透明度
            ObjectAnimator oa1 = ObjectAnimator.ofFloat(mIvAntiVirusAnimationLeft, "translationX", 0, -mIvAntiVirusAnimationLeft.getWidth());
            ObjectAnimator oa2 = ObjectAnimator.ofFloat(mIvAntiVirusAnimationLeft, "alpha", 1.0f, 0.0f);
            //右图右移 透明度
            ObjectAnimator oa3 = ObjectAnimator.ofFloat(mIvAntiVirusAnimationRight, "translationX", 0, mIvAntiVirusAnimationRight.getWidth());
            ObjectAnimator oa4 = ObjectAnimator.ofFloat(mIvAntiVirusAnimationRight, "alpha", 1.0f, 0.0f);

            //扫描结果 透明度
            ObjectAnimator oa5 = ObjectAnimator.ofFloat(mLlAntiVirusScanResult, "alpha", 0.0f, 1.0f);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(oa1, oa2, oa3, oa4, oa5);
            animatorSet.setDuration(1500);
            animatorSet.start();
        }
    }

    private void closeDoorAnimation() {
        // 左图右移 透明度
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(mIvAntiVirusAnimationLeft, "translationX", -mIvAntiVirusAnimationLeft.getWidth(), 0);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(mIvAntiVirusAnimationLeft, "alpha", 0.0f, 1.0f);
        // 右图左移 透明度
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(mIvAntiVirusAnimationRight, "translationX", mIvAntiVirusAnimationRight.getWidth(), 0);
        ObjectAnimator oa4 = ObjectAnimator.ofFloat(mIvAntiVirusAnimationRight, "alpha", 0.0f, 1.0f);

        // 扫描结果 透明度
        ObjectAnimator oa5 = ObjectAnimator.ofFloat(mLlAntiVirusScanResult, "alpha", 1.0f, 0.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(oa1, oa2, oa3, oa4, oa5);
        animatorSet.setDuration(1500);
        animatorSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //关门动画执行完毕后，重新扫描
                //隐藏动画界面
                mLlAntiVirusAnimation.setVisibility(View.INVISIBLE);
                //清理集合避免重复
                mData.clear();
                //显示扫描界面
                mLlAntiVirusScan.setVisibility(View.VISIBLE);
                initData();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animatorSet.start();
    }
}
