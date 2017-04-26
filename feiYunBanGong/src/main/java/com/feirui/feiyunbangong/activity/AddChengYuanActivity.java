package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddChengYuanExpandableListAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.myinterface.AllInterface.OnGroupStateChangedListener;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.RequestParams;

/**
 * 添加成员：
 * 添加审批人也跳转到该页面
 */
public class AddChengYuanActivity extends BaseActivity implements
		OnClickListener, OnGroupStateChangedListener {

	private ExpandableListView expandlist;
	private AddChengYuanExpandableListAdapter adapter;
	@PView(click = "onClick")
	private Button bt_submit;
	private List<Group> groups;
	private Map<Integer, List<ChildItem>> map;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_cheng_yuan);

		/*// 人为设置一个bug:用来测试testin 记得去除；
		Log.e("TAG", type.getBytes() + "");*/

		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		type = intent.getStringExtra("type");

		initView();
		setListView();
		setListener();

	}

	private void setListView() {
		groups = new ArrayList<>();
		map = new HashMap<>();
		adapter = new AddChengYuanExpandableListAdapter(groups, this, map, this);
		expandlist = (ExpandableListView) findViewById(R.id.expandlist);
		expandlist.setGroupIndicator(null);// 设置去掉小箭头
		expandlist.setAdapter(adapter);
	}

	private void setListener() {

		expandlist.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				adapter.setPos(groupPosition, childPosition);
				return true;
			}
		});
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		if (type != null) {
			setCenterString("添加名片");
		} else {
			setCenterString("选择人员");
		}
		setRightVisibility(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_submit:
			submit();
			break;
		}
	}

	@Override
	protected void onResume() {

		requestGroup();// 获取分组信息：
		super.onResume();
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
						}

						for (int i = 0; i < groups.size(); i++) {
							List<ChildItem> ci = new ArrayList<>();
							map.put(i, ci);
						}
						adapter.notifyDataSetChanged();
					}

					@Override
					public void failure(String msg) {
						T.showShort(AddChengYuanActivity.this, msg);
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub

					}
				});
	}

	// 提交：
	private void submit() {
		List<Integer> gp = adapter.getGp();
		List<Integer> cp = adapter.getCp();

		ArrayList<ChildItem> childs = new ArrayList<>();
		for (int i = 0; i < gp.size(); i++) {
			childs.add(map.get(gp.get(i)).get(cp.get(i)));
		}

		if (type != null) {
			AppStore.listener.onSendCard(childs);
			finish();
			return;
		}

		Intent intent = new Intent();
		intent.putExtra("childs", childs);
		setResult(100, intent);

		finish();
	}

	@Override
	public void onGroupExpanded(final int groupPosition) {
		// TODO 请求该分组下的好友信息：
		String url = UrlTools.url + UrlTools.GET_CHILD_OF_GROUP;
		RequestParams params = new RequestParams();
		params.put("group_id",
				((Group) adapter.getGroup(groupPosition)).getId() + "");
		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						ArrayList<HashMap<String, Object>> infor = bean
								.getInfor();
						List<ChildItem> cis = new ArrayList<>();

						for (int i = 0; i < infor.size(); i++) {
							HashMap<String, Object> hm = infor.get(i);
							ChildItem ci = new ChildItem();
							int friend_id = (int) hm.get("friend_id");
							String head = "" + hm.get("staff_head");
							int person_id = (int) hm.get("person_id");
							String remark = "" + hm.get("remark");
							String phone = "" + hm.get("staff_mobile");
							String name = hm.get("staff_name") + "";
							ci.setId(friend_id + "");
							ci.setMarkerImgId(head);
							ci.setPhone(phone);
							ci.setTitle(name);

							cis.add(ci);
						}
						map.put(groupPosition, cis);
						adapter.notifyDataSetChanged();
					}

					@Override
					public void failure(String msg) {
						T.showShort(AddChengYuanActivity.this, msg);
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {

	}

}
