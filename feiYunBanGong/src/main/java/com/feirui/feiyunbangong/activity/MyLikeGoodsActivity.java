package com.feirui.feiyunbangong.activity;

/**
 * Created by rubing on 2017/3/23.
 * rubingem@163.com
 */

import android.os.Bundle;

import com.feirui.feiyunbangong.R;

/**
 * 我收藏的商品列表
 */
public class MyLikeGoodsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_like_goods);

        initTitle();
        setCenterString("我的收藏");
        setLeftDrawable(R.drawable.arrows_left);
    }

}
