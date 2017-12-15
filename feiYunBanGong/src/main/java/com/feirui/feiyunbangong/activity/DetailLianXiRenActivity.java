package com.feirui.feiyunbangong.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.LianXiRen;
import com.feirui.feiyunbangong.fragment.PhoneOfAddFragment;
import com.feirui.feiyunbangong.fragment.PhoneOfMsgFragment;
import com.feirui.feiyunbangong.utils.LianXiRenUtil;
import com.feirui.feiyunbangong.utils.MyInterface;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;

/**
 * 查看手机联系人信息：
 */

public class DetailLianXiRenActivity extends BaseActivity {

	private ImageView iv_back;
	private RadioGroup rg_phone;
	private FragmentManager fm;
	private ArrayList<LianXiRen> lxrs = new ArrayList<>();// 总的联系人集合；
	private ArrayList<LianXiRen> lxrs01 = new ArrayList<>();// 已经注册过得联系人集合；
	private ArrayList<LianXiRen> lxrs02 = new ArrayList<>();// 未注册过的联系人集合；
	private String[] str;// 存放联系人姓名和手机号的数组；
	private MyInterface.OnGetRegistPhone regist;
	private MyInterface.OnGetUnRegistPhone unregist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_lian_xi_ren);
		initUI();
		setListner();
		// 请求数据：
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				request();
			}
		}, 100);

	}

	// 接口初始化：
	public void setInterface(MyInterface.OnGetRegistPhone regist,
			MyInterface.OnGetUnRegistPhone unregist) {
		if (regist != null) {
			this.regist = regist;
		}
		if (unregist != null) {
			this.unregist = unregist;
		}
	}

	private void setListner() {
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initUI() {
		iv_back = (ImageView) findViewById(R.id.iv_back);

		rg_phone = (RadioGroup) findViewById(R.id.rg_phone);

		fm = getSupportFragmentManager();

		final PhoneOfAddFragment add = new PhoneOfAddFragment(this);
		final PhoneOfMsgFragment msg = new PhoneOfMsgFragment(this);

		rg_phone.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.rb_add:
					group.getChildAt(0).setEnabled(false);
					group.getChildAt(1).setEnabled(true);
					fm.beginTransaction().show(add).hide(msg).commit();
					break;
				case R.id.rb_msg:
					group.getChildAt(0).setEnabled(true);
					group.getChildAt(1).setEnabled(false);
					fm.beginTransaction().show(msg).hide(add).commit();
					break;
				}

			}
		});

		rg_phone.check(R.id.rb_msg);

		fm.beginTransaction().add(R.id.ll_container, add).add(R.id.ll_container, msg).show(msg).commit();
	}

	LoadingDialog dialog;

	// 获取手机联系人：
	private void request() {

		dialog = LoadingDialog.getInstance(this);
		dialog.show();

		new Thread(new Runnable() {

			@Override
			public void run() {

				str = LianXiRenUtil.readConnect(DetailLianXiRenActivity.this);

//				Log.e("通讯录联系人", str[0] + "姓名，电话" + str[1]);

				RequestParams params = new RequestParams();
				params.put("phone", str[1]);
				String url = UrlTools.url + UrlTools.SHOUJILIANXIREN;

				Utils.doPost(null, DetailLianXiRenActivity.this, url, params,
						new HttpCallBack() {
							@Override
							public void success(JsonBean bean) {
								addData(bean);
								dialog.dismiss();
							}

							@Override
							public void failure(String msg) {
								T.showShort(DetailLianXiRenActivity.this, msg);
								dialog.dismiss();
							}

							@Override
							public void finish() {
								dialog.dismiss();
							}
						});
			}

		}).start();
	}

	private void addData(JsonBean bean) {
		ArrayList<HashMap<String, Object>> info = bean.getInfor();
		Log.e("TAG", info.size() + "info.size");
		if (info != null) {

			lxrs01.removeAll(lxrs01);
			lxrs02.removeAll(lxrs02);

			if (info.size() == 0) {
				Toast.makeText(DetailLianXiRenActivity.this, "暂无可联系人好友", 0)
						.show();
				return;
			}

			for (int i = 0; i < info.size(); i++) {
				HashMap<String, Object> map = info.get(i);
				LianXiRen lxr = new LianXiRen((String) map.get("staff_name"),
						(String) map.get("phone"), (String) map.get("type"),
						(String) map.get("staff_head"));
				lxrs01.add(lxr);
			}

			Log.e("TAG", "发出广播");

			regist.get(lxrs01);// 接口回调：

			// 删选出未注册的好友发广播给短信邀请：
			String[] split = str[0].split(",");// 姓名
			String[] split2 = str[1].split(",");// 联系电话
			for (int i = 0; i < split2.length; i++) {
				boolean hasRegist = false;
				for (int j = 0; j < lxrs01.size(); j++) {
					if (lxrs01.get(j).getPhone().equals(split2[i])) {
						hasRegist = true;
						break;
					}
				}
				if (!hasRegist) {
					LianXiRen lxr = new LianXiRen();
					lxr.setName(split[i]);
					lxr.setPhone(split2[i]);
					lxrs02.add(lxr);
				}
			}

			unregist.get(lxrs02);// 接口回调：
		}
	}
}
