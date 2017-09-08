package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.feirui.feiyunbangong.R;

/**
 * Created by feirui1 on 2017-08-16.
 */

public class WebViewActivity extends BaseActivity {
    private WebView mView;
    private RelativeLayout actionBar;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.webview);
        initView();
        initData();
    }

    private void initView() {

        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("会办");
        setRightVisibility(false);
        mView = (WebView) findViewById(R.id.wb);
    }

    private void initData() {
        Intent intent = getIntent();
        String uri = intent.getStringExtra("uri");

        mView.getSettings().setJavaScriptEnabled(true);
        //mView.setScrollBarStyle(0);
        WebSettings webSettings = mView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webSettings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webSettings.setSupportZoom(true);//是否可以缩放，默认true
        webSettings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        webSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        webSettings.setAppCacheEnabled(true);//是否使用缓存
        webSettings.setDomStorageEnabled(true);//DOM Storage


        mView.loadUrl(uri);

        //加载数据
        mView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    WebViewActivity.this.setTitle("加载完成");
                } else {
                    WebViewActivity.this.setTitle("加载中.......");
                }
            }
        });

        //这个是当网页上的链接被点击的时候

        mView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
    }

    // goBack()表示返回webView的上一页面
    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (mView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {

            mView.goBack();
            return true;
        }
        return false;
    }
}
