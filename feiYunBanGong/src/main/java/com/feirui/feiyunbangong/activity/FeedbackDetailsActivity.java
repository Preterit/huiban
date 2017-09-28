package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * 反馈-详情
 * 
 * @author admina
 *
 */
public class FeedbackDetailsActivity extends BaseActivity {
	@PView
	TextView tv_name, tv_gongsiming, tv_zhiwei, tv_yixiang, tv_shuoming;// 姓名，公司名，职位，意向，说明

	String id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback_details);
		initView();
		initData();
	}

	private void initView() {
		id = getIntent().getStringExtra("id");
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("反馈详情");
		setRightVisibility(false);
	}

	private void initData() {
		RequestParams params = new RequestParams();

		params.put("id", id);
		String url = UrlTools.url + UrlTools.CUSTOMER_FEEDBACK_LIST;
		L.e("反馈-详情url" + url + " params" + params);
		AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						final JsonBean json = JsonUtils.getMessage(new String(
								arg2));
						if ("200".equals(json.getCode())) {
							runOnUiThread(new Runnable() {
								public void run() {
									tv_name.setText((String) json.getInfor()
											.get(0).get("customer_name"));
									tv_gongsiming.setText((String) json
											.getInfor().get(0)
											.get("customer_company"));
									tv_zhiwei.setText((String) json.getInfor()
											.get(0).get("company_position"));
									tv_yixiang.setText((String) json.getInfor()
											.get(0).get("intention"));
									tv_shuoming.setText((String) json
											.getInfor().get(0).get("captions"));
								}
							});

						} else {
							T.showShort(FeedbackDetailsActivity.this,
									json.getMsg());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						super.onFailure(arg0, arg1, arg2, arg3);

					}
				});

	}
}
