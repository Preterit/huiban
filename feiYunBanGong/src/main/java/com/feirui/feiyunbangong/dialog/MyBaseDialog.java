package com.feirui.feiyunbangong.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.feirui.feiyunbangong.R;

public class MyBaseDialog extends Dialog {

	public MyBaseDialog(Context context, int theme) {
		super(context, theme);
	}

	public MyBaseDialog(Context context) {
		super(context);
	}

	// 重写onstart方法，给对话框一个自定义动画效果：
	@Override
	protected void onStart() {
		super.onStart();
		/*Window window = getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.windowAnimations = R.style.Dialog_animation;
		window.setAttributes(layoutParams);*/
	}

}
