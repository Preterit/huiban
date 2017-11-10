package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
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

 public class MyFuKuanDetailActivity extends BaseActivity {

    private HashMap<String, Object> mData;
    private String mList_id;

    @PView
    TextView et_miaoshu, et_jine, et_duixiang, et_kaihuhang, et_zhanghao,tv_fangshi,tv_riqi;// 付款描述，付款金额，付款对象，开户行，银行账户,付款方式，付款日期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshenpi_fukuan_detail);


        initView();
        getData();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("查看");
        setRightVisibility(false);

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
                    //et_miaoshu, et_jine, et_duixiang, et_kaihuhang, et_zhanghao;// 付款描述，付款金额，付款对象，开户行，银行账户
                    //{id=489, payment_for=李策, staff_duties=null, payment_money=100.00, staff_head=http://123.57.45.74/feiybg/public/static/staff_head/moren/pic_touxiang.png,
                    // staff_name=李策1, ttt=待付款, staff_department=null, account=450098886, payment_time=2017-05-19, payment_type=支付宝, opening_bank=建行,
                    // payment_describe=今天买了个大西瓜}

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
