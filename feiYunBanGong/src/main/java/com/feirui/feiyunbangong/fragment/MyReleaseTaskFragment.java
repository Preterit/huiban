package com.feirui.feiyunbangong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.MyTaskActivity;
import com.feirui.feiyunbangong.adapter.MyReleaseTaskAdapter;
import com.feirui.feiyunbangong.entity.MyTaskReleEntity;
import com.feirui.feiyunbangong.entity.MyTaskReleEntity.ReleaseInfo;
import com.feirui.feiyunbangong.entity.MyTaskReleEntity.Root;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;


/**
 * 自己发布的任务列表
 * */
public class MyReleaseTaskFragment extends Fragment {

    private ListView  releaseTaskList;

    public MyReleaseTaskFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_my_release_task, container, false);
        releaseTaskList=(ListView)v.findViewById(R.id.releaseTaskList);

        setMyTaskList(); //获取自己发布的任务的方法
        return v;
    }
    private void setMyTaskList() {
        String url= UrlTools.pcUrl+UrlTools.TASK_MY_TASK_LIST;
        final RequestParams requestParams = new RequestParams();
        AsyncHttpServiceHelper.post(url,requestParams,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);

                Gson gson=new Gson();
                final MyTaskReleEntity.Root root = gson.fromJson(new String(responseBody), Root.class);
                if (root.getCode()==-400){
                    Toast.makeText(getActivity(),"没有发布任务",Toast.LENGTH_SHORT).show();
                }else if (root.getCode()==200){
                    List<ReleaseInfo> info = root.getInfo();
                    MyReleaseTaskAdapter adapter = new MyReleaseTaskAdapter(getActivity(),info);
                    releaseTaskList.setAdapter(adapter);
                    releaseTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Intent intent = new Intent(getActivity(), Release_FanKuiA ctivity.class);
                            Intent intent = new Intent(getActivity(), MyTaskActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("json", root.getInfo().get(position)+"");
                            bundle.putString("id", root.getInfo().get(position).getId()+"");
//                            bundle.putString("staff_name", (String) root.getInfo().get(position).getName());
                            bundle.putString("time", (String) root.getInfo().get(position).getTime());
                            bundle.putString("task_txt", (String) root.getInfo().get(position).getTask_txt());
                            bundle.putString("task_zt", (String) root.getInfo().get(position).getSubject());
//                            bundle.putString("staff_head", "http://123.57.45.74/feiybg1/"+root.getInfo().get(position).getStaff_head());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
            }
        });

    }


}
