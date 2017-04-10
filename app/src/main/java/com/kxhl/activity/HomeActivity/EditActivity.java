package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
 * Created by Administrator on 2017/2/23.
 */
public class EditActivity extends Activity {
    private String mAskId;
    private EditText et_ask;
    private TextView tv_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit);
        et_ask = (EditText) findViewById(R.id.input_comment);
        tv_btn = (TextView) findViewById(R.id.btn_publish_comment);
        Intent i = getIntent();
        mAskId = i.getStringExtra("pr_id");
        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_ask.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "回答问题不能空", Toast.LENGTH_SHORT).show();
                }else{
                    getService((String) SaveData.get(EditActivity.this, Config.USERID,""),mAskId,et_ask.getText().toString());
                }
            }
        });
    }

    public void getService(String userId, String askId, String content) {
        RequestParams params = new RequestParams();
        params.put("uid", userId);
        params.put("pr_id", askId);
        params.put("text", content);
        KxhlRestClient.post(UrlLIst.INDEX_ANSWER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        switch(response.getString("stat")){
                            case "200":
                                Toast.makeText(EditActivity.this, "回答成功!", Toast.LENGTH_SHORT).show();
                                finish();
                            break;
                            case "300":
                                Toast.makeText(EditActivity.this, "你已回答过该问题", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(EditActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 点击空白处finish
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }
}
