package com.feirui.feiyunbangong.activity;

import android.os.Bundle;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.state.AppStore;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class ZhiFuActivity extends BaseActivity {
    private IWXAPI wxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_fu);
        wxapi = WXAPIFactory.createWXAPI(this, AppStore.APP_ID,false);
        wxapi.registerApp(AppStore.APP_ID);
    }
}
