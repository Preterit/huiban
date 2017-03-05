package com.feirui.feiyunbangong.adapter;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.FenZuGuanLiActivity;
import com.feirui.feiyunbangong.dialog.MyAleartDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog.AlertCallBack;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog.AlertCallBack1;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 分组管理adapter
 * 
 * @author admina
 *
 */
public class FenZuGuanLiAdapter extends BaseAdapter {
	private Context context;
	private JsonBean json;
	public BitmapUtils bitmapUtils;
	public FenZuGuanLiActivity activity;

	public FenZuGuanLiAdapter(Context context, JsonBean json,
			BitmapUtils bitmapUtils) {
		this.context = context;
		this.json = json;
		this.bitmapUtils = bitmapUtils;
	}

	public FenZuGuanLiAdapter(Context context, JsonBean json,
			BitmapUtils bitmapUtils, FenZuGuanLiActivity activity) {
		super();
		this.context = context;
		this.json = json;
		this.bitmapUtils = bitmapUtils;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return json.getInfor().size();
	}

	@Override
	public Object getItem(int position) {
		return json.getInfor().size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.lv_item_fenzuguanli, null);
			holder.tv_zuming = (TextView) view.findViewById(R.id.tv_zuming);
			holder.iv_shanchu = (ImageView) view.findViewById(R.id.iv_shanchu);
			holder.iv_bianji = (ImageView) view.findViewById(R.id.iv_bianji);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_zuming.setText((String) json.getInfor().get(position)
				.get("name"));
		if ("1".equals("" + json.getInfor().get(position).get("default"))) {
			holder.iv_shanchu.setVisibility(View.GONE);
		}
		holder.iv_shanchu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyAleartDialog dia = new MyAleartDialog("删除该组", "确定要删除该组吗？",
						context, new AlertCallBack() {
							@Override
							public void onOK() {
								RequestParams params = new RequestParams();

								params.put(
										"id",
										""
												+ json.getInfor().get(position)
														.get("id"));
								String url = UrlTools.url
										+ UrlTools.USER_DELETEFRIENDGROUP;
								L.e("删除组url" + url + " params" + params);
								AsyncHttpServiceHelper.post(url, params,
										new AsyncHttpResponseHandler() {
											@Override
											public void onSuccess(int arg0,
													Header[] arg1, byte[] arg2) {
												super.onSuccess(arg0, arg1,
														arg2);
												final JsonBean json = JsonUtils
														.getMessage(new String(
																arg2));
												if ("200".equals(json.getCode())) {
													activity.initDate();
												} else {
													((Activity) context)
															.runOnUiThread(new Runnable() {
																public void run() {
																	T.showShort(
																			context,
																			json.getMsg());
																}
															});
												}
											}

											@Override
											public void onFailure(int arg0,
													Header[] arg1, byte[] arg2,
													Throwable arg3) {
												super.onFailure(arg0, arg1,
														arg2, arg3);

											}
										});

							}

							@Override
							public void onCancel() {
							}

						});
				dia.show();
			}
		});
		holder.iv_bianji.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				XiuGaiDialog xiugai = new XiuGaiDialog("修改分组名", ""
						+ json.getInfor().get(position).get("id"),
						(String) json.getInfor().get(position).get("name"),
						context, new AlertCallBack1() {

							@Override
							public void onOK(final String name) {
								((Activity) context)
										.runOnUiThread(new Runnable() {
											public void run() {
												holder.tv_zuming.setText(name);
											}
										});

							}

							@Override
							public void onCancel() {

							}
						});
				xiugai.show();
			}
		});
		return view;
	}

	class ViewHolder {
		TextView tv_zuming;
		ImageView iv_shanchu, iv_bianji;
	}

}
