package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static com.feirui.feiyunbangong.R.id.ll_add_mingxi;

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
    private LinearLayout mLayout;
    private View v_add_mingxi;

    private TextView tv_add_mingxi, tv_title, tv_xuanze_zhifufangshi,
            tv_zhifufangshi, tv_qiwangriqi;
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
        mTvLeiXing = (TextView) findViewById(R.id.tv_leixing);
        mTvTime = (TextView) findViewById(R.id.tv_shijian);

        mTvFangShi = (TextView) findViewById(R.id.tv_fangshi);
        mTvBeiZhu = (TextView) findViewById(R.id.tv_beizhu);
        mIvPic = (ImageView) findViewById(R.id.iv_baoxiao_detail);
        mBtNoPass = (Button) findViewById(R.id.btn_nopass);
        mBtPass = (Button) findViewById(R.id.btn_pass);
        mLayout = (LinearLayout) findViewById(ll_add_mingxi);

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
        final String url =  UrlTools.url + UrlTools.APPROVAL_DETAIL;

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


//        detail=[{"pur_money":"77.00","pur_name":"pp","pur_number":1},
//        {"pur_money":"7.00","pur_name":"ii","pur_number":1}]
        //从服务器获取数据
        Utils.doPost(LoadingDialog.getInstance(this), this, url,
                params, new Utils.HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {

                        ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                        HashMap<String,Object> in_fo = infor.get(0);
                        Log.e("orz", "success: " + in_fo.toString());

                        Log.e("orz", "success: " + in_fo.get("pur_picture0") + "");
                        mTvMiaoShu.setText(in_fo.get("pur_describe") + "");
                        mTvLeiXing.setText(in_fo.get("pur_type") + "");
                        mTvTime.setText(in_fo.get("pur_date") + "");
                        mTvFangShi.setText(in_fo.get("pur_pay_type") + "");
                        mTvBeiZhu.setText(in_fo.get("pur_remarks") + "");

                        Glide.with(ShenPiCaiGouDetailActivity.this).load( in_fo.get("pur_picture0") + "")
                                .error( R.drawable.loading_0 )
                                .into(mIvPic);
//                        ImageLoader.getInstance().displayImage(url  + String.valueOf(in_fo.get("pur_picture")), mIvPic);
                        JSONArray jsonArray = (JSONArray) in_fo.get("detail");

                        if (jsonArray.length() > 1){
                            v_add_mingxi = getLayoutInflater().inflate(R.layout.add_mingxi_caigou,
                                    null);
                            tv_title = (TextView) v_add_mingxi.findViewById(R.id.tv_title);
                            tv_title.setTag(v_add_mingxi);
                            mLayout.addView(v_add_mingxi);
                        }

                        // 采购明细：
                        for (int i = 0; i <  mLayout.getChildCount(); i++) {
                            View v =  mLayout.getChildAt(i);
                            Log.e("tag","-------------------------------------------"+mLayout.getChildAt(i));
                            EditText et_name = (EditText) v.findViewById(R.id.et_mingcheng);
                            EditText et_number = (EditText) v.findViewById(R.id.et_number);
                            EditText et_price = (EditText) v.findViewById(R.id.et_price);

                            mTvName = (TextView)  v.findViewById(R.id.tv_name);
                            mTvNumber = (TextView)  v.findViewById(R.id.tv_number);
                            mTvJiaGe = (TextView)  v.findViewById(R.id.tv_jiage);
                            try{
                                JSONObject detail = (JSONObject) jsonArray.get(i);
                                mTvName.setText(detail.get("pur_name") + "");
                                mTvJiaGe.setText(detail.get("pur_money") + "");
                                mTvNumber.setText(detail.get("pur_number") + "");

                            }catch (JSONException e) {
                                e.printStackTrace();
                            }

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
