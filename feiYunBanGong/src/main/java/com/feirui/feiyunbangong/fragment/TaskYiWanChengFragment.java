package com.feirui.feiyunbangong.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.MyTaskDetailActivity;
import com.feirui.feiyunbangong.adapter.TaskWanChengAdapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.YRecycleview;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 任务单-已完成
 */
public class TaskYiWanChengFragment extends BaseFragment implements YRecycleview.OnRefreshAndLoadMoreListener {

    private YRecycleview yRecycleview;
    private TaskWanChengAdapter adapter;
    private boolean isRefreshState = true;//是否刷新
    private JsonBean json;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_yi_wan_cheng, container, false);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        yRecycleview = (YRecycleview) v.findViewById(R.id.yrv_renwu_wancheng);
        yRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        yRecycleview.setRefreshAndLoadMoreListener(this);
        adapter = new TaskWanChengAdapter(getActivity(), new ArrayList<HashMap<String, Object>>());
        yRecycleview.setAdapter(adapter);
    }

    private void initData() {
        RequestParams params = new RequestParams();
        String url = UrlTools.pcUrl+ UrlTools.RENWU_YWC;
        AsyncHttpServiceHelper.post(url,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                json = JsonUtils.getMessage(new String(responseBody));
                if(json.getInfor()==null){
                    return;
                }
                adapter.addAll(json.getInfor());
            }
        });
        adapter.setOnItemClickListener(new TaskWanChengAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e("点击位置==============", "onItemClick: "+ position);
                //Intent intent = new Intent(getActivity(), Release_FanKuiA ctivity.class);
                Intent intent = new Intent(getActivity(), MyTaskDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("json", json.getInfor().get(position-1)+"");
                bundle.putString("id", (Integer)json.getInfor().get(position-1).get("id")+"");
                bundle.putString("state", json.getInfor().get(position - 1).get("state")+ "");
                bundle.putString("staff_name", (String) json.getInfor().get(position-1).get("staff_name"));
                bundle.putString("accept_id", (Integer) json.getInfor().get(position-1).get("accept_id")+"");
                bundle.putString("release_time", (String) json.getInfor().get(position-1).get("release_time"));
                bundle.putString("task_txt", (String) json.getInfor().get(position-1).get("task_txt"));
                bundle.putString("task_zt", (String) json.getInfor().get(position-1).get("subject"));
                bundle.putString("staff_head", "http://123.57.45.74/feiybg1/"+json.getInfor().get(position-1).get("staff_head"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        isRefreshState = true;
        yRecycleview.setReFreshComplete();
        initData();
    }

    @Override
    public void onLoadMore() {
        yRecycleview.setNoMoreData(true);
        isRefreshState = false;
        initData();
    }

}
