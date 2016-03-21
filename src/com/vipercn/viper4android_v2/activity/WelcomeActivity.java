package com.vipercn.viper4android_v2.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.vipercn.viper4android_v2.R;
import com.vipercn.viper4android_v2.service.ViPER4AndroidService;

import java.util.ArrayList;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (isWorked("com.vipercn.viper4android_v2.service.ViPER4AndroidService")) {
        if (ViPER4AndroidService.isServiceStart) {
            Intent intent = new Intent(WelcomeActivity.this, ViPER4Android.class);
            startActivity(intent);
            finish();
            return;
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, ViPER4Android.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
