package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.HeaderViewRecyclerAdapter;
import com.feirui.feiyunbangong.adapter.ShopAdapter;
import com.feirui.feiyunbangong.entity.FriendShopBean;
import com.feirui.feiyunbangong.entity.Good;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.feirui.feiyunbangong.view.CircleImageView2;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class FriendShop extends BaseActivity {


    private FriendShopBean.InfoBean mInfoBean;
    private View mHeader;
    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private ShopAdapter mShopAdapter;
    private RecyclerView recViewActivityFriendShop;
    private TextView tvFriendShopTitle;
    private CircleImageView2 cicleHead;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvContentMyShop;
    private TextView tvEditGv;
    private Button btnDeleteGood;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_friend_shop);

        Intent intent = getIntent();

        mInfoBean = (FriendShopBean.InfoBean) intent.getSerializableExtra(Constant.INTENT_SERIALIZABLE_DATA);

        initHead();
        initView(mHeader);
        initRv();
        initData();
    }


    private void initView(View header) {
        recViewActivityFriendShop = (RecyclerView) header.findViewById(R.id.recViewActivityFriendShop);
        tvFriendShopTitle = (TextView) header.findViewById(R.id.tvFriendShopTitle);
        cicleHead = (CircleImageView2) header.findViewById(R.id.cicle_head);
        tvName = (TextView) header.findViewById(R.id.tv_name);
        tvPhone = (TextView) header.findViewById(R.id.tv_phone);
        tvAddress = (TextView) header.findViewById(R.id.tv_address);
        tvContentMyShop = (TextView) header.findViewById(R.id.tv_content_my_shop);
        tvEditGv = (TextView) header.findViewById(R.id.tv_edit_gv);
        btnDeleteGood = (Button) header.findViewById(R.id.btn_delete_good);
        LinearLayout lleft = (LinearLayout) header.findViewById(R.id.leftIv);
        LinearLayout rightll = (LinearLayout) header.findViewById(R.id.rightll);

        rightll.setVisibility(View.INVISIBLE);

        lleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initHead() {


        mHeader = getLayoutInflater().inflate(R.layout.myshop_layout, null);


        tvFriendShopTitle.setText(mInfoBean.getTargetName() + "的小店");
        tvEditGv.setVisibility(View.GONE);

        tvName.setText(mInfoBean.getTargetName());
        ImageLoader.getInstance().displayImage(mInfoBean.getTargetHead(), cicleHead, ImageLoaderUtils.getSimpleOptions());
        tvAddress.setText(mInfoBean.getTargetAddress());
        tvPhone.setText(mInfoBean.getTargetPhoe());
        tvContentMyShop.setText(mInfoBean.getContent());
    }

    private GridLayoutManager mGridLayoutManager;

    private void initRv() {
        mShopAdapter = new ShopAdapter(new ArrayList<Good>());
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mShopAdapter);
        mHeaderViewRecyclerAdapter.addHeaderView(mHeader);

        mGridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mShopAdapter.isHeader(position) ? mGridLayoutManager.getSpanCount() : 1;
            }

        });

        recViewActivityFriendShop.setLayoutManager(mGridLayoutManager);
        recViewActivityFriendShop.setAdapter(mHeaderViewRecyclerAdapter);

        mShopAdapter.setData(new ArrayList<Good>());
    }

    private void initData() {
        //获取小店信息

    }
}
