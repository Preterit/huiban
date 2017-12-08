package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.JinDuAdapter;
import com.feirui.feiyunbangong.entity.JinDuBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.List;

import static com.feirui.feiyunbangong.R.id.iv_fankui;


public class MyTaskDetailActivity extends BaseActivity implements View.OnClickListener {
    private String staff_name, time, task_txt, task_zt, staff_head, id;
    private TextImageView iv_head;
    private TextView tv_name, tv_time, tv_task, tv_zt;
    private ImageView iv_complete;
    MapView mMapView;
    BaiduMap mBaiduMap;
    BitmapDescriptor markerIcon;
    LocationClient mLocationClient = null;
    BDLocationListener myListener = new MyLocationListener();
    String address = "";
    private ListView lv_jindu;
    private JinDuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task_detail);
        task_zt = getIntent().getStringExtra("task_zt");
        staff_name = getIntent().getStringExtra("staff_name");
        time = getIntent().getStringExtra("time");
        task_txt = getIntent().getStringExtra("task_txt");
        staff_head = getIntent().getStringExtra("staff_head");
        id = getIntent().getStringExtra("id");
        initView();
        initData();
    }
    private void initData() {
        RequestParams params2 = new RequestParams();
        String url2 = "http://123.57.45.74/feiybg1/public/index.php/home_api/task/Single_details";
        params2.put("id", id + "");
        AsyncHttpServiceHelper.post(url2, params2, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Gson gson = new Gson();
                JinDuBean jindu = gson.fromJson(new String(responseBody), JinDuBean.class);
                if (jindu.getInfo1() != null) {
                    adapter = new JinDuAdapter(MyTaskDetailActivity.this, jindu.getInfo1());
                    lv_jindu.setAdapter(adapter);
                } else {
                    lv_jindu.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("任务单详情");
        setRightVisibility(true);
        lv_jindu = (ListView) findViewById(R.id.lv_jindu);
        iv_head = (TextImageView) findViewById(R.id.tiv_head);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_task = (TextView) findViewById(R.id.tv_task);
        tv_zt = (TextView) findViewById(R.id.tv_zt);
        tv_zt.setText(task_zt);
        tv_name.setText(staff_name);
        tv_time.setText(time);
        tv_task.setText(task_txt);
        iv_complete = (ImageView) findViewById(iv_fankui);
        iv_complete.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(staff_head, iv_head, ImageLoaderUtils.getSimpleOptions());
    }

    private void initLocation() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        markerIcon = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.location));
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mLocationClient = new LocationClient(MyTaskDetailActivity.this); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系

        option.setScanSpan(60 * 30 * 1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置 是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.righttv:

                break;
            case iv_fankui:
                Intent intent = new Intent(MyTaskDetailActivity.this, MyReleaseTaskActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
//                RequestParams params2 = new RequestParams();
//                //String url2 = UrlTools.pcUrl + UrlTools.RENWU_QRJD;
//                String url2 = "http://123.57.45.74/feiybg1/public/index.php/home_api/task/taskFinish";
//                params2.put("id", id);
//                params2.put("button", "1");
//                Log.e("全部任务获取成功-确认接单", "id: " + id);
//                post(url2, params2, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        final JsonBean json = JsonUtils.getMessage(new String(responseBody));
//                        if ("200".equals(json.getCode())) {
//                                Toast.makeText(MyTaskDetailActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
//                                finish();
//                        } else if ("-400".equals(json.getCode())) {
//                            Toast.makeText(MyTaskDetailActivity.this, "该任务已完成", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//                                          Throwable arg3) {
//                        super.onFailure(arg0, arg1, arg2, arg3);
//                    }
//                });
                break;
        }
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

    }
}
