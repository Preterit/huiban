package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.TeamListAdapter;
import com.feirui.feiyunbangong.entity.TeamList_entity.Infor;
import com.feirui.feiyunbangong.entity.TeamList_entity.Root;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;
/**
 * 选择团队界面
 * */
public class SelectorTeamActivity extends BaseActivity implements OnItemClickListener {
    private TeamListAdapter adapter;
    private ListView teamListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector_team);
        teamListView=(ListView)findViewById(R.id.teamListView);
        //获取团队列表的方法
        getTeamList();
        initViews();


    }

    private void initViews() {
        initTitle();
        setCenterString("选择团队");
        setLeftDrawable(R.drawable.arrows_left);
        setRightVisibility(false);


    }

    private void getTeamList() {
        //发送请求
     String url= UrlTools.pcUrl+UrlTools.TASK_GETTEAN;  //请求的地址
        RequestParams requestParams = new RequestParams();
        //此请求是无参的
        AsyncHttpServiceHelper.post(url,requestParams,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Gson gson=new Gson();
                Root root = gson.fromJson(new String(responseBody), Root.class);
                List<Infor> teaminfo = root.getInfor();
                if(teaminfo!=null) {
                    adapter = new TeamListAdapter(SelectorTeamActivity.this, teaminfo);

                    teamListView.setAdapter(adapter);
                }else{
                    Toast.makeText(SelectorTeamActivity.this,"加载中",Toast.LENGTH_SHORT).show();
                }
                }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent();
        intent.putExtra("Team", (Infor)adapter.getItem(position));
        setResult(103,intent);
        finish();
    }


}
