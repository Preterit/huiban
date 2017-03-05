package com.feirui.feiyunbangong.activity;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.DateTimePickDialogUtil;
import com.feirui.feiyunbangong.utils.DateTimePickDialogUtil.DialogCallBack;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 创建拜访计划
 * 
 * @author admina
 *
 */
public class ClientAddActivity extends BaseActivity {
	@PView(click = "onClick")
	TextView tv_time;// 时间
	@PView
	EditText et_name, et_dizhi;// 姓名，地址
	@PView(click = "onClick")
	Button btn_add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_add);
		initView();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("创建拜访计划");
		setRightVisibility(false);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_time: // 提交
			// 点击了选择日期按钮
			DateTimePickDialogUtil dateTimePicker = new DateTimePickDialogUtil(
					this, "");
			dateTimePicker.dateTimePicKDialog(tv_time, new DialogCallBack() {
				@Override
				public void callBack() {
				}
			});
			break;
		case R.id.btn_add: // 提交

			RequestParams params = new RequestParams();

			params.put("customer_name", et_name.getText().toString().trim());
			params.put("visit_time", tv_time.getText());
			params.put("visit_places", et_dizhi.getText().toString().trim());
			String url = UrlTools.url + UrlTools.CUSTOMER_ADD_CUSTOMER;
			L.e("创建拜访计划url" + url + " params" + params);
			AsyncHttpServiceHelper.post(url, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							super.onSuccess(arg0, arg1, arg2);
							final JsonBean json = JsonUtils
									.getMessage(new String(arg2));
							if ("200".equals(json.getCode())) {
								runOnUiThread(new Runnable() {
									public void run() {
										T.showShort(ClientAddActivity.this,
												json.getMsg());
										finish();
										overridePendingTransition(
												R.anim.aty_zoomclosein,
												R.anim.aty_zoomcloseout);
									}
								});

							} else {
								T.showShort(ClientAddActivity.this,
										json.getMsg());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							super.onFailure(arg0, arg1, arg2, arg3);

						}
					});

		}
	}
}
