package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.CaoZuoJiLuAdapter;
import com.feirui.feiyunbangong.adapter.MyShenPiAdapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xw.repo.refresh.PullListView;
import com.xw.repo.refresh.PullToRefreshLayout;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;

import static com.feirui.feiyunbangong.utils.UrlTools.url;

public class MyCaoZuoActivity extends BaseActivity implements View.OnClickListener{
    private static final int ON_REFRESH = 1;  //刷新常量
    private static final int ON_LOAD_MORE = 2;  //加载常量
    private TabLayout mHandle_tab;
    private TabItem mTab_commit,mTab_revise,mTab_send;
    private PullToRefreshLayout mDaishen_pull_to_refresh;
    private PullListView mHaveget;
    private LinearLayout mLl_type_handle;
    //请假，报销，外出，付款，采购，其他
    private TextView righttv,mTv_leave, mTv_submit,mTv_out,mTv_spend,mTv_purchase,mTv_else;
    private boolean flag = true;
    private CaoZuoJiLuAdapter tiAdapter;
    private MyShenPiAdapter adapter;
    private String tijiaourl;  //我提交的地址
    private String shenpiurl; //我审批过的地址
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cao_zuo);
        initView();
        initListener();
        initDate();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("操作记录");
        righttv = (TextView) findViewById(R.id.righttv);
        righttv.setVisibility(View.VISIBLE);
        righttv.setText("类型");

        mHandle_tab = (TabLayout) findViewById(R.id.handle_tab);
        mTab_commit = (TabItem) findViewById(R.id.tab_commit);
        mTab_revise = (TabItem) findViewById(R.id.tab_revise);
        mTab_send = (TabItem) findViewById(R.id.tab_send);
        mDaishen_pull_to_refresh = (PullToRefreshLayout) findViewById(R.id.daishen_pull_to_refresh);
        mHaveget = (PullListView) findViewById(R.id.haveget);

        mTv_leave = (TextView) findViewById(R.id.tv_leave);
        mTv_submit = (TextView) findViewById(R.id.tv_submit);
        mTv_out = (TextView) findViewById(R.id.tv_out);
        mTv_spend = (TextView) findViewById(R.id.tv_spend);
        mTv_purchase = (TextView) findViewById(R.id.tv_purchase);
        mTv_else = (TextView) findViewById(R.id.tv_else);

        mLl_type_handle = (LinearLayout) findViewById(R.id.ll_type_handle);
        mLl_type_handle.setVisibility(View.GONE);

        tijiaourl = url + UrlTools.APP_MY_APPROVAL;
        shenpiurl = url + UrlTools.APPROVAL_MY_APPROVAL_OLD;
    }

    private void initListener() {
        righttv.setOnClickListener(this);
        mTv_leave.setOnClickListener(this);
        mTv_out.setOnClickListener(this);
        mTv_submit.setOnClickListener(this);
        mTv_spend.setOnClickListener(this);
        mTv_purchase.setOnClickListener(this);
        mTv_else.setOnClickListener(this);
        rightll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    mLl_type_handle.setVisibility(View.VISIBLE);
                } else {
                    mLl_type_handle.setVisibility(View.GONE);
                }
                flag = !flag;
            }
        });
    }

    private void initDate() {

        mHandle_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        //我提交的
                        mySubmit();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void mySubmit() {
        tiAdapter = new CaoZuoJiLuAdapter(this, new ArrayList<HashMap<String, Object>>());
        mHaveget.setAdapter(tiAdapter);
        //查看每一项的详细内容
        tiAdapter.setOnChakanClickListener(new CaoZuoJiLuAdapter.OnChakanClickListener() {
            @Override
            public void onChakanClick(HashMap<String, Object> data, int position) {
                String approval_type = (String) data.get("approval_type");
                switch (approval_type){
                    case "请假":
                        Intent qing = new Intent(MyCaoZuoActivity.this,MyShenPiQingJaDetailActivity.class);
                        qing.putExtra("data",data);
                        startActivity(qing);
                        break;
                    case "报销":
                        Intent baoxiao = new Intent(MyCaoZuoActivity.this,MyShenPiBaoXiaoDetailActivity.class);
                        baoxiao.putExtra("data",data);
                        startActivity(baoxiao);
                        break;
                    case "外出":
                        Intent waichu = new Intent(MyCaoZuoActivity.this,MyWaiChuDetailActivity.class);
                        waichu.putExtra("data",data);
                        startActivity(waichu);
                        break;
                    case "付款":
                        Intent fukuan = new Intent(MyCaoZuoActivity.this,MyFuKuanDetailActivity.class);
                        fukuan.putExtra("data",data);
                        startActivity(fukuan);
                        break;
                    case "采购":
                        Intent caigou = new Intent(MyCaoZuoActivity.this,MyCaiGouDetailActivity.class);
                        caigou.putExtra("data",data);
                        startActivity(caigou);
                        break;
                    case "其他":
                        Intent qita = new Intent(MyCaoZuoActivity.this,MyQiTaDetailActivity.class);
                        qita.putExtra("data",data);
                        startActivity(qita);
                        break;


                }
            }
        });

        submitRefresh(shenpiurl);

    }

    private void submitRefresh(final String url) {
        mDaishen_pull_to_refresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentPage = 1;
                loadData(currentPage, ON_REFRESH,isType(righttv.getText().toString()),url);

            }
            private String isType(String type) {
                if ("请假".equals(type)){
                    type = "1";
                }else if ("报销".equals(type)){
                    type = "2";
                }else if ("外出".equals(type)){
                    type = "3";
                }else if ("付款".equals(type)){
                    type = "4";
                }else if ("采购".equals(type)){
                    type = "5";
                }else if ("其他".equals(type)){
                    type = "6";
                }
                return type;
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                currentPage += 1;
                loadData(currentPage, ON_LOAD_MORE,isType(righttv.getText().toString()),url);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.righttv:
                if (flag){
                   mLl_type_handle.setVisibility(View.VISIBLE);
                }else {
                    mLl_type_handle.setVisibility(View.GONE);
                }
                flag = !flag;
                break;
            case R.id.tv_leave://请假
                righttv.setText(mTv_leave.getText());
                loadData(1, ON_REFRESH, "1",shenpiurl);
                break;
            case R.id.tv_submit://报销
                righttv.setText(mTv_submit.getText());
                loadData(1, ON_REFRESH, "2",shenpiurl);
                break;
            case R.id.tv_out://外出
                righttv.setText(mTv_out.getText());
                loadData(1, ON_REFRESH, "3",shenpiurl);
                break;
            case R.id.tv_spend://付款
                righttv.setText(mTv_spend.getText());
                loadData(1, ON_REFRESH, "4",shenpiurl);
                break;
            case R.id.tv_purchase://采购
                righttv.setText(mTv_purchase.getText());
                loadData(1, ON_REFRESH, "5",shenpiurl);
                break;
            case R.id.tv_else://其他
                righttv.setText(mTv_else.getText());
                loadData(1, ON_REFRESH, "6",shenpiurl);
                break;


        }
    }

    private void loadData(int page, final int onRefreshOrLoadMore, String type, String url) {
        RequestParams params = new RequestParams();
        params.put("type",type);
        params.put("current_page", page + "");
        params.put("pagesize", "15");
            AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    super.onSuccess(statusCode, headers, responseBody);

                    JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                    Log.e("操作记录-我提交的", "jsonBean: "+jsonBean.toString());
                    if (jsonBean.getCode().equals("200")) {

                        if (onRefreshOrLoadMore == ON_REFRESH) {

                            tiAdapter.addAll(jsonBean.getInfor());
                            mDaishen_pull_to_refresh.refreshFinish(true);
                        } else {
                            tiAdapter.add(jsonBean.getInfor());
                            mDaishen_pull_to_refresh.loadMoreFinish(true);
                        }
                    }else if(jsonBean.getCode().equals("-400")){
                        T.showShort(MyCaoZuoActivity.this, jsonBean.getMsg());
                    } else {
                        tiAdapter.addAll(jsonBean.getInfor());
                        mDaishen_pull_to_refresh.loadMoreFinish(true);
                        mDaishen_pull_to_refresh.refreshFinish(true);
                    }
                }
            });
        }
}
