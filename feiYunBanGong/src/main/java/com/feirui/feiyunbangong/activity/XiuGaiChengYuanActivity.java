package com.feirui.feiyunbangong.activity;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.SwitchView;
import com.feirui.feiyunbangong.view.TextImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 修改团队成员资料：
 *
 * @author feirui1
 */
public class XiuGaiChengYuanActivity extends BaseActivity implements
        OnClickListener {

    private TextImageView iv_head;
    private TextView tv_name, tv_phone, tv_name_02;
    private Button bt_submit;
    private EditText et_email, et_remark;
    private SwitchView sv_yinshen;
    private TuanDui td;
    public TuanDuiChengYuan tdcy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiu_gai_cheng_yuan);
        Intent intent = getIntent();
        td = (TuanDui) intent.getSerializableExtra("td");/////////////////取不到值
        Log.e("修改成员页面", "td: "+td );
        initData();
        initView();
        setListener();
    }

    private void initData() {
        String url = UrlTools.url + UrlTools.XIUGAI_ChengYuan;
        RequestParams params = new RequestParams();
        Log.e("修改成员", "id: "+td.toString() );
        params.put("id", td.getTid());
        AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                JsonBean bean = JsonUtils.getMessage(new String(arg2));
                Log.e("修改成员页面", "bean: "+bean.toString() );
                Log.e("修改成员页面", "staff_name: "+bean.getInfor().get(0).get("staff_name").toString() );
                tdcy = new TuanDuiChengYuan(
                        bean.getInfor().get(0).get("staff_id").toString(),
                        bean.getInfor().get(0).get("staff_name").toString(),
                        bean.getInfor().get(0).get("staff_head").toString(),
                        bean.getInfor().get(0).get("staff_mobile").toString()
                        );

                if (!TextUtils.isEmpty(tdcy.getT_remark())&&tdcy.getT_remark()!=null) {
                    tv_name.setText(tdcy.getT_remark());
                    tv_name_02.setText("姓 名 ：" + tdcy.getT_remark());
                } else if (!TextUtils.isEmpty(tdcy.getRemark())) {
                    tv_name.setText(tdcy.getRemark());
                    tv_name_02.setText("姓 名 ：" + tdcy.getRemark());
                } else {
                    tv_name_02.setText("姓 名 ：" + tdcy.getName());
                    tv_name.setText(tdcy.getName());
                }
                if( bean.getInfor().get(0).get("stealth").equals(1)){
                    sv_yinshen.setOpened(false);
                } else {
                    sv_yinshen.setOpened(true);
                }
                if (tdcy.getIntroduction() != null && !tdcy.getIntroduction().equals("null")) {
                    et_email.setText("" + tdcy.getIntroduction().toString());
                }
                //tv_phone.setText("手机号：" + tdcy.getPhone().toString());//因为保护隐私,屏蔽手机号

                if (tdcy.getT_remark()!=null) {//如果原来有个人签名,设置个人签名
                    et_remark.setText(tdcy.getT_remark());
                }
//                if(tdcy.getIntroduction()!=null){//如果原来有团队备注,设置团队备注
//                    et_email.setText(tdcy.getIntroduction());
//                }
                if (!"".equals(tdcy.getHead()) && null!= tdcy.getHead() &&
                        !"http://123.57.45.74/feiybg1/public/static/staff_head/19912/53da489597afc6f5abb2a1bae0d767ff.jpeg".equals(tdcy.getHead())&&
                        !"http://123.57.45.74/feiybg1/public/index.php/home_api/public/static/staff_head/430/c8aa160662318c6f62a068de84ed797b.jpeg".equals(tdcy.getHead())) {
                    ImageLoader.getInstance().displayImage(tdcy.getHead(), iv_head);
                } else {
                    iv_head.setText(Utils.getPicTitle(tdcy.getName()));
                }
                super.onSuccess(arg0, arg1, arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        });


    }

    private void setListener() {
        bt_submit.setOnClickListener(this);
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("修改信息");
        setRightVisibility(false);
        Intent intent = getIntent();
        tdcy = (TuanDuiChengYuan) intent.getSerializableExtra("tdcy");

        iv_head = (TextImageView) findViewById(R.id.iv_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        et_email = (EditText) findViewById(R.id.et_email);
        sv_yinshen = (SwitchView) findViewById(R.id.sv_yinshen);
        tv_name_02 = (TextView) findViewById(R.id.tv_name_02);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        et_remark = (EditText) findViewById(R.id.et_remark);

    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                update();
                break;
        }
    }

    private void update() {

        String url = UrlTools.url + UrlTools.XIUGAI_YOUXIANG;
        RequestParams params = new RequestParams();
        Log.e("修改个人简介", "id " + td);
        //判断隐身  0隐身/1在线
        params.put("stealth", sv_yinshen.isOpened()?"0":"1");//隐身状态
        params.put("id", td.getTid());//团队id

        params.put("t_remark", et_remark.getText().toString() + "");//备注

         Log.e("修改个人简介", "params " + params.toString());
         Log.e("修改个人简介", "sv_yinshen " + sv_yinshen.isOpened());
        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                        JsonBean bean = JsonUtils.getMessage(new String(arg2));
                        if ("200".equals(bean.getCode())) {
                             Message msg = handler.obtainMessage(0);
                            handler.sendMessage(msg);
                        } else {
                            Message msg = handler.obtainMessage(1);
                            msg.obj = bean;
                            handler.sendMessage(msg);
                        }

                        super.onSuccess(arg0, arg1, arg2);
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }
                });
    }

    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(XiuGaiChengYuanActivity.this, "修改成功！", 0).show();
                    //tdcy.setEmail(et_email.getText().toString() + "");
//                    Log.e("修改成员界面", "et_email: " + et_email.getText().toString());
//                    tdcy.setIntroduction(et_email.getText().toString());
                    tdcy.setT_remark(et_remark.getText().toString() + "");
                    Log.e("修改成员界面", "tdcy: " + tdcy.toString());
                    setResult(200, new Intent().putExtra("tdcy", tdcy));
                    finish();
                    break;
                case 1:
                    JsonBean bean = (JsonBean) msg.obj;
                    Toast.makeText(XiuGaiChengYuanActivity.this, bean.getMsg(), 0)
                            .show();
                    break;
                case 2:

                    break;
            }
        }

        ;
    };
}
