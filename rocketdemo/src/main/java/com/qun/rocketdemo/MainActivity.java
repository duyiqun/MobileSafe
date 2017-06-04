package com.qun.rocketdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launch(View v){
        startService(new Intent(MainActivity.this,RocketService.class));
        finish();
    }

    public void stop(View v){
        stopService(new Intent(MainActivity.this,RocketService.class));
    }
}
