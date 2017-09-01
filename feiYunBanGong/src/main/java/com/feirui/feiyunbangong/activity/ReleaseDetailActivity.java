package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

/**
 * 任务单详情界面?好像多建立的，没用可以删掉
 * */
public class ReleaseDetailActivity extends BaseActivity {
    public String staff_name,time,task_txt,staff_head;
    int id;
    ImageView rwd_im_tx;
    TextView rwd_tv_mz,rwd_tv_rq,rwd_tv_zt,rwd_tv_xq,rwd_tv_sj,rwd_tv_wz,rwd_tv_xs,rwd_tv_xz;
    private JsonBean json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_detail);
        initDate();
        initView();
    }

    private void initDate() {
        staff_name = getIntent().getStringExtra("staff_name");
        time = getIntent().getStringExtra("time");
        task_txt = getIntent().getStringExtra("task_txt");
        staff_head = getIntent().getStringExtra("staff_head");
        id = getIntent().getIntExtra("id",-1);
        Log.e("详细页面", "staff_name: "+staff_name );
        Log.e("详细页面", "time: "+time );
        Log.e("详细页面", "task_txt: "+task_txt );
        Log.e("详细页面", "staff_head: "+staff_head );
        Log.e("详细页面", "id: "+id );

        RequestParams params = new RequestParams();
        String url = UrlTools.pcUrl+ UrlTools.RENWU_FBR;
        params.put("id",id+"");
        Log.e("任务单--全部任务URL", "url: " + url);
        Log.e("任务单--全部任务URL", "params: " + params.toString());

        AsyncHttpServiceHelper.post(url,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                json = JsonUtils.getMessage(new String(responseBody));
                Log.e("详细任务单jsonBean", "onSuccess:" + json.toString());
                //yRecycleview.setReFreshComplete();
                //setAdapter();
                if(json.getInfor()==null){
                    return;
                }


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
        rwd_tv_mz.setText(staff_name);
        rwd_tv_rq.setText(time);
        rwd_tv_xq.setText(task_txt);
        // ImageLoader.getInstance().displayImage("http://123.57.45.74/feiybg/"+staff_head, rwd_im_tx);
        ImageLoader.getInstance().displayImage(staff_head, rwd_im_tx, ImageLoaderUtils.getSimpleOptions());
    }
}
