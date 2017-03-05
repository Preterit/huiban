package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;

public class AddGongGaoActivity extends BaseActivity implements OnClickListener {

	private EditText et_gonggao;
	private Button bt_gonggao;
	private String id;// 团队id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_gong_gao);
		initView();

		setListener();
	}

	private void setListener() {
		bt_gonggao.setOnClickListener(this);
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("发布公告");
		setRightVisibility(false);

		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		et_gonggao = (EditText) findViewById(R.id.et_gongao);
		bt_gonggao = (Button) findViewById(R.id.bt_submit);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_submit:
			submit();
			break;
		}

	}

	private void submit() {

		if (TextUtils.isEmpty(et_gonggao.getText().toString().trim())) {
			Toast.makeText(this, "请输入公告内容！", 0).show();
			return;
		}

		String url = UrlTools.url + UrlTools.FABU_GONGGAO;
		RequestParams params = new RequestParams();
		params.put("teamid", id + "");
		params.put("content", et_gonggao.getText().toString().trim() + "");

		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						Toast.makeText(AddGongGaoActivity.this, "提交成功！", 0)
								.show();
						finish();
					}

					@Override
					public void failure(String msg) {
						Toast.makeText(AddGongGaoActivity.this, msg, 0).show();
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
						
					}
				});

	}

}
