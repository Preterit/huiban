package com.feirui.feiyunbangong.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog.CallBack;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog.AlertCallBack1;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.LianXiRen;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LianXiRenAdapter extends MyBaseAdapter<LianXiRen> {

	private Activity activity;
	private List<String> strs = new ArrayList<>();
	private int type;
	private ArrayList<String> strGroups = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();
	String st = "";

	// type 1为添加好友，2为短信邀请
	public LianXiRenAdapter(Activity activity, LayoutInflater inflater,
			int type, ArrayList<String> strGroups) {
		super(inflater);
		this.activity = activity;
		this.type = type;
		this.strGroups = strGroups;


	}

	public void addList(List<LianXiRen> lxrs) {

		strs.removeAll(strs);
		add(lxrs);

		for (int i = 0; i < list.size(); i++) {
			if (type == 1) {
				strs.add(list.get(i).getType());
			} else {
				strs.add(null);
			}
		}
	}

	public LianXiRenAdapter() {
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		try {
			ViewHolder holder = null;
			if (v == null) {
				v = mInflater.inflate(R.layout.ll_lianxiren_item, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) v.findViewById(R.id.iv_shenpiren);
				holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
				holder.tv_phone = (TextView) v.findViewById(R.id.tv_phone);
				holder.bt = (Button) v.findViewById(R.id.bt_tianjia);
				v.setTag(holder);
				holder.bt.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						final int pos = (int) v.getTag();
						if (type == 1) {
							// 弹出对话框：
							ChoiceGroupDialog dialog = new ChoiceGroupDialog(
									new CallBack() {
										@Override
										public void OnResultMsg(String res) {
											if ("+".equals(res)) {
												addGroup();// 添加分组；
												return;
											} else {
												st = res;
												Log.e("电话好友列表","groups==="+groups);
												addFriend(list.get(pos)
														.getPhone(), pos,
														groups);
											}
										}
									}, activity, strGroups, "选择分组");
							dialog.show();
						} else if (type == 2) {
							sendMsg(list.get(pos).getPhone(), pos);
						}
					}
				});
			} else {
				holder = (ViewHolder) v.getTag();
			}

			// 解决异步加载图片造成listview滑动过程中的混乱；
			LianXiRen spr = list.get(position);
			holder.tv_name.setText(spr.getName());
			holder.tv_phone.setText(spr.getPhone());

			if ("img/1_1.png".equals(spr.getHead())) {
				holder.iv.setImageResource(R.drawable.fragment_head);
			} else {
				ImageLoader.getInstance()
						.displayImage(spr.getHead(), holder.iv);
			}

			holder.bt.setTag(position);

			if (strs.get(position) == null) {
				holder.bt.setEnabled(true);
				holder.bt.setText("短信邀请");
			} else if ("未添加".equals(strs.get(position))) {
				holder.bt.setEnabled(true);
				holder.bt.setText("未添加");
			} else if ("已添加".equals(strs.get(position))) {
				holder.bt.setEnabled(false);
				holder.bt.setText("已添加");
			} else if ("已发出邀请".equals(strs.get(position))) {
				holder.bt.setEnabled(false);
				holder.bt.setText("已发出邀请");
			} else if ("申请中".equals(strs.get(position))) {
				holder.bt.setEnabled(false);
				holder.bt.setText("申请中");
			}

		} catch (Exception e) {
			Log.i("TAG", e.getMessage());
		}
		return v;
	}

	// 添加分组：
	private void addGroup() {

		String url01 = UrlTools.url + UrlTools.GET_FRIEND_GROUP;

		Utils.doPost(LoadingDialog.getInstance(activity),
				activity, url01, null, new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						ArrayList<HashMap<String, Object>> infor = bean
								.getInfor();
						groups.removeAll(groups);
						strGroups.removeAll(strGroups);

						for (int i = 0; i < infor.size(); i++) {
							int id = (int) infor.get(i).get("id");
							String name = infor.get(i).get("name") + "";
							int default_num = (int) infor.get(i).get("default");
							int count = (int) infor.get(i).get("count");
							Group group = new Group(id, name, default_num,
									count);

							groups.add(group);
							strGroups.add(group.getName());
						}

						strGroups.add("+");

						Log.e("电话好友列表", "strGroups"+strGroups.toString());
					}


					@Override
					public void failure(String msg) {

					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub

					}
				});
		Log.e("电话好友列表", "strGroups----adapter"+strGroups.toString());

		XiuGaiDialog tianjia = new XiuGaiDialog("添加分组", "添加", "输入新增组名",
				activity, new AlertCallBack1() {
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

	private void requestGroup() {
		String url01 = UrlTools.url + UrlTools.GET_FRIEND_GROUP;
		Utils.doPost(null, activity, url01, null, new HttpCallBack() {
			@Override
			public void success(JsonBean bean) {
				ArrayList<HashMap<String, Object>> infor = bean.getInfor();
				groups.removeAll(groups);
				strGroups.removeAll(strGroups);
				Log.e("TAG", infor.size() + "info.size()");

				for (int i = 0; i < infor.size(); i++) {
					int id = (int) infor.get(i).get("id");
					String name = infor.get(i).get("name") + "";
					int default_num = (int) infor.get(i).get("default");
					int count = (int) infor.get(i).get("count");
					Group group = new Group(id, name, default_num, count);
					groups.add(group);
					strGroups.add(group.getName());
				}
				strGroups.add("+");
			}

			@Override
			public void failure(String msg) {
				T.showShort(activity, msg);
			}

			@Override
			public void finish() {
			}
		});
	}

	class ViewHolder {
		ImageView iv;
		TextView tv_name, tv_phone;
		Button bt;
	}

	// 添加好友：
	private void addFriend(String phone, final int pos, ArrayList<Group> groups) {

		final RequestParams params = new RequestParams();
		int id = 0;
		for (int i = 0; i < groups.size(); i++) {
			if (st.equals(groups.get(i).getName())) {
				id = groups.get(i).getId();
				Log.e("电话好友列表--------","groupid"+id);
				break;
			}
		}
		Log.e("电话好友列表","group_id==="+id);
		Log.e("电话好友列表","groups.size()==="+groups.size());
		params.put("group_id", id + "");
		params.put("staff_mobile", phone);
		String url = UrlTools.url + UrlTools.TIANJIAHAOYOU;
		L.e("添加好友url" + url + " params" + params);
		Utils.doPost(LoadingDialog.getInstance(activity), activity, url,
				params, new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						T.showShort(activity, "已发送好友申请");
						strs.set(pos, "申请中");
						notifyDataSetChanged();
					}

					@Override
					public void failure(String msg) {
						T.showShort(activity, msg);
					}

					@Override
					public void finish() {
					}
				});
	}

	// 发送短息：
	private void sendMsg(String phone, final int pos) {
		String url = UrlTools.url + UrlTools.APPLICATION_INVITATIONMESSAGE;
		RequestParams params = new RequestParams();
		params.put("staff_mobile", phone);

		Utils.doPost(LoadingDialog.getInstance(activity), activity, url,
				params, new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						T.showShort(activity, "发送成功！");
						strs.set(pos, "已发出邀请");
						notifyDataSetChanged();
					}

					@Override
					public void failure(String msg) {
						T.showShort(activity, msg);
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub

					}
				});

	}

}
