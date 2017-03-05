package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.view.TextImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

/**
 * 好友资料：
 *
 * @author feirui1
 */
public class HaoYouZiLiaoActivity extends BaseActivity implements
        OnClickListener {
    String phone = "";
    @PView
    TextView tv_name, tv_name_02, tv_phone, tv_email;
    @PView
    TextImageView iv_head;
    @PView(click = "onClick")
    Button bt_liaotian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hao_you_zi_liao);
        initView();
        initData();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("好友资料");
        setRightVisibility(false);
        phone = getIntent().getStringExtra("phone");
    }

    /**
     *
     */
    private void initData() {
        RequestParams params = new RequestParams();

        //todo bug
//        java.lang.ClassCastException: org.json.JSONObject$1 cannot be cast to java.lang.String
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at com.feirui.feiyunbangong.activity.HaoYouZiLiaoActivity$1$1.run(HaoYouZiLiaoActivity.java:87)
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at android.app.Activity.runOnUiThread(Activity.java:5639)
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at com.feirui.feiyunbangong.activity.HaoYouZiLiaoActivity$1.onSuccess(HaoYouZiLiaoActivity.java:75)
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at com.loopj.android.http.AsyncHttpResponseHandler.handleMessage(AsyncHttpResponseHandler.java:365)
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at com.loopj.android.http.AsyncHttpResponseHandler$ResponderHandler.handleMessage(AsyncHttpResponseHandler.java:135)
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at android.os.Handler.dispatchMessage(Handler.java:102)
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at android.os.Looper.loop(Looper.java:150)
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at android.app.ActivityThread.main(ActivityThread.java:5546)
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at java.lang.reflect.Method.invoke(Native Method)
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:794)
//        03-01 22:12:25.860 23684-23684/com.feirui.feiyunbangong W/System.err:     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:684)
        params.put("staff_mobile", phone + "");
        String url = UrlTools.url + UrlTools.USER_SEARCH_MOBILE;
        L.e("好友资料" + url + " params" + params);
        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        super.onSuccess(arg0, arg1, arg2);
                        final JsonBean json = JsonUtils.getMessage(new String(
                                arg2));
                        if ("200".equals(json.getCode())) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    tv_name.setText((String) json.getInfor()
                                            .get(0).get("staff_name"));
                                    tv_name_02.setText("姓名："
                                            + (String) json.getInfor().get(0)
                                            .get("staff_name"));
                                    tv_phone.setText("手机号："
                                            + (String) json.getInfor().get(0)
                                            .get("staff_mobile"));
                                    tv_email.setText("地址："
                                            + (String) json.getInfor().get(0)
                                            .get("address"));
                                    if (!"".equals((String) json.getInfor()
                                            .get(0).get("staff_head"))
                                            && null != (String) json.getInfor()
                                            .get(0).get("staff_head")
                                            && !"img/1_1.png"
                                            .equals((String) json
                                                    .getInfor().get(0)
                                                    .get("staff_head"))) {
                                        ImageLoader.getInstance().displayImage(
                                                (String) json.getInfor().get(0)
                                                        .get("staff_head"),
                                                iv_head);
                                    } else {
                                        iv_head.setText(Utils
                                                .getPicTitle((String) json
                                                        .getInfor().get(0)
                                                        .get("staff_head")));
                                    }
                                }
                            });

                        } else {
                            T.showShort(HaoYouZiLiaoActivity.this,
                                    json.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        super.onFailure(arg0, arg1, arg2, arg3);

                    }
                });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_liaotian:
                finish();
                break;
        }

    }

}
