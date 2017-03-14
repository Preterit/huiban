package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.view.TextImageView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 好友资料：
 *
 * @author feirui1
 */
public class HaoYouZiLiaoActivity extends BaseActivity implements
        OnClickListener {
    String phone = "";
    @PView
    TextView tv_name, tv_name_02, tv_phone, tv_email;
    @PView
    TextImageView iv_head;
    @PView(click = "onClick")
    Button bt_liaotian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hao_you_zi_liao);
        initView();
        initData();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("好友资料");
        setRightVisibility(false);
        phone = getIntent().getStringExtra("phone");
    }

    /**
     *
     */
    private void initData() {
        RequestParams params = new RequestParams();

        params.put("staff_mobile", phone + "");
        String url = UrlTools.url + UrlTools.USER_SEARCH_MOBILE;
        L.e("好友资料" + url + " params" + params);
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                ArrayList<HashMap<String, Object>> infor = bean.getInfor();
                HashMap<String, Object> map = infor.get(0);

                tv_name.setText(String.valueOf(map.get("staff_name")));
                tv_name_02.setText(String.valueOf("姓名 :" + map.get("staff_name")));
                tv_phone.setText(String.valueOf("手机号 :" + map.get("staff_mobile")));
                tv_email.setText(String.valueOf("地址: " + map.get("address")));
                ImageLoader.getInstance().displayImage(map.get("staff_head") + "", iv_head);
            }

            @Override
            public void failure(String msg) {

            }

            @Override
            public void finish() {

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_liaotian:
                finish();
                break;
        }

    }

}
