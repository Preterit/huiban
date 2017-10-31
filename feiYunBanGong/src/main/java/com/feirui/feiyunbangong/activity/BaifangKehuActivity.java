package com.feirui.feiyunbangong.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.DateUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.RequestParams;

import java.util.List;

/**
 * 拜访客户
 * 
 * @author admina
 *
 */
public class BaifangKehuActivity extends BaseActivity {
	@PView(click = "onClick")
	Button btn_add;// 签到
	@PView(click = "onClick")
	TextView tv_time, tv_section, tv_name, tv_site, tv_see_time;// 当前时间，当前位置，姓名,地址，时间

	MapView mMapView;
	BaiduMap mBaiduMap;
	BitmapDescriptor markerIcon;
	LocationClient mLocationClient = null;
	BDLocationListener myListener = new MyLocationListener();
	String id = "";
	String address = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_baifang_kehu);
		initView();
		initData();
		initLocation();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("拜访客户");
		setRightVisibility(false);
		tv_time = (TextView) findViewById(R.id.tv_form_time);
		id = getIntent().getStringExtra("id");
		tv_time.setText(DateUtils.getTime("yyyy-MM-dd",
				System.currentTimeMillis()));
	}

	private void initData() {
		RequestParams params = new RequestParams();

		params.put("id", id);
		String url = UrlTools.url + UrlTools.CUSTOMER_SHOW_LIST;
		L.e("拜访客户url" + url + " params" + params);

		Utils.doPost(LoadingDialog.getInstance(this), BaifangKehuActivity.this,
				url, params, new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						tv_name.setText((String) bean.getInfor().get(0)
								.get("customer_name"));
						tv_site.setText((String) bean.getInfor().get(0)
								.get("visit_places"));
						tv_see_time.setText((String) bean.getInfor().get(0)
								.get("visit_time"));
						if (bean.getInfor().get(0).get("visit_state")
								.equals("签到")) {
							btn_add.setText("签退");
						}
					}

					@Override
					public void failure(String msg) {
						T.showShort(BaifangKehuActivity.this, msg);
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
						
					}
				});
	}

	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.btn_add: // 签到,签退
			String url = null;
			RequestParams params = new RequestParams();

			params.put("customer_id", id);
			params.put("address", address);
			if (btn_add.getText().equals("签到")) {
				url = UrlTools.url + UrlTools.CUSTOMER_CUSTOMER_SIGN;
			} else {
				url = UrlTools.url + UrlTools.CUSTOMER_CUSTOMER_BACK;
			}

			L.e("签到url" + url + " params" + params);

			Utils.doPost(LoadingDialog.getInstance(this),
					BaifangKehuActivity.this, url, params, new HttpCallBack() {

						@Override
						public void success(JsonBean bean) {
							T.showShort(BaifangKehuActivity.this, "成功！");
							finish();
						}

						@Override
						public void failure(String msg) {
							T.showShort(BaifangKehuActivity.this, msg);
						}

						@Override
						public void finish() {
							// TODO Auto-generated method stub
							
						}
					});

			break;
		case R.id.tv_section: // 当前位置
			initLocation();
			break;

		}
	}

	private void initLocation() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		markerIcon = BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.location));
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

		mLocationClient = new LocationClient(BaifangKehuActivity.this); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();

		option.setLocationMode(LocationMode.Battery_Saving);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系

		option.setScanSpan(60 * 30 * 1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				OverlayOptions oo = new MarkerOptions().position(ll)
						.icon(markerIcon).zIndex(9).draggable(true);
				mBaiduMap.addOverlay(oo);
			}

			Log.i("BaiduLocationApiDem", sb.toString());

		}

		@Override
		public void onConnectHotSpotMessage(String s, int i) {

		}


	}
}
