package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.DingShiQiUtil;
import com.feirui.feiyunbangong.utils.ImmersedStatusbarUtils;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 *
 */
public class QickLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mLeftIv;
    private TextView mCenterTv,mDelete;
    private TextImageView mIv_head;
    private TextView mNickTv;
    private FrameLayout mFl_name;
    private AutoCompleteTextView mEt_login_username;
    private RelativeLayout mRl_style_code;
    private EditText mEt_code;
    private Button mBtn_get_code, mBtn_login_submit;
    private String nickName,headUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qick_login);
        Intent intent = getIntent();
        nickName = intent.getStringExtra("name");
        headUrl = intent.getStringExtra("iconurl");
        initView();
        initListener();
    }

    private void initView() {
        // 设置沉浸式状态栏：
        ImmersedStatusbarUtils.initAfterSetContentView(this, findViewById(R.id.quick_act));
        mLeftIv = findViewById(R.id.leftIv);
        mCenterTv = findViewById(R.id.centerTv);
        mIv_head = findViewById(R.id.iv_head);
        mNickTv = findViewById(R.id.nickTv);
        mFl_name =  findViewById(R.id.fl_name);
        mEt_login_username =  findViewById(R.id.et_login_username);
        mRl_style_code = findViewById(R.id.rl_style_code);
        mEt_code =  findViewById(R.id.et_code);
        mBtn_get_code =findViewById(R.id.btn_get_code);
        mBtn_login_submit = findViewById(R.id.btn_login_submit);
        //跳过
        mDelete = findViewById(R.id.quick_del);

        if (TextUtils.isEmpty(nickName)){
            mNickTv.setText("昵称");
        }else {
            mNickTv.setText(nickName);
        }
        if (!TextUtils.isEmpty(headUrl)){
            Glide.with(this).load(headUrl).into(mIv_head);
        }
    }

    private void initListener() {
        mLeftIv.setOnClickListener(this);
        mBtn_get_code.setOnClickListener(this);
        mBtn_login_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftIv:
                finish();
                break;
            case R.id.btn_get_code:
                String phone = mEt_login_username.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    T.showShort(this, "手机号不能为空！");
                    return;
                }

                if (!Utils.isPhone(phone)) {
                    T.showShort(this, "手机号格式错误！");
                    return;
                }
                //获取验证码
                getAuthCode(phone);
                break;
            case R.id.btn_login_submit:
                if (mEt_login_username.getText().toString().trim().length() != 11) {
                    T.showShort(QickLoginActivity.this, "请输入11位手机号");
                    return;
                }
                if (TextUtils.isEmpty(mEt_code.getText().toString().trim())) {
                    T.showShort(this, "短信验证码不能为空");
                    return;
                }
                unitTwo();

                break;
            case R.id.quick_del: //跳过 不绑定手机号
                T.showShort(this,"敬请期待~");
                break;
        }
    }

    /*
    关联 QQ或者微信
     */
    private void unitTwo() {
        T.showShort(this,"敬请期待~");
    }

    private void getAuthCode(String phone) {
        try {
            //获取验证码的接口还没有
            String url = UrlTools.url + UrlTools.DENGLU_REGIST;
            // 发请求
            RequestParams params = new RequestParams();
            params.put("admin_mobile", phone.trim());

            AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    super.onSuccess(statusCode, headers, responseBody);
                    final JsonBean jsonBean = JsonUtils.getMessage(new String(responseBody));
                    if ("200".equals(jsonBean.getCode())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                T.showShort(QickLoginActivity.this,
                                        "验证码已发送请注意查收");
                            }
                        });
                        DingShiQiUtil.init(mBtn_get_code);
                        DingShiQiUtil.open();
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                T.showShort(QickLoginActivity.this,
                                        "验证码--" + jsonBean.getMsg());
                            }
                        });
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DingShiQiUtil.close();// 关闭定时器；
    }
}
