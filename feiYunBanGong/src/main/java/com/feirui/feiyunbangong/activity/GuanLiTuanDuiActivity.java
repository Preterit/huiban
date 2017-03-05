package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.feirui.feiyunbangong.R;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

/**
 * 管理团队：
 * 
 * @author feirui1
 *
 */
public class GuanLiTuanDuiActivity extends BaseActivity implements
		OnClickListener {

	private List<RelativeLayout> rls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guan_li_tuan_dui);
		initView();
		setListener();
	}

	private void setListener() {
		for (int i = 0; i < rls.size(); i++) {
			rls.get(i).setOnClickListener(this);
		}
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("我的团队");
		setRightVisibility(false);
		rls = new ArrayList<>();
		rls.add((RelativeLayout) findViewById(R.id.rl_01));
		rls.add((RelativeLayout) findViewById(R.id.rl_02));
		rls.add((RelativeLayout) findViewById(R.id.rl_03));
		rls.add((RelativeLayout) findViewById(R.id.rl_04));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_01:
			erweima();// 生成二维码：
			break;
		case R.id.rl_02:
			etEmail();// 设置公共邮箱；
			break;
		case R.id.rl_03:
			// 负责人指定：
			startActivity(new Intent(this, SetGuanLIYuanActivity.class));
			overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
			break;
		case R.id.rl_04:

			break;
		}
	}

	// 设置邮箱：

	private void etEmail() {

		final Dialog dialog = new Dialog(this);
		View v = getLayoutInflater().inflate(R.layout.ll_dialog_setemail, null);
		Button bt_queding = (Button) v.findViewById(R.id.bt_queding);
		Button bt_quxiao = (Button) v.findViewById(R.id.bt_quxiao);
		EditText et_email = (EditText) v.findViewById(R.id.et_email);

		bt_queding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO:验证邮箱的正则表达式：

				// TODO:发送邮箱：

			}
		});

		bt_quxiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setContentView(v);
		dialog.setTitle("设置公用邮箱");
		dialog.show();
	}

	private void erweima() {
		String id = "0";
		try {
			Bitmap erweima = EncodingHandler.createQRCode(id, 350);

			Dialog dialog = new Dialog(this);
			View v = getLayoutInflater().inflate(
					R.layout.ll_dialog_erweima_tuandui, null);
			ImageView iv = (ImageView) v.findViewById(R.id.iv_erweima2);
			if (erweima != null) {
				iv.setImageBitmap(erweima);
			}
			dialog.setContentView(v);
			dialog.setTitle("二维码邀请");
			dialog.show();

		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

}
