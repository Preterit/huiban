package com.feirui.feiyunbangong.fragment;

import org.apache.http.Header;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.FragmentProject1Adapter;
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
 * 项目--已完成
 * 
 * @author feirui1
 *
 */
public class FragmentProject2 extends BaseFragment {
	View view;
	@PView
	ListView lv_xiangmu;
	private FragmentProject1Adapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			view = setContentView(inflater, R.layout.fragment_project1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		initDate();
		super.onStart();
	}

	private void initDate() {
		RequestParams params = new RequestParams();

		String url = UrlTools.url + UrlTools.PROJECT_PROJECT_END;
		L.e("项目-已完成url" + url + " params" + params);
		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						super.onSuccess(arg0, arg1, arg2);
						final JsonBean json = JsonUtils.getMessage(new String(
								arg2));
						if ("200".equals(json.getCode())) {
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									BitmapUtils bitmapUtils = new BitmapUtils(
											getActivity());
									adapter = new FragmentProject1Adapter(
											getActivity(), json, bitmapUtils);
									lv_xiangmu.setAdapter(adapter);
									adapter.notifyDataSetChanged();
								}
							});

						} else {
							T.showShort(getActivity(), json.getMsg());
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
