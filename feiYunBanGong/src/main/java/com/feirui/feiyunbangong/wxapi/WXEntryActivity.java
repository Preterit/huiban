package com.feirui.feiyunbangong.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.Constants;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.SPUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.wxapi.Bean.WXAccessTokenInfo;
import com.feirui.feiyunbangong.wxapi.Bean.WXErrorInfo;
import com.feirui.feiyunbangong.wxapi.Bean.WXUserInfo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.apache.http.util.TextUtils;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXEntryActivity";
    private Gson mGson;

    /**
     * 微信组件注册初始化
     *
     * @param context       上下文
     * @param weixin_app_id appid
     * @return 微信组件api对象
     */
    public static IWXAPI initWeiXin(Context context, String weixin_app_id) {
        if (TextUtils.isEmpty(weixin_app_id)) {
            Toast.makeText(context.getApplicationContext(), "app_id 不能为空", Toast.LENGTH_SHORT).show();
        }
        IWXAPI api = WXAPIFactory.createWXAPI(context, weixin_app_id, true);
        api.registerApp(weixin_app_id);
        return api;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Happlication.sApi.handleIntent(getIntent(), this);
        mGson = new Gson();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Happlication.sApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("支付结果页面", "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_tip);
            builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
            builder.show();
            if (resp.errCode == 0) {
                T.showShort(WXEntryActivity.this, "支付成功");
            } else {
                T.showShort(WXEntryActivity.this, "支付失败,请重试!");
            }
            finish();
        }

        String result = "";

        Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                ////拿到了微信返回的code,立马再去请求access_token
                String code = ((SendAuth.Resp) resp).code;
                //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                // 从手机本地获取存储的授权口令信息，判断是否存在access_token，不存在请求获取，存在就判断是否过期
                String accessToken = (String) SPUtils.get(this, "wx_access_token_key", "none");
                String openid = (String) SPUtils.get(this, "wx_openid_key", "");
                if (!"none".equals(accessToken)) {
                    // 有access_token，判断是否过期有效
                    isExpireAccessToken(accessToken, openid);
                } else {
                    // 没有access_token
                    getAccessToken(code);
                }
                result = "发送成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = "不支持错误";
                break;
            default:
                result = "发送返回";
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();

    }

    /**
     * 获取授权口令
     */
    private void getAccessToken(String code) {
        RequestParams params = new RequestParams();
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + Constants.APP_ID +
                "&secret=" + Constants.APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        // 网络请求获取access_token
        AsyncHttpServiceHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                Log.e("gson", "onSuccess:==================== " + new String(responseBody) );
                processGetAccessTokenResult(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
            }

        });

    }

    /**
     * 处理获取的授权信息结果
     *
     * @param response 授权信息结果
     */
    private void processGetAccessTokenResult(String response) {
        // 验证获取授权口令返回的信息是否成功
        if (validateSuccess(response)) {
            // 使用Gson解析返回的授权口令信息
            WXAccessTokenInfo tokenInfo = mGson.fromJson(response, WXAccessTokenInfo.class);
            Log.e("gson", "processGetAccessTokenResult: ----------------" + tokenInfo.toString());
            // 保存信息到手机本地
            saveAccessInfotoLocation(tokenInfo);
            // 获取用户信息
            getUserInfo(tokenInfo.getAccess_token(), tokenInfo.getOpenid());
        } else {
            // 授权口令获取失败，解析返回错误信息
            WXErrorInfo wxErrorInfo = mGson.fromJson(response, WXErrorInfo.class);
            Log.e("gson", wxErrorInfo.toString());
            // 提示错误信息
            T.showShort(WXEntryActivity.this, "错误信息: " + wxErrorInfo.getErrmsg());
        }
    }

    private void saveAccessInfotoLocation(WXAccessTokenInfo tokenInfo) {
        SPUtils.put(this, "wx_access_token_key", tokenInfo.getAccess_token());
        SPUtils.put(this, "wx_openid_key", tokenInfo.getOpenid());
    }

    /**
     * 验证是否成功
     *
     * @param response 返回消息
     * @return 是否成功
     */
    private boolean validateSuccess(String response) {
        String errFlag = "errmsg";
        return (errFlag.contains(response) && !"ok".equals(response))
                || (!"errcode".contains(response) && !errFlag.contains(response));
    }

    /**
     * 判断accesstoken是过期
     *
     * @param accessToken token
     * @param openid      授权用户唯一标识
     */
    private void isExpireAccessToken(final String accessToken, final String openid) {
        RequestParams params = new RequestParams();
        String url = "https://api.weixin.qq.com/sns/auth?" +
                "access_token=" + accessToken +
                "&openid=" + openid;
        AsyncHttpServiceHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                if (validateSuccess(new String(responseBody))) {
                    // accessToken没有过期，获取用户信息
                    getUserInfo(accessToken, openid);
                } else {
                    // 过期了，使用refresh_token来刷新accesstoken
                    refreshAccessToken();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                T.showShort(WXEntryActivity.this, "登录失败");
            }

        });
    }


    /**
     * 刷新获取新的access_token
     */
    private void refreshAccessToken() {
        RequestParams params = new RequestParams();
        // 从本地获取以存储的refresh_token
        final String refreshToken = (String) SPUtils.get(this, "wx_access_token_key", "");
        if (TextUtils.isEmpty(refreshToken)) {
            return;
        }
        // 拼装刷新access_token的url请求地址
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                "appid=" + Constants.APP_ID +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;

        // 请求执行
        AsyncHttpServiceHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Log.e("wx", "refreshAccessToken: " + new String(responseBody));
                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                processGetAccessTokenResult(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                Log.e("wx", "refreshAccessToken: " + error.getMessage());
                T.showShort(WXEntryActivity.this, "登录失败");
                // 重新请求授权
                loginWeixin(WXEntryActivity.this.getApplicationContext(), Happlication.sApi);
            }

        });
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo(String access_token, String openid) {
        RequestParams params = new RequestParams();
        String url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + access_token +
                "&openid=" + openid;
        AsyncHttpServiceHelper.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Log.e("gson", "refreshAccessToken: " + new String(responseBody));
                //用户信息获取结果：WXUserInfo{openid='ofFg80lk53s3-HC4o4Ni1A-iuArw', nickname='灵陌', sex=2, province='Hebei', city='Shijiazhuang', country='CN', headimgurl='http://wx.qlogo.cn/mmopen/vi_32/4bxHyHDMj53kPTa5q5FYWkbbjicO7CaxyJoVzEX7ibQQ6DqiceTPAykQZxcuzIKFfPWUURG1icwZmCydcJXia7qPozA/132', unionid='odSvZw3ZcEhaqjclXVH129gVo30c', privilege=[]}
                // 解析获取的用户信息
                WXUserInfo userInfo = mGson.fromJson(new String(responseBody), WXUserInfo.class);
                T.showShort(WXEntryActivity.this, "获取用户信息成功");
                Log.e("gson", "用户信息获取结果：" + userInfo.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                Log.e("gson", error.getMessage());
                T.showShort(WXEntryActivity.this, "获取用户信息失败");
                // 重新请求授权
                loginWeixin(WXEntryActivity.this.getApplicationContext(), Happlication.sApi);
            }

        });
    }

    /**
     * 登录微信
     *
     * @param api 微信服务api
     */
    public static void loginWeixin(Context context, IWXAPI api) {
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            T.showShort(context.getApplicationContext(), "您还未安装微信客户端！");
            return;
        }
        // 发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();
        // 应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
         */
        req.state = "app_wechat";
        api.sendReq(req);
    }


}