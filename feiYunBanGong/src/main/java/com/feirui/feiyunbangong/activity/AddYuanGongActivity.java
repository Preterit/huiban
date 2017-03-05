package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.List;

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

public class AddYuanGongActivity extends BaseActivity implements
		OnClickListener {

	private Button bt_luru;
	private Button bt_qingkong;
	private List<EditText> ets = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_yuan_gong);

		initUI();
		setListener();

	}

	private void setListener() {

		bt_luru.setOnClickListener(this);
		bt_qingkong.setOnClickListener(this);
	}

	private void initUI() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("添加员工");
		setRightVisibility(false);
		bt_luru = (Button) findViewById(R.id.bt_luru);
		bt_qingkong = (Button) findViewById(R.id.bt_qingkong);
		ets.add((EditText) findViewById(R.id.et01));
		ets.add((EditText) findViewById(R.id.et02));
		ets.add((EditText) findViewById(R.id.et03));
		ets.add((EditText) findViewById(R.id.et04));

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bt_luru:
			luru();
			break;
		case R.id.bt_qingkong:
			qingkong();
			break;
		}

	}

	private void luru() {

		boolean canLuru = true;
		for (int i = 0; i < ets.size(); i++) {
			if (TextUtils.isEmpty(ets.get(i).getText())) {
				canLuru = false;
				String msg = "请输入员工";
				switch (i) {
				case 0:
					msg += "姓名！";
					break;
				case 1:
					msg += "手机！";
					break;
				case 2:
					msg += "职务！";
					break;
				case 3:
					msg += "部门！";
					break;
				}
				Toast.makeText(this, msg, 0).show();
				break;
			}
		}

		if (canLuru) {
			if (!Utils.isPhone(ets.get(1).getText().toString())) {
				Toast.makeText(this, "手机号不合法！", 0).show();
				return;
			}
		}

		if (canLuru) {
			String url = UrlTools.url + UrlTools.ADD_YUANGONG;
			RequestParams params = new RequestParams();
			params.put("staff_name", ets.get(0).getText().toString());
			params.put("staff_mobile", ets.get(1).getText().toString());
			params.put("staff_duties", ets.get(2).getText().toString());
			params.put("staff_department", ets.get(3).getText().toString());

			Utils.doPost(LoadingDialog.getInstance(AddYuanGongActivity.this),
					AddYuanGongActivity.this, url, params, new HttpCallBack() {

						@Override
						public void success(JsonBean bean) {
							Toast.makeText(AddYuanGongActivity.this, "新增员工成功！",
									0).show();
						}

						@Override
						public void failure(String msg) {
							Toast.makeText(AddYuanGongActivity.this, msg, 0)
									.show();
						}

						@Override
						public void finish() {
							// TODO Auto-generated method stub
							
						}
					});

		}

	}

	private void qingkong() {
		for (int i = 0; i < ets.size(); i++) {
			ets.get(i).setText("");
		}
	}
}
