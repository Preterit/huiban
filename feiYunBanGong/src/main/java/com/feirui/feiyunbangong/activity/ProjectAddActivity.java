package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddShenHeAdapter;
import com.feirui.feiyunbangong.entity.AddShenHe;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.DateTimePickDialogUtil;
import com.feirui.feiyunbangong.utils.DateTimePickDialogUtil.DialogCallBack;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * 创建一个新项目
 * 
 * @author admina
 *
 */
public class ProjectAddActivity extends BaseActivity implements OnClickListener {
	@PView(click = "onClick")
	TextView tv_kaishishijian, tv_jieshushijian;// 开始时间，结束时间
	@PView
	EditText et_fuzeren, et_xiangmuming, et_xiangqing;// 负责人，项目名，项目详情
	@PView(click = "onClick")
	Button btn_add;// 创建按钮
	// 添加审批人
	@PView(click = "onClick")
	ImageView iv_add, iv_01;
	@PView
	ListView lv_add_chengyuan;
	AddShenHeAdapter adapter;
	@PView
	ScrollView sv_caigou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_add);
		initView();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("创建一个新项目");
		setRightVisibility(false);
		adapter = new AddShenHeAdapter(getLayoutInflater(),
				ProjectAddActivity.this);
		lv_add_chengyuan.setAdapter(adapter);
		lv_add_chengyuan.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_UP) {
					sv_caigou.requestDisallowInterceptTouchEvent(false);
				} else {
					sv_caigou.requestDisallowInterceptTouchEvent(true);
				}
				return false;
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 101:
			ShenPiRen spr = (ShenPiRen) data.getSerializableExtra("shenpiren");
			if (spr.getId().equals("0")) {
				return;
			}
			AddShenHe ash = new AddShenHe(spr.getName(), spr.getId());
			adapter.add(ash);
			break;

		}
	};

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_01:
			adapter.reduce(((AddShenHe) view.getTag()));// 删除审批人；
			break;
		case R.id.iv_add:
			Intent intent = new Intent(this, ShenPiRenActivity.class);
			startActivityForResult(intent, 101);// 请求码；
			break;
		case R.id.tv_kaishishijian: // 开始时间
			// 点击了选择日期按钮
			DateTimePickDialogUtil kaishishijian = new DateTimePickDialogUtil(
					this, "");
			kaishishijian.dateTimePicKDialog(tv_kaishishijian,
					new DialogCallBack() {
						@Override
						public void callBack() {
						}
					});
			break;
		case R.id.tv_jieshushijian: // 结束时间
			// 点击了选择日期按钮
			DateTimePickDialogUtil jieshushijian = new DateTimePickDialogUtil(
					this, "");
			jieshushijian.dateTimePicKDialog(tv_jieshushijian,
					new DialogCallBack() {
						@Override
						public void callBack() {
						}
					});
			break;
		case R.id.btn_add: // 创建项目
			RequestParams params = new RequestParams();
			params.put("principal_person", et_fuzeren.getText().toString()
					.trim());
			StringBuffer sb_id = new StringBuffer();
			for (int i = 0; i < adapter.getCount(); i++) {
				AddShenHe ash = (AddShenHe) adapter.getItem(i);
				sb_id.append(ash.getId() + ",");
			}
			params.put("project_member", sb_id.deleteCharAt(sb_id.length() - 1)
					.toString());
			params.put("begin_time", tv_kaishishijian.getText().toString()
					.trim());
			params.put("end_time", tv_jieshushijian.getText().toString().trim());
			params.put("project_name", et_xiangmuming.getText().toString()
					.trim());
			params.put("project_detail", et_xiangqing.getText().toString()
					.trim());
			String url = UrlTools.url + UrlTools.PROJECT_ADD_PROJECT;
			L.e("创建项目url=" + url + " params=" + params);
			AsyncHttpServiceHelper.post(url, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							super.onSuccess(arg0, arg1, arg2);
							try {
								final JsonBean json = JsonUtils
										.getMessage(new String(arg2));
								runOnUiThread(new Runnable() {
									public void run() {

										if ("200".equals(json.getCode())) {
											T.showShort(
													ProjectAddActivity.this,
													json.getMsg());
											finish();
											overridePendingTransition(
													R.anim.aty_zoomclosein,
													R.anim.aty_zoomcloseout);
										} else {
											T.showShort(
													ProjectAddActivity.this,
													json.getMsg());
										}
									}
								});
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
			break;

		}
	}

}
