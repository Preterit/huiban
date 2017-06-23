package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 邢悦 on 2017/6/23.
 */

public class ShenPiCaiGouDetailActivity extends BaseActivity{
    @BindView(R.id.et_miaoshu)
    private EditText  mTvMiaoShu;
    @BindView(R.id.tv_leixing)
    private TextView mTvLeiXing;
    @BindView(R.id.tv_shijian)
    private TextView mTvTime;
    @BindView(R.id.tv_name)
    private TextView mTvName;
    @BindView(R.id.tv_number)
    private TextView mTvNumber;
    @BindView(R.id.tv_jiage)
    private TextView mTvJiaGe;
    @BindView(R.id.tv_fangshi)
    private TextView mTvFangShi;
    @BindView(R.id.tv_beizhu)
    private TextView mTvBeiZhu;
    @BindView(R.id.iv_baoxiao_detail)
    private ImageView mIvPic;
    @BindView(R.id.btn_nopass)
    private Button mBtNoPass;
    @BindView(R.id.btn_pass)
    private Button mBtPass;


    private HashMap<String,Object> mData;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_shen_pi_caigou_detail);
        ButterKnife.bind(this);
        initTop();
        getData();

    }

    private void initTop() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("采购");
        setRightVisibility(false);

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

        //从服务器获取数据
        Utils.doPost(LoadingDialog.getInstance(this), this, url,
                params, new Utils.HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        Log.e("orz", "success: " + bean.toString());

                    }

                    @Override
                    public void failure(String msg) {

                    }

                    @Override
                    public void finish() {

                    }
                });

    }

    public void updateShenPi(String list_id, String acceptOrRefuse){

    }



}
