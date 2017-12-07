package com.feirui.feiyunbangong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.ReleaseDetailActivity;
import com.feirui.feiyunbangong.adapter.TaskDaiJieDanAdapter;
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
 * 任务单-待接单
 * */
public class TaskJieDanFragment extends BaseFragment implements YRecycleview.OnRefreshAndLoadMoreListener {
    private YRecycleview yRecycleview;
    private TaskDaiJieDanAdapter adapter;
    private boolean isRefreshState = true;//是否刷新
    private JsonBean json;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_dai_jie_dan, container, false);
        initView(v);
        initData();
        return v;
    }

    private void initView(View v) {
        yRecycleview = (YRecycleview) v.findViewById(R.id.yrv_renwu_daijiedan);
        yRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        yRecycleview.setRefreshAndLoadMoreListener(this);
        adapter = new TaskDaiJieDanAdapter(getActivity(), new ArrayList<HashMap<String, Object>>());
        yRecycleview.setAdapter(adapter);
    }

    private void initData() {
        RequestParams params = new RequestParams();
        String url = UrlTools.pcUrl+ UrlTools.RENWU_DJD;
        AsyncHttpServiceHelper.post(url, new AsyncHttpResponseHandler(){
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
        adapter.setOnItemClickListener(new TaskDaiJieDanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e("全部页面的点击时间", "onItemClick: "+ position);
                //Intent intent = new Intent(getActivity(), Release_FanKuiA ctivity.class);
                Intent intent = new Intent(getActivity(), ReleaseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("json", json.getInfor().get(position-1)+"");
                bundle.putString("id", (Integer)json.getInfor().get(position-1).get("id")+"");
                Log.e("全部页面的点击时间", "id: "+(Integer)json.getInfor().get(position-1).get("id") );
                bundle.putString("staff_name", (String) json.getInfor().get(position-1).get("staff_name"));
                bundle.putString("time", (String) json.getInfor().get(position-1).get("release_time"));
                bundle.putString("task_txt", (String) json.getInfor().get(position-1).get("task_txt"));
                bundle.putString("staff_head", "http://123.57.45.74/feiybg1/"+json.getInfor().get(position-1).get("staff_head"));
                Log.e("全部任务", "onItemClick position: "+position );
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
