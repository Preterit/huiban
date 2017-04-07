package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.TaskListAdapter;
import com.feirui.feiyunbangong.entity.TaskListEntity;
import com.feirui.feiyunbangong.entity.TaskListEntity.InfoBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;

/**
 * creator rubing
 */
public class TasksListActivity extends BaseActivity {


  private RecyclerView recViewTeamList;
  private TaskListAdapter mTaskListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_team_task_list);

    initView();
    initRv();
    loadData();
  }

  private void initView() {
    initTitle();
    setCenterString("任务列表");
    setLeftDrawable(R.drawable.arrows_left);
    recViewTeamList = (RecyclerView) findViewById(R.id.recViewTeamList);
  }


  private void initRv() {
    mTaskListAdapter = new TaskListAdapter(new ArrayList<InfoBean>());

    recViewTeamList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    recViewTeamList.setAdapter(mTaskListAdapter);
  }

  private void loadData() {
    String url = UrlTools.pcUrl + UrlTools.TEAM_TASK_LIST;
    final RequestParams requestParams = new RequestParams();
    AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        super.onSuccess(statusCode, headers, responseBody);

        Gson gson = new Gson();
        TaskListEntity taskListEntity = gson
            .fromJson(new String(responseBody), TaskListEntity.class);
        if (taskListEntity.getCode() == 200) {
          mTaskListAdapter.setData(taskListEntity.getInfo());
        }
      }
    });
  }
}
