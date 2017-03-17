package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GoodDetailActivity extends BaseActivity {

    private OkHttpClient mOkHttpClient = new OkHttpClient();


    private RecyclerView recGoodDetail;
    private ImageView mBanner;
    private TextView tvPrice, tvGoodName, tvPlace, tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_detail_activity);

        initTitle();
        initView();
        initRv();
        loadData();
    }

    private Request mRequest;

    private void loadData() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if (id == null) {
            return;
        }
        RequestParams params = new RequestParams();
        params.add("id", id);

        String url = UrlTools.url + UrlTools.GOOD_DETAIL;
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
            }

            @Override
            public void failure(String msg) {

            }

            @Override
            public void finish() {

            }
        });
    }


    private void initView() {
        setCenterString("商品详情");
        setLeftDrawable(R.drawable.arrows_left);
    }

    private GoodDetailAdapter mGoodDetailAdapter;

    private void initRv() {
        mGoodDetailAdapter = new GoodDetailAdapter();
        recGoodDetail = (RecyclerView) findViewById(R.id.rec_good_detail);
        recGoodDetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }
}
