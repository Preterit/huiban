package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.ReceiveBean;
import com.feirui.feiyunbangong.entity.TaskListEntity.InfoBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class TaskInfoActivity extends BaseActivity {

    private InfoBean taskinfo;
    private TextView huiyiname;        //会议的名称
    private TextView releasepeopleName; //发布任务的人
    private TextView releaseTime;       //发布任务的时间
    private TextView TaskCountInfo;     //任务的详细内容

    private Button acceptbutton;     //接受按钮
    private Button cancelButton;     //取消按钮
    private ImageView acceptoverImage;  //接收完任务的盖章

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        //接受任务单界面传来的数据

        Intent intent = getIntent();
        taskinfo = (InfoBean) intent.getSerializableExtra("Task");
        position = intent.getIntExtra("Position", -1);
        //给控件初始化
        initViews();
        //给数据赋值
        setTaskInfo();
      acceptStatus();  //判断接收状态的方法

    }

    private void acceptStatus() {
 if(taskinfo.getType()==1){
     acceptbutton.setVisibility(View.GONE);
     cancelButton.setVisibility(View.GONE);
     acceptoverImage.setVisibility(View.VISIBLE);
 }else{
     acceptbutton.setVisibility(View.VISIBLE);
     cancelButton.setVisibility(View.VISIBLE);

 }


    }

    private void initViews() {
        huiyiname = (TextView) findViewById(R.id.huiyiname);
        releasepeopleName = (TextView) findViewById(R.id.releaseName);
        releaseTime = (TextView) findViewById(R.id.releaseTime);
        TaskCountInfo = (TextView) findViewById(R.id.TaskCountInfo);
        acceptbutton = (Button) findViewById(R.id.acceptbutton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        acceptoverImage=(ImageView)findViewById(R.id.acceptoverImage);
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("任务详情");


    }

    private void setTaskInfo() {

        huiyiname.setText(taskinfo.getSubject());
        releasepeopleName.setText(taskinfo.getName());
        releaseTime.setText(taskinfo.getTime());
        TaskCountInfo.setText(taskinfo.getTask_txt());

        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptMethod();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

    }

    private void acceptMethod() {
        {
            String url = UrlTools.pcUrl + UrlTools.TASK_ACCEPT; //接收任务的接口
            final RequestParams requestParams = new RequestParams();
            if (taskinfo != null) {
                requestParams.put("id", taskinfo.getId()+"");

                requestParams.put("button", 1 + "");
            }
            AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    super.onSuccess(statusCode, headers, responseBody);

                    Gson gson = new Gson();
                    Log.e("orz", "onSuccess: " +new String(responseBody));
                    ReceiveBean receive = gson.fromJson(new String(responseBody), ReceiveBean.class);
                    Log.e("TAG","此处的对象是"+receive+ " 状态码是 "+receive.getCode());

                    if (receive.getCode() == 200) {
                        Toast.makeText(TaskInfoActivity.this, receive.getMsg(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("Position", position);
                        setResult(RESULT_OK, intent);

                        acceptoverImage.setVisibility(View.VISIBLE);
                        finish();

                    } else if(receive.getCode() == -400){
                        Toast.makeText(TaskInfoActivity.this,receive.getMsg() , Toast.LENGTH_SHORT).show();


                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    super.onFailure(statusCode, headers, responseBody, error);
                    Log.e("orz", "onFailure: "+"wft" );
                }
            });


        }
    }

}
