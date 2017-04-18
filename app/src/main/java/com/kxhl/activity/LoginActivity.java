package com.kxhl.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import util.UrlLIst;

/**
 * Created by Administrator on 2017/1/6.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText et_login_phone;
    private EditText et_login_password;
    private Button btn_login;
    private Button btn_register;
    private TextView tv_login_setPassword;
    private InputMethodManager manager;
    private ImageView iv_login;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(LoginActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        if (Config.hasInternet(this)) {
        } else {
            mHandler.sendEmptyMessage(0);
        }
        initView();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void initView() {
        et_login_phone = (EditText) findViewById(R.id.et_loginPhone);
        et_login_password = (EditText) findViewById(R.id.et_loginPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        tv_login_setPassword = (TextView) findViewById(R.id.tv_login_setPassword);
        iv_login=(ImageView)findViewById(R.id.iv_login_start);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_login_setPassword.setOnClickListener(this);
        iv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                startActivity(new Intent().setClass(this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                if (et_login_phone.getText().toString().length() == 11 && !TextUtils.isEmpty(et_login_password.getText())) {
                    getVip(et_login_phone.getText().toString());
                    pushService(et_login_phone.getText().toString(), et_login_password.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "账号或密码错误!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_login_setPassword:
                startActivity(new Intent(this, FindPassWordActivity.class));
                break;
            case R.id.iv_login_start:
                startActivity(new Intent(this,MainActivity.class));
            break;
        }
    }

    //点击空白处回收键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    public void pushService(String phone, String password) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("password", password);
        KxhlRestClient.post(UrlLIst.LOGIN_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("登录成功！", response.toString());
                    try {
                        if (response.getString("stat").equals("200")) {
                            Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                            String user_Id = response.getString("id");
                            SaveData.put(getApplicationContext(), Config.USERID, user_Id);
                            String userPhone = response.getString("phone");
                            SaveData.put(getApplicationContext(), Config.USER_PHONE, userPhone);
                            startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "账号或密码错误!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
//            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }
    public void getVip(String userPhone){
        RequestParams params=new RequestParams();
        params.put("phone",userPhone);
        KxhlRestClient.post(UrlLIst.FOUND_CHECK_VIP,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        String vip = response.getString("vip");
                        SaveData.put(LoginActivity.this, Config.VIP_CHECK, vip);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
