package com.feirui.feiyunbangong.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.WiFiGridViewAdapter;
import com.feirui.feiyunbangong.adapter.WifiLVAdapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.WifiHelper;
import com.feirui.feiyunbangong.view.MyView;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.List;

public class WifiClockActivity extends BaseActivity  {

    @PView
    private MyView mv;
    @PView
    private GridView gv_wifi;
    @PView
    private ListView lv_wifi;
    @PView(click = "onClick")
    private TextView tv_commit;

    private WifiReceiver mWifiReceiver;
    private WifiHelper mWifiHelper;
    private WifiManager wifi;
    private RequestParams params;
    private String address;
    private String url;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    T.showShort(WifiClockActivity.this, ((JsonBean) msg.obj).getMsg());
                    break;
                case 1:
                    T.showShort(WifiClockActivity.this, ((JsonBean) msg.obj).getMsg());
                    break;
            }
        };

    };


    private String tag;
    private String ssid;
    private String qian;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_clock);

        init();

        tag="0";
        gv_wifi = (GridView) findViewById(R.id.gv_wifi);
        lv_wifi = (ListView) findViewById(R.id.lv_wifi);
        tv_commit=(TextView)findViewById(R.id.tv_commit);

        getwifiInfo();
        obtainWifiInfo();
        getcurrentwifi();





        WiFiGridViewAdapter adapter01 = new WiFiGridViewAdapter(
                getLayoutInflater());
        gv_wifi.setAdapter(adapter01);

        WifiLVAdapter adapter02 = new WifiLVAdapter(getLayoutInflater());
        lv_wifi.setAdapter(adapter02);


        mWifiReceiver = new WifiReceiver();
        registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        mWifiHelper = new WifiHelper(mv, adapter01, adapter02);
        mWifiHelper.startScan(this);
    }

    private void getcurrentwifi() {

        //获取当前连接的wifi信息
        WifiManager wifiManager= (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        Log.e("wifi", "wifi的BSSID"+wifiInfo.getBSSID());
        address=wifiInfo.getBSSID();
        ssid=wifiInfo.getSSID();
    }

    private String getwifiInfo() {

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();

        String maxText = info.getMacAddress();
        String ipText = intToIp(info.getIpAddress());
        String status = "";
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
        {
            status = "WIFI_STATE_ENABLED";
        }
        String ssid = info.getSSID();
        int networkID = info.getNetworkId();
        int speed = info.getLinkSpeed();
        String bssid = info.getBSSID();
        return  "mac：" + maxText + "\n\r"
                + "ip：" + ipText + "\n\r"
                + "wifi status :" + status + "\n\r"
                + "ssid :" + ssid + "\n\r"
                + "net work id :" + networkID + "\n\r"
                + "connection speed:" + speed + "\n\r"
                + "BSSID:" + bssid + "\n\r";


    }

    private String intToIp(int ip)
    {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }



    private void obtainWifiInfo() {

        // 显示扫描到的所有wifi信息：
        wifi= (WifiManager) getSystemService(WIFI_SERVICE);
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
        {
            StringBuilder scanBuilder= new StringBuilder();
            List<ScanResult> scanResults=wifi.getScanResults();//搜索到的设备列表

            for (ScanResult scanResult : scanResults) {

                scanBuilder.append("\n设备名："+scanResult.SSID
                        +"\n信号强度："+wifi.calculateSignalLevel(scanResult.level,1001)
                        +"\nBSSID:" + scanResult.BSSID);
            }
            Log.e("tag", "wifi信息: "+scanBuilder);
        }

    }


    private void init() {
        initTitle();
        setCenterString("WiFi打卡");
        setLeftDrawable(R.drawable.arrows_left);
        setLeftVisibility(true);

        mv = (MyView) findViewById(R.id.mv);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_commit:
//                Log.e("tag", "tag的值"+tag );
//                if("0".equals(tag)||tag.equals("")){
//
//                    wifiqiandao_commit();
//                }else if("1".equals(tag)){
//
//                    wifiqiantui_commit();
//
//                }
//                break;
//        }
//    }


    private void wifiqiandao_commit() {
        params = new RequestParams();
        params.put("work_type", "签到");
        params.put("address", address);
        params.put("check_type", "1");
        params.put("ssid",ssid);
        url = UrlTools.url + UrlTools.HOME_WORK_START;
        L.e("签退url=" + url + " params=" + params);
        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        super.onSuccess(arg0, arg1, arg2);
                        try {
                            JsonBean json = JsonUtils
                                    .getMessage(new String(arg2));
                            final Message mse = new Message();
                            if ("200".equals(json.getCode())) {
                                mse.what = 0;
                                SharedPreferences sharedPref=  getSharedPreferences("TAG",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                SharedPreferences sharedPref1=  getSharedPreferences("QIAN",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPref1.edit();
                                editor.putString("TAG","1");
                                editor.commit();
                                editor1.putString("QIAN","下班WIFI打卡");
                                editor1.commit();

                                finish();
                            } else {
                                mse.what = 1;
                                T.makeText(WifiClockActivity.this,"打卡失败！", Toast.LENGTH_SHORT).show();
                            }
                            mse.obj = json;
                            handler.sendMessage(mse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void wifiqiantui_commit() {
        params = new RequestParams();
        params.put("work_type", "签退");
        params.put("address", address);
        params.put("check_type", "1");
        params.put("ssid",ssid);
        url = UrlTools.url + UrlTools.HOME_WORK_END;
        L.e("签退url=" + url + " params=" + params);
        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        super.onSuccess(arg0, arg1, arg2);
                        try {
                            JsonBean json = JsonUtils.getMessage(new String(arg2));
                            final Message mse = new Message();

                            if ("200".equals(json.getCode())) {
                                mse.what = 0;
                                SharedPreferences sharedPref=  getSharedPreferences("TAG",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                SharedPreferences sharedPref1=  getSharedPreferences("QIAN",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPref1.edit();
                                editor1.putString("QIAN","上班WIFI打卡");
                                editor1.commit();
                                editor.putString("TAG","0");
                                editor.commit();
                                finish();
                            } else {
                                mse.what = 1;
                                T.makeText(WifiClockActivity.this,"打卡失败！", Toast.LENGTH_SHORT).show();
                            }
                            mse.obj = json;
                            handler.sendMessage(mse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("TAG", Context.MODE_PRIVATE);
        tag=sp.getString("TAG", "");
        SharedPreferences sp1 = getSharedPreferences("QIAN", Context.MODE_PRIVATE);
        qian=sp1.getString("QIAN","");
        if(tag.equals("")){
            tag="0";
        }
        if("".equals(qian)){
            qian="上班WIFI签到";
        }
        tv_commit.setText(qian);
        setlisteners();
        Log.e("tag", "tag的值"+tag+"     qian的值"+qian);

    }

    private void setlisteners() {

        tv_commit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if("0".equals(tag)||tag.equals("")){
                    wifiqiandao_commit();
                }else if("1".equals(tag)){
                    wifiqiantui_commit();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mWifiReceiver);
    }



    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                mWifiHelper.getWifiManager().getScanResults();
                Log.e("orz", mWifiHelper.getWifiManager().getScanResults().size() + "");
                mWifiHelper.update(mv, mWifiHelper.getWifiManager().getScanResults());
            }
        }
    }
}
