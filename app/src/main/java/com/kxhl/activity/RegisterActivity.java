package com.kxhl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kxhl.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import util.Config;
import util.KxhlRestClient;
import util.TitleUtil;
import util.UrlLIst;

/**
 * Created by Administrator on 2017/1/6.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText et_registerPhone;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        new TitleUtil(this).setTitleName("注册").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        initView();
    }

    public void initView() {
        et_registerPhone = (EditText) findViewById(R.id.et_registerPhone);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register://验证
                if (Config.isMobileNO(et_registerPhone.getText().toString())) {
                    getData(et_registerPhone.getText().toString());
                } else {
                    Toast.makeText(RegisterActivity.this, "手机号码格式错误！", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 获取数据
     */
    public void getData(String phone) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        KxhlRestClient.post(UrlLIst.LOGIN_CHECK, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    String a = response.getString("stat");
                    if (a.equals("200")) {
                        Intent i=new Intent();
                        i.putExtra("phone",et_registerPhone.getText().toString());
                        i.setClass(RegisterActivity.this,RegisterInfoActivity.class);
                        startActivity(i);
                        Toast.makeText(RegisterActivity.this, "验证成功！", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, "手机号已被注册", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RegisterActivity.this, responseString, Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
