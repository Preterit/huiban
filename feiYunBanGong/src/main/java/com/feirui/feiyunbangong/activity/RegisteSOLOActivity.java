package com.feirui.feiyunbangong.activity;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.SPUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 个人注册第2页
 * 
 * @author admina
 *
 */
public class RegisteSOLOActivity extends BaseActivity {
	// 手机号，姓名，密码，确认密码,邀请码
	@PView
	EditText et_phone, et_name, et_password, et_yaoqing;
	// 注册
	@PView(click = "onClick")
	Button btn_confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_solo);
		initView();
		RegisteActivity.list.add(this);
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("注册");
		setRightVisibility(false);
		et_phone.setText(getIntent().getStringExtra("phone"));
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:

			if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
				T.showShort(RegisteSOLOActivity.this, "手机号不能为空");
				return;
			}
			if (et_phone.getText().toString().trim().length() != 11) {
				T.showShort(RegisteSOLOActivity.this, "请输入11位手机号");
				return;
			}
			if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
				T.showShort(RegisteSOLOActivity.this, "姓名不能为空");
				return;
			}
			if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
				T.showShort(RegisteSOLOActivity.this, "密码不能为空");
				return;
			}
			/*if (TextUtils.isEmpty(et_affirm_password.getText().toString()
					.trim())) {
				T.showShort(RegisteSOLOActivity.this, "确认密码不能为空");
				return;
			}*/
			if (TextUtils.isEmpty(et_password.getText().toString().trim())
					|| et_password.getText().toString().trim().length() < 6
					|| et_password.getText().toString().trim().length() > 18) {
				T.showShort(RegisteSOLOActivity.this, "密码长度为6~18个字符");
				return;
			}
			/*if (!et_password.getText().toString().trim()
					.equals(et_affirm_password.getText().toString().trim())) {
				T.showShort(RegisteSOLOActivity.this, "两次密码输入不一致");
				return;
			}*/
			try {
				// 发请求
				RequestParams params = new RequestParams();
				params.put("staff_name", et_name.getText().toString().trim());
				params.put("staff_mobile", et_phone.getText().toString().trim());
				params.put("staff_password", et_password.getText().toString()
						.trim());
				if ("".equals(et_yaoqing.getText().toString().trim())) {
					params.put("incode", "");
				} else {
					params.put("incode", et_yaoqing.getText().toString().trim());
				}

				String url = UrlTools.url + UrlTools.ZHUCE_USERREGISTER;

				L.e("注册：url " + url + "  params:" + params);

				AsyncHttpServiceHelper.post(url, params,
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								super.onSuccess(arg0, arg1, arg2);
								final JsonBean json = JsonUtils
										.getMessage(new String(arg2));
								if ("200".equals(json.getCode())) {
									runOnUiThread(new Runnable() {
										public void run() {
											SPUtils.put(
													RegisteSOLOActivity.this,
													Constant.SP_USERNAME,
													et_phone.getText()
															.toString().trim());
											SPUtils.put(
													RegisteSOLOActivity.this,
													Constant.SP_PASSWORD,
													et_password.getText()
															.toString());
											T.showShort(
													RegisteSOLOActivity.this,
													json.getMsg());
											for (Activity b : RegisteActivity.list) {

												b.finish();
											}
										}
									});

								} else {
									runOnUiThread(new Runnable() {
										public void run() {
											T.showShort(
													RegisteSOLOActivity.this,
													json.getMsg());
										}
									});
								}

							}
						});

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		}

	}

}
