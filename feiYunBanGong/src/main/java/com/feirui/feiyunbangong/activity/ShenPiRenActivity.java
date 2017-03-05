package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ShenPiRenAdapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ShenPiRenActivity extends BaseActivity implements
		OnItemClickListener {

	private ListView lv;
	private ShenPiRenAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shen_pi_ren);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		initUI();
		setListView();
		request();
		setListener();
		super.onStart();
	}

	private void request() {

		AsyncHttpServiceHelper.post(UrlTools.url + UrlTools.SHENPIREN_LIEBIAO,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						JsonBean bean = JsonUtils.getMessage(new String(arg2));
						Message msg = new Message();
						msg.obj = bean;
						msg.what = 0;
						handler.sendMessage(msg);
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				try {
					JsonBean bean = (JsonBean) msg.obj;
					ArrayList<HashMap<String, Object>> list = bean.getInfor();
					ArrayList<ShenPiRen> sprs = new ArrayList<>();
					for (int i = 0; i < list.size(); i++) {
						HashMap<String, Object> hm = list.get(i);
						ShenPiRen spr = new ShenPiRen(Integer.parseInt(String
								.valueOf(hm.get("id"))),
								(String) hm.get("staff_name"),
								String.valueOf(hm.get("staff_head")),
								String.valueOf(hm.get("staff_department")));
						sprs.add(spr);
					}
					adapter.add(sprs);

				} catch (Exception e) {
					Log.i("TAG", e.getMessage());
				}
				break;
			}

		};
	};

	private void setListener() {
		lv.setOnItemClickListener(this);
	}

	private void setListView() {
		lv.setAdapter(adapter);
	}

	private void initUI() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("选择人员");
		setRightVisibility(false);
		lv = (ListView) findViewById(R.id.lv_shenpiren);
		adapter = new ShenPiRenAdapter(this, this.getLayoutInflater());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.putExtra("shenpiren", (ShenPiRen) adapter.getItem(position));
		setResult(102, intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent intent = new Intent();
			intent.putExtra("shenpiren", new ShenPiRen());
			setResult(102, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
