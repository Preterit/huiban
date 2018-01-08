package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
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
import com.feirui.feiyunbangong.entity.ContactMember;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.LianXiRen;
import com.feirui.feiyunbangong.fragment.PhoneOfAddFragment;
import com.feirui.feiyunbangong.fragment.PhoneOfMsgFragment;
import com.feirui.feiyunbangong.utils.MyInterface;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

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
	private String name="";
	private String phone="" ;
	private ArrayList<ContactMember> ls;// 存放联系人姓名和手机号的数组；

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

//				str = LianXiRenUtil.readConnect(DetailLianXiRenActivity.this);
				ls = getContact(DetailLianXiRenActivity.this);
				Log.e("通讯录", "联系人列表"+ls.toString());
				Log.e("通讯录", "第一个"+ls.get(0).getContact_name());
				for(int i=0;i<ls.size();i++){
					name+=ls.get(i).getContact_name()+",";
					phone+=ls.get(i).getContact_phone()+",";
				}
				name = name.substring(0,name.length());//去掉最后多出来的逗号。
				phone = phone.substring(0,phone.length());//去掉最后多出来的逗号。

				Log.e("通讯录","姓名"+ name + "电话" + phone);
//				str[0]=name;
//				str[1]=phone;

//				Log.e("通讯录联系人", str[0] + "姓名，电话" + str[1]);

				RequestParams params = new RequestParams();
				if(ls==null){
					dialog.dismiss();
					return;

				}
				params.put("phone", phone);//-------------------------------------
				String url = UrlTools.url + UrlTools.SHOUJILIANXIREN;
				Log.e("通讯录","联系人"+ phone);
				Utils.doPost(null, DetailLianXiRenActivity.this, url, params,
						new HttpCallBack() {
							@Override
							public void success(JsonBean bean) {
								Log.e("通讯录", "返回值"+bean.getInfor().toString());
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
				Toast.makeText(DetailLianXiRenActivity.this, "暂无可联系人好友", Toast.LENGTH_SHORT).show();
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
			String[] split = name.split(",");// 姓名
			String[] split2 = phone.split(",");// 联系电话
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
	Cursor c;
	public ArrayList<ContactMember> getContact(Activity context) {
		ArrayList<ContactMember> listMembers = new ArrayList<ContactMember>();
		Cursor cursor = null;
		try {

			Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			// 这里是获取联系人表的电话里的信息  包括：名字，名字拼音，联系人id,电话号码；
			// 然后在根据"sort-key"排序
			cursor = context.getContentResolver().query(uri,
					new String[]{"display_name", "sort_key", "contact_id",
							"data1"}, null, null, "sort_key");
			if (cursor.moveToFirst()) {
				do {
					ContactMember contact = new ContactMember();
					String contact_phone = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					String name = cursor.getString(0);
					String sortKey = getSortKey(cursor.getString(1));
					int contact_id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
					contact.contact_name = name;
					contact.sortKey = sortKey;
					contact.contact_phone = contact_phone;
					contact.setContact_id(contact_id);
					Log.e("通讯录", "name: "+name );
					Log.e("通讯录", "sortKey: "+sortKey );
					Log.e("通讯录", "contact_phone: "+contact_phone );
					if (name != null)
						listMembers.add(contact);
				} while (cursor.moveToNext());
				c = cursor;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context = null;
		}
		return listMembers;
	}

	/**
	 * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
	 *
	 * @param sortKeyString 数据库中读取出的sort key
	 * @return 英文字母或者#
	 */
	private static String getSortKey(String sortKeyString) {
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}
}
