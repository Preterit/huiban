package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.HeaderViewRecyclerAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GoodDetailActivity extends BaseActivity {


    private RecyclerView recGoodDetail;
    private ImageView mBanner;
    private TextView tvPrice, tvGoodName, tvPlace, tvContent;

    private HeaderViewRecyclerAdapter mHeaderRecAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_detail_activity);

        initTitle();
        initView();
        initRv();
        loadData();
    }


    private void loadData() {
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        if (id == null) {
            return;
        }
        RequestParams params = new RequestParams();
        params.add("id", id);

        String url = UrlTools.url + UrlTools.GOOD_DETAIL;
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                ArrayList<HashMap<String, Object>> infor = bean.getInfor();
                String figure = String.valueOf(infor.get(0).get("figure"));
                String mainPic = String.valueOf(infor.get(0).get("main_pic"));
                String goods_content = String.valueOf(infor.get(0).get("goods_content"));
                String goods_price = String.valueOf(infor.get(0).get("goods_price"));
                String goods_name = String.valueOf(infor.get(0).get("goods_name"));

                String[] split = figure.split(",");
                List<String> strings = Arrays.asList(split);

                mGoodDetailAdapter.setGoodDetails(strings);//设置详情数据
                ImageLoader.getInstance().displayImage(mainPic, mBanner);
                tvPrice.setText(goods_price);
                tvGoodName.setText(goods_name);
                tvContent.setText(goods_content);
            }

            /**
             *  null,
             "figure": "http://123.57.45.74/feiybg/public/goods/2017-03-17/ac13e7433ef95ca26cf00262f8061848.jpeg,http://123.57.45.74/feiybg/public/goods/2017-03-17/ac13e7433ef95ca26cf00262f8061848.jpeg"
             * @param msg
             */

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
        mHeaderRecAdapter = new HeaderViewRecyclerAdapter(mGoodDetailAdapter);

        View header = getLayoutInflater().inflate(R.layout.header_good_detail, null);
        mHeaderRecAdapter.addHeaderView(header);


        recGoodDetail = (RecyclerView) findViewById(R.id.rec_good_detail);
        recGoodDetail.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recGoodDetail.setAdapter(mHeaderRecAdapter);

        mBanner = (ImageView) header.findViewById(R.id.good_banner);
        tvGoodName = (TextView) header.findViewById(R.id.tv_good_name_detail);
        tvContent = (TextView) header.findViewById(R.id.good_detail_content);
        tvPrice = (TextView) header.findViewById(R.id.tv_price_good_detail);

    }
}
