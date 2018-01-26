package com.feirui.feiyunbangong.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.EtDialog;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.SPUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.PView;
import com.google.zxing.WriterException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.zxing.encoding.EncodingHandler;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * 团队——加
 *  含分享
 *
 * @author admina
 *         (团队退出按钮)
 */
public class TuanDuiJiaActivity extends BaseActivity implements OnClickListener {
    @PView(click = "onClick")
    LinearLayout ll_saoma, ll_tuiguang, ll_guanli, ll_send_msg, ll_send_talk, ll_xgxx,ll_send_share,ll_update_name;// 扫码，推广，管理,短信邀请；团队聊天,修改信息,分享
    private TuanDui td;
    private Button bt_out_team;//退出团队；
    private String mTuanLiaoID;
    private IYWTribeService mService;
    private YWIMKit mYWIMkit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuan_dui_jia);
        mYWIMkit = AppStore.mIMKit;
        initView();
        setListener();
    }

    private void setListener() {
        bt_out_team.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                out();
            }
        });
    }

    private void initView() {
        AppStore.acts.add(this);
        Intent intent = getIntent();
        //传过来的团队
        td = (TuanDui) intent.getSerializableExtra("td");
        bt_out_team = (Button) findViewById(R.id.bt_out_team);
        Log.e("tdys", "initView: -----------------" + td.toString() );
        setManage(td);
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setRightVisibility(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTuanLiaoId(); //获取该团聊的ID
        String name = (String) SPUtils.get(this,td.getTid(),"");
        Log.e("name", "onResume: -------jia------" + name );
        if (!name.equals(td.getName()) && !TextUtils.isEmpty(name)){
            if (name.length() > 10) {
                setCenterString(name.substring(0, 9) + "...");
            } else {
                setCenterString(name);
            }
        }else {
            if (td.getName().length() > 10) {
                setCenterString(td.getName().substring(0, 9) + "...");
            } else {
                setCenterString(td.getName());
            }
        }
    }

    // 设置管理员的显示与隐藏：
    private void setManage(TuanDui td) {
        if (td.getGuanli_id() != null
                && String.valueOf(td.getGuanli_id()).equals(
                String.valueOf(AppStore.user.getInfor().get(0).get("id")))) {
            ll_guanli.setVisibility(View.VISIBLE);
            ll_update_name.setVisibility(View.VISIBLE);
            bt_out_team.setVisibility(View.INVISIBLE);//管理员不显示退出团队；
        } else {
            bt_out_team.setVisibility(View.VISIBLE);
            ll_update_name.setVisibility(View.GONE);
            ll_guanli.setVisibility(View.GONE);
        }
    }

    @SuppressLint("InflateParams")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_saoma:
                Dialog dialog = new Dialog(this);
                View v = getLayoutInflater().inflate(R.layout.ll_dialog_erweima_tuandui, null);
                ImageView iv = (ImageView) v.findViewById(R.id.iv_erweima2);
                String id = "T" + td.getTid();
                // 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                try {
                    Bitmap erweima = EncodingHandler.createQRCode(id, 350);
                    iv.setImageBitmap(erweima);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                dialog.setContentView(v);
                dialog.setTitle("扫码加入团队");
                dialog.show();
                break;
            case R.id.ll_update_name://修改团队名称
                Intent intent3 = new Intent(this,TuanDui_UpdateNameActivity.class);
                intent3.putExtra("id", td.getTid());
                startActivity(intent3);
//            case R.id.ll_tuiguang:
//                RequestParams params = new RequestParams();
//                params.put("teamid", td.getTid());
//                String url = UrlTools.url + UrlTools.CIRCLE_ADDTEAMCIRCLE;
//                L.e("推广——工作圈url" + url + " params" + params);
//                AsyncHttpServiceHelper.post(url, params,
//                        new AsyncHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int arg0, Header[] arg1,
//                                                  byte[] arg2) {
//                                super.onSuccess(arg0, arg1, arg2);
//                                final JsonBean json = JsonUtils
//                                        .getMessage(new String(arg2));
//                                if ("200".equals(json.getCode())) {
//                                    runOnUiThread(new Runnable() {
//                                        public void run() {
//                                            T.showShort(TuanDuiJiaActivity.this,
//                                                    json.getMsg());
//                                        }
//                                    });
//
//                                } else {
//                                    T.showShort(TuanDuiJiaActivity.this,
//                                            json.getMsg());
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(int arg0, Header[] arg1,
//                                                  byte[] arg2, Throwable arg3) {
//                                super.onFailure(arg0, arg1, arg2, arg3);
//                            }
//                        });
                break;
            case R.id.ll_guanli:  //团长管理团队 将整个团队传过去
                Intent intent = new Intent(this, TuanDuiGuanLiActivity.class);
                intent.putExtra("td", td);
                startActivityForResult(intent, 500);
                break;
            case R.id.ll_xgxx:
                Intent intent1 = new Intent(this, XiuGaiChengYuanActivity.class);
                intent1.putExtra("td", td);
                startActivity(intent1);
                break;
            case R.id.ll_send_msg:
                EtDialog dialog1 = new EtDialog("短信邀请", "请输入手机号",
                        TuanDuiJiaActivity.this, new EtDialog.AlertCallBack1() {
                    @Override
                    public void onOK(String name) {
                        if (Utils.isPhone(name)) {
                            String url = UrlTools.url + UrlTools.SEND_MSG;
                            RequestParams params = new RequestParams();
                            params.put("staff_mobile", name);
                            params.put("teamnum", td.getTid());
                            Utils.doPost(LoadingDialog.getInstance(TuanDuiJiaActivity.this),
                                    TuanDuiJiaActivity.this, url, params,
                                    new HttpCallBack() {
                                        @Override
                                        public void success(JsonBean bean) {
                                            T.showShort(TuanDuiJiaActivity.this, "发送成功！");
                                        }
                                        @Override
                                        public void failure(String msg) {
                                            T.showShort(TuanDuiJiaActivity.this, msg);
                                        }
                                        @Override
                                        public void finish() {
                                            // TODO Auto-generated method stub
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancel() {
                    }
                });
                dialog1.show();
                break;
            case R.id.ll_send_talk:  //团队聊天
                YWIMKit mIMKit = AppStore.mIMKit;
                //参数为群ID号
                Intent intent2 = mIMKit.getTribeChattingActivityIntent(Long.parseLong(mTuanLiaoID));
                startActivity(intent2);
                break;
            case R.id.ll_send_share://分享
                getTeamInfor();
                break;
        }
    }
    // 退出团队：
    private void out() {
        String url = UrlTools.url + UrlTools.OUT_TEAM;
        RequestParams params = new RequestParams();
        params.put("team_id", td.getTid());
        Log.e("TAG", td.getTid() + "td.getid()");
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
                new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        //从本地数据库移除
                        DataSupport.deleteAll(TuanDui.class, "tid = ?", td.getTid());
                        T.showShort(TuanDuiJiaActivity.this, "退出成功！");
                        for (int i = 0; i < AppStore.acts.size(); i++) {
                            AppStore.acts.get(i).finish();
                        }
                        if (!"".equals(mTuanLiaoID)) {
                            outQun();  //退出团队群聊
                        }
                    }

                    @Override
                    public void failure(String msg) {
                        T.showShort(TuanDuiJiaActivity.this, msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });

    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 团队团聊Id
     */
    public void getTuanLiaoId() {
        String url = UrlTools.url + UrlTools.GET_TUANLIAOID;
        RequestParams params = new RequestParams();
        params.put("team_id", td.getTid());
        AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                JsonBean bean = JsonUtils.getMessage(new String(arg2));
                if ("200".equals(bean.getCode())) {
                    mTuanLiaoID = bean.getInfor().get(0).get("team_talk") + "";
                    if ("".equals(mTuanLiaoID) || mTuanLiaoID == null) {
                        ll_send_talk.setVisibility(View.GONE);
                    } else {
                        ll_send_talk.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("chengyuan", "handleMessage: -----------------" + bean.getMsg());
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // 已经移交了管理员：
        if (requestCode == 500 && resultCode == 100) {
            ll_guanli.setVisibility(View.GONE);
            ll_send_talk.setVisibility(View.GONE);
        }
        super.onActivityResult(requestCode, resultCode, intent);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄漏
        UMShareAPI.get(this).release();
    }

    /**
     * 退出群聊
     */
    public void outQun() {
        mService = mYWIMkit.getTribeService();
        mService.exitFromTribe(new MyCallBack() {
            @Override
            public void onSuccess(Object... objects) {

            }

            @Override
            public void onError(int i, String s) {
                T.showShort(TuanDuiJiaActivity.this, "退出团聊失败--" + s);
            }
        }, Long.parseLong(mTuanLiaoID));
    }

    /**
     * 获取团队信息
     * @return
     */
    public void getTeamInfor() {
        String url = UrlTools.url + UrlTools.TEAM_INFOR;
        RequestParams params = new RequestParams();
        params.put("team_id",td.getTid());
        AsyncHttpServiceHelper.post(url,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                JsonBean bean = JsonUtils.getMessage(new String(responseBody));
                if ("200".equals(bean.getCode())){
                    Log.e("infor", "onSuccess:------------------------- " +bean.getInfor().get(0));
                    ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                    String name = infor.get(0).get("team_name") + "";
                    String pic = infor.get(0).get("pic") + "";
                    String introduction = infor.get(0).get("team_introduction") + "";
                    shareTeam(name,introduction,pic);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
            }
        });

    }

    /**
     * 分享团队
     * @param name 团队名称
     * @param introduction 简介
     * @param pic 头像
     */
    private void shareTeam(String name, String introduction, String pic) {
        ShareBoardConfig config = new ShareBoardConfig();
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
        UMWeb  web = new UMWeb(UrlTools.umeng_url + "?team_id=" + td.getTid());
        String title = "";
        if (!TextUtils.isEmpty(name)){
            title = name ;
            web.setTitle(name + "通讯录");//标题
        }else {
            title = td.getTid();
            web.setTitle(td.getTid() + "团队通讯录");//标题
        }
        if (!TextUtils.isEmpty(pic)){
            web.setThumb(new UMImage(this,pic));  //缩略图
        }else {
            web.setThumb(new UMImage(this,R.drawable.logo));  //缩略图
        }

        if (!TextUtils.isEmpty(introduction)){
            web.setDescription(introduction);//描述
        }else {
            web.setDescription("会办是一个社群化的兴趣交友平台，找朋友、找圈子，通过个性化关键词的设置和搜索，精准匹配您附近的小伙伴。");//描述
        }
        new ShareAction(this).setDisplayList(
                SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        T.showShort(TuanDuiJiaActivity.this,share_media + "分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        T.showShort(TuanDuiJiaActivity.this,share_media + "分享失败");
                        Log.e("share", "onError:------------------- " + throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })
                .open(config);
    }


    /**
     * 请求回调的接口
     */
    public static abstract class MyCallBack implements IWxCallback {
        @Override
        public void onProgress(int i) {

        }
    }
}