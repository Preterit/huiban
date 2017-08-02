package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ChengYuanAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.TextImageView;
import com.loopj.android.http.RequestParams;

public class JiaRuTuanDuiActivity extends BaseActivity implements
		OnClickListener {

	private TextImageView tiv_head;
	private TextView tv_name, tv_team_num, tv_num;
	private Button tv_jiaru;
	private ListView lv_yijiaru;
	private String id;// 团队id;
	private ChengYuanAdapter adapter;
	private List<TuanDuiChengYuan> tdcys = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jia_ru_tuan_dui);
		try {
			initView();
			setListener();
			setListView();
			addData();
		} catch (Exception e) {
			Log.e("TAG", e.getMessage());
		}
	}

	private void setListView() {
		lv_yijiaru.setAdapter(adapter);
	}

	private void setListener() {
		tv_jiaru.setOnClickListener(this);
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("加入团队");
		setRightVisibility(false);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_team_num = (TextView) findViewById(R.id.tv_team_num);
		tv_team_num.setText(id);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_jiaru = (Button) findViewById(R.id.tv_jiaru);
		lv_yijiaru = (ListView) findViewById(R.id.lv_yijiaru);
		adapter = new ChengYuanAdapter(getLayoutInflater());
		tiv_head = (TextImageView) findViewById(R.id.tiv_head);
		tiv_head.setText("团队");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_jiaru:
			jiaru();
			finish();
			break;
		}
	}

	// 加入：
	private void jiaru() {
		String url = UrlTools.url + UrlTools.JIARU;
		RequestParams params = new RequestParams();
		params.put("team_id", id);

		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						Toast.makeText(JiaRuTuanDuiActivity.this, "成功成功", 0)
								.show();
						addData();

					}

					@Override
					public void failure(String msg) {
						Toast.makeText(JiaRuTuanDuiActivity.this, msg, 0)
								.show();
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
						
					}
				});
	}

	// 添加数据：
	private void addData() {

		String url = UrlTools.url + UrlTools.JIARU_TUANDUI;
		RequestParams params = new RequestParams();
		params.put("id", id);
		// 二次封装：
		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {

						tdcys.removeAll(tdcys);

						ArrayList<HashMap<String, Object>> infor = bean
								.getInfor();
						HashMap<String, Object> hm = infor.get(0);

						if (hm.get("team_name") != null) {
							tv_name.setText(String.valueOf(hm.get("team_name")));
							//tiv_head.setText(String.valueOf(hm.get("team_name")));
						}

						JSONArray array = (JSONArray) hm.get("list");
						if (array == null || array.length() == 0) {
							return;
						}
						tv_num.setText(array.length() + "人");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = null;
							try {
								obj = (JSONObject) array.get(i);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (obj == null) {
								return;
							}

							TuanDuiChengYuan tdcy = null;

							tdcy = new TuanDuiChengYuan();
							try {
								tdcy.setHead(obj.get("staff_head") + "");
								tdcy.setId(obj.get("staff_id") + "");
								tdcy.setName(obj.get("staff_name") + "");
								tdcy.setType(obj.get("type") + "");
							} catch (JSONException e) {
								e.printStackTrace();
							}
							tdcys.add(tdcy);
						}
						adapter.add(tdcys);
					}

					@Override
					public void failure(String msg) {
						Toast.makeText(JiaRuTuanDuiActivity.this, msg, 0)
								.show();
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
					}
				});
	}
}
