package com.feirui.feiyunbangong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.Release_jieshouAdapter;
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
 * lice
 * 接收任务页面
 */
public class Release_JieShouFragment extends Fragment implements YRecycleview.OnRefreshAndLoadMoreListener{
    private YRecycleview yRecycleview;
    private Release_jieshouAdapter adapter;
    private boolean isRefreshState = true;//是否刷新
    private JsonBean json;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_jie_shou_release, container, false);
        initView(v);
        initData();
        return v;
    }

    private void initData() {
        RequestParams params = new RequestParams();
        String url= UrlTools.pcUrl+UrlTools.TASK_ACCEPT_TASK_LIST;

        AsyncHttpServiceHelper.post(url,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                json = JsonUtils.getMessage(new String(responseBody));
                Log.e("接收任务", "json: " + json.toString());
                //yRecycleview.setReFreshComplete();
                //setAdapter();
                if(json.getInfor()==null){
                    return;
                }
                adapter.addAll(json.getInfor());

            }
        });
    }

    private void initView(View v) {
        yRecycleview = (YRecycleview) v.findViewById(R.id.yrv_jieshou);
        yRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        yRecycleview.setRefreshAndLoadMoreListener(this);
        adapter = new Release_jieshouAdapter(getActivity(), new ArrayList<HashMap<String, Object>>());
        yRecycleview.setAdapter(adapter);
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
