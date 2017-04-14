package com.kxhl.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
 * Created by Administrator on 2017/3/3.
 */
public class FindPassWordActivity extends Activity {
    private EditText et_phone;
    private EditText et_passWord;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        new TitleUtil(this).setTitleName("找回密码").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                   finish();
                    }
                });
        Config.setTranslucent(this);
        et_passWord=(EditText)findViewById(R.id.et_findPassWord);
        et_phone=(EditText)findViewById(R.id.et_findPhone);
        btn=(Button)findViewById(R.id.btn_findPassword);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_passWord.getText())||TextUtils.isEmpty(et_phone.getText())){
                    Toast.makeText(FindPassWordActivity.this,"手机号或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    pushService(et_phone.getText().toString(),et_passWord.getText().toString());
                }
            }
        });
    }
    public void pushService(String userPhone,String userPass){
        RequestParams params=new RequestParams();
        params.put("phone",userPhone);
        params.put("password",userPass);
        KxhlRestClient.post(UrlLIst.LOGIN_FORGET,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            if(statusCode==200){
                Log.i("修改密码",response.toString());
                try {
                    if(response.getString("stat").equals("200")){
                        Toast.makeText(FindPassWordActivity.this,"密码修改成功!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
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
