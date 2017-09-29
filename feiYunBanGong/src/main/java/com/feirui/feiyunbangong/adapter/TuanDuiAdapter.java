package com.feirui.feiyunbangong.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.TuanDui;

public class TuanDuiAdapter extends MyBaseAdapter<TuanDui> {

	public TuanDuiAdapter(LayoutInflater inflater) {
		super(inflater);
	}

	public TuanDuiAdapter() {
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		ViewHolder holder = null;
		if (v == null) {
			v = mInflater.inflate(R.layout.lv_item_tuandui, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.tv_number = (TextView) v.findViewById(R.id.tv_number);
			holder.tv_message = (TextView) v.findViewById(R.id.tv_message);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		TuanDui td = (TuanDui) getItem(position);
		if (td.getName() != null) {
			if (td.getName().length() > 12) {
				StringBuffer sb = new StringBuffer(td.getName());
				String s = sb.substring(0, 9);
				holder.tv_name.setText("团队：" + s + "...");
			} else {
				holder.tv_name.setText("团队：" + td.getName());
			}
		}
		if (td.getTid() != null) {
			holder.tv_number.setText("团队号：" + td.getTid());
		}

		if (td.isHave()) {
			holder.tv_message.setVisibility(View.VISIBLE);
			if(td.getNotice_number()>99){
				holder.tv_message.setText(99+"+");
			}else{
				holder.tv_message.setText(td.getNotice_number()+"");
			}
		} else {
			holder.tv_message.setVisibility(View.INVISIBLE);
		}
		

		return v;
	}

	class ViewHolder {
		TextView tv_name, tv_number;
		TextView tv_message;// 提醒圆点；
	}

}
