package com.feirui.feiyunbangong.fragment;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.TiXian2Adapter;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 提现记录
 * 
 * @author feirui1
 *
 */
public class TiXian2Fragment extends BaseFragment {

	private View view;
	@PView
	TextView textView;
	@PView
	ListView lv_tixianjilu;
	private TiXian2Adapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = setContentView(inflater, R.layout.fragment_tixian2);
		initData();
		return view;

	}

	private void initData() {

		RequestParams params = new RequestParams();

		String url = UrlTools.url + UrlTools.APPLICATION_WITHDRAWCASHLIST;
		L.e("提现记录" + url + " params" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						final JsonBean json = JsonUtils.getMessage(new String(
								arg2));
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								if ("200".equals(json.getCode())) {

									textView.setVisibility(View.GONE);
									lv_tixianjilu.setVisibility(View.VISIBLE);
									BitmapUtils bitmapUtils = new BitmapUtils(
											getActivity());
									adapter = new TiXian2Adapter(getActivity(),
											json, bitmapUtils);
									lv_tixianjilu.setAdapter(adapter);
									adapter.notifyDataSetChanged();

								} else {
									textView.setVisibility(View.VISIBLE);
									lv_tixianjilu.setVisibility(View.GONE);
								}
							}
						});
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						super.onFailure(arg0, arg1, arg2, arg3);

					}
				});

	}

}
