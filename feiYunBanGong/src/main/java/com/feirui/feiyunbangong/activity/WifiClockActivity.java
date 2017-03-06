package com.feirui.feiyunbangong.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.WiFiGridViewAdapter;
import com.feirui.feiyunbangong.adapter.WifiLVAdapter;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.WifiHelper;
import com.feirui.feiyunbangong.view.MyView;
import com.feirui.feiyunbangong.view.PView;

public class WifiClockActivity extends BaseActivity implements OnClickListener {

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


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_clock);

        init();

        gv_wifi = (GridView) findViewById(R.id.gv_wifi);
        lv_wifi = (ListView) findViewById(R.id.lv_wifi);


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                T.showShort(this, "上传成功！");
                finish();
                break;
        }
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
