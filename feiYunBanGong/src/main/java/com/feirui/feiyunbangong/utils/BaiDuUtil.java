package com.feirui.feiyunbangong.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.feirui.feiyunbangong.R;

/**
 * 百度地图业务类：
 */
public class BaiDuUtil {

	// 开启定位：
	public static void initLocation(Activity activity,
			BDLocationListener myListener) {

		LocationClient mLocationClient = new LocationClient(activity); // 声明LocationClient类
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

	// 获取定位位置：包括位置描述，经纬度；
	public static Object[] getResult(BDLocation location) {

		Object[] obj = new Object[3];
		List<Poi> poiList = location.getPoiList();

		for (int i = 0; i < poiList.size(); i++) {
			Log.e("TAG", poiList.get(i).getName() + "poiList.get(i).getName()");
		}

		String address = String.valueOf(location.getAddrStr());
		Log.e("TAG", "location.getDistrict()" + location.getDistrict());
		Log.e("TAG", location.getLocationDescribe()
				+ "location.getLocationDescribe()");

		double latitude = location.getLatitude();// 纬度；
		double longitude = location.getLongitude();// 经度；

		obj[0] = address;
		obj[1] = latitude;
		obj[2] = longitude;

		return obj;
	}

	// 将结果显示到地图上：
	public static void setResult(MapView mMapView, String address,
			double latitude, double longitude, Activity activity) {
		BaiduMap mBaiduMap = null;
		BitmapDescriptor markerIcon = null;

		if (mMapView != null) {
			mBaiduMap = mMapView.getMap();
			// 普通地图
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		}

		markerIcon = BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(activity.getResources(), R.drawable.location));

		LatLng ll = new LatLng(latitude, longitude);

		OverlayOptions oo = new MarkerOptions().position(ll).icon(markerIcon)
				.zIndex(9).draggable(true);
		mBaiduMap.addOverlay(oo);
	}

	// 根据地址获取经纬度封装的对象：

	public static GeoPoint getGeoPointBystr(String str, Activity activity) {
		GeoPoint gpGeoPoint = null;
		if (str != null) {
			Geocoder gc = new Geocoder(activity, Locale.CHINA);
			List<android.location.Address> addressList = null;
			try {
				addressList = gc.getFromLocationName(str, 1);
				if (!addressList.isEmpty()) {
					android.location.Address address_temp = addressList.get(0);
					// 计算经纬度
					double Latitude = address_temp.getLatitude() * 1E6;
					double Longitude = address_temp.getLongitude() * 1E6;
					System.out.println("经度：" + Latitude);
					System.out.println("纬度：" + Longitude);
					// 生产GeoPoint
					gpGeoPoint = new GeoPoint((int) Latitude, (int) Longitude);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return gpGeoPoint;
	}

}
