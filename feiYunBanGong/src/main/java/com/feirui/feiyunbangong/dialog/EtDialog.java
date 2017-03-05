package com.feirui.feiyunbangong.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

public class EtDialog extends MyBaseDialog {

	private Button bt_cancel, bt_ok;
	private EditText tv_alert;
	private TextView tv_title;

	public EtDialog(final String title, String hint, final Context context,
			final AlertCallBack1 callback) {
		super(context, R.style.mydialog); // 注意调这个父类的构造方法，给对话框设置一个样式！！！！！！

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题：
		setContentView(R.layout.dialog_xiugai);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_ok = (Button) findViewById(R.id.bt_ok);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_alert = (EditText) findViewById(R.id.tv_alert);
		tv_title.setText(title);

		tv_alert.setHint(hint);

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
				callback.onOK(tv_alert.getText().toString());
				dismiss();
			}
		});

	}

	public interface AlertCallBack1 {
		void onCancel();

		void onOK(String name);
	}

}
