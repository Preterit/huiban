package com.feirui.feiyunbangong.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

public class DownLoadDialog extends MyBaseDialog {

	private Button bt_cancel;
	private ProgressBar pb;
	private TextView tv_progress;

	// 版本更新：
	public DownLoadDialog(final Context context, final DownLoadCallback callback) {
		super(context, R.style.mydialog); // 注意调这个父类的构造方法，给对话框设置一个样式！！！！！！

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题：
		setContentView(R.layout.ll_myalert_dialog);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);

		pb = (ProgressBar) findViewById(R.id.update_progress);
		tv_progress = (TextView) findViewById(R.id.tv_progress);
		ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.rgb(98, 192,
				253)), Gravity.LEFT, ClipDrawable.HORIZONTAL);
		pb.setProgressDrawable(d);

		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onCancel();
				dismiss();
			}
		});

	}

	public interface DownLoadCallback {
		void onCancel();
	}

	// 设置进度：
	public void setProgress(int pos) {
		pb.setProgress(pos);
		tv_progress.setText(pos + "%");
	}

}
