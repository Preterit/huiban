package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.view.PView;

import java.util.HashMap;

public class ShenPiCaiGouDetailActivity extends BaseActivity {
    private HashMap<String, Object> mData;
    private String mList_id;

    @PView
    TextView et_shenqingmiaoshu, et_caigouleixing, tv_qiwangriqi, tv_mingcheng, tv_number,tv_price,tv_xuanze_zhifufangshi;// 申请描述，采购类型，期望日期，名称，数量,价格，选择方式，
    @PView(click = "onClick")
    Button btn_pass;// 审批通过
    @PView(click = "onClick")
    Button btn_nopass;// 审批拒绝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenpi_caigou_detail);
    }
}
