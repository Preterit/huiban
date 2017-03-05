package com.feirui.feiyunbangong.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.AddShenHe;

public class AddShenHeAdapter extends MyBaseAdapter<AddShenHe> {

	private OnClickListener listener;

	public AddShenHeAdapter(LayoutInflater inflater, OnClickListener listener) {
		super(inflater);
		this.listener = listener;
	}

	public AddShenHeAdapter() {
	}

	public void add(AddShenHe ash) {
		for(int i=0;i<list.size();i++){
			if(list.get(i).getId()==ash.getId()){
				return;
			}
		}
		
		list.add(ash);
		notifyDataSetChanged();
	}

	public void reduce(AddShenHe ash) {
		if(list.contains(ash)){
			list.remove(ash);
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder holder = null;
		if (v == null) {
			v = mInflater.inflate(R.layout.ll_add_shenpiren, null);
			holder = new ViewHolder();
			holder.tv01 = (TextView) v.findViewById(R.id.tv_01);
			holder.iv01 = (ImageView) v.findViewById(R.id.iv_01);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		AddShenHe ash = list.get(position);

		holder.tv01.setText(ash.getName1());
		holder.iv01.setTag(ash);
		holder.iv01.setOnClickListener(listener);
		return v;
	}

	class ViewHolder {
		TextView tv01;
		ImageView iv01;
	}

}
