package com.feirui.feiyunbangong.activity;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.FenZuGuanLiAdapter;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog.AlertCallBack1;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 分组管理
 * 
 * @author admina
 *
 */
public class FenZuGuanLiActivity extends BaseActivity {
	@PView(itemClick = "onItemClick")
	ListView lv_fenzu;
	private Button bt_add;
	private FenZuGuanLiAdapter adapter;
	private JsonBean json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fen_zu_guan_li);
		initView();
		initDate();
	}

	private void initView() {
		initTitle();
		setLeftDrawable(R.drawable.arrows_left);
		setCenterString("分组管理");
		setRightVisibility(false);
		bt_add = (Button) findViewById(R.id.bt_add);
		bt_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				XiuGaiDialog tianjia = new XiuGaiDialog("添加分组", "添加", "输入新增组名",
						FenZuGuanLiActivity.this, new AlertCallBack1() {

							@Override
							public void onOK(final String name) {
								initDate();
							}

							@Override
							public void onCancel() {

							}
						});
				tianjia.show();
			}
		});
	}

	public void initDate() {
		RequestParams params = new RequestParams();
		String url = UrlTools.url + UrlTools.GET_FRIEND_GROUP;
		L.e("获取好友分组url" + url + " params" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						json = JsonUtils.getMessage(new String(arg2));
						if ("200".equals(json.getCode())) {
							runOnUiThread(new Runnable() {
								public void run() {
									BitmapUtils bitmapUtils = new BitmapUtils(
											FenZuGuanLiActivity.this);
									adapter = new FenZuGuanLiAdapter(
											FenZuGuanLiActivity.this, json,
											bitmapUtils,
											FenZuGuanLiActivity.this);
									lv_fenzu.setAdapter(adapter);
									adapter.notifyDataSetChanged();
								}
							});

						} else {
							T.showShort(FenZuGuanLiActivity.this, json.getMsg());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						super.onFailure(arg0, arg1, arg2, arg3);

					}
				});

	}
}
