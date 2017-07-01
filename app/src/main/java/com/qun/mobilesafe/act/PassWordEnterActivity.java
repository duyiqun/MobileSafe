package com.qun.mobilesafe.act;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qun.mobilesafe.R;
import com.qun.mobilesafe.utils.Contants;

public class PassWordEnterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvPasswordIcon;
    private TextView mTvPasswordName;
    private EditText mEt;
    private Button mBt;
    private String mPackageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word_enter);

        initView();
        initData();
    }

    private void initData() {
        mPackageName = getIntent().getStringExtra(Contants.KEY_PACKAGE_NAME);
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mPackageName, 0);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            Drawable appIcon = applicationInfo.loadIcon(packageManager);
            String appName = applicationInfo.loadLabel(packageManager).toString();

            mIvPasswordIcon.setImageDrawable(appIcon);
            mTvPasswordName.setText(appName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mIvPasswordIcon = (ImageView) findViewById(R.id.iv_password_icon);
        mTvPasswordName = (TextView) findViewById(R.id.tv_password_name);
        mEt = (EditText) findViewById(R.id.et);
        mBt = (Button) findViewById(R.id.bt);

        mBt.setOnClickListener(this);
    }

    //使用home键的处理来覆盖返回键的操作
    @Override
    public void onBackPressed() {
        // START {act=android.intent.action.MAIN
        // cat=[android.intent.category.HOME] flg=0x10200000
        // cmp=com.android.launcher/com.android.launcher2.Launcher u=0} from pid

        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                String password = mEt.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.equals(password, "123")) {
                    //进入对应应用中，发送广播将包名传递过去即可
                    Intent intent = new Intent();
                    intent.putExtra(Contants.KEY_PACKAGE_NAME, mPackageName);
                    intent.setAction(Contants.ACTION_SKIP_PACKAGE);
                    sendBroadcast(intent);

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        initData();
//    }

    //如果当前activity设置启动模式为singleInstance，它的intent数据只要界面不销毁不会获取最新的数据，如果需要获取最新的数据则实现以下方法
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData();
    }
}
