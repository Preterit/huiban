package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feirui.feiyunbangong.R;

/**
 * Created by feirui1 on 2017-08-16.
 */

public class WebViewActivity extends BaseActivity {
    private WebView mView;
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
        String uri =  intent.getStringExtra("uri");
        mView.getSettings().setJavaScriptEnabled(true);
        mView.setScrollBarStyle(0);
        WebSettings webSettings = mView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
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
       //这个是当网页上的连接被点击的时候
        mView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view,
                                                    final String url) {
                loadurl(view, url);
                return true;
            }
        });

    }

    private void loadurl(WebView view, String url) {
        mView.loadUrl(url);
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
