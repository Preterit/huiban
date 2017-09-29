package com.feirui.feiyunbangong.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 登录界面
 * 
 * @author admina
 *
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
	// 用户名，密码输入框
	@PView
	EditText et_login_username, et_login_password;
	// 登录
	@PView(click = "onClick")
	Button btn_login_submit;
	// 注册，忘记密码
	@PView(click = "onClick")
	TextView btn_login_register, btn_login_forget, btn_login;

	String type = "personal";// 注册类型,个人注册

	private static final int LOGIN_SUCESS = 1; // 登录成功
	private static final int LOGIN_ERROR = 2;// 登录失败
	private static final int JSON_ERROR = 3;// json解析出错
	private static final int SERVICE_ERROR = 4;// 链接服务器出错
	private PopupWindow popupWindow;
	private List<String> groups;
	private ListView lv_group;
	private View view;
	private Set<String> set;
	private ArrayList user_list;
	private ArrayAdapter<String> adapter;
	private ImageView imageView;
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
					T.showShort(LoginActivity.this, "网络开小差了");
					break;
			}
		}

		;

	};
	private View view1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		imageView= (ImageView) findViewById(R.id.iv_pic);
        imageView.setOnClickListener(this);
	}


	private void showWindow(View v) {
								LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					view = layoutInflater.inflate(R.layout.group_list, null);


					lv_group = (ListView) view.findViewById(R.id.lvGroup);
					SharedPreferences preferences = getApplicationContext().getSharedPreferences("username", Context.MODE_PRIVATE);

					set = new HashSet<String>();

					set = preferences.getStringSet("username", null);

					if (set != null) {
						user_list = new ArrayList(set);//转化存储到list集合中
						adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, user_list);
						lv_group.setAdapter(adapter);
          // 创建一个PopuWidow对象
						popupWindow = new PopupWindow(view, 300, 300);
						//popupWindow.showAsDropDown(et_login_password);
						// 使其聚集
						popupWindow.setFocusable(true);
						// 设置允许在外点击消失
						popupWindow.setOutsideTouchable(true);

						// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
						popupWindow.setBackgroundDrawable(new BitmapDrawable());
						WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
						// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
						int xPos = windowManager.getDefaultDisplay().getWidth() / 2
								- popupWindow.getWidth() / 2;
					//	Log.i("coder", "xPos:" + xPos);

							popupWindow.showAsDropDown(et_login_username, Gravity.CENTER_HORIZONTAL, 100);
					//	popupWindow.showAsDropDown(et_login_username);
					}
lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		et_login_username.setText((CharSequence) user_list.get(position));
		popupWindow.dismiss();
		//Toast.makeText(LoginActivity.this, (CharSequence) user_list.get(position),Toast.LENGTH_SHORT).show();
	}
});
//					lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//						@Override
//						public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//							Toast.makeText(LoginActivity.this,
//									user_list.get(i), Toast.LENGTH_SHORT).show();
//
//							if (popupWindow != null) {
//								popupWindow.dismiss();
//							}
//						}
//					});
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
				LoginMain();
				break;
			case R.id.btn_login: //快速登录
				intent = new Intent(LoginActivity.this, RegisteActivity.class);
				intent.putExtra("type", "denglu");
				startActivity(intent);
				overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomcloseout);
//			finish();
				break;
			case R.id.iv_pic: //
				//Toast.makeText(LoginActivity.this,"adasfafa",Toast.LENGTH_SHORT).show();
				if (popupWindow!=null){
					popupWindow.dismiss();
				}else {
					showWindow(view1);
				}
			//finish();
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
								//保存账号
								saveZhangHao();
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

	private void saveZhangHao() {

		SharedPreferences preferences = getApplication().getSharedPreferences("username", Context.MODE_PRIVATE);
		Set<String> set = new HashSet<String>();
		set = preferences.getStringSet("username", null);
		SharedPreferences.Editor editor = preferences.edit();
		if (set != null) {
			set.add(et_login_username.getText().toString());
			editor.remove("username");//清除必须要有
			editor.commit();//清除后必须再提交一下，否则没有效果
			editor.putStringSet("username", set);
			editor.commit();
		} else {
			set = new HashSet<String>();
			set.add(et_login_username.getText().toString());
			editor.putStringSet("username", set);
			editor.commit();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Happlication.getInstance().exit();
	}
}

