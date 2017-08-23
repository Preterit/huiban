package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog.CallBack;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.TextImageView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 查看团队成员资料：
 * 
 * @author feirui1
 *
 */
public class DetailChengYuanActivity extends BaseActivity implements
		OnClickListener {

	private TextImageView iv_head;
	private TextView tv_name, tv_email, tv_phone, tv_name_02, tv_remark;
	private Button bt_liaotian, bt_addfriend;
	private LinearLayout ll_add_goto;
	private List<Group> groups = new ArrayList<>();// 分组信息；
	private ArrayList<String> group_name = new ArrayList<>();// 组名；

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_cheng_yuan);
		initView();
		setListener();
		updateState();// 更改新团员状态；
	}

	private void updateState() {
		// 老成员不需要更新状态；
		if (tdcy.getState() == 2) {
			return;
		}

		String url = UrlTools.url + UrlTools.UPDATE_TEAM_STATE;
		RequestParams params = new RequestParams();
		params.put("team_member_list_id", tdcy.getTeam_member_list_id() + "");
		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						Log.e("团队成员界面", "成功"+bean.toString());
					}

					@Override
					public void failure(String msg) {
						Log.e("TAG", msg);
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
						
					}
				});

	}

	TuanDuiChengYuan tdcy;

	private void initData(TuanDuiChengYuan tdcy) {

		if (!TextUtils.isEmpty(tdcy.getT_remark())) {
			tv_name.setText(tdcy.getT_remark());
			tv_name_02.setText("姓名：" + tdcy.getT_remark());
		} else if (!TextUtils.isEmpty(tdcy.getRemark())) {
			tv_name.setText("姓名：" + tdcy.getRemark());
			tv_name_02.setText(tdcy.getRemark());
		} else {
			tv_name.setText(tdcy.getName().toString());
			tv_name_02.setText("姓名：" + tdcy.getName().toString());
		}

//		if (tdcy.getEmail() != null && !tdcy.getEmail().equals("null")) {
//			tv_email.setText("个 人 签 名 ：" + tdcy.getEmail().toString());
//		} else {
//			tv_email.setText("个 人 签 名 ：");
//		}
		if (tdcy.getIntroduction() != null && !tdcy.getIntroduction().equals("null")) {
			tv_email.setText("个 人 签 名 ：" + tdcy.getIntroduction().toString());
		} else {
			tv_email.setText("个 人 签 名 ：");
		}
		tv_phone.setText("手机号：" + tdcy.getPhone().toString());

		if (!TextUtils.isEmpty(tdcy.getT_remark())) {
			tv_remark.setText("团队备注名：" + tdcy.getT_remark());
		} else {
			tv_remark.setText("团队备注名：");
		}

		if (!"".equals(tdcy.getHead()) && null != tdcy.getHead()
				&& !"img/1_1.png".equals(tdcy.getHead())) {
			ImageLoader.getInstance().displayImage(tdcy.getHead(), iv_head);
		} else {
			iv_head.setText(Utils.getPicTitle(tdcy.getName()));
		}
	}

	private void setListener() {
		bt_liaotian.setOnClickListener(this);
		bt_addfriend.setOnClickListener(this);
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("成员资料");
		setRightDrawable(R.drawable.bi);
		Intent intent = getIntent();
		tdcy = (TuanDuiChengYuan) intent.getSerializableExtra("tdcy");

		ll_add_goto = (LinearLayout) findViewById(R.id.ll_add_goto);
		tv_remark = (TextView) findViewById(R.id.tv_remark);

		bt_addfriend = (Button) findViewById(R.id.bt_addfriend);
		if ((AppStore.user.getInfor().get(0).get("id") + "").equals(tdcy
				.getStaff_id())) {
			rightll.setVisibility(View.VISIBLE);
			ll_add_goto.setVisibility(View.INVISIBLE);
		} else {
			rightll.setVisibility(View.GONE);
			ll_add_goto.setVisibility(View.VISIBLE);
		}

		rightll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent02 = new Intent(DetailChengYuanActivity.this,
						XiuGaiChengYuanActivity.class);
				intent02.putExtra("tdcy", tdcy);
				startActivityForResult(intent02, 100);
			}
		});

		iv_head = (TextImageView) findViewById(R.id.iv_head);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_email = (TextView) findViewById(R.id.tv_email);
		tv_name_02 = (TextView) findViewById(R.id.tv_name_02);
		bt_liaotian = (Button) findViewById(R.id.bt_liaotian);
		tv_phone = (TextView) findViewById(R.id.tv_phone);

		if (tdcy.getFriendstate() == 1) {
			bt_addfriend.setEnabled(false);
			bt_addfriend.setBackgroundColor(getResources().getColor(
					R.color.huise));
			bt_addfriend.setText("我的好友");
			/*bt_liaotian.setEnabled(true);
			bt_liaotian.setBackgroundColor(getResources().getColor(
					R.color.main_color));*/
		} else {
			bt_addfriend.setEnabled(true);
			bt_addfriend.setBackgroundColor(getResources().getColor(
					R.color.main_color));
			bt_addfriend.setText("加好友");
			/*
			 * bt_liaotian.setEnabled(false);
			 * bt_liaotian.setBackgroundColor(getResources().getColor(
			 * R.color.huise));
			 */
		}

	}

	@Override
	protected void onResume() {
		initData(tdcy);
		requestGroup();// 获取分组信息；
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_liaotian:
			// 打开聊天窗口：
			Intent intent = AppStore.mIMKit.getChattingActivityIntent(
					tdcy.getPhone(), Happlication.APP_KEY);
			startActivity(intent);
			overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
			break;
		case R.id.bt_addfriend:
			addFriend();// 添加好友；
			break;
		}
	}

	private void addFriend() {

		ChoiceGroupDialog dialog = new ChoiceGroupDialog(new CallBack() {

			@Override
			public void OnResultMsg(String res) {

				int pos = 0;
				for (int i = 0; i < groups.size(); i++) {
					if (res.equals(group_name.get(i))) {
						pos = i;
						break;
					}
				}

				String url = UrlTools.url + UrlTools.TIANJIAHAOYOU;
				RequestParams params = new RequestParams();
				params.put("group_id", groups.get(pos).getId() + "");
				L.e("添加好友xxxxxx：" + groups.get(pos).getId());
				params.put("group_id", groups.get(pos).getId() + "");
				params.put("staff_mobile", tdcy.getPhone());


				L.e("添加好友：url " + url + "  params:" + params);
				Utils.doPost(
						LoadingDialog.getInstance(DetailChengYuanActivity.this),
						DetailChengYuanActivity.this, url, params,
						new HttpCallBack() {

							@Override
							public void success(JsonBean bean) {
								T.showShort(DetailChengYuanActivity.this,
										"好友申请已发出！");
								bt_addfriend.setBackgroundColor(getResources()
										.getColor(R.color.huise));
								bt_addfriend.setText("申请中");
								bt_addfriend.setEnabled(false);
							}

							@Override
							public void failure(String msg) {
								T.showShort(DetailChengYuanActivity.this, msg);
							}

							@Override
							public void finish() {
								// TODO Auto-generated method stub
								
							}
						});

			}
		}, this, group_name, "选择分组");

		dialog.show();
	}

	// 获取分组信息：
	private void requestGroup() {
		String url01 = UrlTools.url + UrlTools.GET_FRIEND_GROUP;
		Utils.doPost(LoadingDialog.getInstance(this), this, url01, null,
				new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						ArrayList<HashMap<String, Object>> infor = bean
								.getInfor();

						groups.removeAll(groups);
						group_name.removeAll(group_name);

						for (int i = 0; i < infor.size(); i++) {
							int id = (int) infor.get(i).get("id");
							String name = infor.get(i).get("name") + "";
							int default_num = (int) infor.get(i).get("default");
							int count = (int) infor.get(i).get("count");
							Group group = new Group(id, name, default_num,
									count);
							groups.add(group);
							group_name.add(group.getName());
						}
					}

					@Override
					public void failure(String msg) {
						T.showShort(DetailChengYuanActivity.this, msg);
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
						
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == 100 && resultCode == 200) {
			tdcy = (TuanDuiChengYuan) intent.getSerializableExtra("tdcy");
			tv_email.setText("个 人 签 名 ：" + tdcy.getIntroduction().toString());
			initData(tdcy);
		}
	}

}
