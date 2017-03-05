package com.feirui.feiyunbangong.activity;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.DataStatementAdapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.DateTimePickDialogUtil;
import com.feirui.feiyunbangong.utils.DateTimePickDialogUtil.DialogCallBack;
import com.feirui.feiyunbangong.utils.DateUtils;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 数据报表-月报
 * 
 * @author admina
 *
 */
public class DataStatement3Activity extends BaseActivity {
	@PView(click = "onClick")
	TextView tv_time, tv_section;
	@PView(itemClick = "onItemClick")
	ListView lv_data_statement;
	private DataStatementAdapter adapter;
	private JsonBean json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statement_data);
		initView();
		initDate(DateUtils.getTime("yyyy-MM-dd", System.currentTimeMillis()));
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("月报");
		setRightVisibility(false);
		tv_time.setText(DateUtils.getTime("yyyy-MM-dd",
				System.currentTimeMillis()));
	}

	private void initDate(String date) {
		RequestParams params = new RequestParams();

		params.put("type_id", "3");
		String url = UrlTools.url + UrlTools.FORM_REPORT_LIST;
		L.e("数据报表-月报url" + url + " params" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						json = JsonUtils.getMessage(new String(arg2));
						L.e("数据报表-月报****************" + new String(arg2));
						if ("200".equals(json.getCode())) {
							runOnUiThread(new Runnable() {
								public void run() {
									BitmapUtils bitmapUtils = new BitmapUtils(
											DataStatement3Activity.this);
									adapter = new DataStatementAdapter(
											DataStatement3Activity.this, json,
											bitmapUtils);
									lv_data_statement.setAdapter(adapter);
									adapter.notifyDataSetChanged();
								}
							});

						} else {
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						super.onFailure(arg0, arg1, arg2, arg3);

					}
				});

	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_time: // 选择日期
			// 点击了选择日期按钮
			DateTimePickDialogUtil dateTimePicker = new DateTimePickDialogUtil(
					this, "");
			dateTimePicker.dateTimePicKDialog(tv_time, new DialogCallBack() {
				@Override
				public void callBack() {
					initDate(tv_time.getText().toString());
				}
			});
			break;

		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(DataStatement3Activity.this,
				DataDetailsActivity.class);
		intent.putExtra("id", "" + json.getInfor().get(position).get("id"));
		intent.putExtra("type",
				(String) json.getInfor().get(position).get("type"));

		startActivity(intent);
		overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);

	}
}
