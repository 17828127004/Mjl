package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kxhl.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import util.Config;
import util.KxhlRestClient;
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;

/**
 * Created by Administrator on 2017/1/20.
 */
public class MsgTwoActivity extends Activity {
    private TextView tv_name;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_two);
        new TitleUtil(this).setTitleName("消息详情").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(MsgTwoActivity.this, MsgActivity.class));
                        finish();
                    }
                });
        Config.setTranslucent(this);
        tv_content = (TextView) findViewById(R.id.tv_msgTow_content);
        tv_name = (TextView) findViewById(R.id.tv_MsgTwo_name);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        Intent i = getIntent();
        String name = i.getStringExtra("MsgName");
        String content = i.getStringExtra("MsgContent");
        tv_name.setText(name);
        tv_content.setText(content);
    }

}
