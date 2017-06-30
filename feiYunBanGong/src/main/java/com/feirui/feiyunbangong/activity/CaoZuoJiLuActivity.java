package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.CaoZuoJiLuAdapter;
import com.feirui.feiyunbangong.adapter.ShenPiAdapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xw.repo.refresh.PullListView;
import com.xw.repo.refresh.PullToRefreshLayout;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;

import static com.feirui.feiyunbangong.utils.UrlTools.pcUrl;
import static com.feirui.feiyunbangong.utils.UrlTools.url;

/**
 * Created by 邢悦 on 2017/6/22.
 */

public class CaoZuoJiLuActivity extends BaseActivity
       {
    private static final int ON_REFRESH = 1;  //刷新常量
    private static final int ON_LOAD_MORE = 2;  //加载常量
    private Spinner tiSpinner;
    private Spinner shenSpinner;
    private ArrayAdapter<String> mAdapter;
    private String[] leixing = new String[]{"选择审批类型", "请假", "报销", "外出", "付款",
            "采购", "其他"};

    private RadioButton mTiJiao;
    private RadioButton mShenPi;
    private RadioGroup mRgTools;

    private ShenPiAdapter adapter;
    private CaoZuoJiLuAdapter tiAdapter;

    private PullListView mPullListView;
    private PullToRefreshLayout mPullToRefreshLayout;

    private String tiurl;  //我提交的地址
    private String shenurl; //我审批过的地址


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_caozuo);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadData(currentPage, ON_REFRESH);
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("操作记录");
        setRightVisibility(false);
        mPullListView = (PullListView) findViewById(R.id.haveget);
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.daishen_pull_to_refresh);

        mRgTools = (RadioGroup) findViewById(R.id.rgTools);
        mTiJiao = (RadioButton) findViewById(R.id.rb_ti_jiao);
        mShenPi = (RadioButton) findViewById(R.id.rb_shen_pi);
        tiSpinner = (Spinner) findViewById(R.id.spinner_lei_xing);
        shenSpinner = (Spinner) findViewById(R.id.spinner_lei);
        tiSpinner.setVisibility(View.VISIBLE);

        tiurl = url + UrlTools.APPROVAL_MY_APPROVAL;
        shenurl = pcUrl + UrlTools.APPROVAL_APPROVAL;
        mTiJiao.setChecked(true);
        tiJiaoSpinner();

        mRgTools.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_ti_jiao:
                        tiJiaoSpinner();
                        break;
                    case R.id.rb_shen_pi:
                        shenPiSpinner();
                        break;
                }
            }
        });

    }

    public void tiJiaoSpinner(){
        shenSpinner.setVisibility(View.GONE);
        tiSpinner.setVisibility(View.VISIBLE);
        mAdapter = new ArrayAdapter<String>(CaoZuoJiLuActivity.this, R.layout.sp_item, R.id.tv, leixing);
        tiSpinner.setAdapter(mAdapter);
        tiAdapter = new CaoZuoJiLuAdapter(CaoZuoJiLuActivity.this, new ArrayList<HashMap<String, Object>>());
        mPullListView.setAdapter(tiAdapter);
        //监听选择类型
        tiSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadData(1, ON_REFRESH,tiurl);
                Log.e("tag", "自动加载数据----"+mPullListView.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tiCaoZuo(tiurl);
    }
      public void shenPiSpinner(){
          tiSpinner.setVisibility(View.GONE);
          shenSpinner.setVisibility(View.VISIBLE);
          mAdapter = new ArrayAdapter<String>(CaoZuoJiLuActivity.this, R.layout.sp_item, R.id.tv, leixing);
          shenSpinner.setAdapter(mAdapter);

          adapter = new ShenPiAdapter(CaoZuoJiLuActivity.this, new ArrayList<HashMap<String, Object>>());
          mPullListView.setAdapter(adapter);

          //查看每一项的详细内容
          adapter.setOnChakanClickListener(new ShenPiAdapter.OnChakanClickListener() {
              @Override
              public void onChakanClick(HashMap<String, Object> data, int position) {
                  String approval_type = (String) data.get("approval_type");
                  switch (approval_type){
                      case "请假":
//                          Intent qingjia = new Intent(CaoZuoJiLuActivity.this,MyShenPiQingJaDetailActivity.class);
//                          qingjia.putExtra("data",data);
//                          startActivity(qingjia);
                          break;

                  }
              }
          });

          //监听选择类型
          shenSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  loadData(1, ON_REFRESH,shenurl);
                  Log.e("tag", "自动加载数据----"+mPullListView.toString());
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
          });
          tiCaoZuo(shenurl);
      }

    public void tiCaoZuo(final String url){
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentPage = 1;
                if (url == tiurl){
                    loadData(currentPage, ON_REFRESH,url);
                }else if (url == shenurl){
                    loadData(currentPage, ON_REFRESH,url);
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (url == tiurl){
                    currentPage += 1;
                    loadData(currentPage, ON_LOAD_MORE,url);
                }else if (url == shenurl){
                    currentPage += 1;
                    loadData(currentPage, ON_LOAD_MORE,url);
                }
            }
        });

    }


    private int currentPage = 1;

    public void loadData(int page, final int onRefreshOrLoadMore,String url) {

        RequestParams params = new RequestParams();

        if (url == tiurl ){
            if (!"选择审批类型".equals(tiSpinner.getSelectedItem().toString())) {
                Log.e("tag","leixing------"+tiSpinner.getSelectedItem().toString());
                params.put("type", tiSpinner.getSelectedItem().toString());
            }

            params.put("current_page", page + "");
            params.put("pagesize", "15");
            AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    super.onSuccess(statusCode, headers, responseBody);

                    JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                    Log.d("获取得到的json", "jsonBean: "+jsonBean.toString());
                    if (jsonBean.getCode().equals("200")) {

                        if (onRefreshOrLoadMore == ON_REFRESH) {

                            tiAdapter.addAll(jsonBean.getInfor());
                            mPullToRefreshLayout.refreshFinish(true);
                        } else {
                            tiAdapter.add(jsonBean.getInfor());
                            mPullToRefreshLayout.loadMoreFinish(true);
                        }
                    } else {
                        tiAdapter.addAll(jsonBean.getInfor());
                        mPullToRefreshLayout.loadMoreFinish(true);
                        mPullToRefreshLayout.refreshFinish(true);
                    }
                }
            });
        } else if (url == shenurl){
            if (!"选择审批类型".equals(shenSpinner.getSelectedItem().toString())) {
                Log.e("tag","leixing------"+shenSpinner.getSelectedItem().toString());
                params.put("type", shenSpinner.getSelectedItem().toString());
            }

            params.put("current_page", page + "");
            params.put("pagesize", "15");
            AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    super.onSuccess(statusCode, headers, responseBody);

                    JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                    Log.d("获取得到的json", "jsonBean: "+jsonBean.toString());
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

}
