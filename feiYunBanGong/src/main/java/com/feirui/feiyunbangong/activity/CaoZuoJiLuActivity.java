package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ShenPiAdapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.AutoListView;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xw.repo.refresh.PullListView;
import com.xw.repo.refresh.PullToRefreshLayout;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 审批-操作记录
 *
 * @author Lesgod
 */
public class CaoZuoJiLuActivity extends BaseActivity implements OnItemSelectedListener {
    @PView(click = "onClick")
    private Spinner sp_daishenpi; //审批类型的下拉框
    private ArrayAdapter<String> adt;
    private String[] leixing = new String[]{"选择审批类型", "请假", "报销", "外出", "付款",
            "采购", "其他"};
    @PView
    private AutoListView haveget;
    // adapter加载的数据源
    private JsonBean json;
    // list的适配器
    private ShenPiAdapter adapter;



    private PullToRefreshLayout mPullToRefreshLayout;
    private PullListView mListView;
    private int ON_REFRESH = 1;
    private int ON_LOAD_MORE = 2;
    private int currentPage = 1; // 当前数据页码

    //异步自动加载数据
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.REFRESH:
                    break;
                case Constant.LOAD:
                    break;
                case Constant.PUBLIC_TYPE_ONE:
                    break;
            }
        }
    };

    /**
     * activity出现之后自动请求数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadData(currentPage,ON_LOAD_MORE);
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caozuojilu);
        initView();

    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("操作记录");
        setRightVisibility(false);

        adt = new ArrayAdapter<>(this, R.layout.sp_item, R.id.tv, leixing);
        sp_daishenpi.setAdapter(adt);


        mListView = (PullListView) findViewById(R.id.caozuo_jilu_list);
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.caozuo_jilu_pullRefresh);


        //监听选择类型
        sp_daishenpi.setOnItemSelectedListener(this);
        adapter = new ShenPiAdapter(CaoZuoJiLuActivity.this, new ArrayList<HashMap<String, Object>>());
        mListView.setAdapter(adapter);

        adapter.setOnChakanClickListener(new ShenPiAdapter.OnChakanClickListener() {
            @Override
            public void onChakanClick(HashMap<String, Object> data, int position) {
                String approval_type = (String) data.get("approval_type");
                switch (approval_type) {
                    case "请假":
                        Intent intent = new Intent(CaoZuoJiLuActivity.this, ShenPiQingJaDetailActivity.class);
                        intent.putExtra("data", data);
                        startActivity(intent);
                        break;
                    case "报销":
                        Intent baoxiaoIntent = new Intent(CaoZuoJiLuActivity.this, ShenpiBaoxiaoDetailActivity.class);
                        baoxiaoIntent.putExtra("data", data);
                        startActivity(baoxiaoIntent);
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



    /**
     * 加载数据、刷新数据
     */
    public void loadData(int page, final int onRefreshOrLoadMore) {
        RequestParams params = new RequestParams();
        String url = UrlTools.url + UrlTools.APPROVAL_APPROVAL_ALL;

        if (!"选择审批类型".equals(sp_daishenpi.getSelectedItem().toString())) {
            params.put("type", sp_daishenpi.getSelectedItem().toString());
        }
        params.put("current_page", page + "");
        params.put("pagesize", "15");
        AsyncHttpServiceHelper.post(url, params,    new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);

                JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                if (jsonBean.getCode().equals("200")) {

                    if (onRefreshOrLoadMore == ON_REFRESH) {

                        adapter.addAll(jsonBean.getInfor());
                        mPullToRefreshLayout.refreshFinish(true);
                    } else {
                        adapter.add(jsonBean.getInfor());
                        mPullToRefreshLayout.loadMoreFinish(true);
                    }
                } else {
                    mPullToRefreshLayout.loadMoreFinish(true);
                    mPullToRefreshLayout.refreshFinish(true);
                }
            }
        });
    }

    //选中每一个类型的加载内容
    @Override
    public void onItemSelected(AdapterView<?> parent, View iew, int position, long id) {
         Object itemAtPosition = parent.getItemAtPosition(position);
         String type =(String) itemAtPosition;
         loadData(1,ON_REFRESH);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        loadData(1,ON_LOAD_MORE);
    }
}


