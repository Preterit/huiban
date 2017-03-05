package com.feirui.feiyunbangong.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

public class MyAleartDialog extends MyBaseDialog {

	private Button bt_cancel, bt_ok;
	private TextView tv_title, tv_alert;

	// 版本更新：
	public MyAleartDialog(String title, String content, final Context context,
			final AlertCallBack callback) {
		super(context, R.style.mydialog); // 注意调这个父类的构造方法，给对话框设置一个样式！！！！！！
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题：
		setContentView(R.layout.dialog_alert);
		
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_ok = (Button) findViewById(R.id.bt_ok);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_alert = (TextView) findViewById(R.id.tv_alert);

		if (title == null) {
			tv_title.setVisibility(View.GONE);
		} else {
			tv_title.setText(title);
		}
		if (content == null) {
			tv_alert.setVisibility(View.GONE);
		} else {
			tv_alert.setText(content);
		}

		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onCancel();
				dismiss();
			}
		});

		bt_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.onOK();
				dismiss();
			}
		});

	}
	
	

	public interface AlertCallBack {
		void onCancel();

		void onOK();
	}

}
