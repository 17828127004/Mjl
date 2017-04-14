package com.kxhl.activity.findActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
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
 * Created by Administrator on 2017/2/8.
 */
public class NumStoreActivity extends Activity {
    private WebView wv_store;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numstore);
        Config.setTranslucent(this);
        getService((String) SaveData.get(this,Config.USERID,""));
        getWebView();
        if(!Config.hasInternet(this)){
            Toast.makeText(this,"网络错误！",Toast.LENGTH_SHORT).show();
        }

    }

    public void getWebView() {
        wv_store = (WebView) findViewById(R.id.wv_store);
        pb = (ProgressBar) findViewById(R.id.pb_store);
        WebSettings webSettings = wv_store.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);// 设置支持缩放
        webSettings.setSupportZoom(false);// 不支持缩放
        webSettings.setUseWideViewPort(false);// 将图片调整到适合webview大小
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        new TitleUtil(this).setTitleName("积分商城").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (wv_store.canGoBack()) {
                            wv_store.goBack();
                        } else {
                           finish();
                        }
                    }
                });
        wv_store.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pb.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == pb.getVisibility()) {
                        pb.setVisibility(View.VISIBLE);
                    }
                    pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        wv_store.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void getService(String userId) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        KxhlRestClient.post(UrlLIst.INDEX_BUY_GOODS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Log.i("积分商城", response.toString());
                    try {
                        wv_store.loadUrl(response.getString("url"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("积分商城失败国防法fgdfgdfgdf",responseString);
                Toast.makeText(NumStoreActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
