package com.feirui.feiyunbangong.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.DetailLianXiRenActivity;
import com.feirui.feiyunbangong.adapter.LianXiRenAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.LianXiRen;
import com.feirui.feiyunbangong.utils.MyInterface.OnGetRegistPhone;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class PhoneOfAddFragment extends Fragment {

	private LianXiRenAdapter adapter;
	private ListView lv_laixiren;
	private ArrayList<LianXiRen> lxrs = new ArrayList<>();// 已注册的联系人；
	private View v;
	private ArrayList<String> strGroups = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();

	public PhoneOfAddFragment(DetailLianXiRenActivity activity) {
		activity.setInterface(new OnGetRegistPhone() {
			@Override
			public void get(ArrayList<LianXiRen> lxrs01) {
				lxrs = lxrs01;
				adapter.addList(lxrs01);
			}
		}, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_phone_of_add, container, false);

		requestGroup();// 获取分组信息：

		initView();

		return v;
	}

	private void requestGroup() {
		String url01 = UrlTools.url + UrlTools.GET_FRIEND_GROUP;
		Utils.doPost(null, getActivity(), url01, null, new HttpCallBack() {
			@Override
			public void success(JsonBean bean) {
				ArrayList<HashMap<String, Object>> infor = bean.getInfor();
				groups.removeAll(groups);
				strGroups.removeAll(strGroups);

				for (int i = 0; i < infor.size(); i++) {
					int id = (int) infor.get(i).get("id");
					String name = infor.get(i).get("name") + "";
					int default_num = (int) infor.get(i).get("default");
					int count = (int) infor.get(i).get("count");
					Group group = new Group(id, name, default_num, count);
					groups.add(group);
					strGroups.add(group.getName());
				}
				strGroups.add("+");
			}

			@Override
			public void failure(String msg) {
				T.showShort(getActivity(), msg);
			}

			@Override
			public void finish() {

			}
		});
	}

	private void initView() {
		lv_laixiren = (ListView) v.findViewById(R.id.lv_lianxiren);
		adapter = new LianXiRenAdapter(getActivity(), getActivity()
				.getLayoutInflater(), 1, strGroups);
		lv_laixiren.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
