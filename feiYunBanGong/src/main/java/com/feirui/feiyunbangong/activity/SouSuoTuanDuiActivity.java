package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.TuanDuiAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;

/**
 * 搜索团队！！！！
 * 
 * @author feirui1
 *
 */
public class SouSuoTuanDuiActivity extends BaseActivity implements
		OnKeyListener, OnItemClickListener {

	private EditText et_sousuolianxiren;
	private ListView lv_tuandui;
	private TuanDuiAdapter adapter;
	List<TuanDui> tds = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sou_suo_tuan_dui);
		initView();
		setListener();
		setListView();
	}

	private void setListView() {
		lv_tuandui.setAdapter(adapter);
	}

	private void setListener() {
		et_sousuolianxiren.setOnKeyListener(this);
		lv_tuandui.setOnItemClickListener(this);
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("搜索团队");
		setRightVisibility(false);
		et_sousuolianxiren = (EditText) findViewById(R.id.et_sousuolianxiren);
		lv_tuandui = (ListView) findViewById(R.id.lv_tuandui);
		adapter = new TuanDuiAdapter(getLayoutInflater());
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		Log.e("TAG", keyCode + "");
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.e("TAG", keyCode + "");
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				Log.e("TAG", "keydown");
				if (TextUtils.isEmpty(et_sousuolianxiren.getText().toString()
						.trim())) {
					Toast.makeText(this, "请输入搜索内容！", 0).show();
					return false;
				}
				search(et_sousuolianxiren.getText().toString());
			}
		}
		return false;
	}

	private void search(String id) {

		String url = UrlTools.url + UrlTools.TUANDUI_XINXI;
		RequestParams params = new RequestParams();
		params.put("id", id);

		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						tds.removeAll(tds);
						ArrayList<HashMap<String, Object>> infor = bean
								.getInfor();
						Log.e("搜索团队", "infor: "+infor.toString() );
						HashMap<String, Object> hm = infor.get(0);
						Log.e("搜索团队", "hm: "+hm.toString() );
						TuanDui td = new TuanDui(String.valueOf(hm.get("id")),
								String.valueOf(hm.get("team_name")));
						tds.add(td);
						adapter.add(tds);
					}

					@Override
					public void failure(String msg) {
						adapter.add(tds);
						Toast.makeText(SouSuoTuanDuiActivity.this, msg, 0)
								.show();
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
						
					}
				});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, JiaRuTuanDuiActivity.class);
		intent.putExtra("id", tds.get(position).getTid());
		startActivityForResult(intent,1000);
		overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
	}

}
