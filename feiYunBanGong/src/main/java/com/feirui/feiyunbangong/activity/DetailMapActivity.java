package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.utils.BaiDuUtil;
import com.feirui.feiyunbangong.view.PView;

public class DetailMapActivity extends BaseActivity {

	@PView
	private MapView bmapView;
	@PView
	private TextView tv_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());// 切记！！！！！必须是applicationcontext;
		setContentView(R.layout.activity_detail_map);

		initView();

	}

	private void initView() {

		initTitle();

		setLeftDrawable(R.drawable.arrows_left);
		setRightVisibility(false);

		bmapView = (MapView) findViewById(R.id.bmapView);
		Intent intent = getIntent();
		double latitude = intent.getDoubleExtra("latitude", 0.00);// 纬度
		double longitude = intent.getDoubleExtra("longitude", 0.00);// 经度
		String address = intent.getStringExtra("address");

		tv_address.setText(address);

		setCenterString("查看位置");

		BaiDuUtil.setResult(bmapView, address, latitude, longitude, this);

	}

}
