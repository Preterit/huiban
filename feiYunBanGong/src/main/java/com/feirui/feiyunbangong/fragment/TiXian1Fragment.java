package com.feirui.feiyunbangong.fragment;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 提现说明
 * 
 * @author feirui1
 *
 */
public class TiXian1Fragment extends BaseFragment {

	private View view;
	@PView
	private TextView text6;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = setContentView(inflater, R.layout.fragment_tixian1);
		initData();

		return view;

	}

	/**
	 * 
	 */
	private void initData() {

		RequestParams params = new RequestParams();

		String url = UrlTools.url + UrlTools.APPLICATION_WITHDRAWCASH;
		L.e("提现额度" + url + " params" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						final JsonBean json = JsonUtils.getMessage(new String(
								arg2));
						if ("200".equals(json.getCode())) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									text6.setText("您的提现账号为："
											+ AppStore.yaoqingma + "，您的可用提现额度为"
											+ json.getInfor().get(0).get("num")
											+ "元");
								}
							});
						} else {
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
