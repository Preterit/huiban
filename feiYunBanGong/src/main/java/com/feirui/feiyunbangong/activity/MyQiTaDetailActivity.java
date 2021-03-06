package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by feirui1 on 2017-07-04.
 */

 public class MyQiTaDetailActivity extends BaseActivity {

    private HashMap<String, Object> mData;
    private String mList_id;

    @PView
    TextView et_miaoshu, tv_shijian;//
    @PView(click = "onClick")
    Button btn_pass;// 审批通过
    @PView(click = "onClick")
    Button btn_nopass;// 审批拒绝
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenpi_qita_detail);

        initView();
        getData();
    }
    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("查看");
        setRightVisibility(false);
        layout = (ConstraintLayout) findViewById(R.id.shenpi);
        layout.setVisibility(View.GONE);


    }

    public void getData() {
        mData = (HashMap<String, Object>) getIntent().getSerializableExtra("data");

        Object id = mData.get("id");
        Object approval_type = mData.get("approval_type");
        Object approval_id = mData.get("approval_id");
        Object list_id = mData.get("list_id");
        mList_id = list_id + "";

        RequestParams params = new RequestParams();
        String url = UrlTools.url + UrlTools.APP_DETAIL;

//        params.put("id", id + "");
//        params.put("approval_type", approval_type + "");
//        params.put("approval_id", approval_id + "");
        params.put("list_id", list_id + "");

        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                ArrayList<HashMap<String, Object>> infor = bean.getInfor();
                Log.d("审批其他界面bean---------", "in_fo: " + bean.toString());

                HashMap<String, Object> in_fo = infor.get(0);
                Log.d("审批其他界面in_fo---------", "in_fo: " + in_fo.toString());
                try {

                    //付款描述
                    String miaoshu = String.valueOf(in_fo.get("describe"));
                    et_miaoshu.setText(miaoshu);
                    //付款描述
                    String shijian = String.valueOf(in_fo.get("to_time"));
                    tv_shijian.setText(shijian);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(String msg) {
                Log.e("orz", "failure: " + msg);
            }

            @Override
            public void finish() {

            }
        });

    }

}
