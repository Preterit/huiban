package com.feirui.feiyunbangong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.JinDuAdapter;
import com.feirui.feiyunbangong.entity.JinDuBean;
import com.feirui.feiyunbangong.entity.RenWuDanBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

public class ChaKanJinDuActivity extends BaseActivity implements View.OnClickListener {
    private String staff_name, time, task_txt, task_zt, staff_head, id, accept_id, state,xuanshang,jiedanren_name,jiedanren_head;
    private TextImageView iv_head;
    private TextView tv_name, tv_time, tv_task, tv_zt, rwd_tv_sj, rwd_tv_wz, rwd_tv_xs, rwd_tv_xz, tv_jieshu,tv_money,tv_jieshu_hui;
    private Button btn_querenzhifu;
    private ImageView iv_complete;
    private LinearLayout ll_zhifu;
    String address = "";
    private ListView lv_jindu;
    private JinDuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cha_kan_jin_du);
        task_zt = getIntent().getStringExtra("task_zt");
        staff_name = getIntent().getStringExtra("staff_name");
        time = getIntent().getStringExtra("release_time");
        task_txt = getIntent().getStringExtra("task_txt");
        staff_head = getIntent().getStringExtra("staff_head");
        id = getIntent().getStringExtra("id");
        state = getIntent().getStringExtra("state");
        accept_id = getIntent().getStringExtra("accept_id");
        jiedanren_name=getIntent().getStringExtra("jiedanren_name");
        jiedanren_head=getIntent().getStringExtra("jiedanren_head");
        initView();
        initTask();
        initData();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("任务单详情");
        setRightVisibility(true);
        lv_jindu = (ListView) findViewById(R.id.lv_jindu);
        iv_head = (TextImageView) findViewById(R.id.tiv_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_task = (TextView) findViewById(R.id.tv_task);
        tv_zt = (TextView) findViewById(R.id.tv_zt);
        tv_zt.setText(task_zt);
        tv_name.setText(staff_name);
        tv_time.setText(time);
        tv_task.setText(task_txt);
        tv_jieshu = (TextView) findViewById(R.id.tv_jieshu);
        tv_jieshu.setOnClickListener(this);
        tv_jieshu_hui= (TextView) findViewById(R.id.tv_jieshu_hui);
        ll_zhifu= (LinearLayout) findViewById(R.id.ll_zhifu);
        tv_money= (TextView) findViewById(R.id.tv_money);
        btn_querenzhifu= (Button) findViewById(R.id.btn_querenzhifu);
        btn_querenzhifu.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(staff_head, iv_head, ImageLoaderUtils.getSimpleOptions());
        rwd_tv_sj = (TextView) findViewById(R.id.rwd_tv_sj);
        rwd_tv_wz = (TextView) findViewById(R.id.rwd_tv_wz);
        rwd_tv_xs = (TextView) findViewById(R.id.rwd_tv_xs);
        rwd_tv_xz = (TextView) findViewById(R.id.rwd_tv_xz);
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
                    xuanshang=renwudan.getInfo().get(0).getReward() + "";
                    if ("".equals(renwudan.getInfo().get(0).getReward())){
                        tv_jieshu.setVisibility(View.GONE);
                        tv_jieshu_hui.setVisibility(View.VISIBLE);
                    }else {
                        tv_jieshu.setVisibility(View.VISIBLE);
                        tv_jieshu_hui.setVisibility(View.GONE);
                    }
                } else {
                }
            }
        });
    }

    private void initData() {
        RequestParams params2 = new RequestParams();
        String url2 = "http://123.57.45.74/feiybg1/public/index.php/home_api/task/Single_details";
        params2.put("id", id + "");
        params2.put("accept_id", accept_id + "");
        AsyncHttpServiceHelper.post(url2, params2, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Gson gson = new Gson();
                JinDuBean jindu = gson.fromJson(new String(responseBody), JinDuBean.class);
                if (jindu.getInfo1() != null) {
                    adapter = new JinDuAdapter(ChaKanJinDuActivity.this, jindu.getInfo1());
                    lv_jindu.setAdapter(adapter);
                    Utils.reMesureListViewHeight(lv_jindu);
                } else {
                    lv_jindu.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_jieshu:
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChaKanJinDuActivity.this);
                dialog.setTitle("支付报酬");
                dialog.setMessage(jiedanren_name+"已出色完成任务，是否进行支付报酬？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_jieshu.setVisibility(View.GONE);
                        ll_zhifu.setVisibility(View.VISIBLE);
                        tv_money.setText(xuanshang+"   元");
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.btn_querenzhifu:
                Intent intent = new Intent(ChaKanJinDuActivity.this, ZhiFuActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("task_id", id);
                bundle.putString("accept_id",accept_id );
                bundle.putString("xuanshang", xuanshang);
                bundle.putString("task_zt", task_zt);
                bundle.putString("jiedanren_head", jiedanren_head);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
