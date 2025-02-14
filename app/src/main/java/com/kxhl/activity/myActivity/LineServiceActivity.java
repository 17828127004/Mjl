package com.kxhl.activity.myActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kxhl.R;

import util.Config;
import util.TitleUtil;

/**
 * Created by Administrator on 2017/3/2.
 */

public class LineServiceActivity extends Activity {
    private WebView wv_store;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numstore);
        Config.setTranslucent(this);
    }

    @Override
    protected void onResume() {
        getWebView();
        super.onResume();
    }

    public void getWebView() {
        wv_store = (WebView) findViewById(R.id.wv_store);
        pb = (ProgressBar) findViewById(R.id.pb_store);
        WebSettings webSettings = wv_store.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);// 将图片调整到适合webview大小
        String url = "http://tb.53kf.com/code/client/10110538/1";
        wv_store.loadUrl(url);
        new TitleUtil(this).setTitleName("在线客服").setLeftImage(R.drawable.happy_mine_back)
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
}
