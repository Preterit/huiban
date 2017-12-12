package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.JieDanRenAdapter;
import com.feirui.feiyunbangong.entity.JieDanRenBean;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.RenWuDanBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.List;

import static com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper.post;

/**
 * 新的任务单详情界面
 * lice
 */
public class ReleaseDetailActivity extends BaseActivity implements View.OnClickListener {
    public String staff_name, release_time, task_txt, staff_head, task_zt;
    String id,accept_id;
    ImageView rwd_im_tx;
    TextView rwd_tv_mz, rwd_tv_rq, rwd_tv_zt, rwd_tv_xq, rwd_tv_sj, rwd_tv_wz, rwd_tv_xs, rwd_tv_xz;
    RecyclerView rwd_rec_jdr;
    Button rwd_btn_qrjd;
    private JieDanRenAdapter adapter;
    private List<JieDanRenBean.InforBean> infor;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                setView();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_detail);
        task_zt = getIntent().getStringExtra("task_zt");
        staff_name = getIntent().getStringExtra("staff_name");
        release_time = getIntent().getStringExtra("release_time");
        task_txt = getIntent().getStringExtra("task_txt");
        staff_head = getIntent().getStringExtra("staff_head");
        id = getIntent().getStringExtra("id");
        accept_id = getIntent().getStringExtra("accept_id");
        initView();
        initDate();
        initData();
    }
    private void initData() {
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
                if (jieDanRenBean.getCode() == 200) {
                    infor = jieDanRenBean.getInfor();
                    handler.sendEmptyMessage(0);
                } else {
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                Log.e("orz", "onFailure: " + "wft");
            }
        });
    }
    private void setView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rwd_rec_jdr.setLayoutManager(linearLayoutManager);
        adapter = new JieDanRenAdapter(ReleaseDetailActivity.this, infor);
        rwd_rec_jdr.setAdapter(adapter);

    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("任务单详情");
        setRightVisibility(false);

        rwd_im_tx = (ImageView) findViewById(R.id.rwd_im_tx);//发布人头像
        rwd_tv_mz = (TextView) findViewById(R.id.rwd_tv_mz);//发布人名字
        rwd_tv_sj = (TextView) findViewById(R.id.rwd_tv_sj);//开始时间
        rwd_tv_rq = (TextView) findViewById(R.id.rwd_tv_rq);//任务日期
        rwd_tv_xq = (TextView) findViewById(R.id.rwd_tv_xq);//任务详情
        rwd_tv_zt = (TextView) findViewById(R.id.rwd_tv_zt);//任务主题
        rwd_tv_wz = (TextView) findViewById(R.id.rwd_tv_wz);//任务位置
        rwd_tv_xs = (TextView) findViewById(R.id.rwd_tv_xs);//任务悬赏
        rwd_tv_xz = (TextView) findViewById(R.id.rwd_tv_xz);//任务限制
        rwd_btn_qrjd = (Button) findViewById(R.id.rwd_btn_qrjd);
        rwd_btn_qrjd.setOnClickListener(this);

        //得到控件
        rwd_rec_jdr = (RecyclerView) findViewById(R.id.rwd_rec_jdr);

        rwd_tv_mz.setText(staff_name);
        rwd_tv_rq.setText(release_time);

        rwd_tv_xq.setText(task_txt);
        rwd_tv_zt.setText(task_zt);
        //ImageLoader.getInstance().displayImage("http://123.57.45.74/feiybg/"+staff_head, rwd_im_tx);
        ImageLoader.getInstance().displayImage(staff_head, rwd_im_tx, ImageLoaderUtils.getSimpleOptions());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rwd_btn_qrjd://确认接单
                RequestParams params2 = new RequestParams();
                String url2 = UrlTools.pcUrl + UrlTools.RENWU_QRJD;
                //String url2 = "http://123.57.45.74/feiybg1/public/index.php/home_api/Task/task_accept";
                params2.put("id", id);
                params2.put("button", "1");
                Log.e("全部任务获取成功-确认接单", "id: " + id);
                post(url2, params2, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        final JsonBean json = JsonUtils.getMessage(new String(responseBody));
                        if ("200".equals(json.getCode())) {
                            if ("已经接受了".equals(json.getMsg())) {
                                Toast.makeText(ReleaseDetailActivity.this, "您已接过该任务!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ReleaseDetailActivity.this, "接单成功!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else if ("203".equals(json.getCode())) {
                            Toast.makeText(ReleaseDetailActivity.this, "此任务已结束", Toast.LENGTH_SHORT).show();
                        } else if ("-400".equals(json.getCode())) {
                            Toast.makeText(ReleaseDetailActivity.this, "接单人数已达到限制", Toast.LENGTH_SHORT).show();
                        }
//                        if ("-400".equals(json.getCode())) {
//                            Log.e("详细任务-确认接单按钮", "成 功"+json.toString());
//                        }else {
//                            Log.e("详细任务-确认接单按钮", "失败"+json.toString());
//                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }
                });

//                Utils.doPost(LoadingDialog.getInstance(this), this, url2, params2, new Utils.HttpCallBack() {
//                    @Override
//                    public void success(JsonBean bean) {
//
//                        ArrayList<HashMap<String, Object>> infor = bean.getInfor();
//                        Log.e("详细任务-确认接单按钮", "成功");
//                    }
//
//                    @Override
//                    public void failure(String msg) {
//                        Log.e("详细任务-确认接单按钮", "失败，返回值: " + msg);
//                    }
//
//                    @Override
//                    public void finish() {
//                        //finish();
//                    }
//                });

                break;
        }
    }
}
