package com.feirui.feiyunbangong.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.BaiDuUtil;
import com.feirui.feiyunbangong.utils.ImageUtil;
import com.feirui.feiyunbangong.utils.SPUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UpdateManager;
import com.feirui.feiyunbangong.utils.UpdateManager.IsUpdate;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;

/**
 * 最先启动的activity用来判断是否是第一次登录，如果是第一次登录自动跳到新手指导页， 否则自动跳到首页
 *
 * @author feirui1
 */
public class SplashActivity extends BaseActivity implements IsUpdate {

    private ImageView iv;
    private boolean flag = true;// 长时间无任何反应的标识；
    private StringBuffer stringBuffer = new StringBuffer(256);
    private String mPostion;
    private SharedPreferences mShareds;
    private SharedPreferences.Editor mEditor;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 判断是否登陆过？
                    if (SPUtils.contains(SplashActivity.this,
                            Constant.SP_ALREADYUSED)
                            && (Boolean) (SPUtils.get(SplashActivity.this,
                            Constant.SP_ALREADYUSED, false))) {
                        LoginMain();
                    } else {// 没有登陆过
                        Intent intent = new Intent(SplashActivity.this,
                                GuideActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.aty_zoomin,
                                R.anim.aty_zoomout);
                        SplashActivity.this.finish();
                    }
                    break;
                case 1:
                    //获取定位
                    BaiDuUtil.initLocation(SplashActivity.this, new BDLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            try {
                                if (bdLocation != null && bdLocation.getLocType() != BDLocation.TypeServerError) {
                                    stringBuffer.append(bdLocation.getLatitude());//纬度
                                    stringBuffer.append(",");
                                    stringBuffer.append(bdLocation.getLongitude());//经度
                                    mPostion = bdLocation.getAddrStr();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mShareds = getSharedPreferences("position",Context.MODE_PRIVATE);
        mEditor = mShareds.edit();//获取编辑器
        iv = (ImageView) findViewById(R.id.imageView1);
        // 压缩：
        iv.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(
                getResources(), R.drawable.welcome_logo, 1000, 1500));

        //启动的同时 定位
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, 100);

        update();// 检查更新：

        // 定时，如果三秒钟扔无任何反应则执行start()操作：
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 该flag为true代表着需要自己去执行start();
                if (flag) {
                    start();
                    Log.e("TAG", "卡住了，自己去启动吧.....");
                }
            }
        }, 3000);

        // Utils.getFenBianLv(this);// 获取屏幕分辨率；
    }

    UpdateManager manager;

    private void start() {
        Log.e("TAG", "start()!!!!!!!!!!!!!!!!");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 1000);
    }

    private void update() {
        manager = new UpdateManager(this);
        // 检查软件是否需要更新
        manager.isUpdate(this);
    }

    @Override
    public void canUpdate() {
        flag = false;
        manager.showNoticeDialog();
    }

    @Override
    public void canNotUpdate() {
        flag = false;
        start();
    }

    @Override
    public void complete() {
        flag = false;
        start();
    }

    @Override
    public void cancel() {
        flag = false;
        start();
    }

    // 自动登录：
    private void LoginMain() {

        RequestParams params = new RequestParams();
        params.put("staff_mobile", (String) SPUtils.get(SplashActivity.this,
                Constant.SP_USERNAME, ""));
        params.put("staff_password", (String) SPUtils.get(SplashActivity.this,
                Constant.SP_PASSWORD, ""));
        params.put("location", stringBuffer.toString());
        params.put("position",mPostion);
        Log.e("string", "LoginMain: ---------LoginMain-------------" + params);
//        if (mEditor.)
        mEditor.putString("mPostion",mPostion); //保存登录时的位置信息
        mEditor.putString("mLocation",stringBuffer.toString()); //保存登录时的位置信息
        mEditor.commit();
        String url = UrlTools.url + UrlTools.LOGIN_LOGIN;

        Utils.doPost(null, this, url, params, new HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                SplashActivity.this.finish();
                AppStore.user = bean;
                T.showShort(SplashActivity.this, bean.getMsg());
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
            }

            @Override
            public void failure(String msg) {

                AppStore.user = null;
                T.showShort(SplashActivity.this, "自动登录失败，请重新登录！");
                startActivity(new Intent(SplashActivity.this,
                        LoginActivity.class));
                SplashActivity.this.finish();
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);

            }

            @Override
            public void finish() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
