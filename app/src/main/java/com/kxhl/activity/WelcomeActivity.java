package com.kxhl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kxhl.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/10.
 */
public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final Intent intent=new Intent(this,MainActivity.class);
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
            startActivity(intent);
                finish();
            }
        };
        timer.schedule(task,1000);
    }
}
