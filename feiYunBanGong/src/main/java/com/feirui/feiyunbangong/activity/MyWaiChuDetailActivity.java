package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by feirui1 on 2017-07-04.
 */

public  class MyWaiChuDetailActivity extends  BaseActivity {
    private HashMap<String, Object> mData;
    private String mList_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshenpi_wai_chu_detail);

        initView();
        setCenterString("查看");
        getData();
    }

    private TextView tv_tijiaoname, tv_tijiaotime, tv_starttime, tv_endtime, tv_shiyou, end_time;
    private ImageView  iv_sp_1, iv_sp_2, iv_sp_3;

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setRightVisibility(false);

        tv_tijiaotime = (TextView) findViewById(R.id.tv_sp_tijiaotime);
        tv_starttime = (TextView) findViewById(R.id.tv_sp_starttime);
        tv_endtime = (TextView) findViewById(R.id.tv_sp_endtime);
        end_time = (TextView) findViewById(R.id.end_time);
        tv_shiyou = (TextView) findViewById(R.id.tv_sp_shiyou);

        iv_sp_1 = (ImageView) findViewById(R.id.iv_sp_1);
        iv_sp_2 = (ImageView) findViewById(R.id.iv_sp_2);
        iv_sp_3 = (ImageView) findViewById(R.id.iv_sp_3);


    }



    private void getData() {
        mData = (HashMap<String, Object>) getIntent().getSerializableExtra("data");
        Log.e("tag", "getData: " + mData.toString());

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

        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                ArrayList<HashMap<String, Object>> infor = bean.getInfor();


                HashMap<String, Object> in_fo = infor.get(0);
                Log.d("审批外出界面in_fo---------", "in_fo: " + in_fo.toString());

                try {
                    //开始时间
                    String out_start = String.valueOf(in_fo.get("out_start"));
                    tv_starttime.setText(out_start);
                    //结束时间
                    String out_end = String.valueOf(in_fo.get("out_end"));
                    tv_endtime.setText(out_end);
                    //外出时间
                    String out_time = String.valueOf(in_fo.get("out_time"));
                    end_time.setText(out_time);
                    //外出事由
                    String out_reason = String.valueOf(in_fo.get("out_reason"));
                    tv_shiyou.setText(out_reason);

                    //提交日期
//                    String out_reason = String.valueOf(in_fo.get("out_reason"));
//                    tv_tijiaotime.setText(out_reason);

                    Log.d("审批外出界面in_fo---------", "in_fo: " +out_end + out_reason );

                    //头像
//                    ImageLoader.getInstance().displayImage(String.valueOf(in_fo.get("staff_head")), mWaichuHead);
                    if(!in_fo.get("out_picture0").equals("")){
                        ImageLoader.getInstance().displayImage(String.valueOf(in_fo.get("out_picture0")), iv_sp_1);
                    }
                    if(!in_fo.get("out_picture1").equals("")){
                        iv_sp_2.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(String.valueOf(in_fo.get("out_picture1")), iv_sp_2);
                    }
                    if(!in_fo.get("out_picture2").equals("")){
                        iv_sp_3.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(String.valueOf(in_fo.get("out_picture2")), iv_sp_3);
                    }


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

