package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.AddShopActivity;
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
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyShopActivity extends BaseActivity implements OnClickListener {

    private RecyclerView mRecyclerView;
    private GoodsAdapter adapter;
    private List<Good> goods;
    private LinearLayout leftll;
    private String store_id;
    private TextView tvEdit;
    private View mHeader;
    private Button entWD;
    private TextView updataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);

        initHeader();

        initView();
        setListener();
    }

    private void initHeader() {
        mHeader = getLayoutInflater().inflate(R.layout.myshop_layout, null);
        entWD=(Button)mHeader.findViewById(R.id.EntweidianBtn);
        updataText=(TextView)mHeader.findViewById(R.id.updataText);
        TextView tvName = (TextView) mHeader.findViewById(R.id.tv_name);
        TextView tvAddress = (TextView) mHeader.findViewById(R.id.tv_address);
        TextView tvPhone = (TextView) mHeader.findViewById(R.id.tv_phone);
        TextView tvContent = (TextView) mHeader.findViewById(R.id.tv_content_my_shop);
        tvEdit = (TextView) mHeader.findViewById(R.id.tv_edit_gv);

        CircleImageView2 circleImageView2 = (CircleImageView2) mHeader.findViewById(R.id.cicle_head);
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
    }

    private void setListener() {
        leftll.setOnClickListener(this);
        entWD.setOnClickListener(this);
        updataText.setOnClickListener(this);
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
        leftll = (LinearLayout) mHeader.findViewById(R.id.leftll);


        mBtnDelete = (Button) mHeader.findViewById(R.id.btn_delete_good);

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

    private GridLayoutManager mGridLayoutManager;

    private void initRv() {
        mGridLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);

        mRecyclerView = (RecyclerView) findViewById(R.id.recView_myshop);

        mRecyclerView.setLayoutManager(mGridLayoutManager);

        mShopAdapter = new ShopAdapter(new ArrayList<Good>());
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mShopAdapter);
        footer = LayoutInflater.from(this).inflate(R.layout.add_good_footer, null, false);
        footer.setVisibility(View.GONE);
        mHeaderViewRecyclerAdapter.addFooterView(footer);
        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        mHeaderViewRecyclerAdapter.addHeaderView(mHeader);

        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mShopAdapter.isHeader(position) ? mGridLayoutManager.getSpanCount() : 1;
            }

        });
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
            case R.id.EntweidianBtn:
                enterWd();
                break;
            case R.id.updataText:
                updataUrl();
                break;
        }
    }

    private void updataUrl() {
        startActivity(new Intent(MyShopActivity.this, AddShopActivity.class));
        finish();
    }
    private void enterWd() {
        String postUrl="http://123.57.45.74/feiybg/public/index.php/api/Store/enter_store";
        RequestParams requestParams = new RequestParams();
        Utils.doPost(LoadingDialog.getInstance(this), this, postUrl, requestParams, new HttpCallBack() {
            @Override
            public void success(JsonBean  bean) {
//                T.showShort(MyShopActivity.this, "获取微店地址成功");
                ArrayList<HashMap<String, Object>> edurl = bean.getInfor();
                //Log.e("Tag","获得的微店地址"+edurl.get(0).get("store_url"));
                String urll = (String)edurl.get(0).get("store_url");
                if("0".equals(urll)){
                    Toast.makeText(MyShopActivity.this,"您需要先添加微店地址",Toast.LENGTH_SHORT).show();
                }else{
                    setListenerss(urll);
                }
            }
            @Override
            public void failure(String msg) {

            }

            @Override
            public void finish() {

            }
        });
    }
    private void setListenerss(String edurl) {
        Intent intent =new Intent();
        intent.putExtra("uri",edurl);
        intent.putExtra("TAG","1");
        intent.setClass(MyShopActivity.this,WebViewActivity.class);
        startActivity(intent);

    }

}
