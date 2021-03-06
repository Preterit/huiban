package com.feirui.feiyunbangong.activity;

import android.content.Intent;
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
import com.feirui.feiyunbangong.adapter.ChaoSongAdapter;
import com.feirui.feiyunbangong.adapter.ChaoSongMeAdapter;
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


/**
 * Created by 邢悦 on 2017/6/22.
 */

public class CaoZuoJiLuActivity extends BaseActivity{
    private static final int ON_REFRESH = 1;  //刷新常量
    private static final int ON_LOAD_MORE = 2;  //加载常量
    private Spinner tiSpinner;
    private Spinner shenSpinner,chaoSpinner;
    private ArrayAdapter<String> mAdapter;
    private String[] leixing = new String[]{"选择审批类型", "请假", "报销", "外出", "付款",
            "采购", "其他"};

    private RadioButton mTiJiao;
    private RadioButton mShenPi;
    private RadioGroup mRgTools;

    private MyShenPiAdapter adapter;
    private CaoZuoJiLuAdapter tiAdapter;
    private ChaoSongAdapter chaoAdapter;

    private PullListView mPullListView;
    private PullToRefreshLayout mPullToRefreshLayout;

    private String tijiaourl;  //我提交的地址
    private String shenpiurl; //我审批过的地址
    private String chaosongurl;

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
        chaoSpinner = (Spinner) findViewById(R.id.spinner);
        tiSpinner.setVisibility(View.VISIBLE);

        tijiaourl = UrlTools.url + UrlTools.APP_MY_APPROVAL;
        shenpiurl = UrlTools.url + UrlTools.APPROVAL_MY_APPROVAL_OLD;
        chaosongurl = UrlTools.url + UrlTools.APPROVAL_MY_APPROVAL_SEND;
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
                    case R.id.rb_chao_song:
                        chaosongSpinner();
                        break;
                }
            }
        });

    }

    public void tiJiaoSpinner(){
        shenSpinner.setVisibility(View.GONE);
        chaoSpinner.setVisibility(View.GONE);
        tiSpinner.setVisibility(View.VISIBLE);
        mAdapter = new ArrayAdapter<String>(CaoZuoJiLuActivity.this, R.layout.sp_item, R.id.tv, leixing);
        tiSpinner.setAdapter(mAdapter);
        tiAdapter = new CaoZuoJiLuAdapter(CaoZuoJiLuActivity.this, new ArrayList<HashMap<String, Object>>());
        mPullListView.setAdapter(tiAdapter);

        //查看每一项的详细内容
        tiAdapter.setOnChakanClickListener(new CaoZuoJiLuAdapter.OnChakanClickListener() {
            @Override
            public void onChakanClick(HashMap<String, Object> data, int position) {
                String approval_type = (String) data.get("approval_type");
                switch (approval_type){
                    case "请假":
                        Intent qing = new Intent(CaoZuoJiLuActivity.this,MyShenPiQingJaDetailActivity.class);
                        qing.putExtra("data",data);
                        startActivity(qing);
                        break;
                    case "报销":
                        Intent baoxiao = new Intent(CaoZuoJiLuActivity.this,MyShenPiBaoXiaoDetailActivity.class);
                        baoxiao.putExtra("data",data);
                        startActivity(baoxiao);
                        break;
                    case "外出":
                        Intent waichu = new Intent(CaoZuoJiLuActivity.this,MyWaiChuDetailActivity.class);
                        waichu.putExtra("data",data);
                        startActivity(waichu);
                        break;
                    case "付款":
                        Intent fukuan = new Intent(CaoZuoJiLuActivity.this,MyFuKuanDetailActivity.class);
                        fukuan.putExtra("data",data);
                        startActivity(fukuan);
                        break;
                    case "采购":
                        Intent caigou = new Intent(CaoZuoJiLuActivity.this,MyCaiGouDetailActivity.class);
                        caigou.putExtra("data",data);
                        startActivity(caigou);
                        break;
                    case "其他":
                        Intent qita = new Intent(CaoZuoJiLuActivity.this,MyQiTaDetailActivity.class);
                        qita.putExtra("data",data);
                        startActivity(qita);
                        break;


                }
            }
        });


        //监听选择类型
        tiSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadData(1, ON_REFRESH, tijiaourl);
                Log.e("tag", "自动加载数据----"+mPullListView.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tiCaoZuo(tijiaourl);
    }
      public void shenPiSpinner(){
          chaoSpinner.setVisibility(View.GONE);
          tiSpinner.setVisibility(View.GONE);
          shenSpinner.setVisibility(View.VISIBLE);
          mAdapter = new ArrayAdapter<String>(CaoZuoJiLuActivity.this, R.layout.sp_item, R.id.tv, leixing);
          shenSpinner.setAdapter(mAdapter);

          adapter = new MyShenPiAdapter(CaoZuoJiLuActivity.this, new ArrayList<HashMap<String, Object>>());
          mPullListView.setAdapter(adapter);

          //查看每一项的详细内容
          adapter.setOnChakanClickListener(new MyShenPiAdapter.OnChakanClickListener() {
              @Override
              public void onChakanClick(HashMap<String, Object> data, int position) {
                  String approval_type = (String) data.get("approval_type");
                  switch (approval_type){
                      case "请假":
                          Intent qingjia = new Intent(CaoZuoJiLuActivity.this,MyShenPiQingJaDetailActivity.class);
                          qingjia.putExtra("data",data);
                          startActivity(qingjia);
                          break;
                      case "报销":
                          Intent baoxiao = new Intent(CaoZuoJiLuActivity.this,MyShenPiBaoXiaoDetailActivity.class);
                          baoxiao.putExtra("data",data);
                          startActivity(baoxiao);
                          break;
                      case "外出":
                          Intent waichu = new Intent(CaoZuoJiLuActivity.this,MyWaiChuDetailActivity.class);
                          waichu.putExtra("data",data);
                          startActivity(waichu);
                          break;
                      case "付款":
                          Intent fukuan = new Intent(CaoZuoJiLuActivity.this,MyFuKuanDetailActivity.class);
                          fukuan.putExtra("data",data);
                          startActivity(fukuan);
                          break;
                      case "采购":
                          Intent caigou = new Intent(CaoZuoJiLuActivity.this,MyCaiGouDetailActivity.class);
                          caigou.putExtra("data",data);
                          startActivity(caigou);
                          break;
                      case "其他":
                          Intent qita = new Intent(CaoZuoJiLuActivity.this,MyQiTaDetailActivity.class);
                          qita.putExtra("data",data);
                          startActivity(qita);
                          break;


                  }
              }
          });

          //监听选择类型
          shenSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  loadData(1, ON_REFRESH, shenpiurl);
                  Log.e("tag", "自动加载数据----"+mPullListView.toString());
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
          });
          tiCaoZuo(shenpiurl);
      }

    public void chaosongSpinner(){
        chaoSpinner.setVisibility(View.VISIBLE);
        tiSpinner.setVisibility(View.GONE);
        shenSpinner.setVisibility(View.GONE);
        mAdapter = new ArrayAdapter<String>(CaoZuoJiLuActivity.this, R.layout.sp_item, R.id.tv, leixing);
        chaoSpinner.setAdapter(mAdapter);

        chaoAdapter = new ChaoSongAdapter(CaoZuoJiLuActivity.this, new ArrayList<HashMap<String, Object>>());
        mPullListView.setAdapter(chaoAdapter);

        //查看每一项的详细内容
        chaoAdapter.setOnChakanClickListener(new ChaoSongAdapter.OnChakanClickListener() {
            @Override
            public void onChakanClick(HashMap<String, Object> data, int position) {
                String approval_type = (String) data.get("approval_type");
                switch (approval_type){
                    case "请假":
                        Intent qingjia = new Intent(CaoZuoJiLuActivity.this,MyShenPiQingJaDetailActivity.class);
                        qingjia.putExtra("data",data);
                        startActivity(qingjia);
                        break;
                    case "报销":
                        Intent baoxiao = new Intent(CaoZuoJiLuActivity.this,MyShenPiBaoXiaoDetailActivity.class);
                        baoxiao.putExtra("data",data);
                        startActivity(baoxiao);
                        break;
                    case "外出":
                        Intent waichu = new Intent(CaoZuoJiLuActivity.this,MyWaiChuDetailActivity.class);
                        waichu.putExtra("data",data);
                        startActivity(waichu);
                        break;
                    case "付款":
                        Intent fukuan = new Intent(CaoZuoJiLuActivity.this,MyFuKuanDetailActivity.class);
                        fukuan.putExtra("data",data);
                        startActivity(fukuan);
                        break;
                    case "采购":
                        Intent caigou = new Intent(CaoZuoJiLuActivity.this,MyCaiGouDetailActivity.class);
                        caigou.putExtra("data",data);
                        startActivity(caigou);
                        break;
                    case "其他":
                        Intent qita = new Intent(CaoZuoJiLuActivity.this,MyQiTaDetailActivity.class);
                        qita.putExtra("data",data);
                        startActivity(qita);
                        break;


                }
            }
        });

        //监听选择类型
        chaoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadData(1, ON_REFRESH, chaosongurl);
                Log.e("tag", "自动加载数据----"+mPullListView.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tiCaoZuo(chaosongurl);
    }

    public void tiCaoZuo(final String url){
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                currentPage = 1;
                loadData(currentPage, ON_REFRESH,url);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                currentPage += 1;
                loadData(currentPage, ON_LOAD_MORE,url);
            }
        });

    }


    private int currentPage = 1;

    public void loadData(int page, final int onRefreshOrLoadMore,String url) {

        RequestParams params = new RequestParams();

        if (url == tijiaourl){
            if (!"选择审批类型".equals(tiSpinner.getSelectedItem().toString())) {
                Log.e("tag","leixing------"+tiSpinner.getSelectedItem().toString());
                switch (tiSpinner.getSelectedItem().toString()){
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
            AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    super.onSuccess(statusCode, headers, responseBody);

                    JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                    Log.e("操作记录-我提交的", "jsonBean: "+jsonBean.toString());
                    if (jsonBean.getCode().equals("200")) {
                        if (jsonBean.getInfor().size() == 0){
                            T.showShort(CaoZuoJiLuActivity.this,"暂无该数据~");
                            return;
                        }

                        if (onRefreshOrLoadMore == ON_REFRESH) {
                            tiAdapter.addAll(jsonBean.getInfor());
                            mPullToRefreshLayout.refreshFinish(true);
                        } else {
                            tiAdapter.add(jsonBean.getInfor());
                            mPullToRefreshLayout.loadMoreFinish(true);
                        }
                    }else if(jsonBean.getCode().equals("-400")){
                        T.showShort(CaoZuoJiLuActivity.this, jsonBean.getMsg());
                    } else {
                        tiAdapter.addAll(jsonBean.getInfor());
                        mPullToRefreshLayout.loadMoreFinish(true);
                        mPullToRefreshLayout.refreshFinish(true);
                    }
                }
            });
        } else if (url == shenpiurl){
            if (!"选择审批类型".equals(shenSpinner.getSelectedItem().toString())) {
                Log.e("操作记录-我审批的","类型"+shenSpinner.getSelectedItem().toString());
                switch (shenSpinner.getSelectedItem().toString()){
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
            Log.e("操作记录-我审批的", "params: "+shenpiurl+params.toString() );
            AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    super.onSuccess(statusCode, headers, responseBody);

                    JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                    Log.e("操作记录-我审批的", "jsonBean: "+jsonBean.toString());

                        if (jsonBean.getCode().equals("200")) {
                            if (jsonBean.getInfor().size() == 0){
                                T.showShort(CaoZuoJiLuActivity.this,"暂无该数据~");
                                return;
                            }

                            if (onRefreshOrLoadMore == ON_REFRESH) {

                                adapter.addAll(jsonBean.getInfor());
                                mPullToRefreshLayout.refreshFinish(true);
                            } else {
                                adapter.add(jsonBean.getInfor());
                                mPullToRefreshLayout.loadMoreFinish(true);
                            }
                        } else if(jsonBean.getCode().equals("-400")){
                            T.showShort(CaoZuoJiLuActivity.this, jsonBean.getMsg());
                        }
                        else {
                            adapter.addAll(jsonBean.getInfor());
                            mPullToRefreshLayout.loadMoreFinish(true);
                            mPullToRefreshLayout.refreshFinish(true);
                        }


                }
            });
        }  else if (url == chaosongurl){
        if (!"选择审批类型".equals(chaoSpinner.getSelectedItem().toString())) {
            Log.e("操作记录-抄送我的","类型"+chaoSpinner.getSelectedItem().toString());
            switch (chaoSpinner.getSelectedItem().toString()){
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
        }else {
            params.put("type", "");
        }

        params.put("current_page", page + "");
        params.put("pagesize", "15");
        Log.e("操作记录-抄送我的", "params: "+chaosongurl+params.toString() );
        AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);

                JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                Log.e("操作记录-抄送我的", "jsonBean: "+jsonBean.toString());

                if (jsonBean.getCode().equals("200")) {
                    if (jsonBean.getInfor().size() == 0){
                        T.showShort(CaoZuoJiLuActivity.this,"暂无该数据~");
                        return;
                    }

                    if (onRefreshOrLoadMore == ON_REFRESH) {

                        chaoAdapter.addAll(jsonBean.getInfor());
                        mPullToRefreshLayout.refreshFinish(true);
                    } else {
                        chaoAdapter.add(jsonBean.getInfor());
                        mPullToRefreshLayout.loadMoreFinish(true);
                    }
                } else if(jsonBean.getCode().equals("-400")){
                    T.showShort(CaoZuoJiLuActivity.this, jsonBean.getMsg());
                }
                else {
                    chaoAdapter.addAll(jsonBean.getInfor());
                    mPullToRefreshLayout.loadMoreFinish(true);
                    mPullToRefreshLayout.refreshFinish(true);
                }


            }
        });
    }
    }

}
