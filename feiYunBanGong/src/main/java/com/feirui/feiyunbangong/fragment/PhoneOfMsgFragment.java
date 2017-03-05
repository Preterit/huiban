package com.feirui.feiyunbangong.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.DetailLianXiRenActivity;
import com.feirui.feiyunbangong.adapter.LianXiRenAdapter;
import com.feirui.feiyunbangong.entity.LianXiRen;
import com.feirui.feiyunbangong.utils.MyInterface.OnGetUnRegistPhone;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class PhoneOfMsgFragment extends Fragment {

	private LianXiRenAdapter adapter;
	private ListView lv_laixiren;
	private ArrayList<LianXiRen> lxrs = new ArrayList<>();// 已注册的联系人；
	private View v;
	private ArrayList<String> strGroups = new ArrayList<>();

	public PhoneOfMsgFragment(DetailLianXiRenActivity activity) {
		activity.setInterface(null, new OnGetUnRegistPhone() {
			@Override
			public void get(ArrayList<LianXiRen> lxrs02) {
				lxrs = lxrs02;
				adapter.addList(lxrs02);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_phone_of_add, container, false);

		initView();

		return v;
	}

	private void initView() {
		lv_laixiren = (ListView) v.findViewById(R.id.lv_lianxiren);
		adapter = new LianXiRenAdapter(getActivity(), getActivity()
				.getLayoutInflater(), 2, strGroups);
		lv_laixiren.setAdapter(adapter);
	}

}
