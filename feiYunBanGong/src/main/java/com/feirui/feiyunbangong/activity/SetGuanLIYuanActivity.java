package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.SetGuanLiYuanAdapter;
import com.feirui.feiyunbangong.entity.SetGuanLiYuan;

public class SetGuanLIYuanActivity extends BaseActivity {

	private ListView lv_guanliyuan;
	private SetGuanLiYuanAdapter adapter;
	private List<SetGuanLiYuan> sglys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_guan_liyuan);
		initView();
		setListView();
		addData();
	}

	private void addData() {

		for (int i = 0; i < 5; i++) {
			SetGuanLiYuan sgly = new SetGuanLiYuan("1", "哈哈", "", false);
			sglys.add(sgly);
		}
		for (int i = 0; i < 5; i++) {
			SetGuanLiYuan sgly = new SetGuanLiYuan("2", "嘿嘿", "", true);
			sglys.add(sgly);
		}
		adapter.add(sglys);
	}

	private void setListView() {
		adapter = new SetGuanLiYuanAdapter(getLayoutInflater());
		lv_guanliyuan.setAdapter(adapter);
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("设置管理员");
		setRightVisibility(false);
		lv_guanliyuan = (ListView) findViewById(R.id.lv_guanliyuan);
		sglys = new ArrayList<>();
	}

}
