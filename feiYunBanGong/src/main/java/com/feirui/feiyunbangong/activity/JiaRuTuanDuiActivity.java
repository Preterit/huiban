package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ChengYuanAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.TextImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 加入团队
 * */
public class JiaRuTuanDuiActivity extends BaseActivity implements
		OnClickListener {

	private ImageView tiv_head;
	private TextView tv_name, tv_team_num, tv_num;
	private Button tv_jiaru;
	private ListView lv_yijiaru;
	private String id;// 团队id;
	private ChengYuanAdapter adapter;
	private List<TuanDuiChengYuan> tdcys = new ArrayList<>();
	private String mTuanLiaoID; //团聊的Id
	private IYWTribeService mService;
	private YWIMKit mYWIMkit;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jia_ru_tuan_dui);
		mYWIMkit = AppStore.mIMKit;
		mService = mYWIMkit.getTribeService();
		try {
			initView();
			setListener();
			setListView();
			addData();
		} catch (Exception e) {
			Log.e("TAG", e.getMessage());
		}
	}

	private void setListView() {
		Log.e("加入团队页面", "setListView: ");
		lv_yijiaru.setAdapter(adapter);
	}

	private void setListener() {
		Log.e("加入团队页面", "setListener ");
		tv_jiaru.setOnClickListener(this);
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("加入团队");
		setRightVisibility(false);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");

		Log.e("加入团队页面--initView", "id: "+id );

		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_team_num = (TextView) findViewById(R.id.tv_team_num);
		tv_team_num.setText(id);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_jiaru = (Button) findViewById(R.id.tv_jiaru);
		lv_yijiaru = (ListView) findViewById(R.id.lv_yijiaru);
		adapter = new ChengYuanAdapter(getLayoutInflater());
		tiv_head = (ImageView) findViewById(R.id.tiv_head);
		//tiv_head.setText("团队");
		getTuanLiaoId(); //获取该团聊的ID
	}

	/**
	 * 团队团聊Id
	 */
	public void getTuanLiaoId(){
		String url = UrlTools.url + UrlTools.GET_TUANLIAOID;
		RequestParams params = new RequestParams();
		params.put("team_id",id);
		AsyncHttpServiceHelper.post(url,params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				JsonBean bean = JsonUtils.getMessage(new String(arg2));
				if ("200".equals(bean.getCode())) {
					Log.e("chengyuan", "handleMessage: -----------------" + bean.getInfor().get(0).get("team_talk") );
					mTuanLiaoID = bean.getInfor().get(0).get("team_talk") + "";
				} else {
					Log.e("chengyuan", "handleMessage: -----------------" + bean.getMsg() );
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_jiaru:
			jiaru();
			finish();
			break;
		}
	}

	// 加入：
	private void jiaru() {
		String url = UrlTools.url + UrlTools.JIARU;
		RequestParams params = new RequestParams();
		params.put("team_id", id);

		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						Toast.makeText(JiaRuTuanDuiActivity.this, "成功加入团队", 0)
								.show();
						addData();
						if (!"".equals(mTuanLiaoID)){
							addTuanLiao();
						}
					}

					@Override
					public void failure(String msg) {
						Toast.makeText(JiaRuTuanDuiActivity.this, msg, 0)
								.show();
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
						
					}
				});
	}

	// 添加数据：
	private void addData() {
		Log.e("加入团队页面", "id: "+id.toString() );
		String url = UrlTools.url + UrlTools.JIARU_TUANDUI;
		RequestParams params = new RequestParams();
		params.put("id", id);
		// 二次封装：
		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {
					@Override
					public void success(JsonBean bean) {
						tdcys.removeAll(tdcys);
						ArrayList<HashMap<String, Object>> infor = bean.getInfor();
						Log.e("加入团队页面", "infor: "+infor.toString());
						HashMap<String, Object> hm = infor.get(0);
						ImageLoader.getInstance().displayImage(hm.get("team_head").toString(), tiv_head);
						if (hm.get("team_name") != null) {
							tv_name.setText(String.valueOf(hm.get("team_name")));
							//tiv_head.setText(String.valueOf(hm.get("team_name")));
						}

						JSONArray array = (JSONArray) hm.get("list");
						if (array == null || array.length() == 0) {
							return;
						}
						tv_num.setText(array.length() + "人");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = null;
							try {
								obj = (JSONObject) array.get(i);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if (obj == null) {
								return;
							}

							TuanDuiChengYuan tdcy = null;

							tdcy = new TuanDuiChengYuan();
							try {
								tdcy.setHead(obj.get("staff_head") + "");
								tdcy.setId(obj.get("staff_id") + "");
								tdcy.setName(obj.get("staff_name") + "");
								tdcy.setType(obj.get("type") + "");
							} catch (JSONException e) {
								e.printStackTrace();
							}
							tdcys.add(tdcy);
						}
						adapter.add(tdcys);
					}

					@Override
					public void failure(String msg) {
						Toast.makeText(JiaRuTuanDuiActivity.this, msg, 0)
								.show();
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
					}
				});
	}

	/**
	 * 主动加入团聊
	 */
	public void addTuanLiao(){
		mService.joinTribe(new MyCallBack() {
			@Override
			public void onSuccess(Object... objects) {

			}

			@Override
			public void onError(int i, String s) {
				T.showShort(JiaRuTuanDuiActivity.this,"加入团聊失败---" + s);
			}
		},Long.parseLong(mTuanLiaoID));
	}


	/**
	 * 请求回调
	 */
	public static abstract class MyCallBack implements IWxCallback{
		@Override
		public void onProgress(int i) {

		}
	}
}
