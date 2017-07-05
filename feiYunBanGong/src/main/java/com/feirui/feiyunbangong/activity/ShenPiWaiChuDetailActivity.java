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


public class ShenPiWaiChuDetailActivity extends BaseActivity {


    private HashMap<String, Object> mData;
    private String mList_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenpi_wai_chu_detail);

        initTitle();
        initView();
        setCenterString("待审批");
        getData();
    }

    private TextView tv_tijiaoname, tv_tijiaotime, tv_starttime, tv_endtime, tv_shiyou, end_time;
    private ImageView mWaichuHead, iv_sp_1, iv_sp_2, iv_sp_3;
    private Button btn_pass, btn_nopass;

    private void initView() {
        tv_tijiaoname = (TextView) findViewById(R.id.tv_sp_tijiaoname);
        tv_tijiaotime = (TextView) findViewById(R.id.tv_sp_tijiaotime);
        tv_starttime = (TextView) findViewById(R.id.tv_sp_starttime);
        tv_endtime = (TextView) findViewById(R.id.tv_sp_endtime);
        end_time = (TextView) findViewById(R.id.end_time);
        tv_shiyou = (TextView) findViewById(R.id.tv_sp_shiyou);
        mWaichuHead = (ImageView) findViewById(R.id.baoxiao_head_img);
        iv_sp_1 = (ImageView) findViewById(R.id.iv_sp_1);
        iv_sp_2 = (ImageView) findViewById(R.id.iv_sp_2);
        iv_sp_3 = (ImageView) findViewById(R.id.iv_sp_3);
        btn_nopass = (Button) findViewById(R.id.btn_nopass);
        btn_pass = (Button) findViewById(R.id.btn_pass);


        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mList_id.equals("")) {
                    updataShenPi(mList_id, "通过");
                }
            }
        });
        btn_nopass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mList_id.equals("")) {
                    //不能写"不通过"，要写“拒绝”
                    updataShenPi(mList_id, "拒绝");
                }
            }
        });
    }

    private void updataShenPi(String list_id, String acceptOrRefuse) {
        RequestParams params = new RequestParams();
        params.put("list_id", list_id + "");
        params.put("type", acceptOrRefuse);
        //请求接口
        String url = UrlTools.url + UrlTools.APPROVAL_UPDATE;

        Utils.doPost(LoadingDialog.getInstance(ShenPiWaiChuDetailActivity.this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                if (bean.getCode().equals("200")) {
                    T.showShort(ShenPiWaiChuDetailActivity.this, bean.getMsg());
                    ShenPiWaiChuDetailActivity.this.finish();
                }
            }

            @Override
            public void failure(String msg) {
                T.showShort(ShenPiWaiChuDetailActivity.this, msg);
            }

            @Override
            public void finish() {

            }
        });

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
                Log.d("审批外出界面bean---------", "in_fo: " + bean.toString());


                HashMap<String, Object> in_fo = infor.get(0);
                Log.d("审批外出界面in_fo---------", "in_fo: " + in_fo.toString());

                try {
                    //开始时间
                    String out_start = String.valueOf(in_fo.get("out_start"));
                    tv_starttime.setText(out_start);
                    //提交时间截取的是开始时间，
                    String[] tj_time = out_start.split(" ");
                    tv_tijiaotime.setText(tj_time[0]);
                    //结束时间
                    String out_end = String.valueOf(in_fo.get("out_end"));
                    tv_endtime.setText(out_end);
                    //外出时间
                    String out_time = String.valueOf(in_fo.get("out_time"));
                    end_time.setText(out_time);
                    //外出事由
                    String out_reason = String.valueOf(in_fo.get("out_reason"));
                    tv_shiyou.setText(out_reason);
                    //提交人的姓名
                    String staff_name = String.valueOf(in_fo.get("staff_name"));
                    tv_tijiaoname.setText(staff_name);
                    //提交日期
//                    String out_reason = String.valueOf(in_fo.get("out_reason"));
//                    tv_tijiaotime.setText(out_reason);



                    //头像
                    ImageLoader.getInstance().displayImage(String.valueOf(in_fo.get("staff_head")), mWaichuHead);
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
