package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ShenPiAdapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xw.repo.refresh.PullListView;
import com.xw.repo.refresh.PullToRefreshLayout;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 待审批操作 别人提交给自己的
 */

public class DaiShenPiActivity extends BaseActivity implements
        OnItemSelectedListener {
    private static final int ON_REFRESH = 1;  //刷新常量
    private static final int ON_LOAD_MORE = 2;  //加载常量
    @PView
    private Spinner sp_daishenpi;

    private ArrayAdapter<String> adt;  //审批类型的适配器
    private ShenPiAdapter adapter;
    private String[] leixing = new String[]{"选择审批类型", "请假", "报销", "外出", "付款",
            "采购","其他"};

    private  JsonBean json;
    private static int count;
    // 当前数据页码

    private PullListView mPullListView;
    private PullToRefreshLayout mPullToRefreshLayout;

//    private Handler handler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            switch (msg.what) {
//                case Constant.REFRESH:
//                    Log.e("tag","----------刷新autolist-------------");
//                    break;
//                case Constant.LOAD:
//                    Log.e("tag","----------加载autolist-------------");
//                    break;
//                case Constant.PUBLIC_TYPE_ONE:
//                    Log.e("tag","----------公用标记-------------");
//                    break;
//            }
//        }
//
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dai_shen_pi);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(currentPage, ON_REFRESH);
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("待审批");
        setRightVisibility(false);
        mPullListView = (PullListView) findViewById(R.id.haveget);
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.daishen_pull_to_refresh);

        sp_daishenpi.setOnItemSelectedListener(this);
        adt = new ArrayAdapter<>(this, R.layout.sp_item, R.id.tv, leixing);
        sp_daishenpi.setAdapter(adt);

        adapter = new ShenPiAdapter(DaiShenPiActivity.this, new ArrayList<HashMap<String, Object>>());
        mPullListView.setAdapter(adapter);
        Log.e("tag", "页面数据列表mPullListView---- "+mPullListView.toString());

        //查看各种审批类型的详情
        adapter.setOnChakanClickListener(new ShenPiAdapter.OnChakanClickListener() {
            @Override
            public void onChakanClick(HashMap<String, Object> data, int position) {
                String approval_type = (String) data.get("approval_type");
                switch (approval_type) {
                    case "请假":
                        Intent intent = new Intent(DaiShenPiActivity.this, ShenPiQingJaDetailActivity.class);
                        intent.putExtra("data", data);
                        startActivity(intent);
                        break;
                    case "报销":
                        Intent baoxiaoIntent = new Intent(DaiShenPiActivity.this, ShenpiBaoxiaoDetailActivity.class);
                        baoxiaoIntent.putExtra("data", data);
                        startActivity(baoxiaoIntent);
                        break;

                    case "外出":
                        Intent waichuIntent = new Intent(DaiShenPiActivity.this, ShenPiWaiChuDetailActivity.class);
                        waichuIntent.putExtra("data", data);
                        startActivity(waichuIntent);
                        break;
                    case "付款":
                        Intent fukuanIntent = new Intent(DaiShenPiActivity.this, ShenPiFuKuanDetailActivity.class);
                        fukuanIntent.putExtra("data", data);
                        startActivity(fukuanIntent);
                        break;
                    case "采购":
                        Intent caigouIntent = new Intent(DaiShenPiActivity.this, ShenPiCaiGouDetailActivity.class);
                        caigouIntent.putExtra("data", data);
                        startActivity(caigouIntent);
                        break;
                    case "其他":
                        Intent qitaIntent = new Intent(DaiShenPiActivity.this, ShenPiQiTaDetailActivity.class);
                        qitaIntent.putExtra("data", data);
                        startActivity(qitaIntent);
                        break;
                }

            }
        });
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentPage = 1;
                loadData(currentPage, ON_REFRESH);

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                currentPage += 1;
                loadData(currentPage, ON_LOAD_MORE);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

        Object itemAtPosition = parent.getItemAtPosition(position);
        String type = (String) itemAtPosition;

        loadData(1, ON_REFRESH);
        Log.e("待审批页面", "自动加载数据----"+mPullListView.toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private int currentPage = 1;

    public void loadData(int page, final int onRefreshOrLoadMore) {

        RequestParams params = new RequestParams();
        String url = UrlTools.url + UrlTools.APPROVAL_APPROVAL;

        if (!"选择审批类型".equals(sp_daishenpi.getSelectedItem().toString())) {
            Log.e("待审批页面","leixing------"+sp_daishenpi.getSelectedItem().toString());

            switch (sp_daishenpi.getSelectedItem().toString()){
                case "请假" :
                    params.put("type", "1");
                    break;
                case "外出" :
                    params.put("type", "2");
                    break;
                case "报销" :
                    params.put("type", "3");
                    break;
                case "付款" :
                    params.put("type", "4");
                    break;
                case "采购" :
                    params.put("type", "5");
                    break;
                case "其他" :
                    params.put("type", "6");
                    break;

            }
        }
        params.put("current_page", page + "");
        params.put("pagesize", "15");
        Log.e("待审批页面","params---"+params.toString());
        AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);

                JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                Log.e("待审批页面", "待审批jsonBean: "+jsonBean.toString());
                if (jsonBean.getCode().equals("200")) {

                    if (onRefreshOrLoadMore == ON_REFRESH) {

                        adapter.addAll(jsonBean.getInfor());
                        mPullToRefreshLayout.refreshFinish(true);
                    } else {
                        adapter.add(jsonBean.getInfor());
                        mPullToRefreshLayout.loadMoreFinish(true);
                       }
                } else {
                    adapter.addAll(jsonBean.getInfor());
                    mPullToRefreshLayout.loadMoreFinish(true);
                    mPullToRefreshLayout.refreshFinish(true);
                }
            }
        });
    }



}
