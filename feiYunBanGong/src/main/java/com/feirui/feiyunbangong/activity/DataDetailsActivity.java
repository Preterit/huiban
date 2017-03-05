package com.feirui.feiyunbangong.activity;

import org.apache.http.Header;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 数据报表-详情
 * 
 * @author admina
 *
 */
public class DataDetailsActivity extends BaseActivity {
	@PView
	TextView tv_name, tv_time, tv_1, tv_2, tv_3, tv_4;// 名字，时间，今日，未完成，明日，备注
	@PView
	TextView tv_a, tv_b, tv_c;
	@PView
	ImageView iv, iv_head;
	BitmapUtils bitmapUtils = new BitmapUtils(this);
	String id = "";
	String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_details);
		initView();
		initData();
	}

	private void initView() {
		id = getIntent().getStringExtra("id");
		type = getIntent().getStringExtra("type");
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString(type + "---详情");
		setRightVisibility(false);
	}

	private void initData() {
		RequestParams params = new RequestParams();

		params.put("id", id);
		String url = UrlTools.url + UrlTools.FORM_DETAILS;
		L.e("数据报表-详情url" + url + " params" + params);
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
									tv_name.setText((String) json.getInfor()
											.get(0).get("staff_name"));
									tv_time.setText((String) json.getInfor()
											.get(0).get("form_time"));
									tv_a.setText((String) json.getInfor()
											.get(0).get("option_one_name"));
									tv_1.setText((String) json.getInfor()
											.get(0).get("option_one_value"));
									tv_b.setText((String) json.getInfor()
											.get(0).get("option_two_name"));
									tv_2.setText((String) json.getInfor()
											.get(0).get("option_two_value"));
									tv_c.setText((String) json.getInfor()
											.get(0).get("option_three_name"));
									tv_3.setText((String) json.getInfor()
											.get(0).get("option_three_value"));
									tv_4.setText((String) json.getInfor()
											.get(0).get("remarks"));
									if (!"img/1_1.png".equals((String) json
											.getInfor().get(0).get("picture"))) {

										// 1,实例化BitmapDisplayConfig
										BitmapDisplayConfig config = new BitmapDisplayConfig();
										// 2,设置bitmapConfig
										config.setBitmapConfig(Bitmap.Config.RGB_565);
										config.setAutoRotation(true);

										// // 3,设置加载失败时显示的图片
										config.setLoadFailedDrawable(DataDetailsActivity.this
												.getResources().getDrawable(
														R.drawable.banner1));
										// // 4,设置正在加载时显示的图片
										config.setLoadingDrawable(DataDetailsActivity.this
												.getResources().getDrawable(
														R.drawable.banner1));
										// 5,设置图片的最大宽高
										config.setBitmapMaxSize(new BitmapSize(
												100, 100));
										// 6,设置disk缓存开启,图片会缓存到sd卡,即DiskLruCache
										bitmapUtils
												.configDiskCacheEnabled(false);

										// 7,设置Memory缓存开启,即LruCache
										bitmapUtils
												.configMemoryCacheEnabled(false);

										bitmapUtils.configThreadPoolSize(2);
										// 8,根据上面设置的条件展示图片
										bitmapUtils
												.display(
														iv,
														(String) json
																.getInfor()
																.get(0)
																.get("picture"),
														config);
										bitmapUtils
												.display(
														iv_head,
														""
																+ AppStore.user
																		.getInfor()
																		.get(0)
																		.get("staff_head"),
														config);
									}
								}
							});

						} else {
							T.showShort(DataDetailsActivity.this, json.getMsg());
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
