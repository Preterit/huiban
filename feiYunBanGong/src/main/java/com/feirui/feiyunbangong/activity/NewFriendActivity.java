package com.feirui.feiyunbangong.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.NewFriendAdapter;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog.CallBack;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog.AlertCallBack1;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonB;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.LianXiRen;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.IMUtil;
import com.feirui.feiyunbangong.utils.JsonUtil;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 添加新朋友：
 */

public class NewFriendActivity extends BaseActivity implements
		OnItemClickListener, OnTouchListener {

	private ListView lv_newfriend;
	private List<LianXiRen> lxrs = new ArrayList<>();
	private NewFriendAdapter adapter;
	ArrayList<String> strGroups = new ArrayList<>();
	ArrayList<Group> groups = new ArrayList<>();
	private String phone;// 请求者的电话；

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friend);
		initUI();
		setListView();
		setListener();
		setData();
	}

	@Override
	protected void onResume() {
		requestGroup();// 获取分组信息；
		super.onResume();
	}

	private void requestGroup() {
		String url01 = UrlTools.url + UrlTools.GET_FRIEND_GROUP;

		Utils.doPost(LoadingDialog.getInstance(NewFriendActivity.this),
				NewFriendActivity.this, url01, null, new HttpCallBack() {
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
						T.showShort(NewFriendActivity.this, msg);
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
						
					}
				});
	}

	private void setData() {
		RequestParams params=new RequestParams();
		AsyncHttpServiceHelper.post(UrlTools.url + UrlTools.SHENQING_LIEBIAO,params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						JsonB bean = JsonUtil.getMessage(new String(arg2));
						Message msg = new Message();
						msg.what = 0;
						msg.obj = bean;
						handler.sendMessage(msg);
						super.onSuccess(arg0, arg1, arg2);
					}
				});
	}

	private void setListView() {
		adapter = new NewFriendAdapter(this, this, this.getLayoutInflater());
		lv_newfriend.setAdapter(adapter);
	}

	private void setListener() {
		lv_newfriend.setOnItemClickListener(this);
	}

	private void initUI() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("添加新朋友");
		setRightVisibility(false);
		lv_newfriend = (ListView) findViewById(R.id.lv_newfriend);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (MotionEvent.ACTION_DOWN == event.getAction()) {
			switch (v.getId()) {
			case R.id.bt_jieshou:
				Object[] tag = (Object[]) v.getTag();
				phone = tag[2] + "";
				TextView tv = (TextView) tag[1];
				Log.e("TAG", tv.getText().toString() + "选择的是类型是：");
				if (tv.getText().equals("请选择")) {
					Toast.makeText(NewFriendActivity.this, "请选择分组！", Toast.LENGTH_SHORT).show();
					return true;
				}
				jieshou(Integer.parseInt(tag[0] + ""), tv.getText().toString());
				break;
			case R.id.bt_jvjue:
				Object[] tag1 = (Object[]) v.getTag();
				Log.e("TAG", tag1[0] + "选择的id是");
				jvjue(tag1[0]);
				break;
			case R.id.tv_choice:
				try {
					final TextView tv2 = (TextView) v;
					ChoiceGroupDialog dialog = new ChoiceGroupDialog(
							new CallBack() {
								@Override
								public void OnResultMsg(String res) {
									if ("+".equals(res)) {
										addGroup();// 添加分组；
										return;
									}
									tv2.setText(res);
								}
							}, this, strGroups, "选择分组");
					dialog.show();
				} catch (Exception e) {
					Log.e("TAG", e.getMessage());
				}
				break;
			}
		}

		return true;
	}

	// 添加分组：
	private void addGroup() {
		XiuGaiDialog tianjia = new XiuGaiDialog("添加分组", "添加", "输入新增组名",
				NewFriendActivity.this, new AlertCallBack1() {
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

	private void jvjue(Object id) {
		String url = UrlTools.url + UrlTools.JVJUE_SHENQING;
		RequestParams params = new RequestParams();
		Log.i("TAG", id + "");
		params.put("person_app_id", id + "");
		Log.e("TAG", params.toString());
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						final JsonBean bean = JsonUtils.getMessage(new String(
								arg2));
						if ("200".equals(bean.getCode())) {
							Log.e("TAG",  "----------------------------");
							adapter.notifyDataSetChanged();
							Message msg = new Message();
							msg.what = 1;
							msg.obj = bean;
							handler.sendMessage(msg);
						} else {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(NewFriendActivity.this,
											bean.getMsg(), Toast.LENGTH_SHORT).show();
								}
							});
						}
						super.onSuccess(arg0, arg1, arg2);
					}
				});
		finish();
	}

	private void jieshou(Object id, Object type) {
		String url = UrlTools.url + UrlTools.TONGYI_SHENQING;
		RequestParams params = new RequestParams();
		params.put("person_app_id", id + "");
		Group group = null;
		for (int i = 0; i < groups.size(); i++) {
			if ((type + "").equals(groups.get(i).getName())) {
				group = groups.get(i);
				break;
			}
		}
		if (group == null) {
			return;
		}
		Log.e("TAG", group.getId() + "group+getID()");
		int group_id = group.getId();
		params.put("group_id", group_id + "");

		Log.e("TAG", params.toString());
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@SuppressLint("ShowToast")
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						final JsonBean bean = JsonUtils.getMessage(new String(
								arg2));
						if ("200".equals(bean.getCode())) {
							Message msg = new Message();
							msg.what = 2;
							msg.obj = bean;
							handler.sendMessage(msg);
						} else {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(NewFriendActivity.this,
											bean.getMsg(), Toast.LENGTH_SHORT).show();
								}
							});
						}
						super.onSuccess(arg0, arg1, arg2);
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				lxrs.removeAll(lxrs);
				JsonB bean = (JsonB) msg.obj;
				ArrayList<HashMap<String, Object>> infor = bean.getData();
				for (int i = 0; i < infor.size(); i++) {
					String id = String.valueOf(infor.get(i).get("id"));
					Log.e("TAG", id + "id..................");
					String name = String.valueOf(infor.get(i).get("name"));
					String staff_head = String.valueOf(infor.get(i).get("staff_head"));
					String staff_mobile = String.valueOf(infor.get(i).get("staff_mobile"));
					String state = String.valueOf(infor.get(i).get("state"));
					LianXiRen lxr = new LianXiRen(name, staff_mobile, null,
							staff_head, id, state);
					lxrs.add(lxr);
				}
				adapter.add(lxrs);
			} else if (msg.what == 1) {
				Toast.makeText(NewFriendActivity.this, "拒绝成功！", Toast.LENGTH_SHORT).show();
			} else {
				// 发第一条消息给对方：
				IMUtil.sendMsg(phone);
				
				Toast.makeText(NewFriendActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
				setData();
			}
		}
	};

}
