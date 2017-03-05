package com.feirui.feiyunbangong.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.PingLunItem;

/**
 * 评论列表是适配器：
 * 
 * @author feirui1
 *
 */
public class PingLunAdapter extends MyBaseAdapter<PingLunItem> {

	public PingLunAdapter(LayoutInflater inflater, List<PingLunItem> plis) {
		super(inflater);
		super.add(plis);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder holder = null;
		if (v == null) {
			holder = new ViewHolder();
			v = mInflater.inflate(R.layout.ll_item_pinglun, null);
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.tv_text = (TextView) v.findViewById(R.id.tv_text);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		PingLunItem item = (PingLunItem) getItem(position);
		String name = item.getName();
		String text = item.getText();
		if (name != null) {
			holder.tv_name.setText(name);
		}
		if (text != null) {
			holder.tv_text.setText(text);
		}
		return v;
	}

	class ViewHolder {
		TextView tv_name, tv_text;
	}

}
