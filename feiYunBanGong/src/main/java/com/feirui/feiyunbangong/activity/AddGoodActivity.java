package com.feirui.feiyunbangong.activity;

import android.os.Bundle;

import com.feirui.feiyunbangong.R;

public class AddGoodActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_good);

        initTop();
    }

    private void initTop() {
        initTitle();
        setTitle("添加商品");
    }


}
