package com.feirui.feiyunbangong.adapter;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.SetGuanLiYuan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SetGuanLiYuanAdapter extends MyBaseAdapter<SetGuanLiYuan> {

	public SetGuanLiYuanAdapter(LayoutInflater inflater) {
		super(inflater);
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder holder = null;
		if (v == null) {
			holder = new ViewHolder();
			v = mInflater.inflate(R.layout.ll_fuzeren_lv, null);
			holder.iv_head = (ImageView) v.findViewById(R.id.iv_head);
			holder.tv_guanliyuan = (TextView) v
					.findViewById(R.id.tv_isguanliyuan);
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.bt = (Button) v.findViewById(R.id.bt_sheding);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		SetGuanLiYuan sgly = (SetGuanLiYuan) getItem(position);
		holder.tv_name.setText(sgly.getName());
		if (sgly.isGuanLiYuan()) {
			holder.tv_guanliyuan.setVisibility(View.VISIBLE);
			holder.bt.setText("取消");
		} else {
			holder.tv_guanliyuan.setVisibility(View.INVISIBLE);
			holder.bt.setText("设定");
		}
		return v;
	}

	class ViewHolder {
		ImageView iv_head;
		TextView tv_name, tv_guanliyuan;
		Button bt;
	}

}
