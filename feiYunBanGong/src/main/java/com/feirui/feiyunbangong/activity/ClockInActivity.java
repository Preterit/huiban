package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.DateUtils;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

/**
 * 打卡
 * 
 * @author admina
 *
 */
public class ClockInActivity extends BaseActivity {
	// 签到，签退
	@PView(click = "onClick")
	LinearLayout ll_start, ll_end;
	// 上班时间，下班时间，周，日期
	@PView(click = "onClick")
	TextView tv_in, tv_out, tv_week, tv_day, tv_sign;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	BitmapDescriptor markerIcon;
	BaiduMap mBaiduMap;
	String address;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				T.showShort(ClockInActivity.this, ((JsonBean) msg.obj).getMsg());
				break;
			case 1:
				T.showShort(ClockInActivity.this, ((JsonBean) msg.obj).getMsg());
				break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clock_in);
		initView();
		initData();
		initLocation();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("打卡");
		setRightVisibility(false);
		// 显示时间格式
		tv_day.setText(DateUtils.getTime("yyyy年MM月dd日",
				System.currentTimeMillis()));
		tv_week.setText(DateUtils.StringData());
	}

	/**
	 * 获取上下班时间
	 */
	private void initData() {
		RequestParams params = new RequestParams();
		String url = UrlTools.url + UrlTools.HOME_ATTENDANCE;
		L.e("获取上下班时间url=" + url + " params=" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						try {
							final JsonBean json = JsonUtils
									.getMessage(new String(arg2));
							final Message mse = new Message();
							if ("200".equals(json.getCode())) {
								runOnUiThread(new Runnable() {
									public void run() {
										tv_in.setText((String) json.getInfor()
												.get(0).get("work_set_stime"));
										tv_out.setText((String) json.getInfor()
												.get(0).get("work_set_etime"));
									}
								});
							} else {
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void initLocation() {
		mLocationClient = new LocationClient(ClockInActivity.this); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();

		option.setLocationMode(LocationMode.Battery_Saving);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系

		option.setScanSpan(30 * 60 * 1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);

		mLocationClient.start();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(final BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());

			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// 单位度
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
			}
			sb.append("\nlocationdescribe : ");
			sb.append(location.getLocationDescribe());// 位置语义化信息
			List<Poi> list = location.getPoiList();// POI数据
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			if (list != null) {
				Log.e("tag", "获取位置成功");
				// 设置定位数据
				address = String.valueOf(location.getAddrStr());

			}

			Log.i("BaiduLocationApiDem", sb.toString());

		}

	}

	public void onClick(View view) {
		RequestParams params;
		String url;
		switch (view.getId()) {
		case R.id.ll_start: // 签到
			params = new RequestParams();
			params.put("work_type", "签到");
			params.put("address", address);
			url = UrlTools.url + UrlTools.HOME_WORK_START;
			L.e("签到url=" + url + " params=" + params);
			AsyncHttpServiceHelper.post(url, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							super.onSuccess(arg0, arg1, arg2);
							try {
								JsonBean json = JsonUtils
										.getMessage(new String(arg2));
								final Message mse = new Message();
								if ("200".equals(json.getCode())) {
									mse.what = 0;
								} else {
									mse.what = 1;
								}
								mse.obj = json;
								handler.sendMessage(mse);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
			break;
		case R.id.ll_end: // 签退
			params = new RequestParams();
			params.put("work_type", "签退");
			params.put("address", address);
			url = UrlTools.url + UrlTools.HOME_WORK_END;
			L.e("签退url=" + url + " params=" + params);
			AsyncHttpServiceHelper.post(url, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							super.onSuccess(arg0, arg1, arg2);
							try {
								JsonBean json = JsonUtils
										.getMessage(new String(arg2));
								final Message mse = new Message();
								if ("200".equals(json.getCode())) {
									mse.what = 0;
								} else {
									mse.what = 1;
								}
								mse.obj = json;
								handler.sendMessage(mse);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
			break;
		case R.id.tv_sign:
			startActivity(new Intent(ClockInActivity.this,
					WifiClockActivity.class));
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			mLocationClient = null;
		}
	}

}
