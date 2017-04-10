package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fynn.fluidlayout.FluidLayout;
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
import util.SaveData;
import util.TitleUtil;
import util.UrlLIst;
import view.LoadingDialog;

/**
 * Created by Administrator on 2017/1/20.
 */
public class StoreActivity extends Activity {
    private String storeId;//店铺id
    private String time;//时间
    private LinearLayout ll_storeMsg_bj;//店铺背景
    private TextView tv_storeMsg_name;
    private TextView tv_storeMsg_address;
    private ImageView iv_storeMsg_start1, iv_storeMsg_start2, iv_storeMsg_start3, iv_storeMsg_start4, iv_storeMsg_start5;
    private TextView tv_storeMsg_time;
    private ImageView iv_storeMsg_phone;
    private ImageView iv_storeMsg_back;
    private Button btn_storeMsg_time;
    private LoadingDialog dialog;
    private FluidLayout mTagCloudLayout;
    private List<String> mList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            msg.what = 00;
            dialog.dismiss();
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
        setContentView(R.layout.activity_store);
        initView();
        Bundle bundle = getIntent().getExtras();//得到传过来的bundle
        storeId = bundle.getString("storeId");
        time = bundle.getString("time");
        getMsg(storeId);
        iv_storeMsg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_storeMsg_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushStore(storeId, (String) SaveData.get(StoreActivity.this, Config.USERID, ""), time);
            }
        });
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void initView() {
        ll_storeMsg_bj = (LinearLayout) findViewById(R.id.ll_storeMsg_bj);
        tv_storeMsg_name = (TextView) findViewById(R.id.tv_storeMsg_name);
        tv_storeMsg_address = (TextView) findViewById(R.id.tv_storeMsg_address);
        tv_storeMsg_time = (TextView) findViewById(R.id.tv_storeMsg_time);
        iv_storeMsg_start1 = (ImageView) findViewById(R.id.iv_storeMsg_start1);
        iv_storeMsg_start2 = (ImageView) findViewById(R.id.iv_storeMsg_start2);
        iv_storeMsg_start3 = (ImageView) findViewById(R.id.iv_storeMsg_start3);
        iv_storeMsg_start4 = (ImageView) findViewById(R.id.iv_storeMsg_start4);
        iv_storeMsg_start5 = (ImageView) findViewById(R.id.iv_storeMsg_start5);
        iv_storeMsg_phone = (ImageView) findViewById(R.id.iv_storeMsg_phone);
        iv_storeMsg_back = (ImageView) findViewById(R.id.iv_storeMsg_back);
        btn_storeMsg_time = (Button) findViewById(R.id.btn_storeMsg_time);
        mTagCloudLayout = (FluidLayout) findViewById(R.id.tcl);
    }

    private void addTextView(List<String> str) {
        mTagCloudLayout.removeAllViews();
        mTagCloudLayout.setGravity(Gravity.CENTER);
        if (str.size() > 0) {
            for (int i = 0; i < str.size(); i++) {
                TextView tv = new TextView(this);
                tv.setText(str.get(i));
                tv.setTextColor(Color.rgb(255, 255, 255));
                tv.setTextSize(12);
                tv.setBackgroundResource(R.drawable.btn_red_lable);
                FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(12, 12, 12, 12);
                mTagCloudLayout.addView(tv, params);
            }
        } else {
            mTagCloudLayout.removeAllViews();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.hasInternet(this)) {
            dialog = new LoadingDialog(this);
            dialog.show();
        }
    }
    public void getMsg(String id) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        KxhlRestClient.post(UrlLIst.APPOINTMENT_SHOW, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                if (statusCode == 200) {
                    try {
                        Log.i("店铺详情！", response.toString());
                        tv_storeMsg_name.setText(response.getString("name"));
                        tv_storeMsg_address.setText("地址：" + response.getString("location"));
                        tv_storeMsg_time.setText("营业时间：" + response.getString("time"));
                        String logo = response.getString("logo");
                        if (!response.getString("about").isEmpty()) {
                            mList = Config.stringToList(response.getString("about"));
                            addTextView(mList);
                        }

                        Glide.with(StoreActivity.this).load(logo).asBitmap().
                                into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        ll_storeMsg_bj.setBackgroundDrawable(new BitmapDrawable(resource));
                                        handler.sendEmptyMessage(00);
                                    }
                                });

                        switch (response.getString("star")) {
                            case "5":
                                iv_storeMsg_start1.setImageResource(R.drawable.start);
                                iv_storeMsg_start2.setImageResource(R.drawable.start);
                                iv_storeMsg_start3.setImageResource(R.drawable.start);
                                iv_storeMsg_start4.setImageResource(R.drawable.start);
                                iv_storeMsg_start5.setImageResource(R.drawable.start);
                                break;
                            case "4":
                                iv_storeMsg_start1.setImageResource(R.drawable.start);
                                iv_storeMsg_start2.setImageResource(R.drawable.start);
                                iv_storeMsg_start3.setImageResource(R.drawable.start);
                                iv_storeMsg_start4.setImageResource(R.drawable.start);
                                iv_storeMsg_start5.setImageResource(R.drawable.start_no);
                                break;
                            case "3":
                                iv_storeMsg_start1.setImageResource(R.drawable.start);
                                iv_storeMsg_start2.setImageResource(R.drawable.start);
                                iv_storeMsg_start3.setImageResource(R.drawable.start);
                                iv_storeMsg_start4.setImageResource(R.drawable.start_no);
                                iv_storeMsg_start5.setImageResource(R.drawable.start_no);
                                break;
                            case "2":
                                iv_storeMsg_start1.setImageResource(R.drawable.start);
                                iv_storeMsg_start2.setImageResource(R.drawable.start);
                                iv_storeMsg_start3.setImageResource(R.drawable.start_no);
                                iv_storeMsg_start4.setImageResource(R.drawable.start_no);
                                iv_storeMsg_start5.setImageResource(R.drawable.start_no);
                                break;
                            case "1":
                                iv_storeMsg_start1.setImageResource(R.drawable.start);
                                iv_storeMsg_start2.setImageResource(R.drawable.start_no);
                                iv_storeMsg_start3.setImageResource(R.drawable.start_no);
                                iv_storeMsg_start4.setImageResource(R.drawable.start_no);
                                iv_storeMsg_start5.setImageResource(R.drawable.start_no);
                                break;
                            case "0":
                                iv_storeMsg_start1.setImageResource(R.drawable.start_no);
                                iv_storeMsg_start2.setImageResource(R.drawable.start_no);
                                iv_storeMsg_start3.setImageResource(R.drawable.start_no);
                                iv_storeMsg_start4.setImageResource(R.drawable.start_no);
                                iv_storeMsg_start5.setImageResource(R.drawable.start_no);
                                break;

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    iv_storeMsg_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                call(response.getString("phone"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(StoreActivity.this, "网络错误！", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 预约门店
     */
    public void pushStore(String storeId, String userId, String time) {
        RequestParams params = new RequestParams();
        params.put("sid", storeId);
        params.put("uid", userId);
        params.put("appointment_time", time);
        KxhlRestClient.post(UrlLIst.APPOINTMENT_APPOINTMENT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    try {
                        switch (response.getString("stat")) {
                            case "200":
                                getDialog("预约已提交!");
                                btn_storeMsg_time.setClickable(false);
                                break;
                            case "400":
                                getDialog("预约提交失败!");
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
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }
}
