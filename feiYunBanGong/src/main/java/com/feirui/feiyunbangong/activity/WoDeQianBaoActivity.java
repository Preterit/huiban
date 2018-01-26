package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.Yuebeen;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class WoDeQianBaoActivity extends Activity implements View.OnClickListener{

    private TextView yueText;
    private RelativeLayout tixian_layourt;
    private RelativeLayout chongzhi_layout;
    private LinearLayout jifen_layout;
    private LinearLayout hongbao_layout;
    private LinearLayout yajin_layout;
    private LinearLayout leftll;
    private TextView centerTv;
    private TextView righttv;
    private RelativeLayout rightll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wo_de_qian_bao);
        
        initViews();
        yuePost();
    }

    private void yuePost() {
        RequestParams params = new RequestParams();

        String url = UrlTools.url+UrlTools.MY_WALLET;
        AsyncHttpServiceHelper.post(url,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                final JsonBean bean = JsonUtils.getMessage(new String(responseBody));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("200".equals(bean.getCode())) {
                            Gson gson = new Gson();
                            Log.e("orz", "onSuccess: " +new String(responseBody));
                            Yuebeen receive = gson.fromJson(new String(responseBody), Yuebeen.class);
                            yueText.setText(receive.getInfo().getAmount());
                        } else {

                        }
                    }
                });
            }
        });


    }

    private void initViews() {

        leftll=(LinearLayout)findViewById(R.id.leftll);

        centerTv=(TextView)findViewById(R.id.centerTv);
        centerTv.setText("余额");
        righttv=(TextView)findViewById(R.id.righttv);
        righttv.setVisibility(View.VISIBLE);
        righttv.setText("明细");
        rightll=(RelativeLayout)findViewById(R.id.rightll);


        yueText=(TextView)findViewById(R.id.yueText);
        tixian_layourt=(RelativeLayout)findViewById(R.id.tixian_layourt);
        chongzhi_layout=(RelativeLayout)findViewById(R.id.chongzhi_layout);
        jifen_layout=(LinearLayout)findViewById(R.id.jifen_layout);
        hongbao_layout=(LinearLayout)findViewById(R.id.hongbao_layout);
        yajin_layout=(LinearLayout)findViewById(R.id.yajin_layout);

        tixian_layourt.setOnClickListener(this);
        chongzhi_layout.setOnClickListener(this);
        jifen_layout.setOnClickListener(this);
        hongbao_layout.setOnClickListener(this);
        yajin_layout.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftll:
                onBackPressed();
                break;
            case R.id.rightll:

                break;

            case R.id.tixian_layourt:
                posts();
                break;
            case R.id.chongzhi_layout:

                break;
            case R.id.jifen_layout:

                break;
            case R.id.hongbao_layout:

                break;
            case R.id.yajin_layout:

                break;
        }
    }

    private void posts() {

        RequestParams params = new RequestParams();

        String url = UrlTools.url+UrlTools.SHOW_BACKCARD;
        AsyncHttpServiceHelper.post(url,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final JsonBean bean = JsonUtils.getMessage(new String(responseBody));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("200".equals(bean.getCode())) {

                            startActivity(new Intent(WoDeQianBaoActivity.this,XuanZeYinHangKaActivity.class));

                        } else {
                            Intent intent=new Intent();
                            intent.putExtra("TAG","0");
                            intent.setClass(WoDeQianBaoActivity.this,QianTiXianActivity.class);
                            startActivity(intent);

                        }
                    }
                });
            }
        });
    }


}
