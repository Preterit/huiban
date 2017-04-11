package com.feirui.feiyunbangong.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.RecyclerViewTaskAdapter;
import com.feirui.feiyunbangong.entity.TaskListEntity;
import com.feirui.feiyunbangong.entity.TaskListEntity.InfoBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * creator rubing
 */
public class TasksListActivity extends BaseActivity {

    private RecyclerViewTaskAdapter mAdapter;
    private List<InfoBean> mList;
    private RecyclerView recViewTeamList;
    private RecyclerViewTaskAdapter mTaskListAdapter;
    private ImageView rightIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_task_list);

        initView();
        initRv();
        loadData();
        RadioButtonListener();
    }

    private void initView() {
        rightIv = (ImageView) findViewById(R.id.rightIv);

        initTitle();
        setCenterString("任务单");
        setLeftDrawable(R.drawable.arrows_left);
        setRightDrawable(R.drawable.jia);
        recViewTeamList = (RecyclerView) findViewById(R.id.recViewTeamList);
    }

    private void RadioButtonListener() {

        //设置TextView的监听器
        rightIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final CharSequence items[] = {"发布任务", "个人任务详情"};

                AlertDialog alertDialog = new AlertDialog.Builder(TasksListActivity.this).setIcon(R.drawable.selectimage)
                        .setTitle("请选择方式").setItems(items, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        startActivity(new Intent(TasksListActivity.this, ReleaseTask.class));
                                        break;
                                    case 1:
                                        startActivity(new Intent(TasksListActivity.this, WoDeTaskActivity.class));
                                        break;
                                }

                            }
                        }).show();

            }
        });
    }

    private void initRv() {

        mTaskListAdapter = new RecyclerViewTaskAdapter(this, new ArrayList<InfoBean>());
        recViewTeamList
                .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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

                    mTaskListAdapter.setItemClickListener(new RecyclerViewTaskAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, List<InfoBean> mList) {
                            InfoBean data = mList.get(position);
                            Intent intent = new Intent(TasksListActivity.this, TaskInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Task", data);
                            // intent.putExtra("Task",data);
                            bundle.putInt("Position", position);
                            // intent.putExtra("Position",position);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 101);

                            Toast.makeText(TasksListActivity.this, "点击了" + position + "   " + data.getId(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 101) {
           // setResult(RESULT_OK, data);
            Bundle bundle = data.getExtras();
            int position = bundle.getInt("Position");
            Log.i("TAG","传来的下标是"+position);
            mTaskListAdapter.setStatus(position);



        }
    }
}

