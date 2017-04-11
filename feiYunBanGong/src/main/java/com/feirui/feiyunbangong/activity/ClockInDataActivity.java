package com.feirui.feiyunbangong.activity;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ClockInDataAdapter;
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
 * 打卡数据
 * 
 * @author admina
 *
 */
public class ClockInDataActivity extends BaseActivity {
	@PView(click = "onClick")
	TextView tv_time;// 时间

	@PView
	ListView lv_clockIn;
	private ClockInDataAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clock_in_data);
		initView();
		initDate(DateUtils.getTime("yyyy-MM-dd", System.currentTimeMillis()));
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("打卡数据");
		setRightVisibility(false);
		tv_time.setText(DateUtils.getTime("yyyy-MM-dd",
				System.currentTimeMillis()));
	}

	private void initDate(String date) {
		RequestParams params = new RequestParams();

		params.put("work_stime", date);
		String url = UrlTools.url + UrlTools.HOME_ATTENDANCE_LIST;
		L.e("打卡数据url" + url + " params" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						final JsonBean json = JsonUtils.getMessage(new String(
								arg2));
						L.e("打卡数据****************" + new String(arg2));
						if ("200".equals(json.getCode())) {
							runOnUiThread(new Runnable() {
								public void run() {
									BitmapUtils bitmapUtils = new BitmapUtils(
											ClockInDataActivity.this);
									adapter = new ClockInDataAdapter(
											ClockInDataActivity.this, json,
											bitmapUtils);
									lv_clockIn.setAdapter(adapter);
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
		case R.id.tv_form_time: // 选择日期
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
}
