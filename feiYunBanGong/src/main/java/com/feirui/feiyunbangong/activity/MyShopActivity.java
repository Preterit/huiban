package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.GoodsAdapter;
import com.feirui.feiyunbangong.adapter.HeaderViewRecyclerAdapter;
import com.feirui.feiyunbangong.adapter.ShopAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.Good;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.CircleImageView2;
import com.feirui.feiyunbangong.view.CustomGridLayoutManager;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyShopActivity extends BaseActivity implements OnClickListener {

    private RecyclerView mRecyclerView;
    private GoodsAdapter adapter;
    private List<Good> goods;
    private ScrollView sv;
    private LinearLayout leftll;
    private String store_id;
    private TextView tvEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);
        TextView tvName = (TextView) findViewById(R.id.tv_name);
        TextView tvAddress = (TextView) findViewById(R.id.tv_address);
        TextView tvPhone = (TextView) findViewById(R.id.tv_phone);
        TextView tvContent = (TextView) findViewById(R.id.tv_content_my_shop);
        tvEdit = (TextView) findViewById(R.id.tv_edit_gv);

        CircleImageView2 circleImageView2 = (CircleImageView2) findViewById(R.id.cicle_head);

        Intent intent = getIntent();
        JsonBean jsonBean = (JsonBean) intent.getSerializableExtra("json_bean");

        if (jsonBean != null) {
            ArrayList<HashMap<String, Object>> list = jsonBean.getInfor();
            if (list.get(0) != null) {
                HashMap<String, Object> map = list.get(0);
                String content = String.valueOf(map.get("content"));
                String storeName = String.valueOf(map.get("store_name"));
                String provinceName = String.valueOf(map.get("provice_id"));
                String cityName = String.valueOf(map.get("city_id"));

                store_id = String.valueOf(map.get("id"));//小店的id

                tvAddress.setText(provinceName + " " + cityName);
                tvContent.setText(content);

                if (AppStore.myuser != null) {
                    if (AppStore.myuser.getName() != null) {
                        tvName.setText(AppStore.myuser.getName());
                    }
                    if (AppStore.myuser.getPhone() != null) {
                        tvPhone.setText(AppStore.myuser.getPhone());
                    }
                    if (AppStore.myuser.getHead() != null) {
                        ImageLoader.getInstance().displayImage(AppStore.myuser.getHead(), circleImageView2);
                    }
                }
            }
        }
        initView();
        setListener();
    }

    private void setListener() {
        leftll.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {

        goods = new ArrayList<>();

        String url = UrlTools.url + UrlTools.XIAO_DIAN_GOODS;
        RequestParams requestParams = new RequestParams();
        requestParams.put("currentpage", 0);
        requestParams.put("pagesize", Constant.DEFAULT_PAGE_SIZE);
        Utils.doPost(LoadingDialog.getInstance(this), this, url, requestParams, new HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                ArrayList<HashMap<String, Object>> arrayList = bean.getInfor();
                for (HashMap<String, Object> map : arrayList) {

                    Good good = new Good();
                    String good_name = String.valueOf(map.get("goods_name"));
                    String price = String.valueOf(map.get("goods_price"));
                    String pic = String.valueOf(map.get("main_pic"));

                    good.setGood_name(good_name);
                    good.setPrivce(price);
                    good.setImgUrl(pic);
                    good.setId((Integer) map.get("id"));

                    goods.add(good);
                }


                Log.e("orz", "" + goods);
                mShopAdapter.setData(goods);
            }

            @Override
            public void finish() {
            }

            @Override
            public void failure(String msg) {
                mShopAdapter.setData(new ArrayList<Good>());
            }
        });

    }

    private Button mBtnDelete;

    private void initView() {
        initRv();
        sv = (ScrollView) findViewById(R.id.sv);
        leftll = (LinearLayout) findViewById(R.id.leftll);


        mBtnDelete = (Button) findViewById(R.id.btn_delete_good);

        tvEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvEdit.getText().equals("编辑")) {
                    mShopAdapter.setEdit(true);
                    tvEdit.setText("取消");
                    mBtnDelete.setVisibility(View.VISIBLE);
                } else {
                    tvEdit.setText("编辑");
                    mShopAdapter.setEdit(false);
                    mBtnDelete.setVisibility(View.GONE);
                }
            }
        });
        mBtnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final int selectedPosition = mShopAdapter.getSelectedPosition();
                if (selectedPosition != -1) {
                    String url = UrlTools.url + UrlTools.DELETE_GOOD;
                    RequestParams requestParams = new RequestParams();

                    int id = ((Good) mShopAdapter.getItem(selectedPosition)).getId();

                    Log.e("orz", "onClick: " + id);
                    requestParams.put("id", id + "");
                    Utils.doPost(LoadingDialog.getInstance(MyShopActivity.this), MyShopActivity.this, url, requestParams, new HttpCallBack() {
                        @Override
                        public void success(JsonBean bean) {
                            if (bean.getCode().equals("200")) {
                                T.showShort(MyShopActivity.this, "删除成功");
                                mShopAdapter.setSelectedItem(-1);
                                mShopAdapter.remove(selectedPosition);
                            }
                        }

                        @Override
                        public void failure(String msg) {
                            T.showShort(MyShopActivity.this, msg);
                        }

                        @Override
                        public void finish() {

                        }
                    });
                } else {
                    T.showShort(MyShopActivity.this, "请选择要删除的商品");
                }
            }
        });
    }

    private HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;
    private ShopAdapter mShopAdapter;
    private View footer;

    private void initRv() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rec_shop_goods);
        mRecyclerView.setLayoutManager(new CustomGridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        mShopAdapter = new ShopAdapter(new ArrayList<Good>());
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mShopAdapter);
        footer = LayoutInflater.from(this).inflate(R.layout.add_good_footer, null, false);
        mHeaderViewRecyclerAdapter.addFooterView(footer);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        footer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyShopActivity.this, AddGoodActivity.class);
                intent.putExtra("id", store_id);
                startActivity(intent);
            }
        });

        mShopAdapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ShopAdapter.ViewHolder holder, int position) {
                if (mShopAdapter.isEdit()) {//编辑模式
                    //点击改变图标,重置其他位置的图标
                    mShopAdapter.setSelectedItem(position);
                } else {
                    //跳转到详情页
                    Intent intent = new Intent(MyShopActivity.this, GoodDetailActivity.class);
                    intent.putExtra("id", mShopAdapter.getItem(position).getId() + "");

                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftll:
                finish();
                break;
        }
    }

}
