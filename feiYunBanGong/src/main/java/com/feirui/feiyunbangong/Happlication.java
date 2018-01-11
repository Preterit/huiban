package com.feirui.feiyunbangong;


import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.tcms.env.YWEnvType;
import com.alibaba.wxlib.util.SysUtil;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.stetho.Stetho;
import com.feirui.feiyunbangong.entity.Constants;
import com.feirui.feiyunbangong.utils.IMUtil;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.feirui.feiyunbangong.wxapi.WXEntryActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.litepal.LitePal;

import java.util.ArrayList;

public class Happlication extends MultiDexApplication {
    // application实例
    private static Happlication instance;
    public static boolean isDebug;// 是否打开调试
    private static Context context;
    // 应用程序中创建的每个activity
    private static ArrayList<Activity> activitylist = new ArrayList<Activity>();
    public static String APP_KEY = "23529997";
    public static YWEnvType sEnvType = YWEnvType.TEST;

    private String out_trade_no;
    public static IWXAPI sApi;


    /**
     * 实现单例模式
     *
     * @return
     */
    public static Happlication getInstance() {
        isDebug = true;
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化LitePal
        LitePal.initialize(this);
        Stetho.initializeWithDefaults(this);
        // 必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
        SysUtil.setApplication(this);
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        }
        // 第一个参数是Application Context
        // 这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
        if (SysUtil.isMainProcess()) {
            IMUtil.bind();// 绑定自定义会话列表等；
            YWAPI.init(this, APP_KEY);
        }

        // 初始化ImageLoader;
        ImageLoaderConfiguration config = ImageLoaderUtils.getNowConfig(this);
        ImageLoader.getInstance().init(config);

		/*
         * // 使用andbase进行屏幕适配： AbAppConfig.UI_WIDTH = 1080;
		 * AbAppConfig.UI_HEIGHT = 1920;
		 */
        SDKInitializer.initialize(getApplicationContext());
        // 初始化微信组件
        initWeiXin();
    }

    private void initWeiXin() {
        sApi = WXEntryActivity.initWeiXin(this, Constants.APP_ID);
    }


    public static ArrayList<Activity> getActivities() {
        return activitylist;
    }

    public static Context getAppContext() {
        return Happlication.context;
    }

    public static void ClearAty() {
        try {
            for (Activity activity : activitylist) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void exit() {
        try {
            for (Activity activity : activitylist) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }

    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
            MultiDex.install(this) ;
       }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String number) {
        this.out_trade_no = number;
    }
}
