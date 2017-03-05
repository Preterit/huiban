package com.feirui.feiyunbangong.activity;

import org.apache.http.Header;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class YiJianActivity extends BaseActivity implements OnClickListener {

	private CheckBox cb;
	private EditText et;
	private Button bt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yi_jian);
		initView();
		setListener();
	}

	private void setListener() {
		bt.setOnClickListener(this);
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("意见反馈");
		setRightVisibility(false);
		cb = (CheckBox) findViewById(R.id.cb_niming);
		et = (EditText) findViewById(R.id.et_fankui);
		bt = (Button) findViewById(R.id.bt_submit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_submit:
			submit();
			break;
		}
	}

	private void submit() {

		String content = et.getText().toString();

		if (TextUtils.isEmpty(content.trim())) {
			Toast.makeText(this, "请输入反馈内容！", 0).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.put("content", content);
		params.put("anonymous", cb.isChecked() ? "0" : "1");
		String url = UrlTools.url + UrlTools.FANKUI;

		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

						final JsonBean bean = new Gson().fromJson(new String(
								arg2), JsonBean.class);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								if ("200".equals(bean.getCode())) {
									Toast.makeText(YiJianActivity.this,
											"提交成功！", 0).show();
									finish();
								} else {
									Toast.makeText(YiJianActivity.this,
											bean.getMsg(), 0).show();
								}
							}
						});

						super.onSuccess(arg0, arg1, arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(YiJianActivity.this, "提交失败！", 0)
										.show();
							}
						});
						super.onFailure(arg0, arg1, arg2, arg3);
					}

				});

	}

}
