package com.feirui.feiyunbangong.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.EtDialog;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.Friend;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;
import com.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加好友：
 */

public class AddFriendActivity extends BaseActivity implements OnClickListener,
		OnKeyListener {

	private List<LinearLayout> lls = new ArrayList<>();
	private EditText et_sousuolianxiren;
	private final int SCANER_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friend);
		initUI();
		setListener();
	}

	private void setListener() {
		for (int i = 0; i < lls.size(); i++) {
			lls.get(i).setOnClickListener(this);
		}
		et_sousuolianxiren.setOnKeyListener(this);
	}

	private void initUI() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("添加好友");
		setRightVisibility(false);
		lls.add((LinearLayout) findViewById(R.id.ll_shoujilianxiren));
		lls.add((LinearLayout) findViewById(R.id.ll_saoyisao));
		lls.add((LinearLayout) findViewById(R.id.ll_invite));
		et_sousuolianxiren = (EditText) findViewById(R.id.et_sousuolianxiren);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == lls.get(0).getId()) {
			startActivity(new Intent(AddFriendActivity.this,
					DetailLianXiRenActivity.class));
			overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
		} else if (v.getId() == lls.get(1).getId()) {
			Log.e("TAG", "扫一扫");
			// 扫一扫：
			saoyisao();
		} else if (v.getId() == lls.get(2).getId()) {
			invite();
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				if (!Utils.isPhone(et_sousuolianxiren.getText().toString())) {
					Toast.makeText(this, "手机号格式错误！", Toast.LENGTH_SHORT).show();
					return false;
				}
				search(et_sousuolianxiren.getText().toString());
			}
		}
		return false;
	}

	private void search(String phone) {

		String url = UrlTools.url + UrlTools.SOUSUO_LIANXIREN;
		RequestParams params = new RequestParams();
		params.put("staff_mobile", phone);

		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						try {
							String name = "";
							String phone = "";
							String address = "";
							String head = "";
							if (bean.getInfor().get(0).get("staff_name") != null) {
								name = String.valueOf(bean.getInfor().get(0)
										.get("staff_name"));
							}
							if (bean.getInfor().get(0).get("staff_mobile") != null) {
								phone = String.valueOf(bean.getInfor().get(0)
										.get("staff_mobile"));
							}
							if (bean.getInfor().get(0).get("address") != null) {
								address = String.valueOf(bean.getInfor().get(0)
										.get("address"));
							}
							if (bean.getInfor().get(0).get("staff_head") != null) {
								head = String.valueOf(bean.getInfor().get(0)
										.get("staff_head"));
							}
							Friend friend = new Friend(name, phone, address,
									head);
							Intent intent = new Intent(AddFriendActivity.this,
									AboutFriendActivity.class);
							intent.putExtra("friend", friend);
							startActivity(intent);
							overridePendingTransition(R.anim.aty_zoomin,
									R.anim.aty_zoomout);
						} catch (Exception e) {
							Log.e("TAG", e.getMessage());
						}
					}

					@Override
					public void failure(String msg) {
						Toast.makeText(AddFriendActivity.this, msg, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub

					}
				});
	}

	private void saoyisao() {
		try {
			// 打开扫描界面扫描条形码或二维码
			Intent openCameraIntent = new Intent(AddFriendActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, SCANER_CODE);
			overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
		} catch (Exception e) {
			Log.e("TAG", e.getMessage());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("TAG", resultCode + "");
		if (resultCode == -1) {
			if (requestCode == SCANER_CODE) {
				Toast.makeText(AddFriendActivity.this, "扫描成功！", Toast.LENGTH_SHORT).show();
				Bundle bundle = data.getExtras();
				String scanResult = bundle.getString("result");
				if (Utils.isPhone(scanResult)) {
					search(scanResult);
				} else if (scanResult.charAt(0) == 'T') {
					addTeam(scanResult.substring(1, scanResult.length()));// 加入团队；
				} else {
					Toast.makeText(AddFriendActivity.this, "无法识别该二维码！", Toast.LENGTH_SHORT)
							.show();
				}
				Log.e("TAG", scanResult);
			}
		}
	}

	// 加入团队：
	@SuppressLint("ShowToast")
	private void addTeam(String id) {
		Intent intent = new Intent(this, JiaRuTuanDuiActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
		overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
	}

	// 短信邀请：
	public void invite() {
		EtDialog dialog1 = new EtDialog("短信邀请", "请输入手机号",
				AddFriendActivity.this, new EtDialog.AlertCallBack1() {

					@Override
					public void onOK(String name) {
						
						if (!Utils.isPhone(name)) {
							Toast.makeText(AddFriendActivity.this, "手机号不合法！", Toast.LENGTH_SHORT)
									.show();
							return;
						}

						if (Utils.isPhone(name)) {

							String url = UrlTools.url
									+ UrlTools.APPLICATION_INVITATIONMESSAGE;

							RequestParams params = new RequestParams();
							params.put("staff_mobile", name);
							Utils.doPost(LoadingDialog
									.getInstance(AddFriendActivity.this),
									AddFriendActivity.this, url, params,
									new HttpCallBack() {

										@Override
										public void success(JsonBean bean) {
											T.showShort(AddFriendActivity.this,
													"发送成功！");
										}

										@Override
										public void failure(String msg) {
											T.showShort(AddFriendActivity.this,
													msg);
										}

										@Override
										public void finish() {
											// TODO Auto-generated method stub

										}
									});

						} else {

						}

					}

					@Override
					public void onCancel() {

					}
				});

		dialog1.show();
	}

}
