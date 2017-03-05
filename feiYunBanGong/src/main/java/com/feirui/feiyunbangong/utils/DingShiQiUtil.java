package com.feirui.feiyunbangong.utils;

import android.os.Handler;
import android.widget.Button;

public class DingShiQiUtil {

	static Handler han1, han2;
	static Runnable run1, run2;
	static int position = 60;

	// 初始化定时器：
	public static void init(final Button bt_yanzhengma) {
		position = 60;

		bt_yanzhengma.setEnabled(false);
		
		han1 = new Handler();
		han2 = new Handler();

		run1 = new Runnable() {
			@Override
			public void run() {
				position--;
				if (bt_yanzhengma.isEnabled()) {
					return;
				}
				bt_yanzhengma.setText(position + "秒后重新发送");
				if (!bt_yanzhengma.isEnabled()) {
					han1.postDelayed(this, 1000);
				}
			}
		};

		run2 = new Runnable() {
			@Override
			public void run() {
				bt_yanzhengma.setEnabled(true);
				position = 60;
				bt_yanzhengma.setText("获取验证码");
			}
		};
	}

	// 开始定时：
	public static void open() {
		han1.postDelayed(run1, 1000);
		han2.postDelayed(run2, 60 * 1000);
	}

	// 停止定时：
	public static void close() {
		if (han1 != null && run1 != null) {
			han1.removeCallbacks(run1);
		}
		if (han2 != null && han2 != null) {
			han2.removeCallbacks(run2);
		}
	}

}
