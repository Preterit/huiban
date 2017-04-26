package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.feirui.feiyunbangong.dialog.SelectZTDialog;
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

/**
 * 审批-付款
 * 
 * @author admina
 *
 */
public class FuKuanActivity extends BaseActivity implements OnClickListener {
	@PView(click = "onClick")
	Button btn_submit;// 提交
	@PView(click = "onClick")
	TextView tv_riqi, tv_fangshi;// 付款日期，付款方式
	@PView
	EditText et_miaoshu, et_jine, et_duixiang, et_kaihuhang, et_zhanghao;// 付款描述，付款金额，付款对象，开户行，银行账户
	// 添加审批人
	@PView(click = "onClick")
	ImageView iv_add, iv_01;
	private ArrayList<JsonBean> list1 = new ArrayList<>();
	@PView
	ListView lv_add_shenpiren;
//	AddShenHeAdapter adapter;
	AddShenHeUpdateAdapter adapter;
	@PView
	ScrollView sv_caigou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fukuan);
		initView();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("付款");
		setRightVisibility(false);
//		adapter = new AddShenHeAdapter(getLayoutInflater(), FuKuanActivity.this);

		adapter = new AddShenHeUpdateAdapter(getLayoutInflater(),list1,FuKuanActivity.this);
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
		if (requestCode == 200 && resultCode == 100) {
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
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_fangshi:// 付款方式
			ArrayList<String> list = new ArrayList<>();
			list.add("支付宝");
			list.add("微信支付");
			list.add("快捷支付");
			SelectZTDialog dialog = new SelectZTDialog(this, "请选择支付方式", list,
					new SelectZTDialog.MyDailogCallback() {
						@Override
						public void onOK(String s) {
							tv_fangshi.setText(s);
						}

						@Override
						public void onCancel() {
						}
					});
			dialog.show();
			break;
		case R.id.iv_01:
//			adapter.reduce(((AddShenHe) view.getTag()));// 删除审批人；
			break;
		case R.id.iv_add:
//			Intent intent = new Intent(this, ShenPiRenActivity.class);
//			startActivityForResult(intent, 101);// 请求码；

			Intent intent = new Intent(this,AddChengYuanActivity.class);
			startActivityForResult(intent,200);
			overridePendingTransition(R.anim.aty_zoomin,R.anim.aty_zoomout);

			break;
		case R.id.tv_riqi:
			// 点击了选择日期按钮
			DateTimePickDialogUtil kaishishijian = new DateTimePickDialogUtil(
					this, "");
			kaishishijian.dateTimePicKDialog(tv_riqi, new DialogCallBack() {
				@Override
				public void callBack() {
				}
			});
		case R.id.btn_submit: // 提交

			RequestParams params = new RequestParams();

			params.put("payment_describe", et_miaoshu.getText().toString()
					.trim());
			params.put("payment_money", et_jine.getText().toString().trim());
			params.put("payment_type", tv_fangshi.getText().toString().trim());
			params.put("payment_for", et_duixiang.getText().toString().trim());
			params.put("payment_time", tv_riqi.getText().toString().trim());
			params.put("opening_bank", et_kaihuhang.getText().toString().trim());
			params.put("account", et_zhanghao.getText().toString().trim());

			//从适配器中取出审批人集合
			List<ChildItem> shenPi = adapter.getList();
			StringBuffer sb_id = new StringBuffer();
			// 循环拼接添加成员id,每个id后加逗号
			for (int i = 0; i < shenPi.size(); i++) {
				sb_id.append(shenPi.get(i).getId());
				sb_id.append(",");
				Log.d("adapterTag","适配器上的数据"+sb_id);
			}

			params.put("approvers", sb_id.deleteCharAt(sb_id.length() - 1)
					.toString());

			String url = UrlTools.url + UrlTools.BUY_ADD_BUY;
			L.e("审批-付款url" + url + " params" + params);
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
										T.showShort(FuKuanActivity.this,
												json.getMsg());
										finish();
										overridePendingTransition(
												R.anim.aty_zoomclosein,
												R.anim.aty_zoomcloseout);
									}
								});

							} else {
								T.showShort(FuKuanActivity.this, json.getMsg());
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