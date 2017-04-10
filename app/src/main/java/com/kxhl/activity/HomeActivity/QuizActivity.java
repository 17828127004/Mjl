package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;

/**
 * Created by Administrator on 2017/1/19.
 */
public class QuizActivity extends Activity {
    private Button btn_quiz;
    private EditText et_quizContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        new TitleUtil(this).setTitleName("提问").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(QuizActivity.this,AnswersActivity.class));
                    }
                });
        btn_quiz=(Button)findViewById(R.id.btn_quiz);
        et_quizContent=(EditText)findViewById(R.id.et_quizContent);
        btn_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_quizContent.getText().toString().equals("")){
                    Toast.makeText(QuizActivity.this, "提问不能空", Toast.LENGTH_SHORT).show();
                }else{
                    getService((String) SaveData.get(QuizActivity.this, Config.USERID,""),et_quizContent.getText().toString());
                }
            }
        });
    }
    public void getService(String userId,String content){
        RequestParams params=new RequestParams();
        params.put("id",userId);
        params.put("text",content);
        KxhlRestClient.post(UrlLIst.INDEX_PRO_ASK,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode==200){
                    try {
                        if(response.getString("stat").equals("200")){
                            Toast.makeText(QuizActivity.this,"提交问题成功！",Toast.LENGTH_SHORT).show();
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
               Toast.makeText(QuizActivity.this,"网络错误!",Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
