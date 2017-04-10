package com.kxhl.activity.myActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;

/**
 * Created by Administrator on 2017/1/20.
 */
public class SetNameActivity extends Activity {
    private EditText et_setName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        new TitleUtil(this).setLeftImage(R.drawable.happy_mine_back).setRightText(R.string.my_save)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).setRightTvOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_setName.getText()) && et_setName.getText().length() < 16) {
                    pushService((String) SaveData.get(SetNameActivity.this, Config.USERID, ""), et_setName.getText().toString());
                }else {
                    Toast.makeText(SetNameActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        et_setName = (EditText) findViewById(R.id.et_setName);
    }

    public void pushService(String userId, String name) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        params.put("name", name);
        KxhlRestClient.post(UrlLIst.MINE_EDIT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        if (response.getString("stat").equals("200")) {
                            Toast.makeText(SetNameActivity.this, "修改成功!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SetNameActivity.this, "修改失败!", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SetNameActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
