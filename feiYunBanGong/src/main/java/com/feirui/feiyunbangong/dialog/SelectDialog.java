package com.feirui.feiyunbangong.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.feirui.feiyunbangong.R;

/**
 * 选择注册个人或企业
 */
public class SelectDialog extends MyBaseDialog {
	// 个人，企业
	LinearLayout ll_personage, ll_firm;

	/**
	 * @param context
	 */
	public SelectDialog(final Context context, final Callback callback) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_select);

		ll_personage = (LinearLayout) findViewById(R.id.ll_personage);
		ll_firm = (LinearLayout) findViewById(R.id.ll_firm);
		// 个人
		ll_personage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.onOK("personal");
				dismiss();
			}
		});
		// 企业
		ll_firm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callback.onOK("company");
				dismiss();
			}
		});

	}

	public interface Callback {

		void onOK(String s);

		void onCancel();

	}
}
