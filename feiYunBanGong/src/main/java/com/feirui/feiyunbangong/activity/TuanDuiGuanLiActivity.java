package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.GuanLiChengYuanAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog.AlertCallBack;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 团队管理：
 * 
 * @author feirui1
 *
 */
public class TuanDuiGuanLiActivity extends BaseActivity implements
		OnClickListener {

	private TuanDui td;
	private GuanLiChengYuanAdapter adapter;
	private List<TuanDuiChengYuan> tdcys;
	private ListView lv_chengyuan;
	private int pos;// 点击的item位置；
	private Button bt_dissolve_team;// 解散团队按钮；

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tuan_dui_guan_li);
		initView();
		setListener();
		setListView();
		initData();
	}

	private void setListView() {
		lv_chengyuan.setAdapter(adapter);
	}

	private void initData() {

		String url = UrlTools.url + UrlTools.CHENGYUAN_WU_GUANLIYUAN;
		RequestParams params = new RequestParams();
		params.put("team_id", td.getId());
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						JsonBean bean = JsonUtils.getMessage(new String(arg2));
						if ("200".equals(bean.getCode())) {
							Message msg = handler.obtainMessage(0);
							msg.obj = bean;
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage(1);
							msg.obj = bean;
							handler.sendMessage(msg);
						}
						super.onSuccess(arg0, arg1, arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						super.onFailure(arg0, arg1, arg2, arg3);
					}

				});
	}

	private void setListener() {
		bt_dissolve_team.setOnClickListener(this);
		lv_chengyuan.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pos = position;
				parent.getChildAt(position).setBackgroundColor(Color.WHITE);
			}
		});
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("团队管理");
		setRightVisibility(false);
		Intent intent = getIntent();
		td = (TuanDui) intent.getSerializableExtra("td");
		adapter = new GuanLiChengYuanAdapter(getLayoutInflater(), this);
		tdcys = new ArrayList<>();
		lv_chengyuan = (ListView) findViewById(R.id.lv_chengyuan);
		bt_dissolve_team = (Button) findViewById(R.id.bt_dissolve_team);
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.iv_set_manager:

			MyAleartDialog dialog = new MyAleartDialog("移交团长", "确定移交团长吗？",
					this, new AlertCallBack() {
						@Override
						public void onOK() {
							setManager(v);
						}

						@Override
						public void onCancel() {
						}
					});

			dialog.show();

			break;
		case R.id.bt_dissolve_team:

			MyAleartDialog dissolve_dialog = new MyAleartDialog("解散团队",
					"确定解散团队吗？", this, new AlertCallBack() {
						@Override
						public void onOK() {
							dissolve();
						}

						@Override
						public void onCancel() {
						}
					});
			dissolve_dialog.show();

			break;
		default:
			MyAleartDialog delete_dialog = new MyAleartDialog("删除团队成员",
					"确定删除团队该成员吗？", this, new AlertCallBack() {
						@Override
						public void onOK() {
							delete(v);
						}

						@Override
						public void onCancel() {
						}
					});
			delete_dialog.show();
			break;
		}
	}

	// 解散团队：
	private void dissolve() {
		String url = UrlTools.url + UrlTools.DISSOLVE_TEAM;
		RequestParams params = new RequestParams();
		params.put("team_id", td.getId());

		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						T.showShort(TuanDuiGuanLiActivity.this, "团队已解散！");
						TuanDuiGuanLiActivity.this.finish();
						for (int i = 0; i < AppStore.acts.size(); i++) {
							AppStore.acts.get(i).finish();
						}
					}

					@Override
					public void failure(String msg) {
						T.showShort(TuanDuiGuanLiActivity.this, msg);
					}

					@Override
					public void finish() {

					}
				});

	}

	// 移交管理员：
	private void setManager(final View v) {
		TuanDuiChengYuan tdcy = (TuanDuiChengYuan) v.getTag();
		String url = UrlTools.url + UrlTools.SET_TEEM_MANAGER;
		RequestParams params = new RequestParams();

		params.put("team_id", td.getId());
		params.put("staff_id", tdcy.getStaff_id());
		params.put("oldstaff_id", td.getGuanli_id());

		Log.e("TAG", params.toString());

		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						Intent intent = new Intent(TuanDuiGuanLiActivity.this,
								TuanDuiJiaActivity.class);
						setResult(100, intent);
						finish();
						Toast.makeText(TuanDuiGuanLiActivity.this, "移交管理员成功", 0)
								.show();
					}

					@Override
					public void failure(String msg) {
						T.showShort(TuanDuiGuanLiActivity.this, msg);
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub

					}
				});

	}

	private void delete(View v) {

		final TuanDuiChengYuan tdcy = (TuanDuiChengYuan) v.getTag();

		String url = UrlTools.url + UrlTools.DELETE_TUANDUI_CHENGYUAN;
		RequestParams params = new RequestParams();
		params.put("id", tdcy.getId());

		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

						JsonBean bean = JsonUtils.getMessage(new String(arg2));
						if ("200".equals(bean.getCode())) {
							Message msg = handler.obtainMessage(3);
							msg.obj = tdcy;
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage(4);
							msg.obj = bean;
							handler.sendMessage(msg);
						}
						;
						super.onSuccess(arg0, arg1, arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Message msg = handler.obtainMessage(5);
						handler.sendMessage(msg);
						super.onFailure(arg0, arg1, arg2, arg3);
					}
				});

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				Log.e("TAG", "成功！！！！！！！！！！！！！");
				JsonBean bean = (JsonBean) msg.obj;
				ArrayList<HashMap<String, Object>> infor = bean.getInfor();
				for (int i = 0; i < infor.size(); i++) {
					HashMap<String, Object> hm = infor.get(i);
					TuanDuiChengYuan tdcy = new TuanDuiChengYuan(
							String.valueOf(hm.get("id")), String.valueOf(hm
									.get("staff_name")), String.valueOf(hm
									.get("staff_head")));
					tdcy.setStaff_id(hm.get("staff_id") + "");
					tdcys.add(tdcy);
				}
				if (tdcys != null && tdcys.size() != 0) {
					adapter.add(tdcys);
				}
				break;
			case 1:
				JsonBean bean02 = (JsonBean) msg.obj;
				Toast.makeText(TuanDuiGuanLiActivity.this, bean02.getMsg(), 0)
						.show();
				break;
			case 2:

				break;
			case 3:
				// 删除成功！
				TuanDuiChengYuan item = (TuanDuiChengYuan) msg.obj;
				adapter.reduce(item);
				break;
			case 4:
				// 删除失败：
				Toast.makeText(TuanDuiGuanLiActivity.this,
						((JsonBean) msg.obj).getMsg(), 0).show();
				break;
			case 5:
				// 删除失败：
				Toast.makeText(TuanDuiGuanLiActivity.this, "删除失败！", 0).show();
				break;
			}

		};
	};

}
