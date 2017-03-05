package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.DingShiQiUtil;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 注册,忘记密码第一页获取验证码
 * 
 * @author admina
 *
 */
public class RegisteActivity extends BaseActivity {
	// 手机号，验证码
	@PView
	EditText et_phone, et_verify;
	// 下一步,获取验证码
	@PView(click = "onClick")
	Button btn_confirm, bt_checking;
	String type = "";// 注册类型
	public static ArrayList<Activity> list = new ArrayList<Activity>();

	@Override
	protected void onDestroy() {
		DingShiQiUtil.close();// 关闭定时器；
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
		list.add(this);
	}

	private void initView() {
		type = getIntent().getStringExtra("type");
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		if (!type.equals("wangjimima")) {
			setCenterString("注册");
		} else {
			setCenterString("忘记密码");
		}
		setRightVisibility(false);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_checking:// 获取验证码
			String phone = et_phone.getText().toString().trim();
			if (TextUtils.isEmpty(phone)) {
				T.showShort(RegisteActivity.this, "手机号不能为空！");
				return;
			}

			if (!Utils.isPhone(phone)) {
				T.showShort(RegisteActivity.this, "手机号格式错误！");
				return;
			}

			DingShiQiUtil.init(bt_checking);
			DingShiQiUtil.open();

			try {
				String url = "";
				// 发请求
				RequestParams params = new RequestParams();

				if (!type.equals("wangjimima")) {
					params.put("admin_mobile", et_phone.getText().toString()
							.trim());
					params.put("type", type);
					url = UrlTools.url + UrlTools.ZHUCE_REGIST;
					L.e("注册获取验证码：url " + url + "  params:" + params);
				} else {
					params.put("staff_mobile", et_phone.getText().toString()
							.trim());
					url = UrlTools.url + UrlTools.ZHUCE_FORGET_PWD;
					L.e("忘记密码获取验证码：url " + url + "  params:" + params);
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
									runOnUiThread(new Runnable() {
										public void run() {
											T.showShort(RegisteActivity.this,
													"验证码已发送请注意查收");
										}
									});

								} else {
									runOnUiThread(new Runnable() {
										public void run() {
											T.showShort(RegisteActivity.this,
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

		case R.id.btn_confirm:
			// 下一步
			if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
				T.showShort(RegisteActivity.this, "手机号不能为空");
				return;
			}
			if (et_phone.getText().toString().trim().length() != 11) {
				T.showShort(RegisteActivity.this, "请输入11位手机号");
				return;
			}

			if (TextUtils.isEmpty(et_verify.getText().toString().trim())) {
				T.showShort(RegisteActivity.this, "验证码不能为空");
				return;
			}
			try {
				// 发请求
				RequestParams params = new RequestParams();
				params.put("mobile", et_phone.getText().toString().trim());
				params.put("code", et_verify.getText().toString().trim());

				String url = UrlTools.url + UrlTools.ZHUCE_CHECK_REGIST;

				L.e("验证：url " + url + "  params:" + params);

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
											if (type.equals("personal")) {
												// 个人注册
												Intent intent = new Intent(
														RegisteActivity.this,
														RegisteSOLOActivity.class);
												intent.putExtra("phone",
														et_phone.getText()
																.toString()
																.trim());
												startActivity(intent);
												overridePendingTransition(
														R.anim.aty_zoomin,
														R.anim.aty_zoomout);
											} else if (type.equals("company")) {
												// 企业注册
												Intent intent = new Intent(
														RegisteActivity.this,
														RegisteFIRMActivity.class);
												intent.putExtra("phone",
														et_phone.getText()
																.toString()
																.trim());
												startActivity(intent);
												overridePendingTransition(
														R.anim.aty_zoomin,
														R.anim.aty_zoomout);
											} else {
												// 忘记密码
												Intent intent = new Intent(
														RegisteActivity.this,
														ForgetPasswordActivity.class);
												intent.putExtra("phone",
														et_phone.getText()
																.toString()
																.trim());
												startActivity(intent);
												overridePendingTransition(
														R.anim.aty_zoomin,
														R.anim.aty_zoomout);
											}

										}
									});

								} else {
									runOnUiThread(new Runnable() {
										public void run() {
											T.showShort(RegisteActivity.this,
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
