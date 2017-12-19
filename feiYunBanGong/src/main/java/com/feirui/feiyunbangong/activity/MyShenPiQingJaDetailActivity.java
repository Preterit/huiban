package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/6/23.
 * 我提交的请假信息
 */

public class MyShenPiQingJaDetailActivity  extends BaseActivity{

    private HashMap<String, Object> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshen_pi_detail);
        initTop();
        getData();
    }


    private TextView mTvLeave, mTvstartTime, mTvEndTime, mTvReason, mTvDays, mTvBeizhu;


    private void initTop() {
        initTitle();
        setCenterString("查看");
        setLeftDrawable(R.drawable.arrows_left);

        mTvLeave = (TextView) findViewById(R.id.tv_leave_type);
        mTvstartTime = (TextView) findViewById(R.id.shenpi_start_time);
        mTvEndTime = (TextView) findViewById(R.id.shenpi_end_time);
        mTvReason = (TextView) findViewById(R.id.shenpi_shiyou);
        mTvDays = (TextView) findViewById(R.id.shenpi_tianshu);

        mTvBeizhu = (TextView) findViewById(R.id.tv_beizhu);
        mTvBeizhu.setVisibility(View.INVISIBLE);

    }

    private String mList_id;

    private void getData() {
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
//        Log.e("审批请假页面", "getData: " + id + ":" + approval_type + ":" + approval_id + ":" + list_id);
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                Log.e("审批请假页面", "success: " + bean.toString());

                String leave_type = bean.getInfor().get(0).get("leave_type") + "";

                String leave_start = bean.getInfor().get(0).get("start_time") + "";
                String leave_end = bean.getInfor().get(0).get("finish_time") + "";
                String leave_reason = bean.getInfor().get(0).get("leave_reason") + "";
                String leave_days = bean.getInfor().get(0).get("leave_days") + "";
                String ttt = bean.getInfor().get(0).get("ttt") + "";

                mTvLeave.setText(leave_type);
                mTvDays.setText(leave_days);
                mTvEndTime.setText(leave_end);
                mTvstartTime.setText(leave_start);
                mTvReason.setText(leave_reason);

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
