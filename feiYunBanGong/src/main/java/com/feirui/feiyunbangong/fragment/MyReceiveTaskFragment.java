package com.feirui.feiyunbangong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.MyReceiveTaskAdapter;
import com.feirui.feiyunbangong.entity.MyTaskEntity;
import com.feirui.feiyunbangong.entity.MyTaskEntity.MyTaskInfo;
import com.feirui.feiyunbangong.entity.MyTaskEntity.MyTaskRoot;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

/***
 * 自己接收的任务的列表
 * */
public class MyReceiveTaskFragment extends Fragment {

    private ListView receiveTaskList;
    private MyReceiveTaskAdapter adapter;

    public MyReceiveTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_my_receive_task, container, false);
        receiveTaskList=(ListView)v.findViewById(R.id.receiveTaskList);
       setReceiveTaskList(); //自己接收的任务列表

        return v;

    }

    private void setReceiveTaskList() {
        String url= UrlTools.pcUrl+UrlTools.TASK_ACCEPT_TASK_LIST;
       final RequestParams params=new RequestParams();
        AsyncHttpServiceHelper.post(url,params,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Gson gson=new Gson();
                MyTaskRoot root = gson.fromJson(new String(responseBody), MyTaskEntity.MyTaskRoot.class);
                List<MyTaskInfo> taskinfo = root.getInfo();
                adapter=new MyReceiveTaskAdapter(getActivity(),taskinfo);
                receiveTaskList.setAdapter(adapter);
            }
        });


    }


}
