package com.feirui.feiyunbangong.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.feirui.feiyunbangong.activity.DaiShenPiActivity;
import com.feirui.feiyunbangong.activity.DetailGongGaoActivity;
import com.feirui.feiyunbangong.activity.TuanDui_DetailActivity;
import com.feirui.feiyunbangong.activity.MainActivity;
import com.feirui.feiyunbangong.activity.NewFriendActivity;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.state.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @SuppressWarnings("unused")
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        JSONObject json = null;

        Log.e("TAG", "接收到推送相关广播......");

        try {

            boolean isHave = false;

            for (String key : bundle.keySet()) {
                // 判断是否含有这个键值；
                if (JPushInterface.EXTRA_EXTRA.equals(key)) {
                    isHave = true;
                    break;
                }
            }

            if (!isHave) {
                Log.e("TAG", "不含有！！！！");
                return;
            }

            Log.e("TAG", "含有");
            json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));

            Log.e(TAG, "onReceive: " + json.toString());

            if (json == null) {
                return;
            }

            Log.e("TAG", json + "json");

            if ("addnotice".equals(json.get("key"))) {
                // 发送广播：
                context.sendBroadcast(new Intent(Constant.GET_TEAM_BROADCAST));
                // 接收到团队新增成员推送：
            } else if ("member_add".equals(json.get("key"))) {
                Log.e("TAG", "团队新增成员、。。。。");
                // 团队新增成员发出的广播：
                Intent i = new Intent();
                i.setAction(Constant.ON_RECEIVE_NEW_MEMBER_ADD);
                i.putExtra("id", json.get("team_id") + "");
                context.sendBroadcast(i);
                Log.e("TAG", "团队新增成员、。走了吗是。。。");
                // 您被邀请加入团队：刷新团队列表接口：
            } else if ("team_member_add".equals(json.get("key"))) {
                Log.e("TAG", "您被邀请加入团队");
                // 发出被邀请加入团队的广播：刷新查看团队页面接口：
                context.sendBroadcast(new Intent(Constant.ON_REACEIVE_ADD_TEAM));
            } else if ("add_leave".equals("key")) {
                context.sendBroadcast(new Intent(Constant.NEED_TO_SHEN_HE));
            } else {
                Log.e(TAG, "onReceive: -------------添加好友-----------------" );
                context.sendBroadcast(new Intent(
                        Constant.GET_BROADCAST_ABOUT_FRIEND));
            }

        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }

        Log.e(TAG, "onReceive: -------------添加好友-----------------" );
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            /*
             * String regId = bundle
			 * .getString(JPushInterface.EXTRA_REGISTRATION_ID);
			 */
            // Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            // send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            /*
             * Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " +
			 * bundle.getString(JPushInterface.EXTRA_MESSAGE));
			 * processCustomMessage(context, bundle);
			 */
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            /*
             * Log.e(TAG, "[MyReceiver] 接收到推送下来的通知"); int notifactionId = bundle
			 * .getInt(JPushInterface.EXTRA_NOTIFICATION_ID); Log.e(TAG,
			 * "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
			 */
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {

            Log.e("TAG", "点开了通知！！！！！！！");
            /* Log.e(TAG, "[MyReceiver] 用户点击打开了通知"); */
            Object object = null;
            try {
                object = json.get("key");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (Constant.JPUSH.QINGJIA_SHENPI.equals(object)) {//收到请假审批


                Intent shenpiDetailIntent = new Intent(context, DaiShenPiActivity.class);
                HashMap<String, Object> map = new HashMap<>();

                context.startActivity(intent);
            }

            if ("addfriend".equals(object)) {
                openAdd(context, bundle);
            } else if ("accept".equals(object)) {
                openAccept(context, bundle);
            } else if ("addnotice".equals(object + "")) {
                try {
                    Object teamid = json.get("key2");
                    Log.e("TAG", "teamid" + teamid);
                    openTeamNotice(context, teamid);// 打开团队公告；
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 有新成员加入团队：
            } else if ("member_add".equals(object + "")) {
                try {
                    // 团队有新成员加入：
                    Object id = json.get("team_id");
                    Object name = json.get("team_name");
                    TuanDui td = new TuanDui();
                    td.setTid(id + "");
                    td.setName(name + "");
                    // 跳转到查看团队页面：
                    openDetailTeam(context, td);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if ("team_member_add".equals(object + "")) {
                // 您被邀请加入某团队：
                Log.e("TAG", "您已加入新团队！");
                try {
                    Object id = json.get("team_id");
                    Object name = json.get("team_name");
                    TuanDui td = new TuanDui();
                    td.setTid(id + "");
                    td.setName(name + "");
                    // 跳转到查看团队页面：
                    openDetailTeam(context, td);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("TAG", "新好友申请！");
                openAccept(context, bundle);
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction()))

        {
            /*
             * Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " +
			 * bundle.getString(JPushInterface.EXTRA_EXTRA));
			 */
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction()))

        {
            /*
             * boolean connected = intent.getBooleanExtra(
			 * JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			 */
            /*
             * Log.e(TAG, "[MyReceiver]" + intent.getAction() +
			 * " connected state change to " + connected);
			 */
        } else

        {
            // Log.i(TAG, "[MyReceiver] Unhandled intent - " +
            // intent.getAction());
        }

    }

    // 跳转到查看团队页面：
    private void openDetailTeam(Context context, TuanDui td) {
        Intent i = new Intent(context, TuanDui_DetailActivity.class);
        i.putExtra("tuanDui", td);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    private void openTeamNotice(Context context, Object teamid) {
        // 跳转到查看公告页面：
        Intent i = new Intent(context, DetailGongGaoActivity.class);
        i.putExtra("id", teamid + "");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    // 打开添加朋友页面：
    public void openAdd(Context context, Bundle bundle) {
        Intent i = new Intent(context, NewFriendActivity.class);

        i.putExtras(bundle); //
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    //打开审批页面
    public void openShenpi(Context context, Bundle bundle) {

    }

    // 打开接受朋友页面：
    public void openAccept(Context context, Bundle bundle) {
        // 打开自定义的Activity
        Intent i = new Intent(context, MainActivity.class);
        i.putExtras(bundle);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

}
