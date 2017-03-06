package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

/**
 * 审批详情
 */
public class ShenPiDetailActivity extends BaseActivity {
    private HashMap<String, Object> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shen_pi_detail);


        initTop();
        getData();
    }


    private TextView mTvLeave, mTvstartTime, mTvEndTime, mTvReason, mTvDays;
    private EditText mEtBeizhu;
    private Button mBtnAccept, mBtnRefuse;

    private void initTop() {
        initTitle();
        setCenterString("查看");
        setLeftDrawable(R.drawable.arrows_left);

        mTvLeave = (TextView) findViewById(R.id.tv_leave_type);
        mTvstartTime = (TextView) findViewById(R.id.shenpi_start_time);
        mTvEndTime = (TextView) findViewById(R.id.shenpi_end_time);
        mTvReason = (TextView) findViewById(R.id.shenpi_shiyou);
        mTvDays = (TextView) findViewById(R.id.shenpi_tianshu);

        mEtBeizhu = (EditText) findViewById(R.id.et_beizhu);

        mBtnAccept = (Button) findViewById(R.id.btn_shenpi_accept);
        mBtnRefuse = (Button) findViewById(R.id.btn_shenpi_refuse);

        mBtnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEtBeizhu.getText().toString().trim())) {
                    T.showShort(ShenPiDetailActivity.this, "请输入备注信息");
                    return;
                }
                updateShenPi(mList_id, "拒绝");
            }
        });
        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEtBeizhu.getText().toString().trim())) {
                    T.showShort(ShenPiDetailActivity.this, "请输入备注信息");
                    return;
                }

                updateShenPi(mList_id, "通过");
            }
        });
    }

    private void updateShenPi(String list_id, String acceptOrRefuse) {
        RequestParams params = new RequestParams();
        params.put("list_id", list_id + "");
        params.put("type", acceptOrRefuse);

        String url = UrlTools.url + UrlTools.APPROVAL_UPDATE;

        Utils.doPost(LoadingDialog.getInstance(ShenPiDetailActivity.this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                if (bean.getCode().equals("200")) {
                    T.showShort(ShenPiDetailActivity.this, bean.getMsg());

                    ShenPiDetailActivity.this.finish();
                }
            }

            @Override
            public void failure(String msg) {
                T.showShort(ShenPiDetailActivity.this, msg);
            }

            @Override
            public void finish() {

            }
        });
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
        String url = UrlTools.url + UrlTools.APPROVAL_DETAIL;

        params.put("id", id + "");
        params.put("approval_type", approval_type + "");
        params.put("approval_id", approval_id + "");
        params.put("list_id", list_id + "");
        Log.e("orz", "getData: " + id + ":" + approval_type + ":" + approval_id + ":" + list_id);

        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                Log.e("orz", "success: " + bean.toString());

                String leave_type = bean.getInfor().get(0).get("leave_type") + "";

                String leave_start = bean.getInfor().get(0).get("leave_start") + "";
                String leave_end = bean.getInfor().get(0).get("leave_end") + "";
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
