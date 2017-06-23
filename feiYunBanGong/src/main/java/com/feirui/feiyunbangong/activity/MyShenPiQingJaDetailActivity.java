package com.feirui.feiyunbangong.activity;

import android.os.Bundle;

import com.feirui.feiyunbangong.R;

import static android.R.attr.data;

/**
 * Created by Administrator on 2017/6/23.
 * 我提交的请假信息
 */

class MyShenPiQingJaDetailActivity  extends BaseActivity{

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_shen_pi_detail);
        initTop();
        getData();
    }

    private void initTop() {
        initTitle();

    }

    public Object getData() {
        return data;
    }
}
