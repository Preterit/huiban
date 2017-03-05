package com.feirui.feiyunbangong.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.R.id;
import com.feirui.feiyunbangong.R.layout;
import com.feirui.feiyunbangong.adapter.NoticeIsReadAdapter;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.myinterface.AllInterface.NoticeCallBack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class UnReadFragment extends BaseFragment implements NoticeCallBack {
	
	private List<TuanDuiChengYuan> tdcys = new ArrayList<>();
	private ListView lv_unread_notice;
	private NoticeIsReadAdapter adapter;
	private View v;

	public UnReadFragment() {
	}

	public NoticeCallBack getCallBack() {
		return this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.fragment_un_read, null);

		initView(inflater);

		return v;
	}

	private void initView(LayoutInflater inflater) {

		lv_unread_notice = (ListView) v.findViewById(R.id.lv_unread_notice);
		adapter = new NoticeIsReadAdapter(inflater);
		lv_unread_notice.setAdapter(adapter);

	}

	@Override
	public void callBack(ArrayList<HashMap<String, Object>> infor) {
		for (int i = 0; i < infor.size(); i++) {

			TuanDuiChengYuan tdcy = new TuanDuiChengYuan();
			HashMap<String, Object> hm = infor.get(i);
			tdcy.setName(hm.get("staff_name") + "");
			tdcy.setT_remark(hm.get("t_remark") + "");
			tdcy.setHead(hm.get("staff_head") + "");
			tdcys.add(tdcy);

		}
		adapter.add(tdcys);
	}

}
