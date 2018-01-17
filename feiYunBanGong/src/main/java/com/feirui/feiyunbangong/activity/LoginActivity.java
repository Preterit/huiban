package com.feirui.feiyunbangong.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.Constants;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.BaiDuUtil;
import com.feirui.feiyunbangong.utils.DingShiQiUtil;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.SPUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.wxapi.WXEntryActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.icu.lang.UScript.getCode;


/**
 * 登录界面
 *
 * @author admina
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @PView
    FrameLayout fl_name;
    // 用户名，密码输入框
    AutoCompleteTextView et_login_username;
    @PView
    EditText et_login_password,et_code;
    // 登录
    @PView(click = "onClick")
    Button btn_login_submit,btn_get_code;
    // 注册，忘记密码,登录方式
    @PView(click = "onClick")
    TextView btn_login_register, btn_login_forget, tv_code, tv_pwd, tv_style_login;
    //微信 qq
    @PView(click = "onClick")
    ImageView btn_WX_login, btn_QQ_login;
    //登录方式
    @PView(click = "onClick")
    LinearLayout style_login;
    //忘记密码 密码登录 验证码输入
    @PView
    RelativeLayout rl_forget, rl_style_pwd, rl_style_code;

    String type = "personal";// 注册类型,个人注册
    private StringBuffer stringBuffer = new StringBuffer(256);
    private String mPostion;

    private static final int LOGIN_SUCESS = 1; // 登录成功
    private static final int LOGIN_ERROR = 2;// 登录失败
    private static final int JSON_ERROR = 3;// json解析出错
    private static final int SERVICE_ERROR = 4;// 链接服务器出错
    private static final int LOCATION = 5;// 定位
    private PopupWindow popupWindow;
    private List<String> groups;
    private ListView lv_group;
    private View view;
    private Set<String> set;
    private ArrayList user_list;
    private ArrayAdapter<String> adapter;
    private ImageView imageView;
    private ArrayList<SnsPlatform> platform = new ArrayList<>();
    private int flag = 0;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCESS:
                    AppStore.user = (JsonBean) msg.obj;
                    // 设置已经登陆过
                    SPUtils.put(LoginActivity.this, Constant.SP_ALREADYUSED, true);
                    // 将用户名密码缓存
                    SPUtils.put(LoginActivity.this, Constant.SP_USERNAME,
                            et_login_username.getText().toString() + "");
                    SPUtils.put(LoginActivity.this, Constant.SP_PASSWORD,
                            et_login_password.getText().toString() + "");
                    T.showShort(LoginActivity.this, ((JsonBean) msg.obj).getMsg());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                    finish();
                    break;
                case LOGIN_ERROR:
                    AppStore.user = null;
                    T.showShort(LoginActivity.this, ((JsonBean) msg.obj).getMsg());
                    break;
                case JSON_ERROR:
                    AppStore.user = null;
                    T.showShort(LoginActivity.this, "json解析出错");
                    break;
                case SERVICE_ERROR:
                    AppStore.user = null;
                    T.showShort(LoginActivity.this, "网络开小差了");
                    break;
                case LOCATION:
                    //获取定位
                    BaiDuUtil.initLocation(LoginActivity.this, new BDLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            if (bdLocation != null && bdLocation.getLocType() != BDLocation.TypeServerError) {
                                stringBuffer.append(bdLocation.getLatitude());//纬度
                                stringBuffer.append(",");
                                stringBuffer.append(bdLocation.getLongitude());//经度
                                mPostion = bdLocation.getAddrStr();
                            }
                        }
                    });
                    break;
            }
        }

        ;

    };
    private View view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //创建微信并注册到微信
        Happlication.sApi.registerApp(Constants.APP_ID);
        //启动的同时 定位
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                L.e("登录url================================");
                handler.sendEmptyMessage(LOCATION);
            }
        }, 100);

        et_login_username = (AutoCompleteTextView) findViewById(R.id.et_login_username);
        et_login_username.setOnClickListener(this);
    }

    private final class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            saveHistory("history", et_login_username);
        }
    }

    /**
     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段
     *
     * @param field                保存在sharedPreference中的字段名
     * @param autoCompleteTextView 要操作的AutoCompleteTextView
     */

    private void saveHistory(String field,

                             AutoCompleteTextView autoCompleteTextView) {

        String text = autoCompleteTextView.getText().toString();

        SharedPreferences sp = getSharedPreferences("network_url", 0);

        String longhistory = sp.getString(field, "");

        if (!longhistory.contains(text + ",")) {

            StringBuilder sb = new StringBuilder(longhistory);

            sb.insert(0, text + ",");

            sp.edit().putString("history", sb.toString()).commit();

        }

    }

    /**
     * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
     *
     * @param field                保存在sharedPreference中的字段名
     * @param autoCompleteTextView 要操作的AutoCompleteTextView
     */

    private void initAutoComplete(String field,

                                  final AutoCompleteTextView autoCompleteTextView) {
        SharedPreferences sp = getSharedPreferences("network_url", 0);

        String longhistory = sp.getString("history", "");

        final String[] histories = longhistory.split(",");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,

                R.layout.item, histories);

        // 只保留最近的50条的记录

        if (histories.length > 3) {

            String[] newHistories = new String[3];

            System.arraycopy(histories, 0, newHistories, 0, 3);

            adapter = new ArrayAdapter<String>(this,

                    R.layout.item, newHistories);

        }

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override

            public void onFocusChange(View v, boolean hasFocus) {

                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    et_login_username.setDropDownWidth(fl_name.getWidth());
                    view.showDropDown();

                }

            }

        });

    }


    @Override
    protected void onResume() {
        initView();
        super.onResume();
    }


    /**
     * 返回到登录页，回显用户名密码
     */
    private void initView() {
        if (!TextUtils.isEmpty((String) SPUtils.get(LoginActivity.this,
                Constant.SP_USERNAME, ""))
                && !TextUtils.isEmpty((String) SPUtils.get(LoginActivity.this,
                Constant.SP_PASSWORD, ""))) {
            et_login_username.setText((String) SPUtils.get(LoginActivity.this,
                    Constant.SP_USERNAME, ""));
            et_login_password.setText((String) SPUtils.get(LoginActivity.this,
                    Constant.SP_PASSWORD, ""));
        }

    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_login_forget: // 忘记密码
                intent = new Intent(LoginActivity.this, RegisteActivity.class);
                intent.putExtra("type", "wangjimima");
                startActivity(intent);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                break;
            case R.id.btn_login_register: // 注册
                intent = new Intent(LoginActivity.this, RegisteActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                break;
            case R.id.btn_login_submit:// 登录
                if (et_login_username.getText().toString().trim().length() != 11) {
                    T.showShort(LoginActivity.this, "请输入11位手机号");
                    return;
                }
                if (flag == 0){
                    if (TextUtils.isEmpty(et_login_username.getText().toString())
                            || TextUtils
                            .isEmpty(et_login_password.getText().toString())) {
                        T.showShort(LoginActivity.this, "用户名或密码不能为空");
                        return;
                    }
                }else if (flag == 1){
                    if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
                        T.showShort(this, "短信验证码不能为空");
                        return;
                    }

                }
                LoginMain(flag);
                break;
//            case R.id.btn_login: //快速登录
//                intent = new Intent(LoginActivity.this, RegisteActivity.class);
//                intent.putExtra("type", "denglu");
//                startActivity(intent);
//                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomcloseout);
//                break;
            case R.id.et_login_username:
                initAutoComplete("history", et_login_username);
                break;
            case R.id.btn_WX_login:
                WXEntryActivity.loginWeixin(this, Happlication.sApi);
                break;
            case R.id.btn_QQ_login:
                Log.e("qq", "o + temp-----------");
                platform.clear();
                platform.add(SHARE_MEDIA.QQ.toSnsPlatform());
                if (platform != null) {
                    UMShareAPI.get(this).getPlatformInfo(this, platform.get(0).mPlatform, mUmAuthListener);
                }
                break;
            case R.id.tv_code://验证码登录
                style_login.setBackgroundResource(R.drawable.num_login);
                tv_style_login.setText("验证码登录");
                rl_forget.setVisibility(View.GONE);
                rl_style_pwd.setVisibility(View.GONE);
                tv_pwd.setVisibility(View.VISIBLE);
                tv_code.setVisibility(View.GONE);
                rl_style_code.setVisibility(View.VISIBLE);
                flag = 1;
                break;
            case R.id.tv_pwd://密码登录
                style_login.setBackgroundResource(R.drawable.pwd_login);
                tv_style_login.setText("密码登录");
                rl_forget.setVisibility(View.VISIBLE);
                rl_style_pwd.setVisibility(View.VISIBLE);
                rl_style_code.setVisibility(View.GONE);
                tv_code.setVisibility(View.VISIBLE);
                tv_pwd.setVisibility(View.GONE);
                flag = 0;
                break;
            case R.id.btn_get_code:
                String phone = et_login_username.getText().toString().trim();
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
        }

    }

    /**
     * 获取验证码
     * @param phone 手机号
     */
    private void getAuthCode(String phone) {
        try {
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
                                T.showShort(LoginActivity.this,
                                        "验证码已发送请注意查收");
                            }
                        });
                        DingShiQiUtil.init(btn_get_code);
                        DingShiQiUtil.open();
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                T.showShort(LoginActivity.this,
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

    private UMAuthListener mUmAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            //授权开始的回调
            Log.e("qq", "onStart-------------");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            String temp = "";
            for (String key : map.keySet()) {
                temp = temp + key + " : " + map.get(key) + "\n";
            }
            Log.e("qq", "onComplete: ---------------" + temp);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Log.e("qq", "onError: ---------------" + throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void LoginMain(final int flag) {
        String url = "";
        RequestParams params = new RequestParams();
        params.put("staff_mobile", et_login_username.getText().toString());
        params.put("location", stringBuffer.toString());
        params.put("position", mPostion);
        if (flag == 0){
            params.put("staff_password", et_login_password.getText().toString());
            url = UrlTools.url + UrlTools.LOGIN_LOGIN;
        }else if (flag == 1){
            params.put("code",et_code.getText().toString().trim());
            url = UrlTools.url + UrlTools.CODE_LOGIN;
        }
        L.e("登录url=" + url + " params=" + params);
        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        super.onSuccess(arg0, arg1, arg2);
                        try {
                            JsonBean json = JsonUtils.getMessage(new String(
                                    arg2));
                            final Message mse = new Message();

                            if ("200".equals(json.getCode())) {
                                mse.what = LOGIN_SUCESS;
                                //保存账号
                                saveHistory("history", et_login_username);
//								saveZhangHao();
                            } else {
                                mse.what = LOGIN_ERROR;
                            }

                            if (flag == 0){
                                mse.obj = json;
                                handler.sendMessage(mse);
                            }else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(JSON_ERROR);
                        }

                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {

                        super.onFailure(arg0, arg1, arg2, arg3);
                        handler.sendEmptyMessage(SERVICE_ERROR);

                    }

                });

    }

    private void saveZhangHao() {
        SharedPreferences preferences = getApplication().getSharedPreferences("username", Context.MODE_PRIVATE);
        Set<String> set = new HashSet<String>();
        set = preferences.getStringSet("username", null);
        SharedPreferences.Editor editor = preferences.edit();
        if (set != null) {
            set.add(et_login_username.getText().toString());
            editor.remove("username");//清除必须要有
            editor.commit();//清除后必须再提交一下，否则没有效果
            editor.putStringSet("username", set);
            editor.commit();
        } else {
            set = new HashSet<String>();
            set.add(et_login_username.getText().toString());
            editor.putStringSet("username", set);
            editor.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Happlication.getInstance().exit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        DingShiQiUtil.close();// 关闭定时器；
    }
}

