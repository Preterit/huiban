package com.feirui.feiyunbangong.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.feirui.feiyunbangong.R;

public class LoadingDialog extends MyBaseDialog {

	// 加载时弹出的对话框：单例模式：
	public static LoadingDialog dialog;

	private LoadingDialog(final Context context) {
		super(context, R.style.mydialog); // 注意调这个父类的构造方法，给对话框设置一个样式！！！！！！
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题：
		setContentView(R.layout.ll_download_dialog);
	}

	public static LoadingDialog getInstance(Context context) {
		if (dialog != null) {
			return dialog;
		}
		return new LoadingDialog(context);
	}

}
