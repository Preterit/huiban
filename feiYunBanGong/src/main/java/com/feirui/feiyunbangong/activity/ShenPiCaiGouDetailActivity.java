package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 邢悦 on 2017/6/23.
 */

public class ShenPiCaiGouDetailActivity extends BaseActivity{

    private TextView  mTvMiaoShu;

    private TextView mTvLeiXing;

    private TextView mTvTime;

    private TextView mTvName;

    private TextView mTvNumber;

    private TextView mTvJiaGe;

    private TextView mTvFangShi;

    private TextView mTvBeiZhu;

    private ImageView mIvPic;

    private Button mBtNoPass;

    private Button mBtPass;


    private HashMap<String,Object> mData;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_shen_pi_caigou_detail);
//        ButterKnife.bind(this);
        initTop();
        getData();

    }

    private void initTop() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("采购");
        setRightVisibility(false);
        mTvMiaoShu = (TextView) findViewById(R.id.tv_miaoshu);
        mTvLeiXing = (TextView) findViewById(R.id.tv_miaoshu);
        mTvTime = (TextView) findViewById(R.id.tv_shijian);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvNumber = (TextView) findViewById(R.id.tv_number);
        mTvJiaGe = (TextView) findViewById(R.id.tv_jiage);
        mTvFangShi = (TextView) findViewById(R.id.tv_fangshi);
        mTvBeiZhu = (TextView) findViewById(R.id.tv_beizhu);
        mIvPic = (ImageView) findViewById(R.id.iv_baoxiao_detail);
        mBtNoPass = (Button) findViewById(R.id.btn_nopass);
        mBtPass = (Button) findViewById(R.id.btn_pass);

        mBtNoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateShenPi(mList_id, "拒绝");
            }
        });
        mBtPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateShenPi(mList_id,"通过");
            }
        });

    }

    String mList_id;

    private void getData() {
        mData = (HashMap<String, Object>) getIntent().getSerializableExtra("data");

        Object id = mData.get("id");
        Object approval_type = mData.get("approval_type");
        Object approval_id = mData.get("approval_id");
        Object list_id = mData.get("list_id");
        mList_id = list_id +"";
        String url =  UrlTools.url + UrlTools.APPROVAL_DETAIL;

        RequestParams params = new RequestParams();
        params.put("id",id + "");
        params.put("approval_type",approval_type + "");
        params.put("approval_id",approval_id + "");
        params.put("list_id",mList_id);

//        {code='200', msg='成功',
//         infor=[{pur_picture0=http://123.57.45.74/feiybg/public/img/429/222d78d76f62012253e7e78185764b1b.jpeg,
            // staff_duties=null, pur_pay_type=现金支付, pur_remarks=投影仪,
            // detail=[{"pur_money":"44.00","pur_name":"56","pur_number":1}],
            // pur_type=技术采购, staff_head=http://123.57.45.74/feiybg/public/static/staff_head/429/c377415a094e4b0a67eff79fcf5cad60.jpeg,
            // pur_describe=dftg, staff_department=null,
            // pur_picture=/public/img/429/222d78d76f62012253e7e78185764b1b.jpeg,
            // id=429, staff_name=邢悦, ttt=待采购, pur_date=2017-06-23 00:00:00, detail_count=1}]}

        //从服务器获取数据
        Utils.doPost(LoadingDialog.getInstance(this), this, url,
                params, new Utils.HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {

                        ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                        HashMap<String,Object> in_fo = infor.get(0);
                        Log.e("orz", "success: " + in_fo.get("pur_describe") + "");

                        mTvMiaoShu.setText(in_fo.get("pur_describe") + "");
                        mTvLeiXing.setText(in_fo.get("pur_type") + "");
                        mTvTime.setText(in_fo.get("pur_date") + "");
                        mTvFangShi.setText(in_fo.get("pur_pay_type") + "");
                        mTvBeiZhu.setText(in_fo.get("pur_remarks") + "");

                        Glide.with(ShenPiCaiGouDetailActivity.this).load(in_fo.get("pur_picture"))
                                .error( R.drawable.loading_0 )
                                .into(mIvPic);
                        JSONArray jsonArray = (JSONArray) in_fo.get("detail");
                        try{
                            JSONObject detail = (JSONObject) jsonArray.get(0);
                            mTvName.setText(detail.get("pur_name") + "");
                            mTvJiaGe.setText(detail.get("pur_money") + "");
                            mTvNumber.setText(detail.get("pur_number") + "");

                        }catch (JSONException e) {
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

    public void updateShenPi(String list_id, String acceptOrRefuse){
        RequestParams params = new RequestParams();
        params.put("list_id", list_id + "");
        params.put("type", acceptOrRefuse);

        //请求接口
        String url = UrlTools.url + UrlTools.APPROVAL_UPDATE;
        Utils.doPost(LoadingDialog.getInstance(this),this,url,params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                if (bean.getCode().equals("200")) {
                    T.showShort(ShenPiCaiGouDetailActivity.this, bean.getMsg());

                    ShenPiCaiGouDetailActivity.this.finish();
                }else {
                    T.showShort(ShenPiCaiGouDetailActivity.this, bean.getMsg());
                }
            }

            @Override
            public void failure(String msg) {
                T.showShort(ShenPiCaiGouDetailActivity.this, msg);
            }

            @Override
            public void finish() {

            }
        });

    }



}
