package com.feirui.feiyunbangong.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.feirui.feiyunbangong.adapter.WiFiGridViewAdapter;
import com.feirui.feiyunbangong.adapter.WifiLVAdapter;
import com.feirui.feiyunbangong.view.MyView;

import java.util.ArrayList;
import java.util.List;

/**
 * wifi业务类：
 */
public class WifiHelper extends BroadcastReceiver {

    private WifiManager wifiManager = null;
    private List<ScanResult> scanResults = new ArrayList<ScanResult>();
    private MyView mMyView;
    private WiFiGridViewAdapter mNameAdaper;
    private WifiLVAdapter adapter02;
    private Context mContext;

    public WifiHelper(MyView myView, WiFiGridViewAdapter nameAdaper, WifiLVAdapter adapter) {

        mMyView = myView;
        mNameAdaper = nameAdaper;
        adapter02 = adapter;

        List<String> x_content = new ArrayList<>();
        List<String> y_content = new ArrayList<>();
        // 设置横纵坐标：
        for (int i = 0; i < 18; i++) {
            x_content.add(i + "");
        }
        for (int i = -100; i <= 0; i += 10) {
            y_content.add(i + "");
        }
        myView.setContent(x_content, y_content);

    }

    public void startScan(Context context) {
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        context.registerReceiver(this, intentFilter);

        mContext = context;
        if (wifiManager == null) {
            wifiManager = (WifiManager) context
                    .getSystemService(context.WIFI_SERVICE);
        }
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();//广播接受器接收结果


    }

    private static void update(final MyView mv, final Context context,
                               final WiFiGridViewAdapter adapter01,
                               final WifiLVAdapter adapter02,
                               List<ScanResult> srs
    ) {

        ArrayList<Integer> wifiStrengh = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        Log.e("orz", "update: " + srs.size());
        int length = srs.size() > 7 ? 7 : srs.size();

        for (int i = 0; i < length; i++) {
            wifiStrengh.add(srs.get(i).level);
            names.add(srs.get(i).SSID);
            srs.add(srs.get(i));
        }
        mv.setStrength(wifiStrengh);
        adapter01.add(names);
        adapter02.add(srs);
        mv.invalidate();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("orz ", "onReceive " + intent.getAction());
        scanResults = wifiManager.getScanResults();
        Log.e("orz", "onReceive: " + scanResults.size());
        if (scanResults.size() != 0) {
            update(mMyView, mContext, mNameAdaper, adapter02, scanResults);
        }
    }
}
