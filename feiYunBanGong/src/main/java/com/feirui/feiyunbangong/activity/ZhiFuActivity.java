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
import com.feirui.feiyunbangong.entity.Constants;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.MD5;
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
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
//    private sb;

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
//                                payWX((String) bean.getInfor().get(0).get("out_trade_no"));
                                PayWXMoney((String) bean.getInfor().get(0).get("out_trade_no"));
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
    /**
     * @param string
     *
     *            微信支付 获取当前订单信息
     */
//    protected void payWX(String string) {
//        // TODO Auto-generated method stub
//
//        RequestParams params = new RequestParams();
//        params.put("account_number", ChangeAppliction.user.getData().get(0)
//                .get("account_number"));
//        params.put("uid", ChangeAppliction.user.getData().get(0).get("id"));
//        params.put("amount", text.getText().toString());
//        String url = Constant.url + Constant.WX_GETINFO_MESSAGE;
//        L.e("///////////////////" + url + "*****************" + params);
//        Log.e("--lesgod--", url + ";;;;;" + params);
//        AsyncHttpServiceHelper.post(url, params,
//                new AsyncHttpResponseHandler() {
//
//                    public void onSuccess(int arg0,
//                                          org.apache.http.Header[] arg1, byte[] arg2) {
//
//                        // TODO Auto-generated method stub
//                        super.onSuccess(arg0, arg1, arg2);
//
//                        JsonBean bean = JsonUtils.getMessage(new String(arg2));
//
//                        System.out.println("name---------+" + bean.getData());
//                        if (bean.getCode().equals("200")) {
//
//                            Log.e("lesgod", "name---------+" + bean.getData());
//
//                            PayWXMoney(bean.getData().get(0)
//                                    .get("out_trade_no"));
//
//                        } else {
//                            T.makeText(PayAty.this, bean.getMsg(),
//                                    Toast.LENGTH_LONG);
//                        }
//
//                    };
//
//                    /*
//                     * (non-Javadoc)
//                     *
//                     * @see
//                     * com.loopj.android.http.AsyncHttpResponseHandler#onFailure
//                     * (int, org.apache.http.Header[], byte[],
//                     * java.lang.Throwable)
//                     */
//                    @Override
//                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//                                          Throwable arg3) {
//                        // TODO Auto-generated method stub
//
//                        T.makeText(PayAty.this, "网络异常", Toast.LENGTH_LONG);
//                        super.onFailure(arg0, arg1, arg2, arg3);
//                    }
//
//                });
//
//    }

    /**
     * @author Lesgods 微信付款信息
     *
     */
    protected void PayWXMoney(String no) {
        // TODO Auto-generated method stub

        RequestParams params =new RequestParams();
        params.put("out_trade_no", no);
        String url =UrlTools.pcUrl + UrlTools.WX_GETINFO_WXPAY;
        Log.e("支付页面", url + "---" + params);
        Log.e("---", no + "--" + url + "--" + params);
        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {/*
												 * (non-Javadoc)
												 *
												 * @see com.loopj.android.http.
												 * AsyncHttpResponseHandler
												 * #onSuccess(int,
												 * org.apache.http.Header[],
												 * byte[])
												 */
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        super.onSuccess(arg0, arg1, arg2);
                        JsonBean bean = JsonUtils.getMessage(new String(arg2));

                        Log.e("lesgods", "------" + bean.getInfor());

                        if (bean.getCode().equals("200")) {
                            Log.e("lesgods", "------" + bean.getCode());
                            try {
                                PayWXPay((String) bean.getInfor().get(0).get("info"));
                            } catch (SAXException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (ParserConfigurationException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    }
                });

    }
    /**
     * @param string
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    protected void PayWXPay(final String string) throws SAXException,
            IOException, ParserConfigurationException {

        new Thread() {

            public void run() {
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder builder;
                try {
                    builder = factory.newDocumentBuilder();
                    InputStream in_nocode = new ByteArrayInputStream(string.getBytes());
                    Document document = builder.parse(in_nocode);

                    String appid = ((Element) document.getElementsByTagName("appid").item(0)).getTextContent().toString();
                    String mch_id = ((Element) document.getElementsByTagName("mch_id").item(0)).getTextContent().toString();
                    String nonce_str = ((Element) document.getElementsByTagName("nonce_str").item(0)).getTextContent().toString();
                    String prepay_id = ((Element) document.getElementsByTagName("prepay_id").item(0)).getTextContent().toString();

                    req = new PayReq();

                    req.appId = appid;
                    req.partnerId = mch_id;
                    req.prepayId = prepay_id;
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = nonce_str;
                    req.timeStamp = String.valueOf(Calendar.getInstance()
                            .getTimeInMillis() / 1000);

                    // abf6d964981d7725113c7134e9e72bc0

                    // 顺序一定按照这个来 否则一直出现 -1返回码！
                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

                    req.sign = genAppSign(signParams);

//                    handler2.sendEmptyMessage(0);

                } catch (ParserConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SAXException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            };
        }.start();

        Log.e("Lesgod", string + "-----------------");

    }

//    private String genAppSign1(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < params.size(); i++) {
//            sb.append(params.get(i).getName());
//            sb.append('=');
//            sb.append(params.get(i).getValue());
//            sb.append('&');
//        }
//        sb.append("key=");
//
//        sb.append(UrlTools.API_KEY_WX);
//        Log.e("sb", sb.toString());
//
//        String appSign = com.feirui.feiyunbangong.entity.MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        Log.i("TAG", appSign);
//
//        return appSign.toUpperCase();
//    }
    /**
     * 生成prepayid
     * */
//    private String genProductArgs() {
//        StringBuffer xml = new StringBuffer();
//
//        try {
//            String nonceStr = genNonceStr();
//
//            xml.append("</xml>");
//            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
//            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
//            packageParams.add(new BasicNameValuePair("body", "weixin"));
//            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
//            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
//            packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
//            packageParams.add(new BasicNameValuePair("out_trade_no", "123"));//订单号
//            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
//            packageParams.add(new BasicNameValuePair("total_fee", "1"));//总金额
//            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
//            String sign = genAppSign(packageParams);
//            packageParams.add(new BasicNameValuePair("sign", sign));
//
////            String xmlstring = toXml(packageParams);
//
////            return xmlstring;
//
//        } catch (Exception e) {
//            Log.e("支付页面", "genProductArgs fail, ex = " + e.getMessage());
//            return null;
//        }
//
//    }



    private String genNonceStr() {
        Random random = new Random();
        return com.feirui.feiyunbangong.entity.MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }
    /**
     * APP签名
     * */
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

        sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }
/**
 * 支付参数配置
 * */
//    private void genPayReq() {
//
//        req.appId = Constants.APP_ID;
//        req.partnerId = Constants.MCH_ID;
//        req.prepayId = resultunifiedorder.get("prepay_id");
//        req.packageValue = "Sign=WXPay";
//        req.nonceStr = genNonceStr();
//        req.timeStamp = String.valueOf(genTimeStamp());
//
//        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//        signParams.add(new BasicNameValuePair("appid", req.appId));
//        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//        signParams.add(new BasicNameValuePair("package", req.packageValue));
//        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//
//        req.sign = genAppSign(signParams);
//
//        sb.append("sign\n" + req.sign + "\n\n");
//
//        show.setText(sb.toString());
//
//        Log.e("orion", signParams.toString());
//
//    }
//    Handler handler2 = new Handler() {
//
//        public void handleMessage(Message msg) {
//            ChangeAppliction.user.getData().get(0).put("pay_state", "1");
//            IWXAPI msgApi;
//            msgApi = WXAPIFactory.createWXAPI(PayAty.this, null);
//
//            msgApi.registerApp(Constants.APP_ID);
//            msgApi.sendReq(req);
//
//            dia.dismiss();
//        };
//    };
}
