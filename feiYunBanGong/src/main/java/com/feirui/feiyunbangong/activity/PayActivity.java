package com.feirui.feiyunbangong.activity;

import android.os.Bundle;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.state.AppStore;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by feirui1 on 2017-08-24.
 */

public class PayActivity extends BaseActivity {
    private IWXAPI wxapi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_wu_list);
        wxapi = WXAPIFactory.createWXAPI(this, AppStore.APP_ID,false);
        wxapi.registerApp(AppStore.APP_ID);
//        initView();
    }

}
