package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.R.drawable;
import com.feirui.feiyunbangong.R.id;
import com.feirui.feiyunbangong.R.layout;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.fragment.ReadFragment;
import com.feirui.feiyunbangong.fragment.UnReadFragment;
import com.feirui.feiyunbangong.myinterface.AllInterface.NoticeCallBack;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NoticeReadUnReadActivity extends BaseActivity implements
		OnClickListener {

	@PView(click = "onClick")
	private Button bt_read;
	@PView(click = "onClick")
	private Button bt_unread;

	private Fragment read_fragment, unread_fragment;

	private NoticeCallBack callback_read, callback_unread;
	private FragmentManager fm;
	private int id;// 公告id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_read_un_read);

		initView();

	}

	@Override
	protected void onResume() {
		requestData();
		super.onResume();
	}

	// 获取数据：
	private void requestData() {
		id = getIntent().getIntExtra("id", 0);
		String url = UrlTools.url + UrlTools.TEAM_MESSAGE_READ_UNREAD;

		RequestParams params = new RequestParams();
		params.put("id", id + "");
		Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
				new HttpCallBack() {

					@Override
					public void success(JsonBean bean) {
						ArrayList<HashMap<String, Object>> infor = bean
								.getInfor();

						ArrayList<HashMap<String, Object>> infor_read = new ArrayList<>();
						ArrayList<HashMap<String, Object>> infor_unread = new ArrayList<>();

						for (int i = 0; i < infor.size(); i++) {
							if ((int) infor.get(i).get("state") == 2) {
								infor_read.add(infor.get(i));
							} else {
								infor_unread.add(infor.get(i));
							}
						}

						bt_read.setText("已读" + infor_read.size());
						bt_unread.setText("未读" + infor_unread.size());

						callback_read.callBack(infor_read);
						callback_unread.callBack(infor_unread);

					}

					@Override
					public void failure(String msg) {
						T.showShort(NoticeReadUnReadActivity.this, msg);
					}

					@Override
					public void finish() {
						// TODO Auto-generated method stub
						
					}
				});
	}

	private void initView() {

		initTitle();
		setCenterString("公告已读未读");
		setLeftDrawable(R.drawable.arrows_left);
		setRightVisibility(false);

		read_fragment = new ReadFragment();
		unread_fragment = new UnReadFragment();

		callback_read = ((ReadFragment) read_fragment).getCallBack();
		callback_unread = ((UnReadFragment) unread_fragment).getCallBack();

		fm = getSupportFragmentManager();

		fm.beginTransaction().add(R.id.ll_container, read_fragment)
				.add(R.id.ll_container, unread_fragment).show(read_fragment)
				.hide(unread_fragment).commit();

		bt_read.setEnabled(false);
		bt_unread.setEnabled(true);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_read:
			fm.beginTransaction().show(read_fragment).hide(unread_fragment)
					.commit();
			bt_read.setEnabled(false);
			bt_unread.setEnabled(true);
			break;
		case R.id.bt_unread:
			fm.beginTransaction().show(unread_fragment).hide(read_fragment)
					.commit();
			bt_read.setEnabled(true);
			bt_unread.setEnabled(false);
			break;
		}
	}

}
