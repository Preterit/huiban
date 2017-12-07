package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.JieDanAdapter;
import com.feirui.feiyunbangong.entity.JieDanRenBean;
import com.feirui.feiyunbangong.entity.RenWuDanBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.TextImageView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

public class MyTaskActivity extends BaseActivity {
    private TextImageView tiv_head;
    private TextView tv_name, tv_zt, tv_task, tv_number,tv_time;
    private ListView lv_jiedanren;
    private String id, staff_name, time, task_txt, task_zt, staff_head;
    TextView  rwd_tv_sj, rwd_tv_wz, rwd_tv_xs, rwd_tv_xz;
    JieDanAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        task_zt = getIntent().getStringExtra("task_zt");
        staff_name = getIntent().getStringExtra("staff_name");
        time = getIntent().getStringExtra("time");
        task_txt = getIntent().getStringExtra("task_txt");
        staff_head = getIntent().getStringExtra("staff_head");
        id = getIntent().getStringExtra("id");
        initView();
        initData();
        initTask();
        initDate();
    }

    private void initTask() {
        RequestParams params2 = new RequestParams();
        String url2 = UrlTools.pcUrl + UrlTools.RENWU_DETAIL;
        params2.put("id", id + "");
//        params2.put("accept_id", accept_id + "");
        AsyncHttpServiceHelper.post(url2, params2, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Gson gson = new Gson();
                RenWuDanBean renwudan = gson.fromJson(new String(responseBody), RenWuDanBean.class);
                if (renwudan.getCode() == 200) {
                    rwd_tv_sj.setText(renwudan.getInfo().get(0).getBegin_time() + "");
                    rwd_tv_wz.setText(renwudan.getInfo().get(0).getAddresslimit() + "");
                    rwd_tv_xs.setText(renwudan.getInfo().get(0).getReward() + "");
                    rwd_tv_xz.setText(renwudan.getInfo().get(0).getNumber() + "");
                } else {
                }
            }
        });
    }
    private void initDate() {
        RequestParams params = new RequestParams();
        String url = UrlTools.pcUrl + UrlTools.RENWU_JDR;
        params.put("id", id + "");
        AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Gson gson = new Gson();
                JieDanRenBean jieDanRenBean = gson.fromJson(new String(responseBody), JieDanRenBean.class);
                int num=0;
                for (int i=0;i<jieDanRenBean.getInfor().size();i++){
                    if (jieDanRenBean.getInfor().get(i).getState().equals("2"));
                    num++;
                    i++;
                }
                tv_number.setText(num+"/"+jieDanRenBean.getInfor().size());
                adapter=new JieDanAdapter(jieDanRenBean.getInfor(),MyTaskActivity.this);
                lv_jiedanren.setAdapter(adapter);
                lv_jiedanren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                Log.e("orz", "onFailure: " + "wft");
            }
        });
    }
    private void initData() {
        RequestParams params2 = new RequestParams();
        String url2 = "http://123.57.45.74/feiybg1/public/index.php/home_api/task/my_task_detaile";
        params2.put("id", id + "");
        AsyncHttpServiceHelper.post(url2, params2, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Gson gson = new Gson();
//                if (jindu.getInfo1() != null) {
//                    adapter = new JinDuAdapter(MyTaskDetailActivity.this, jindu.getInfo1());
//                    lv_jindu.setAdapter(adapter);
//                } else {
//                    lv_jindu.setVisibility(View.GONE);
//                }
            }
        });
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("任务单详情");
        setRightVisibility(false);
        lv_jiedanren= (ListView) findViewById(R.id.lv_jiedanren);
        tiv_head= (TextImageView) findViewById(R.id.tiv_head);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_zt= (TextView) findViewById(R.id.tv_zt);
        tv_task= (TextView) findViewById(R.id.tv_task);
        tv_number= (TextView) findViewById(R.id.tv_number);
        tv_time= (TextView) findViewById(R.id.tv_time);
        ImageLoader.getInstance().displayImage(staff_head, tiv_head, ImageLoaderUtils.getSimpleOptions());
        tv_name.setText(staff_name);
        tv_time.setText(time);
        tv_zt.setText(task_zt);
        tv_task.setText(task_txt);
        rwd_tv_sj = (TextView) findViewById(R.id.rwd_tv_sj);//开始时间
        rwd_tv_wz = (TextView) findViewById(R.id.rwd_tv_wz);//任务位置
        rwd_tv_xs = (TextView) findViewById(R.id.rwd_tv_xs);//任务悬赏
        rwd_tv_xz = (TextView) findViewById(R.id.rwd_tv_xz);//任务限制
    }
}
