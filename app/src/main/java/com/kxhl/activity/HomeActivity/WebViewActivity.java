package com.kxhl.activity.HomeActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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
 * Created by Administrator on 2017/2/27.
 */
public class WebViewActivity extends Activity {
    private WebView webView;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Config.setTranslucent(this);
        getWebView();
    }

    public void getWebView() {

        new TitleUtil(this).setTitleName("积分兑换").setLeftImage(R.drawable.happy_mine_back).setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {

                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        webView = (WebView) findViewById(R.id.wv_homeMsg);
        pb=(ProgressBar)findViewById(R.id.pb);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);// 设置支持缩放
        settings.setSupportZoom(false);// 不支持缩放
        settings.setUseWideViewPort(false);// 将图片调整到适合webview大小
        settings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        webView.loadUrl(bundle.getString("url"));
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
               if(newProgress==100){
                   pb.setVisibility(View.INVISIBLE);
               }else{
                   if(View.INVISIBLE==pb.getVisibility()){
                       pb.setVisibility(View.VISIBLE);

                   }
                   pb.setProgress(newProgress);
               }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
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
