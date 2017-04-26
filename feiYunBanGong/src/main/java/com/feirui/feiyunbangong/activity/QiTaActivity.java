package com.feirui.feiyunbangong.activity;

import org.apache.http.Header;

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
import com.feirui.feiyunbangong.adapter.AddShenHeUpdateAdapter;
import com.feirui.feiyunbangong.entity.AddShenHe;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.state.AppStore;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 审批-其他
 * 
 * @author admina
 *
 */
public class QiTaActivity extends BaseActivity implements OnClickListener {
	@PView(click = "onClick")
	Button btn_submit;// 提交
	@PView(click = "onClick")
	TextView tv_shijian;// 时间
	@PView
	EditText et_miaoshu;// 描述
	// 添加审批人
	@PView(click = "onClick")
	ImageView iv_add, iv_01;
	@PView
	ListView lv_add_shenpiren;
//	AddShenHeAdapter adapter;
	AddShenHeUpdateAdapter adapter;
	private ArrayList<JsonBean> list1 = new ArrayList<>();

	@PView
	ScrollView sv_caigou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qita);
		initView();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("其他");
		setRightVisibility(false);
		adapter = new AddShenHeUpdateAdapter(getLayoutInflater(),list1, QiTaActivity.this);
		lv_add_shenpiren.setAdapter(adapter);
		lv_add_shenpiren.setOnTouchListener(new OnTouchListener() {

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

		if(requestCode == 200 && resultCode == 100) {
			@SuppressWarnings("unchecked")
			ArrayList<ChildItem> childs = (ArrayList<ChildItem>) data
					.getSerializableExtra("childs");
			HashMap<String, Object> hm = AppStore.user.getInfor().get(0);
			childs.add(
					0,
					new ChildItem(hm.get("staff_name") + "", hm
							.get("staff_head") + "", hm.get("staff_mobile")
							+ "", hm.get("id") + "", 0));
			//去掉自己
			childs.remove(0);
			if (childs != null && childs.size() > 0) {
				adapter.addList(childs);
			}

			super.onActivityResult(requestCode, resultCode, data);
		}
	};

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_01:
//			adapter.reduce(((AddShenHe) view.getTag()));// 删除审批人；
			break;
		case R.id.iv_add:
			Intent intent = new Intent(this, AddChengYuanActivity.class);
			startActivityForResult(intent,200);
			overridePendingTransition(R.anim.aty_zoomin,R.anim.aty_zoomout);
//			startActivityForResult(intent, 101);// 请求码；

			break;
		case R.id.tv_shijian:
			// 点击了选择日期按钮
			DateTimePickDialogUtil shijian = new DateTimePickDialogUtil(this,
					"");
			shijian.dateTimePicKDialog(tv_shijian, new DialogCallBack() {
				@Override
				public void callBack() {
				}
			});

			break;
		case R.id.btn_submit: // 提交

			RequestParams params = new RequestParams();

			params.put("describe", et_miaoshu.getText().toString().trim());
			StringBuffer sb_id = new StringBuffer();
			List<ChildItem> shenPi = adapter.getList();
			for (int i = 0; i < shenPi.size(); i++) {
				sb_id.append(shenPi.get(i).getId() + ",");
			}
			params.put("approval", sb_id.deleteCharAt(sb_id.length() - 1)
					.toString());
			params.put("to_time", tv_shijian.getText().toString().trim());
			String url = UrlTools.url + UrlTools.OTHER_OTHER_ADD;
			L.e("审批-其他url" + url + " params" + params);
			AsyncHttpServiceHelper.post(url, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							super.onSuccess(arg0, arg1, arg2);
							final JsonBean json = JsonUtils
									.getMessage(new String(arg2));
							if ("200".equals(json.getCode())) {
								runOnUiThread(new Runnable() {
									public void run() {
										T.showShort(QiTaActivity.this,
												json.getMsg());
										finish();
										overridePendingTransition(
												R.anim.aty_zoomclosein,
												R.anim.aty_zoomcloseout);
									}
								});

							} else {
								T.showShort(QiTaActivity.this, json.getMsg());
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							super.onFailure(arg0, arg1, arg2, arg3);

						}
					});

			break;

		}
	}
}