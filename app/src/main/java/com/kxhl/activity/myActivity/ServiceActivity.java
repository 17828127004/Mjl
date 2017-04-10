package com.kxhl.activity.myActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kxhl.R;

import util.TitleUtil;

/**
 * Created by Administrator on 2017/1/18.
 */
public class ServiceActivity extends Activity implements View.OnClickListener{
    private LinearLayout ll_service,ll_service_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sevres);
        new TitleUtil(this).setTitleName("服务中心").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        ll_service=(LinearLayout)findViewById(R.id.ll_service_service);
        ll_service_phone=(LinearLayout)findViewById(R.id.ll_service_phone);
        ll_service.setOnClickListener(this);
        ll_service_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(ServiceActivity.this,"点击了",Toast.LENGTH_SHORT);
        switch (v.getId()){
            case R.id.ll_service_service://在线客服

            break;
            case R.id.ll_service_phone://客服热线

                break;
        }
    }
}
