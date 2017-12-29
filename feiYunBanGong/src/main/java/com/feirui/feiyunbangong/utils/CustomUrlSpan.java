package com.feirui.feiyunbangong.utils;

import android.content.Context;
import android.content.Intent;
import android.text.style.ClickableSpan;
import android.view.View;

import com.feirui.feiyunbangong.activity.WebViewActivity;

/**
 * Created by xy on 2017-12-29.
 * 自定义TextView连接跳转
 */

public class CustomUrlSpan extends ClickableSpan {

    private Context context;
    private String url;
    public CustomUrlSpan(Context context,String url){
        this.context = context;
        this.url = url;
    }

    @Override
    public void onClick(View widget) {
        // 在这里可以做任何自己想要的处理
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("uri",url);
        context.startActivity(intent);
    }
}
