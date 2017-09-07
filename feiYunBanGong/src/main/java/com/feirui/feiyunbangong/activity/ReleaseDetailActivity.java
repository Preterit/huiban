package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.JieDanRenAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 任务单详情界面?好像多建立的，没用可以删掉
 * */
public class ReleaseDetailActivity extends BaseActivity implements View.OnClickListener{
    public String staff_name,time,task_txt,staff_head;
    int id;
    ImageView rwd_im_tx;
    TextView rwd_tv_mz,rwd_tv_rq,rwd_tv_zt,rwd_tv_xq,rwd_tv_sj,rwd_tv_wz,rwd_tv_xs,rwd_tv_xz;
    RecyclerView rwd_rec_jdr;
    Button rwd_btn_qrjd;
    private JsonBean json;
    private JsonBean json1;
    private JieDanRenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_detail);

        staff_name = getIntent().getStringExtra("staff_name");
        time = getIntent().getStringExtra("time");
        task_txt = getIntent().getStringExtra("task_txt");
        staff_head = getIntent().getStringExtra("staff_head");
        id = getIntent().getIntExtra("id",-1);

        initDate();
        initView();
    }

    private void initDate() {
        RequestParams params = new RequestParams();
        String url = UrlTools.pcUrl+ UrlTools.RENWU_JDR;
        params.put("id",id+"");
        Log.e("任务单--全部任务URL", "url: " + url);
        Log.e("任务单--全部任务URL-Date", "params: " + params.toString());

        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {

                ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                Log.e("全部任务获取成功-接单人", "success: "+bean);
                Log.e("全部任务获取成功-接单人", "success: "+infor);
                HashMap<String, HashMap<String, Object>> in_fo = new HashMap<>();
                for (int i=0;i<infor.size();i++){
                    in_fo.put(i+"",infor.get(i));
                }

                adapter.addAll(infor);
            }

            @Override
            public void failure(String msg) {
                Log.e("全部任务获取失败-接单人", "failure: " + msg);
            }

            @Override
            public void finish() {

            }
        });

        String url1 = UrlTools.pcUrl+ UrlTools.RENWU_FBR;

        Utils.doPost(LoadingDialog.getInstance(this), this, url1, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {

                ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                Log.e("全部任务获取成功-发布人", "bean: " + bean);
                Log.e("全部任务获取成功-发布人", "infor: " + infor);
                Log.e("全部任务获取成功-发布人", "infor-staff_head: " + infor.get(0).get("staff_head"));


            }

            @Override
            public void failure(String msg) {
                Log.e("全部任务获取成功-发布人", "failure: " + msg);
            }

            @Override
            public void finish() {

            }
        });
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
        rwd_btn_qrjd.setOnClickListener((View.OnClickListener) this);

        //得到控件
        rwd_rec_jdr = (RecyclerView) findViewById(R.id.rwd_rec_jdr);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rwd_rec_jdr.setLayoutManager(linearLayoutManager);
        //设置适配器
        adapter = new JieDanRenAdapter(this,new ArrayList<HashMap<String, Object>>());
        rwd_rec_jdr.setAdapter(adapter);


        rwd_tv_mz.setText(staff_name);
        rwd_tv_rq.setText(time);
        rwd_tv_xq.setText(task_txt);
        // ImageLoader.getInstance().displayImage("http://123.57.45.74/feiybg/"+staff_head, rwd_im_tx);
        ImageLoader.getInstance().displayImage(staff_head, rwd_im_tx, ImageLoaderUtils.getSimpleOptions());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rwd_btn_qrjd://确认接单
                RequestParams params2 = new RequestParams();
                String url2 = UrlTools.pcUrl+ UrlTools.RENWU_QRJD;
                params2.put("id",id);
                params2.put("button",1);
                Log.e("全部任务获取成功-确认接单", "id: " + id);

                Utils.doPost(LoadingDialog.getInstance(this), this, url2, params2, new Utils.HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {

                        ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                    }
                    @Override
                    public void failure(String msg) {
                        Log.e("全部任务获取成功-确认接单", "返回值: " + msg);
                    }
                    @Override
                    public void finish() {

                    }
                });

                break;
        }
    }
}
