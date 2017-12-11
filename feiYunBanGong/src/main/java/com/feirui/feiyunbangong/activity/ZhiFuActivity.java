package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.Header;

public class ZhiFuActivity extends BaseActivity implements View.OnClickListener {
    private IWXAPI wxapi;
    private Button btn_submit;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    EditText text;

    // 标题
    private String title = "易货交易平台";

    // 总价格
    private String total = "0.01";
    private IWXAPI msgApi;
    private PayReq req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_fu);
        wxapi = WXAPIFactory.createWXAPI(this, AppStore.APP_ID,false);
        wxapi.registerApp(AppStore.APP_ID);

        initViews();
    }

    private void initViews() {
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        text = (EditText) findViewById(R.id.editText1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub

                if (checkBox1.isChecked()) {
                    checkBox2.setChecked(false);
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub

                if (checkBox2.isChecked()) {
                    checkBox1.setChecked(false);
                }
            }
        });


    }
    public void wxzf(View view){
        Log.e("支付页面", "微信: " );//18210532546
        Toast.makeText(ZhiFuActivity.this,"微信",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (text.getText().toString().equals("")) {
//            AnimationHelper.shakeAnimation(text);
            Toast.makeText(ZhiFuActivity.this,"没有金额",Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkBox1.isChecked()) {

            getOrderInfo();

        } else if (checkBox2.isChecked()) {
            getOrderInfo();
            Toast.makeText(ZhiFuActivity.this,"微信支付正在申请中",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ZhiFuActivity.this,"请选择支付方式",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     *
     *
     * 获取订单信息
     */
    private void getOrderInfo() {
        RequestParams params = new RequestParams();
        params.put("task_id",123);
        params.put("accept_id",123);
        String url = UrlTools.pcUrl+UrlTools.RENWU_ORDER;
        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {

                    public void onSuccess(int arg0,
                                          org.apache.http.Header[] arg1, byte[] arg2) {

                        // TODO Auto-generated method stub
                        super.onSuccess(arg0, arg1, arg2);

                        JsonBean bean = JsonUtils.getMessage(new String(arg2));
                        Log.e("支付页面", "获取订单信息: "+ bean.getInfor() );
                        if (bean.getCode().equals("200")) {

                            if (checkBox1.isChecked()) {

                                pay((String) bean.getInfor().get(0).get("out_trade_no"));
                            } else {
                                payWX((String) bean.getInfor().get(0).get("out_trade_no"));
                            }
                        } else {
                            Toast.makeText(ZhiFuActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
                        }

                    };

                    /*
                     * (non-Javadoc)
                     *
                     * @see
                     * com.loopj.android.http.AsyncHttpResponseHandler#onFailure
                     * (int, org.apache.http.Header[], byte[],
                     * java.lang.Throwable)
                     */
                    @Override
                    public void onFailure(int arg0, Header[] arg1,
                                          byte[] arg2, Throwable arg3) {
                        // TODO Auto-generated method stub
                        Toast.makeText(ZhiFuActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }

                });

    }

    private void pay(String orderCode) {
        /**
         * 参数1 是subject 参数2 是body 参数3 是price 参数4 是订单号即下订单时生成的流水号,从后台拿到
         */
    }
    private void payWX(String orderCode) {
    }
}
