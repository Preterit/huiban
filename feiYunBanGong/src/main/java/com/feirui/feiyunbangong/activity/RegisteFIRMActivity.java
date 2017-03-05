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
 * 企业注册第2页
 * 
 * @author admina
 *
 */
public class RegisteFIRMActivity extends BaseActivity {
	// 手机号，公司名称，姓名，职位，密码，确认密码
	@PView
	EditText et_phone, et_firmName, et_name, et_post, et_password;
	// 注册
	@PView(click = "onClick")
	Button btn_confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_firm);
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
		case R.id.btn_confirm:// 注册按钮

			if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
				T.showShort(RegisteFIRMActivity.this, "手机号不能为空");
				return;
			}
			if (et_phone.getText().toString().trim().length() != 11) {
				T.showShort(RegisteFIRMActivity.this, "请输入11位手机号");
				return;
			}
			if (TextUtils.isEmpty(et_firmName.getText().toString().trim())) {
				T.showShort(RegisteFIRMActivity.this, "公司姓名不能为空");
				return;
			}
			if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
				T.showShort(RegisteFIRMActivity.this, "姓名不能为空");
				return;
			}
			if (TextUtils.isEmpty(et_post.getText().toString().trim())) {
				T.showShort(RegisteFIRMActivity.this, "职位不能为空");
				return;
			}
			if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
				T.showShort(RegisteFIRMActivity.this, "密码不能为空");
				return;
			}
			/*
			 * if (TextUtils.isEmpty(et_affirm_password.getText().toString()
			 * .trim())) { T.showShort(RegisteFIRMActivity.this, "确认密码不能为空");
			 * return; }
			 */
			if (TextUtils.isEmpty(et_password.getText().toString().trim())
					|| et_password.getText().toString().trim().length() < 6
					|| et_password.getText().toString().trim().length() > 18) {
				T.showShort(RegisteFIRMActivity.this, "密码长度为6~18个字符");
				return;
			}
			/*
			 * if (!et_password.getText().toString().trim()
			 * .equals(et_affirm_password.getText().toString().trim())) {
			 * T.showShort(RegisteFIRMActivity.this, "两次密码输入不一致"); return; }
			 */
			try {
				// 发请求
				RequestParams params = new RequestParams();
				params.put("admin_mobile", et_phone.getText().toString().trim());
				params.put("company_name", et_firmName.getText().toString()
						.trim());
				params.put("admin_name", et_name.getText().toString().trim());
				params.put("admin_duties", et_post.getText().toString().trim());
				params.put("pwd1", et_password.getText().toString().trim());
				params.put("pwd2", et_password.getText().toString().trim());

				String url = UrlTools.url + UrlTools.ZHUCE_COMPANY_ADD;

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
													RegisteFIRMActivity.this,
													Constant.SP_USERNAME,
													et_phone.getText()
															.toString().trim());
											SPUtils.put(
													RegisteFIRMActivity.this,
													Constant.SP_PASSWORD,
													et_password.getText()
															.toString());
											T.showShort(
													RegisteFIRMActivity.this,
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
													RegisteFIRMActivity.this,
													json.getMsg());
										}
									});
								}
							}

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								T.showShort(RegisteFIRMActivity.this, "失败！");
								super.onFailure(arg0, arg1, arg2, arg3);
							}
						});

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		}

	}

}
