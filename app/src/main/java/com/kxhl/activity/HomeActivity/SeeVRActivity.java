package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.kxhl.R;

import util.TitleUtil;

/**
 * Created by Administrator on 2017/3/2.
 */
public class SeeVRActivity extends Activity{
    private WebView wv_store;
    private ProgressBar pb;
    private String url="http://www.cdjiayibai.com/home/index/vr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numstore);
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
        webSettings.setBuiltInZoomControls(true);// 设置支持缩放
        webSettings.setSupportZoom(false);// 不支持缩放
        webSettings.setUseWideViewPort(false);// 将图片调整到适合webview大小
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
//        String url=getIntent().getExtras().getString("photo");
        wv_store.loadUrl(url);
        new TitleUtil(this).setTitleName("VR展示").setLeftImage(R.drawable.happy_mine_back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (wv_store.canGoBack()) {
//                            wv_store.goBack();
//                        } else {
//                            finish();
//                        }
                        finish();
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
