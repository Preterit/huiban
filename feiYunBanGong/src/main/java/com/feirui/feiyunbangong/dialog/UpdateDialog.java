package com.feirui.feiyunbangong.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

/**
 * 更新提示
 */
public class UpdateDialog extends MyBaseDialog {

	Button bt_update, bt_cancel;
	TextView tv_content;

	// 版本更新：
	public UpdateDialog(final Context context, final UpdataCallback callback,
			String content) {
		super(context, R.style.mydialog); // 注意调这个父类的构造方法，给对话框设置一个样式！！！！！！
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题：
		setContentView(R.layout.ll_update_dialog);
		tv_content = (TextView) findViewById(R.id.tv_content);
		try {
			if (content != null && content.length() > 0) {
				String[] split = content.split(";");
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < split.length; i++) {
					sb.append(split[i]);
					if (i < split.length - 1) {
						sb.append("\n");
					}
				}
				tv_content.setText(sb.toString());
			} else {
				tv_content
						.setText("1.进一步完善工作圈功能\n2.修改了一些bug\n3.实现团队管理功能\n4.UI界面更加美观");
			}
		} catch (Exception e) {
			Log.e("TAG", e.getMessage());
		}
		bt_update = (Button) findViewById(R.id.bt_update);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);

		// 个人
		bt_update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onOK();
				dismiss();
			}
		});

		// 企业
		bt_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.onCancel();
				dismiss();
			}
		});

	}

	public interface UpdataCallback {

		void onOK();

		void onCancel();

	}
}
