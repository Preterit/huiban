package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.YinHangKaLieBiaoAdapter;
import com.feirui.feiyunbangong.entity.AddCardSuccessbeen;
import com.feirui.feiyunbangong.entity.DeleteBank;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.ShowCardbeen;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

public class XuanZeYinHangKaActivity extends Activity {

    private LinearLayout leftll;
    private TextView righttv;
    private TextView centerTv;
    private RelativeLayout rightll;
    private ListView yinhangkaList;
    private ImageView leftIv;
    private List<ShowCardbeen.InfoBean> infoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuan_ze_yin_hang_ka);
        initViews();
        cardPost();
        setListeners();
    }



    private void initViews() {

        leftll=(LinearLayout)findViewById(R.id.leftll);
        leftll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        leftIv=(ImageView)findViewById(R.id.leftIv);
        leftIv.setImageResource(R.drawable.arrows_left);
        centerTv=(TextView)findViewById(R.id.centerTv);
        centerTv.setText("选择银行卡");
        righttv=(TextView)findViewById(R.id.righttv);
        righttv.setVisibility(View.VISIBLE);
        righttv.setText("添加");
        rightll=(RelativeLayout)findViewById(R.id.rightll);
        yinhangkaList=(ListView)findViewById(R.id.yinhangkaList);


    }


    private void cardPost() {


        RequestParams params = new RequestParams();

        String url = UrlTools.url+UrlTools.SHOW_BACKCARD;
        AsyncHttpServiceHelper.post(url,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                final JsonBean bean = JsonUtils.getMessage(new String(responseBody));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("200".equals(bean.getCode())) {
                            Gson gson = new Gson();
                            Log.e("ooo", "onSuccess: " +new String(responseBody));
                            ShowCardbeen receive = gson.fromJson(new String(responseBody), ShowCardbeen.class);
                            infoList = receive.getInfo();
                            YinHangKaLieBiaoAdapter adapter = new YinHangKaLieBiaoAdapter(XuanZeYinHangKaActivity.this, infoList);
                            yinhangkaList.setAdapter(adapter);
                        } else {
                            Toast.makeText(XuanZeYinHangKaActivity.this,"网络错误！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }



    private void setListeners() {

        rightll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("TAG","1");
                intent.setClass(XuanZeYinHangKaActivity.this,QianTiXianActivity.class);
                startActivity(intent);
            }
        });


        yinhangkaList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShowCardbeen.InfoBean cdinfo = infoList.get(i);
                final int card_id=cdinfo.getCard_id();

                AlertDialog.Builder builder= new AlertDialog.Builder(new ContextThemeWrapper(XuanZeYinHangKaActivity.this, R.style.AlertDialogCustom));
                builder.setIcon(R.drawable.tishi_icon);//设置图标
                builder.setTitle("删除银行卡");//设置对话框的标题
                builder.setMessage("你确定要删除此银行卡吗？");//设置对话框的内容
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteCard(card_id);

                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {  //取消按钮
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
                AlertDialog b=builder.create();
                b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
                return false;
            }
        });
    }

    private void deleteCard(int cardid) {

        Log.e("tag", "此方法走了吗？");
        RequestParams params = new RequestParams();
        params.put("card_id",cardid+"");
        Log.e("tag", "删除的参数---"+cardid );
        String url = UrlTools.url+UrlTools.DELETE_BANKCARD;

        AsyncHttpServiceHelper.post(url,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                Gson gson = new Gson();
                final DeleteBank bean = gson.fromJson(new String(responseBody), DeleteBank.class);

                Log.e("tag", "数据"+bean.toString() );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson=new Gson();
                        AddCardSuccessbeen cardinfo = gson.fromJson(new String(responseBody), AddCardSuccessbeen.class);
                        if(cardinfo.getCode()==200){
                            cardPost();
                            Toast.makeText(getApplication(),"删除成功！",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplication(),"删除失败！",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        cardPost();
    }
}

