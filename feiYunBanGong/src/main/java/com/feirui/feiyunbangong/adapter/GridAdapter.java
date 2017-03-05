package com.feirui.feiyunbangong.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;

public class GridAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<JsonBean> list;

	public GridAdapter(Context context, ArrayList<JsonBean> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null)
			return 0;
		else
			return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.lv_footer_tuandui_chengyuan, null);
			holder.tv_TextView = (Button) view.findViewById(R.id.bt_add);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_TextView.setText(list.get(position).getMsg());
		return view;
	}

	class ViewHolder {
		Button tv_TextView;
	}

}
