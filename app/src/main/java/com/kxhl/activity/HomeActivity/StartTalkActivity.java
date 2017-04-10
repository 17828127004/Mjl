package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kxhl.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import util.Config;
import util.KxhlRestClient;
import util.TitleUtil;
import util.UrlLIst;
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/1/20.
 */
public class StartTalkActivity extends Activity implements View.OnClickListener {
    private ImageView iv_start1, iv_start2, iv_start3, iv_start4, iv_start5;
    private TextView tv_start;
    private RelativeLayout rl_startBj;
    private TextView tv_startName;
    private String aid;//店铺aid
    private Button btn_startUp;
    private Button lable1, lable2, lable3, lable4, lable5, lable6;
    private String lab1 = "1", lab2 = "2", lab3 = "3", lab4 = "4", lab5 = "5", lab6 = "6";
    private String is;
    private String isStart = "4星";
    private List<String> mLists;
    private String mStart = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_talk);
        new TitleUtil(this).setTitleName("星级评价").setLeftImage(R.drawable.happy_mine_back).
                setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(StartTalkActivity.this, TalkActivity.class));
                    }
                });
        initView();
        mLists = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        aid = i.getStringExtra("aid");
        tv_startName.setText(i.getStringExtra("name"));
        Glide.with(this).load(i.getStringExtra("logo")).asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        rl_startBj.setBackgroundDrawable(new BitmapDrawable(resource));
                    }
                });
    }


    public void initView() {
        iv_start1 = (ImageView) findViewById(R.id.iv_start1);
        iv_start2 = (ImageView) findViewById(R.id.iv_start2);
        iv_start3 = (ImageView) findViewById(R.id.iv_start3);
        iv_start4 = (ImageView) findViewById(R.id.iv_start4);
        iv_start5 = (ImageView) findViewById(R.id.iv_start5);
        tv_start = (TextView) findViewById(R.id.tv_start);
        rl_startBj = (RelativeLayout) findViewById(R.id.rl_startBj);
        tv_startName = (TextView) findViewById(R.id.tv_startName);
        lable1 = (Button) findViewById(R.id.btn_startLale1);
        lable2 = (Button) findViewById(R.id.btn_startLale2);
        lable3 = (Button) findViewById(R.id.btn_startLale3);
        lable4 = (Button) findViewById(R.id.btn_startLale4);
        lable5 = (Button) findViewById(R.id.btn_startLale5);
        lable6 = (Button) findViewById(R.id.btn_startLale6);
        btn_startUp = (Button) findViewById(R.id.btn_startUp);
        iv_start1.setOnClickListener(this);
        iv_start2.setOnClickListener(this);
        iv_start3.setOnClickListener(this);
        iv_start4.setOnClickListener(this);
        iv_start5.setOnClickListener(this);
        lable1.setOnClickListener(this);
        lable2.setOnClickListener(this);
        lable3.setOnClickListener(this);
        lable4.setOnClickListener(this);
        lable5.setOnClickListener(this);
        lable6.setOnClickListener(this);
        btn_startUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_start1:
                iv_start1.setImageResource(R.drawable.start);
                iv_start2.setImageResource(R.drawable.start_no);
                iv_start3.setImageResource(R.drawable.start_no);
                iv_start4.setImageResource(R.drawable.start_no);
                iv_start5.setImageResource(R.drawable.start_no);
                tv_start.setText("比较满意，仍可改善");
                mStart = "1";
                mLists.clear();
                is = "0";
                getString(is);
                isStart = "4星";

                break;
            case R.id.iv_start2:
                iv_start1.setImageResource(R.drawable.start);
                iv_start2.setImageResource(R.drawable.start);
                iv_start3.setImageResource(R.drawable.start_no);
                iv_start4.setImageResource(R.drawable.start_no);
                iv_start5.setImageResource(R.drawable.start_no);
                tv_start.setText("比较满意，仍可改善");
                mStart = "2";
                mLists.clear();
                is = "0";
                getString(is);
                isStart = "4星";
                break;
            case R.id.iv_start3:
                iv_start1.setImageResource(R.drawable.start);
                iv_start2.setImageResource(R.drawable.start);
                iv_start3.setImageResource(R.drawable.start);
                iv_start4.setImageResource(R.drawable.start_no);
                iv_start5.setImageResource(R.drawable.start_no);
                tv_start.setText("比较满意，仍可改善");
                mStart = "3";
                is = "0";
                mLists.clear();
                getString(is);
                isStart = "4星";
                break;
            case R.id.iv_start4:
                iv_start1.setImageResource(R.drawable.start);
                iv_start2.setImageResource(R.drawable.start);
                iv_start3.setImageResource(R.drawable.start);
                iv_start4.setImageResource(R.drawable.start);
                iv_start5.setImageResource(R.drawable.start_no);
                tv_start.setText("比较满意，仍可改善");
                lable1.setText("卫生不合格");
                lable2.setText("服务态度差");
                lable3.setText("质量不好");
                lable4.setText("店铺乱");
                lable5.setText("售后差");
                lable6.setText("设施与描述不符");
                mStart = "4";
                mLists.clear();
                is = "0";
                getString(is);
                isStart = "4星";
                break;
            case R.id.iv_start5:
                iv_start1.setImageResource(R.drawable.start);
                iv_start2.setImageResource(R.drawable.start);
                iv_start3.setImageResource(R.drawable.start);
                iv_start4.setImageResource(R.drawable.start);
                iv_start5.setImageResource(R.drawable.start);
                tv_start.setText("非常满意，无可改善");
                lable1.setText("卫生合格");
                lable2.setText("服务态度好");
                lable3.setText("质量好");
                lable4.setText("店铺干净");
                lable5.setText("售后不错");
                lable6.setText("星级店铺");
                mStart = "5";
                mLists.clear();
                is = "0";
                getString(is);
                isStart = "5星";
                break;
            case R.id.btn_startLale1:
                if (isStart.equals("4星")) {
                    if (lab1.equals("1")) {
                        lable1.setBackgroundResource(R.drawable.talk_bj);
                        lable1.setTextColor(Color.rgb(234, 92, 95));
                        lab1 = "1.0";
                        mLists.add("1");
                    } else {
                        lable1.setBackgroundResource(R.drawable.talk_bj_no);
                        lable1.setTextColor(Color.rgb(190, 190, 190));
                        lab1 = "1";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("1")) {
                                mLists.remove(i);
                            }
                        }
                    }
                } else {
                    if (lab1.equals("1")) {
                        lable1.setBackgroundResource(R.drawable.talk_bj);
                        lable1.setTextColor(Color.rgb(234, 92, 95));
                        lab1 = "1.0";
                        mLists.add("7");
                    } else {
                        lable1.setBackgroundResource(R.drawable.talk_bj_no);
                        lable1.setTextColor(Color.rgb(190, 190, 190));
                        lab1 = "1";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("7")) {
                                mLists.remove(i);
                            }
                        }
                    }
                }

                break;
            case R.id.btn_startLale2:
                if (isStart.equals("4星")) {
                    if (lab2.equals("2")) {
                        lable2.setBackgroundResource(R.drawable.talk_bj);
                        lable2.setTextColor(Color.rgb(234, 92, 95));
                        lab2 = "2.0";
                        mLists.add("2");
                    } else {
                        lable2.setBackgroundResource(R.drawable.talk_bj_no);
                        lable2.setTextColor(Color.rgb(190, 190, 190));
                        lab2 = "2";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("2")) {
                                mLists.remove(i);
                            }
                        }
                    }
                } else {
                    if (lab2.equals("2")) {
                        lable2.setBackgroundResource(R.drawable.talk_bj);
                        lable2.setTextColor(Color.rgb(234, 92, 95));
                        lab2 = "2.0";
                        mLists.add("8");
                    } else {
                        lable2.setBackgroundResource(R.drawable.talk_bj_no);
                        lable2.setTextColor(Color.rgb(190, 190, 190));
                        lab2 = "2";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("8")) {
                                mLists.remove(i);
                            }
                        }
                    }
                }
                break;
            case R.id.btn_startLale3:
                if (isStart.equals("4星")) {
                    if (lab3.equals("3")) {
                        lable3.setBackgroundResource(R.drawable.talk_bj);
                        lable3.setTextColor(Color.rgb(234, 92, 95));
                        lab3 = "3.0";
                        mLists.add("3");
                    } else {
                        lable3.setBackgroundResource(R.drawable.talk_bj_no);
                        lable3.setTextColor(Color.rgb(190, 190, 190));
                        lab3 = "3";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("3")) {
                                mLists.remove(i);
                            }
                        }
                    }
                } else {
                    if (lab3.equals("3")) {
                        lable3.setBackgroundResource(R.drawable.talk_bj);
                        lable3.setTextColor(Color.rgb(234, 92, 95));
                        lab3 = "3.0";
                        mLists.add("9");
                    } else {
                        lable3.setBackgroundResource(R.drawable.talk_bj_no);
                        lable3.setTextColor(Color.rgb(190, 190, 190));
                        lab3 = "3";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("9")) {
                                mLists.remove(i);
                            }
                        }
                    }
                }
                break;
            case R.id.btn_startLale4:
                if (isStart.equals("4星")) {
                    if (lab4.equals("4")) {
                        lable4.setBackgroundResource(R.drawable.talk_bj);
                        lable4.setTextColor(Color.rgb(234, 92, 95));
                        lab4 = "4.0";
                        mLists.add("4");
                    } else {
                        lable4.setBackgroundResource(R.drawable.talk_bj_no);
                        lable4.setTextColor(Color.rgb(190, 190, 190));
                        lab4 = "4";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("4")) {
                                mLists.remove(i);
                            }
                        }
                    }
                } else {
                    if (lab4.equals("4")) {
                        lable4.setBackgroundResource(R.drawable.talk_bj);
                        lable4.setTextColor(Color.rgb(234, 92, 95));
                        lab4 = "4.0";
                        mLists.add("10");
                    } else {
                        lable4.setBackgroundResource(R.drawable.talk_bj_no);
                        lable4.setTextColor(Color.rgb(190, 190, 190));
                        lab4 = "4";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("10")) {
                                mLists.remove(i);
                            }
                        }
                    }
                }
                break;
            case R.id.btn_startLale5:
                if (isStart.equals("4星")) {
                    if (lab5.equals("5")) {
                        lable5.setBackgroundResource(R.drawable.talk_bj);
                        lable5.setTextColor(Color.rgb(234, 92, 95));
                        lab5 = "5.0";
                        mLists.add("5");
                    } else {
                        lable5.setBackgroundResource(R.drawable.talk_bj_no);
                        lable5.setTextColor(Color.rgb(190, 190, 190));
                        lab5 = "5";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("5")) {
                                mLists.remove(i);
                            }
                        }
                    }
                } else {
                    if (lab5.equals("5")) {
                        lable5.setBackgroundResource(R.drawable.talk_bj);
                        lable5.setTextColor(Color.rgb(234, 92, 95));
                        lab5 = "5.0";
                        mLists.add("11");
                    } else {
                        lable5.setBackgroundResource(R.drawable.talk_bj_no);
                        lable5.setTextColor(Color.rgb(190, 190, 190));
                        lab5 = "5";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("11")) {
                                mLists.remove(i);
                            }
                        }
                    }
                }
                break;
            case R.id.btn_startLale6:
                if (isStart.equals("4星")) {
                    if (lab6.equals("6")) {
                        lable6.setBackgroundResource(R.drawable.talk_bj);
                        lable6.setTextColor(Color.rgb(234, 92, 95));
                        lab6 = "6.0";
                        mLists.add("6");
                    } else {
                        lable6.setBackgroundResource(R.drawable.talk_bj_no);
                        lable6.setTextColor(Color.rgb(190, 190, 190));
                        lab6 = "6";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("6")) {
                                mLists.remove(i);
                            }
                        }
                    }
                } else {
                    if (lab6.equals("6")) {
                        lable6.setBackgroundResource(R.drawable.talk_bj);
                        lable6.setTextColor(Color.rgb(234, 92, 95));
                        lab6 = "6.0";
                        mLists.add("12");
                    } else {
                        lable6.setBackgroundResource(R.drawable.talk_bj_no);
                        lable6.setTextColor(Color.rgb(190, 190, 190));
                        lab6 = "6";
                        for (int i = 0; i < mLists.size(); i++) {
                            if (mLists.get(i).equals("6")) {
                                mLists.remove(i);
                            }
                        }
                    }
                }
                break;
            case R.id.btn_startUp://评价提交
                getService(aid, mStart, Config.listToString(mLists));
                break;
        }
    }

    /**
     * 评价提交
     */
    public void getService(String aID, String star, String type) {
        RequestParams params = new RequestParams();
        params.put("id", aID);
        params.put("star", star);
        params.put("type", type);
        KxhlRestClient.post(UrlLIst.INDEX_STAR_SET, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("评价成功信息", response.toString());
                    try {
                        if(response.getString("stat").equals("200")){
                            Toast.makeText(StartTalkActivity.this,"评价成功！",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StartTalkActivity.this,TalkActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(StartTalkActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getString(String s) {
        if (s.equals("0")) {
            lable1.setBackgroundResource(R.drawable.talk_bj_no);
            lable1.setTextColor(Color.rgb(190, 190, 190));
            lable2.setBackgroundResource(R.drawable.talk_bj_no);
            lable2.setTextColor(Color.rgb(190, 190, 190));
            lable3.setBackgroundResource(R.drawable.talk_bj_no);
            lable3.setTextColor(Color.rgb(190, 190, 190));
            lable4.setBackgroundResource(R.drawable.talk_bj_no);
            lable4.setTextColor(Color.rgb(190, 190, 190));
            lable5.setBackgroundResource(R.drawable.talk_bj_no);
            lable5.setTextColor(Color.rgb(190, 190, 190));
            lable6.setBackgroundResource(R.drawable.talk_bj_no);
            lable6.setTextColor(Color.rgb(190, 190, 190));
        }
    }
}
