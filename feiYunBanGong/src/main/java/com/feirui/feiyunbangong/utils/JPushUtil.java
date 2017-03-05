package com.feirui.feiyunbangong.utils;

import java.util.Set;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.state.AppStore;

import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JPushUtil {

	// 初始化极光推送：
	public static void jOnCreate(Context context) {
		// TODO Auto-generated method stub
		JPushInterface.setDebugMode(true);
		JPushInterface.init(context);
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(
				context);
		builder.statusBarDrawable = R.drawable.logo;
		JPushInterface.setDefaultPushNotificationBuilder(builder);
	}

	// 极光推送onresume;
	public static void jOnResume(Context context) {
		JPushInterface.onResume(context);
	}

	// 极光推送暂停：
	public static void jOnPause(Context context) {
		JPushInterface.onPause(context);
	}

	/**
	 * 设置当前设备的别名
	 */
	public static void setJPSH(Context context) {
		// TODO Auto-generated method stub
		/*
		 * WifiManager wm = (WifiManager) context
		 * .getSystemService(Context.WIFI_SERVICE);
		 *//*********
		 * 网卡地址
		 * ***/
		/*
		 * String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		 */
		try {
			String phone = (String) AppStore.user.getInfor().get(0)
					.get("staff_mobile");
			Log.e("TAG", "设置别名：" + phone);
			setBieming(context, phone);
		} catch (Exception e) {
			Log.e("TAG", e.getMessage() + "设置别名出错！！！！！！");
		}
	}

	private static void setBieming(final Context context, final String phone) {
		Log.e("TAG", "开始设置了..............................");
		JPushInterface.setAlias(context, phone, new TagAliasCallback() {
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				Log.e("TAG", "2222222222222222222222222222");
				Log.e("TAG", "设置别名状态：" + arg0);
				if (arg0 != 0) {
					setBieming(context, phone);
				}

			}

		});
	}

}
