package com.feirui.feiyunbangong.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.Constants;
import com.feirui.feiyunbangong.entity.MD5;
import com.feirui.feiyunbangong.entity.RenwuOrderBean;
import com.feirui.feiyunbangong.entity.Util;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ZhiFuActivity extends BaseActivity implements View.OnClickListener {
    private IWXAPI wxapi;
    private Button btn_submit;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    EditText text;
    TextView show;
    private TextView tv_money, tv_zt;
    private ImageView iv_head;

    //订单号
    public static String out_trade_no;

    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
    PayReq req;
    StringBuffer sb;
    Map<String, String> resultunifiedorder;
    private String staff_name, time, task_txt, task_zt, jiedanren_head, task_id, accept_id, state, xuanshang, jiedanren_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_fu);
        task_zt = getIntent().getStringExtra("task_zt");
        jiedanren_head = getIntent().getStringExtra("staff_head");
        task_id = getIntent().getStringExtra("task_id");
        accept_id = getIntent().getStringExtra("accept_id");
        xuanshang = getIntent().getStringExtra("xuanshang");
        req = new PayReq();
        sb = new StringBuffer();
        msgApi.registerApp(Constants.APP_ID);
        initViews();
    }

    private void initViews() {
        show = (TextView) findViewById(R.id.editText_prepay_id);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        text = (EditText) findViewById(R.id.editText1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (checkBox1.isChecked()) {
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (checkBox2.isChecked()) {
                    checkBox1.setChecked(false);
                    checkBox3.setChecked(false);
                }
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (checkBox3.isChecked()) {
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                }
            }
        });
        initData();
        tv_money= (TextView) findViewById(R.id.tv_money);
        tv_zt= (TextView) findViewById(R.id.tv_zt);
        iv_head= (ImageView) findViewById(R.id.iv_head);
        tv_money.setText(xuanshang+"   元");
        tv_zt.setText(task_zt);
        ImageLoader.getInstance().displayImage(jiedanren_head, iv_head, ImageLoaderUtils.getSimpleOptions());
    }

    private void initData() {

        getOrderInfo();//获取订单信息

    }




    @Override
    public void onClick(View v) {

        if (checkBox1.isChecked()) {//微信支付
            //第二步 生成APP微信支付参数 sign
            genPayReq();

        } else if (checkBox2.isChecked()) {
            Toast.makeText(ZhiFuActivity.this, "支付宝正在申请中", Toast.LENGTH_SHORT).show();
        } else if (checkBox3.isChecked()) {
            Toast.makeText(ZhiFuActivity.this, "余额支付正在开发中", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ZhiFuActivity.this, "请选择支付方式", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取订单信息
     */
    private void getOrderInfo() {
        RequestParams params = new RequestParams();
        params.put("task_id", task_id);
        params.put("accept_id", accept_id);
        Log.e("支付页面-传值====",task_id+"====."+accept_id);
        String url = UrlTools.pcUrl + UrlTools.RENWU_ORDER;
        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {

                    public void onSuccess(int arg0, org.apache.http.Header[] arg1, byte[] arg2) {
                        super.onSuccess(arg0, arg1, arg2);
                        Gson gson = new Gson();
                        RenwuOrderBean bean = gson.fromJson(new String(arg2), RenwuOrderBean.class);
                        Log.e("支付页面", "获取订单信息: " + bean.toString());
                        out_trade_no = bean.getInfo().getOut_trade_no();
//                        Happlication application = (Happlication) getApplication().getApplicationContext();
//                        application.setOut_trade_no(out_trade_no);//out_trade_no值

                        //第一步 APP生成预支付订单 prepay_id
                        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();//asyncTask
                        getPrepayId.execute();

                        if (bean.getCode() != 200) {
                            out_trade_no= bean.getInfo().getOut_trade_no();
                            if (checkBox1.isChecked()) {//微信支付

                            } else {//支付宝支付
                                pay(bean.getInfo().getOut_trade_no());
                            }
                        } else {
                            Toast.makeText(ZhiFuActivity.this, bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1,
                                          byte[] arg2, Throwable arg3) {
                        // TODO Auto-generated method stub
                        Toast.makeText(ZhiFuActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }

                });

    }

    private void pay(String orderCode) {
        /**
         * 参数1 是subject 参数2 是body 参数3 是price 参数4 是订单号即下订单时生成的流水号,从后台拿到
         */
    }


    /**
     * 支付参数配置---demo 的方法
     */
    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            Log.e("支付页面", "onPreExecute方法");
            dialog = ProgressDialog.show(ZhiFuActivity.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            for (int i = 0; i < result.size(); i++) {
//                Log.e("MAP", i + ":" + result.get(i));
            }
            Log.e("支付页面", "result.get(\"prepay_id\"): " + result.get("prepay_id"));
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");

            show.setText(sb.toString());

            resultunifiedorder = result;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

            Log.e("支付页面-orion", entity);

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Log.e("支付页面-微信返回的--content", content);
            Map<String, String> xml = decodeXml(content);

            Log.e("支付页面-MAP", "xml:" + xml);
            return xml;
        }
    }

    /**
     * 获取预支付订单
     */
    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();
        Log.e("支付页面-获取支付订单", "out_trade_no: " + out_trade_no+" 悬赏 "+(int)(Double.parseDouble(xuanshang)*100));
        int money =(int)(Double.parseDouble(xuanshang)*100);
        try {
            String nonceStr = genNonceStr();
//            Happlication application = (Happlication) getApplication().getApplicationContext();
//            out_trade_no = application.getOut_trade_no();
//            out_trade_no=genOutTradNo();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", "huiban"));
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://123.57.45.74/feiybg1/public/index.php/home_api/notify/wx_notify"));
            packageParams.add(new BasicNameValuePair("out_trade_no", out_trade_no));//获取后台的订单号
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee",money+""));//(Integer.parseInt(xuanshang)*100)+""//金额'
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            Log.e("支付页面-获取支付订单", "packageParams: " + packageParams.toString());
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);

            return xmlstring;
        } catch (Exception e) {
            Log.e("支付页面-", "genProductArgs 错误, ex = " + e.getMessage());
            return null;
        }
    }


    /**
     * 生成微信支付参数
     */
    private void genPayReq() {
        Happlication application = (Happlication) getApplication().getApplicationContext();
        out_trade_no = application.getOut_trade_no();
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        req.packageValue = "Sign=WXPay";

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);
        req.extData = "app data"; // optional
        sb.append("sign\n" + req.sign + "\n\n");

        show.setText(sb.toString());

        //第三步 调起微信支付
        sendPayReq();

        Log.e("支付页面-signParams", signParams.toString());

    }


    /**
     * 调起微信支付
     */
    private void sendPayReq() {
        Log.e("支付页面-sendPayReq", "APP_ID: " + Constants.APP_ID);
        Log.e("支付页面-sendPayReq", "appId: " + req.appId);
        Log.e("支付页面-sendPayReq", "appId: " + req.prepayId);
//        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
        boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        Toast.makeText(this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
    }


    /**
     * 转换xml方法
     */
    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("支付页面-orion", e.toString());
        }
        return null;

    }

    /**
     * 转换xml方法
     */
    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("支付页面-orion", sb.toString());
        return sb.toString();
    }

    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);


        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("支付页面-packageSign", packageSign);
        return packageSign;
    }

    /**
     * 获取时间戳
     */
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取订单号
     */
    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 获取随机数
     */
    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * APP签名
     */
    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        Log.e("支付页面-orion", appSign);
        return appSign;
    }

}
