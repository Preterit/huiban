package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog.CallBack;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog.AlertCallBack1;
import com.feirui.feiyunbangong.entity.Friend;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.IMUtil;
import com.feirui.feiyunbangong.utils.LoadImage;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.RequestParams;

/**
 * 添加好友：
 */
public class AboutFriendActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_name, tv_phone, tv_address, tv_choice;
	@PView(click = "onClick")
	private Button bt_submit;
	private ImageView iv;
	@PView(click = "onClick")
	private LinearLayout ll_choice;// 添加 分组对话框弹出；
	private ArrayList<String> strGroups = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_friend);

		initUI();
		setData();

	}

	private void setData() {

		String name = null;
		String phone = null;
		String address = null;
		String head = null;
		Intent intent = getIntent();
		Friend friend = (Friend) intent.getSerializableExtra("friend");
		name = friend.getName();
		phone = friend.getPhone();
		address = friend.getAddress();
		head = friend.getHead();
		if (!TextUtils.isEmpty(name)) {
			tv_name.setText("姓名：" + name);
		}
		if (!TextUtils.isEmpty(phone)) {
			tv_phone.setText("手机号：" + phone);
		}
		if (!TextUtils.isEmpty(address)) {
			tv_address.setText("地址：" + address);
		}

		if (!TextUtils.isEmpty(head) && !"img/1_1.png".equals(head)) {
			LoadImage.loadImage(this, iv, head, 300);
		}

	}

	@Override
	protected void onResume() {
		requestGroup();// 获取分组信息：
		super.onResume();
	}

	private void requestGroup() {
		String url01 = UrlTools.url + UrlTools.GET_FRIEND_GROUP;
		Utils.doPost(LoadingDialog.getInstance(AboutFriendActivity.this),
				AboutFriendActivity.this, url01, null, new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						ArrayList<HashMap<String, Object>> infor = bean
								.getInfor();
						Log.e("TAG", "获取分组成功！");
						groups.removeAll(groups);
						strGroups.removeAll(strGroups);
						Log.e("TAG", infor.size() + "info.size()");

						for (int i = 0; i < infor.size(); i++) {
							int id = (int) infor.get(i).get("id");
							String name = infor.get(i).get("name") + "";
							int default_num = (int) infor.get(i).get("default");
							int count = (int) infor.get(i).get("count");
							Group group = new Group(id, name, default_num,
									count);
							Log.e("TAG", group.toString());
							groups.add(group);
							strGroups.add(group.getName());
						}
						strGroups.add("+");
					}

					@Override
					public void failure(String msg) {
						T.showShort(AboutFriendActivity.this, msg);
					}

					@Override
					public void finish() {

					}
				});
		strGroups.add("+");
	}

	private void initUI() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("添加好友");
		setRightVisibility(false);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_address = (TextView) findViewById(R.id.tv_address);
		iv = (ImageView) findViewById(R.id.iv);
		tv_choice = (TextView) findViewById(R.id.tv_choice);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.bt_submit:
				submit();
				break;
			case R.id.ll_choice:
				// 弹出对话框：
				ChoiceGroupDialog dialog = new ChoiceGroupDialog(new CallBack() {
					@Override
					public void OnResultMsg(String res) {
						if ("+".equals(res)) {
							addGroup();// 添加分组；
							return;
						}
						tv_choice.setText(res);
					}
				}, this, strGroups, "选择分组");
				dialog.show();
				break;
		}
	}

	// 添加分组：
	private void addGroup() {

		XiuGaiDialog tianjia = new XiuGaiDialog("添加分组", "添加", "输入新增组名",
				AboutFriendActivity.this, new AlertCallBack1() {
			@Override
			public void onOK(final String name) {
				requestGroup();
			}

			@Override
			public void onCancel() {
			}
		});
		tianjia.show();

	}

	private void submit() {

		if ("分组".equals(tv_choice.getText().toString())) {
			Toast.makeText(this, "请选择分组！", Toast.LENGTH_SHORT).show();
		} else {
			String url = UrlTools.url + UrlTools.TIANJIAHAOYOU;
			Log.e("TAG", url);
			int id = 0;
			for (int i = 0; i < groups.size(); i++) {
				if (tv_choice.getText().toString()
						.equals(groups.get(i).getName())) {
					id = groups.get(i).getId();
					break;
				}
			}
			String phone = tv_phone.getText().toString()
					.substring(4, tv_phone.getText().toString().length());
			Log.e("TAG", phone + "phone");
			RequestParams params = new RequestParams();
			params.put("group_id", id + "");
			params.put("staff_mobile", phone);
			Log.e("TAG", params.toString());

			Utils.doPost(LoadingDialog.getInstance(AboutFriendActivity.this),
					AboutFriendActivity.this, url, params, new HttpCallBack() {

						@Override
						public void success(JsonBean bean) {
							Toast.makeText(AboutFriendActivity.this,
									"已发出加好友申请！", Toast.LENGTH_SHORT).show();
							finish();
						}

						@Override
						public void failure(String msg) {
							Toast.makeText(AboutFriendActivity.this, msg, 0).show();
						}

						@Override
						public void finish() {

						}
					});

		}
	}
}
