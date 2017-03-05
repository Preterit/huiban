package com.feirui.feiyunbangong.activity;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
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
 * 登录界面
 * 
 * @author admina
 *
 */
public class LoginActivity extends BaseActivity {
	// 用户名，密码输入框
	@PView
	EditText et_login_username, et_login_password;
	// 登录
	@PView(click = "onClick")
	Button btn_login_submit;
	// 注册，忘记密码
	@PView(click = "onClick")
	TextView btn_login_register, btn_login_forget;

	String type = "personal";// 注册类型,个人注册

	private static final int LOGIN_SUCESS = 1; // 登录成功
	private static final int LOGIN_ERROR = 2;// 登录失败
	private static final int JSON_ERROR = 3;// json解析出错
	private static final int SERVICE_ERROR = 4;// 链接服务器出错

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOGIN_SUCESS:
				AppStore.user = (JsonBean) msg.obj;
				// 设置已经登陆过
				SPUtils.put(LoginActivity.this, Constant.SP_ALREADYUSED, true);
				// 将用户名密码缓存
				SPUtils.put(LoginActivity.this, Constant.SP_USERNAME,
						et_login_username.getText().toString() + "");
				SPUtils.put(LoginActivity.this, Constant.SP_PASSWORD,
						et_login_password.getText().toString() + "");
				T.showShort(LoginActivity.this, ((JsonBean) msg.obj).getMsg());
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
				finish();
				break;
			case LOGIN_ERROR:
				AppStore.user = null;
				T.showShort(LoginActivity.this, ((JsonBean) msg.obj).getMsg());
				break;
			case JSON_ERROR:
				AppStore.user = null;
				T.showShort(LoginActivity.this, "json解析出错");
				break;
			case SERVICE_ERROR:
				AppStore.user = null;
				T.showShort(LoginActivity.this, "服务器出错了");
				break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void onResume() {
		initView();
		super.onResume();
	}

	/**
	 * 返回到登录页，回显用户名密码
	 */
	private void initView() {
		if (!TextUtils.isEmpty((String) SPUtils.get(LoginActivity.this,
				Constant.SP_USERNAME, ""))
				&& !TextUtils.isEmpty((String) SPUtils.get(LoginActivity.this,
						Constant.SP_PASSWORD, ""))) {
			et_login_username.setText((String) SPUtils.get(LoginActivity.this,
					Constant.SP_USERNAME, ""));
			et_login_password.setText((String) SPUtils.get(LoginActivity.this,
					Constant.SP_PASSWORD, ""));
		}

	}

	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.btn_login_forget: // 忘记密码
			intent = new Intent(LoginActivity.this, RegisteActivity.class);
			intent.putExtra("type", "wangjimima");
			startActivity(intent);
			overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
			break;
		case R.id.btn_login_register: // 注册
			intent = new Intent(LoginActivity.this, RegisteActivity.class);
			intent.putExtra("type", type);
			startActivity(intent);
			overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
			// SelectDialog dia = new SelectDialog(LoginActivity.this,
			// new Callback() {
			// @Override
			// public void onOK(String s) {
			// type = s;
			// Intent intent = new Intent(LoginActivity.this,
			// RegisteActivity.class);
			// intent.putExtra("type", type);
			// startActivity(intent);
			// }
			//
			// @Override
			// public void onCancel() {
			// }
			// });
			// dia.show();
			break;
		case R.id.btn_login_submit:// 登录
			if (TextUtils.isEmpty(et_login_username.getText().toString())
					|| TextUtils
							.isEmpty(et_login_password.getText().toString())) {
				T.showShort(LoginActivity.this, "用户名或密码不能为空");
				return;
			}
			if (et_login_username.getText().toString().trim().length() != 11) {
				T.showShort(LoginActivity.this, "请输入11位手机号");
				return;

			}
			// if (TextUtils
			// .isEmpty(et_login_password.getText().toString().trim())
			// || et_login_password.getText().toString().trim().length() < 6
			// || et_login_password.getText().toString().trim().length() > 18) {
			// T.showShort(getApplicationContext(), "密码长度为6~18个字符");
			// return;
			// }
			LoginMain();
			break;
		}

	}

	private void LoginMain() {

		RequestParams params = new RequestParams();
		params.put("staff_mobile", et_login_username.getText().toString());
		params.put("staff_password", et_login_password.getText().toString());
		String url = UrlTools.url + UrlTools.LOGIN_LOGIN;
		L.e("登录url=" + url + " params=" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						try {
							JsonBean json = JsonUtils.getMessage(new String(
									arg2));
							final Message mse = new Message();

							if ("200".equals(json.getCode())) {

								mse.what = LOGIN_SUCESS;
							} else {

								mse.what = LOGIN_ERROR;

							}
							mse.obj = json;
							handler.sendMessage(mse);
						} catch (Exception e) {
							e.printStackTrace();
							handler.sendEmptyMessage(JSON_ERROR);
						}

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {

						super.onFailure(arg0, arg1, arg2, arg3);
						handler.sendEmptyMessage(SERVICE_ERROR);

					}

				});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Happlication.getInstance().exit();
	}

}
