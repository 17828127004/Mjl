package com.kxhl.activity.myActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kxhl.R;
import com.kxhl.activity.LoginActivity;
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
 * Created by Administrator on 2017/1/16.
 */
public class SetPasswordActivity extends Activity {
    private EditText et_setPassword_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myset_password);
        new TitleUtil(this).setTitleName("修改密码").setLeftImage(R.drawable.happy_mine_back).
                setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(SetPasswordActivity.this,MySettingActivity.class));
                        finish();
                    }
                }).setRightText(R.string.save).setRightTvOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_setPassword_set.getText())) {
                    Toast.makeText(SetPasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    getSetService((String) SaveData.get(SetPasswordActivity.this,Config.USERID,""),et_setPassword_set.getText().toString());
                }
            }
        });
        Config.setTranslucent(this);
        initView();
    }

    public void initView() {
        et_setPassword_set = (EditText) findViewById(R.id.et_setPassWord);
    }

    public void getSetService(String userId, String passWord) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        params.put("password", passWord);
        KxhlRestClient.post(UrlLIst.MINE_EDIT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        if(response.getString("stat").equals("200")){
                            Toast.makeText(SetPasswordActivity.this,"修改成功！",Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(SetPasswordActivity.this, LoginActivity.class));
                            finish();
                        }else{
                            Toast.makeText(SetPasswordActivity.this,"修改失败！",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SetPasswordActivity.this,"网络错误！",Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
