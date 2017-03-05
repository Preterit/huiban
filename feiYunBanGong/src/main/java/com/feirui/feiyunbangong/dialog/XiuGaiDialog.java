package com.feirui.feiyunbangong.dialog;

import org.apache.http.Header;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.mobileim.channel.event.IWxCallback;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.IMUtil;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class XiuGaiDialog extends MyBaseDialog {

	private Button bt_cancel, bt_ok;
	private EditText tv_alert;
	private TextView tv_title;

	public XiuGaiDialog(final String title, final String id, String name,
			final Context context, final AlertCallBack1 callback) {
		super(context, R.style.mydialog); // 注意调这个父类的构造方法，给对话框设置一个样式！！！！！！

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题：
		setContentView(R.layout.dialog_xiugai);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_ok = (Button) findViewById(R.id.bt_ok);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_alert = (EditText) findViewById(R.id.tv_alert);
		tv_title.setText(title);
		tv_alert.setHint(name);

		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onCancel();
				dismiss();
			}
		});

		// 修改本地缓存中的备注：
		bt_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				RequestParams params = new RequestParams();
				String url = "";

				if ("修改备注".equals(title)) {
					// 修改阿里百川缓存数据：
					Log.e("TAG", AppStore.phone + "备注信息id");

					/*
					 * IMUtil.remark(AppStore.phone,
					 * tv_alert.getText().toString(), new IWxCallback() {
					 * 
					 * @Override public void onSuccess(Object... arg0) {
					 * Log.e("TAG", "修改成功！！！！！！！！！！！！！！！！"); }
					 * 
					 * @Override public void onProgress(int arg0) { }
					 * 
					 * @Override public void onError(int arg0, String arg1) { }
					 * });
					 */
					if (TextUtils.isEmpty(tv_alert.getText().toString().trim())) {
						T.showShort(context, "备注名不能为空！");
						return;
					}

					params.put("person_id", id);
					params.put("remark", tv_alert.getText().toString().trim());
					url = UrlTools.url + UrlTools.USER_UPDATREMARK;
					L.e("修改备注url" + url + " params" + params);
				} else if ("添加".equals(id)) {

					if (TextUtils.isEmpty(tv_alert.getText().toString().trim())) {
						T.showShort(context, "分组名不能为空！");
						return;
					}
					params.put("name", tv_alert.getText().toString().trim());
					url = UrlTools.url + UrlTools.USER_ADDFRIENDGROUP;
					L.e("添加分组url" + url + " params" + params);
				} else {
					if (TextUtils.isEmpty(tv_alert.getText().toString().trim())) {
						T.showShort(context, "分组名不能为空！");
						return;
					}
					params.put("id", id);
					params.put("name", tv_alert.getText().toString().trim());
					url = UrlTools.url + UrlTools.USER_UPDATEFRIENDGROUP;
					L.e("修改分组名url" + url + " params" + params);
				}

				AsyncHttpServiceHelper.post(url, params,
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								super.onSuccess(arg0, arg1, arg2);
								final JsonBean json = JsonUtils
										.getMessage(new String(arg2));
								if ("200".equals(json.getCode())) {

									callback.onOK(tv_alert.getText().toString()
											.trim());
									dismiss();

								} else {
									((Activity) context)
											.runOnUiThread(new Runnable() {
												public void run() {
													T.showShort(context,
															json.getMsg());
												}
											});
								}
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								super.onFailure(arg0, arg1, arg2, arg3);

							}
						});

				dismiss();
			}
		});

	}

	public interface AlertCallBack1 {
		void onCancel();

		void onOK(String name);
	}

}
