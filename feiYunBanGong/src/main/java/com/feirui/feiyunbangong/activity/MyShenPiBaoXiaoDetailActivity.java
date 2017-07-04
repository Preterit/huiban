package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by feirui1 on 2017-07-04.
 */

public class MyShenPiBaoXiaoDetailActivity extends BaseActivity{

    private HashMap<String, Object> mData;
    private String mList_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenpi_baoxiao_detail);
        initView();
        setCenterString("查看");
        getData();
    }


    private TextView mTvType, mTvJine, tvAllBaoxiao, mTvBaoxiaoName;
    private ImageView mBaoxiaoHead, ivBaoxiaoDetail;
    private Button mBtnAccept, mBtnRefuse;
    private RelativeLayout mReLayout;

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setRightVisibility(false);
        mTvJine = ((TextView) findViewById(R.id.jine_baoxiao));
        mTvType = (TextView) findViewById(R.id.leixing_baoxiao);
        tvAllBaoxiao = (TextView) findViewById(R.id.all_baoxiao);
        mBaoxiaoHead = (ImageView) findViewById(R.id.baoxiao_head_img);
        ivBaoxiaoDetail = (ImageView) findViewById(R.id.iv_baoxiao_detail);
        mTvBaoxiaoName = (TextView) findViewById(R.id.baoxiao_name);
        mBtnAccept = (Button) findViewById(R.id.btn_baoxiao_accept);
        mBtnRefuse = (Button) findViewById(R.id.btn_baoxiao_refuse);
        mReLayout = (RelativeLayout) findViewById(R.id.baoxiao_head);
        mReLayout.setVisibility(View.GONE);
        mBtnAccept.setVisibility(View.GONE);
        mBtnRefuse.setVisibility(View.GONE);

    }

    private void getData() {
        mData = (HashMap<String, Object>) getIntent().getSerializableExtra("data");
        Log.e("tag","报销获取的数据---------"+mData.toString());

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
                Log.e("orz", "success: " + bean.toString());
                ArrayList<HashMap<String, Object>> infor = bean.getInfor();
                Log.d("tag","报销获取的数据---------"+infor);  //是一个数组且只有一个数据
                HashMap<String, Object> in_fo = infor.get(0);

                //以下是infor.get(0)获取到的数据
//                detail=[{"expense_type":"回家","expense_money":"222.00","expense_detail":"1213"}],
//                total_money=1894949.00,
//                        staff_department=null, staff_duties=null, ttt=待报销, id=434, detail_count=1,
//                staff_head=http://123.57.45.74/feiybg/public/static/staff_head/434/8918267cfc972d7b6af62972c3678de6.jpeg,
                // staff_name=XY, expense_pic0=http://123.57.45.74/feiybg/public/img/434/b8c92c4f5dd5cbe286bee29232e028c5.jpe, expense_picture=/public/img/434/b8c92c4f5dd5cbe286bee29232e028c5.jpe


                JSONArray jsonArray = (JSONArray) in_fo.get("detail");

                try {
                    //获取json数组中的第一项
                    JSONObject detail = jsonArray.getJSONObject(0);


                    String expense_type = String.valueOf(detail.get("expense_type"));
                    mTvType.setText(expense_type);

                    String expense_money = String.valueOf(detail.get("expense_money"));
                    mTvJine.setText(expense_money);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String total_money = String.valueOf(in_fo.get("total_money"));
                tvAllBaoxiao.setText(total_money);

                String name = String.valueOf(in_fo.get("staff_name"));
                mTvBaoxiaoName.setText(name);


                ImageLoader.getInstance().displayImage(String.valueOf(in_fo.get("staff_head")), mBaoxiaoHead);
                ImageLoader.getInstance().displayImage(String.valueOf(in_fo.get("expense_pic0")), ivBaoxiaoDetail);
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
