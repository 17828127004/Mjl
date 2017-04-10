package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kxhl.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.MyAnswersAdapter;
import util.Config;
import util.KxhlRestClient;
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;

/**
 * Created by Administrator on 2017/1/22.
 */
public class MyAnswersActivity extends Activity {
    private ListView lv_myAnswers;
    private MyAnswersAdapter adapter;
    private List<String> mTexts;//回答的内容
    private List<String> mStats;//问题状态，1 采纳 0未采纳
    private List<String> mPr_ids;//问题id
    private List<String> mTimes;//回答时间
    private List<String> mPr_times;//提问时间
    private List<String> mPr_texts;//问题内容
    private List<String> mPr_names;//提问用户名
    private List<String> mPr_paths;//提问头像
//    private List<String> mNames;//用户名字
    private String mPaths;//用户头像
    private List<String> mCounts;//回答数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_answers);
        new TitleUtil(this).setTitleName("我的回答").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MyAnswersActivity.this, AnswersActivity.class));
                    }
                });
        lv_myAnswers = (ListView) findViewById(R.id.lv_myAnswers);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getService((String) SaveData.get(MyAnswersActivity.this, Config.USERID, ""));

    }

    public void getService(String usetId) {
        RequestParams params = new RequestParams();
        params.put("id", usetId);
        KxhlRestClient.post(UrlLIst.INDEX_MYANSWER, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("我的回答", response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("myanswer");
                        getJson(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(MyAnswersActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getJson(JSONArray array) throws JSONException {
        mTexts = new ArrayList<>();
        mStats = new ArrayList<>();
        mPr_ids = new ArrayList<>();
        mTimes = new ArrayList<>();
        mPr_times = new ArrayList<>();
        mPr_texts = new ArrayList<>();
        mPr_names = new ArrayList<>();
        mPr_paths = new ArrayList<>();
//        mNames = new ArrayList<>();

        mCounts = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            mTexts.add(i, jsonObject.getString("text"));
            mStats.add(i, jsonObject.getString("stat"));
            mPr_ids.add(i, jsonObject.getString("pr_id"));
            mTimes.add(i, jsonObject.getString("time"));
            mPr_times.add(i, jsonObject.getString("pr_time"));
            mPr_texts.add(i, jsonObject.getString("pr_text"));
            mPr_names.add(i, jsonObject.getString("pr_name"));
            mPr_paths.add(i, jsonObject.getString("pr_path"));
//            mNames.add(i, jsonObject.getString("name"));
            mPaths=jsonObject.getString("path");
            mCounts.add(i, jsonObject.getString("count"));
        }
        adapter = new MyAnswersAdapter(MyAnswersActivity.this, mTexts, mStats, mPr_ids, mTimes, mPr_times, mPr_texts, mPr_names
                , mPr_paths,mPaths , mCounts);
        lv_myAnswers.setAdapter(adapter);
    }
}
