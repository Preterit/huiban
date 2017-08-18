package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.YaoQingAdapter;
import com.feirui.feiyunbangong.dialog.EtDialog;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * 邀请奖励
 * 
 * @author admina
 *
 */
public class YaoQingActivity extends BaseActivity {
	// 获取、邀请码,总金额
	@PView(click = "onClick")
	TextView tv_yaoqingma, tv_zongjine;
	@PView
	ListView lv_yiyaoqing;
	@PView(click = "onClick")
	Button btn_duanxin;
	private YaoQingAdapter adapter;
	private JsonBean json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yaoqing);
		initView();
		initData();
		initData1();
		initDate2();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("邀请奖励");
		setRightDrawable(R.drawable.jianglitixian);
		rightll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(YaoQingActivity.this,
						TiXianActivity.class));
				overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
			}
		});
	}

	/**
	 * 获取邀请码
	 */
	private void initData() {
		RequestParams params = new RequestParams();

		String url = UrlTools.url + UrlTools.APPLICATION_SHOW_CODE;
		L.e("邀请码url" + url + " params" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						final JsonBean json = JsonUtils.getMessage(new String(
								arg2));
						if ("200".equals(json.getCode())) {
							runOnUiThread(new Runnable() {
								public void run() {
									tv_yaoqingma.setText(""
											+ json.getInfor().get(0)
													.get("infor"));
									AppStore.yaoqingma = ""
											+ json.getInfor().get(0)
													.get("infor");
								}
							});

						} else {
							tv_yaoqingma.setText("获取");
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						super.onFailure(arg0, arg1, arg2, arg3);

					}
				});
	}

	/**
	 * 获取总费用
	 */
	private void initData1() {
		RequestParams params = new RequestParams();

		String url = UrlTools.url + UrlTools.APPLICATION_STATISTICS;
		L.e("总费用url" + url + " params" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						final JsonBean json = JsonUtils.getMessage(new String(
								arg2));
						if ("200".equals(json.getCode())) {
							runOnUiThread(new Runnable() {
								public void run() {
									tv_zongjine.setText("￥  "
											+ json.getInfor().get(0)
													.get("infor") + "元");
								}
							});

						} else {
							runOnUiThread(new Runnable() {
								public void run() {
									tv_zongjine.setText("￥  00.00");
								}
							});
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						super.onFailure(arg0, arg1, arg2, arg3);

					}
				});
	}

	/**
	 * 邀请详情
	 */
	private void initDate2() {
		RequestParams params = new RequestParams();

		String url = UrlTools.url + UrlTools.APPLICATION_STATISLIST;
		L.e("邀请详情url" + url + " params" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						json = JsonUtils.getMessage(new String(arg2));
						if ("200".equals(json.getCode())) {
							runOnUiThread(new Runnable() {
								public void run() {
									adapter = new YaoQingAdapter(
											YaoQingActivity.this, json);
									lv_yiyaoqing.setAdapter(adapter);
									adapter.notifyDataSetChanged();
								}
							});

						} else {
							T.showShort(YaoQingActivity.this, json.getMsg());
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
		case R.id.tv_yaoqingma: // 获取

			RequestParams params = new RequestParams();

			String url = UrlTools.url + UrlTools.APPLICATION_INVITATION;
			L.e("获取邀请码url" + url + " params" + params);
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
										tv_yaoqingma.setText(""
												+ json.getInfor().get(0)
														.get("infor"));
										AppStore.yaoqingma = json.getInfor()
												.get(0).get("infor")
												+ "";
									}
								});

							} else {
								T.showShort(YaoQingActivity.this, json.getMsg());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							super.onFailure(arg0, arg1, arg2, arg3);

						}
					});

			break;
		case R.id.btn_duanxin: // 邀请

			EtDialog dialog1 = new EtDialog("短信邀请", "请输入手机号",
					YaoQingActivity.this, new EtDialog.AlertCallBack1() {

						@Override
						public void onOK(String name) {

							if (Utils.isPhone(name)) {

								String url = UrlTools.url + UrlTools.APPLICATION_INVITATIONMESSAGE;

								RequestParams params = new RequestParams();
								params.put("staff_mobile", name);
								Utils.doPost(LoadingDialog
										.getInstance(YaoQingActivity.this),
										YaoQingActivity.this, url, params,
										new HttpCallBack() {

											@Override
											public void success(JsonBean bean) {
												T.showShort(
														YaoQingActivity.this,
														"发送成功！");
											}

											@Override
											public void failure(String msg) {
												T.showShort(
														YaoQingActivity.this,
														msg);
											}

											@Override
											public void finish() {
												// TODO Auto-generated method
												// stub

											}
										});

							} else {

							}

						}

						@Override
						public void onCancel() {

						}
					});

			dialog1.show();

			break;

		}
	}
}
