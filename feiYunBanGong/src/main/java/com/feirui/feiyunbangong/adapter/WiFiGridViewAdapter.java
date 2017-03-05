package com.feirui.feiyunbangong.adapter;

import com.feirui.feiyunbangong.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WiFiGridViewAdapter extends MyBaseAdapter<String> {

	public WiFiGridViewAdapter(LayoutInflater inflater) {
		super(inflater);
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder holder = null;
		if (v == null) {
			v = mInflater.inflate(R.layout.gv_item, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		holder.tv_name.setBackgroundColor(Color.parseColor(colors[position]));
		String name = (String) getItem(position);
		holder.tv_name.setText(name);
		return v;
	}

	class ViewHolder {
		TextView tv_name;
	}

	// 表示各个信道的颜色值
	public String[] colors = new String[] { "#ff784a", "#8fc31f", "#ae5da1",
			"#00a0e9", "#FF00FF", "#f19ec2", "#cfa972" };
}
