package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

public class ShenPiFuKuanDetailActivity extends BaseActivity {

    private HashMap<String, Object> mData;
    private String mList_id;

    @PView
    TextView et_miaoshu, et_jine, et_duixiang, et_kaihuhang, et_zhanghao,tv_fangshi,tv_riqi;// 付款描述，付款金额，付款对象，开户行，银行账户,付款方式，付款日期
    @PView(click = "onClick")
    Button btn_pass;// 审批通过
    @PView(click = "onClick")
    Button btn_nopass;// 审批拒绝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenpi_fukuan_detail);


        initView();
        getData();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("付款审批");
        setRightVisibility(false);

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

        Utils.doPost(LoadingDialog.getInstance(ShenPiFuKuanDetailActivity.this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                if (bean.getCode().equals("200")) {
                    T.showShort(ShenPiFuKuanDetailActivity.this, bean.getMsg());
                    ShenPiFuKuanDetailActivity.this.finish();
                }
            }

            @Override
            public void failure(String msg) {
                T.showShort(ShenPiFuKuanDetailActivity.this, msg);
            }

            @Override
            public void finish() {

            }
        });

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
                Log.d("审批付款界面bean---------", "in_fo: " + bean.toString());


                HashMap<String, Object> in_fo = infor.get(0);
                Log.d("审批付款界面in_fo---------", "in_fo: " + in_fo.toString());
                try {
                    //付款描述
                    String miaoshu = String.valueOf(in_fo.get("payment_describe"));
                    et_miaoshu.setText(miaoshu);
                    //付款金额
                    String jine = String.valueOf(in_fo.get("payment_money"));
                    et_jine.setText(jine);
                    //外出对象
                    String duixiang = String.valueOf(in_fo.get("payment_for"));
                    et_duixiang.setText(duixiang);
                    //开户行
                    String kaihuhang = String.valueOf(in_fo.get("opening_bank"));
                    et_kaihuhang.setText(kaihuhang);
                    //银行账户
                    String zhanghao = String.valueOf(in_fo.get("account"));
                    et_zhanghao.setText(zhanghao);
                    //付款方式
                    String fangshi = String.valueOf(in_fo.get("payment_type"));
                    tv_fangshi.setText(zhanghao);
                    String riqi = String.valueOf(in_fo.get("payment_time"));
                    tv_riqi.setText(riqi);

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
