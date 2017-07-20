package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.GongGaoAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog.AlertCallBack;
import com.feirui.feiyunbangong.entity.GongGao;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.RequestParams;

public class DetailGongGaoActivity extends BaseActivity implements
		OnRefreshListener, OnItemClickListener, OnItemLongClickListener {
	@PView(click = "onClick")
	private ListView lv_detailgonggao;
	private SwipeRefreshLayout swipe_container;
	private GongGaoAdapter adapter;
	private String id;// 团队id
	private String manage_phone;// 管理员手机号；
	private List<GongGao> ggs = new ArrayList<>();
	private ArrayList<TuanDuiChengYuan> tdcys = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_gong_gao);

		initView();
		setListView();
		setListener();

	}

	@Override
	protected void onResume() {
		addData();
		super.onResume();
	}

	private void setListener() {
		Utils.setRefresh(swipe_container, this);
		lv_detailgonggao.setOnItemClickListener(this);
		lv_detailgonggao.setOnItemLongClickListener(this);
	}

	private void addData() {

		String url = UrlTools.url + UrlTools.GONGGAO_TUANDUI;
		RequestParams params = new RequestParams();
		params.put("teamid", id + "");
		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						swipe_container.setRefreshing(false);
						ArrayList<HashMap<String, Object>> infor = bean
								.getInfor();
						ggs.removeAll(ggs);
						for (int i = 0; i < infor.size(); i++) {
							HashMap<String, Object> hm = infor.get(i);
							GongGao gg = new GongGao(hm.get("staff_head") + "",
									hm.get("staff_name") + "", hm.get("time")
											+ "", hm.get("content") + "");
							gg.setId((int) hm.get("id"));
							gg.setPhone(hm.get("staff_mobile") + "");
							ggs.add(gg);
						}
						adapter.add(ggs);
					}

					@Override
					public void failure(String msg) {
						swipe_container.setRefreshing(false);
						if ("暂无数据".equals(msg)) {
							ggs.removeAll(ggs);
							adapter.add(ggs);
							return;
						}
						Toast.makeText(DetailGongGaoActivity.this, msg, 0)
								.show();
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
						
					}
				});
	}

	private void setListView() {
		lv_detailgonggao.setAdapter(adapter);
	}

	@SuppressWarnings("unchecked")
	private void initView() {

		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("团队公告");
		setRightDrawable(R.drawable.jia);
		rightll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetailGongGaoActivity.this,
						AddGongGaoActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
				overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
			}
		});

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		id = bundle.getString("id");
		tdcys = (ArrayList<TuanDuiChengYuan>) bundle.getSerializable("tdcys");

		lv_detailgonggao = (ListView) findViewById(R.id.lv_detailgonggao);
		swipe_container = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		adapter = new GongGaoAdapter(getLayoutInflater(), this, tdcys);

	}

	@Override
	public void onRefresh() {
		addData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent intent = new Intent(this, NoticeReadUnReadActivity.class);
		intent.putExtra("id", ggs.get(position).getId());
		startActivity(intent);

	}

	// 判断是否具有删除权限：只有自己和团长可以删除：
	private boolean canDelete(int position) {

		boolean flag = false;
		GongGao gg = ggs.get(position);
		int id = gg.getId();

		for (int i = 0; i < tdcys.size(); i++) {
			if ("团长".equals(tdcys.get(i).getType())) {
				manage_phone = tdcys.get(i).getPhone();
				break;
			}
			;
		}

		Log.e("TAG", AppStore.myuser.getPhone() + "AppStore.myuser.getPhone()");
		Log.e("TAG", manage_phone + "manage_phone");
		Log.e("TAG", gg.getPhone() + "gg.getPhone()");

		if (gg.getPhone().equals(AppStore.myuser.getPhone())
				|| AppStore.myuser.getPhone().equals(manage_phone)) {
			flag = true;
		}

		return flag;
	}

	// 删除公告：
	private void delete(final String id) {
		// 弹出对话框，确认删除吗？
		MyAleartDialog dissolve_dialog = new MyAleartDialog("删除公告",
				"确定删除该公告吗？", this, new AlertCallBack() {
					@Override
					public void onOK() {
						// 删除：
						String url = UrlTools.url + UrlTools.TEAM_NOTICE_DELETE;
						RequestParams params = new RequestParams();
						params.put("id", id);
						Utils.doPost(LoadingDialog
								.getInstance(DetailGongGaoActivity.this),
								DetailGongGaoActivity.this, url, params,
								new HttpCallBack() {

									@Override
									public void success(JsonBean bean) {
										T.showShort(DetailGongGaoActivity.this,
												"删除成功！");
										addData();
									}

									@Override
									public void failure(String msg) {
										T.showShort(DetailGongGaoActivity.this,
												msg);
									}

									@Override
									public void finish() {
										// TODO Auto-generated method stub
										
									}
								});
					}

					@Override
					public void onCancel() {
					}
				});

		dissolve_dialog.show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

		boolean flag = canDelete(position);
		if (flag) {
			delete(ggs.get(position).getId() + "");
		}
		return true;
	}

}
