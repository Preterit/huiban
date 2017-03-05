package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;

/**
 * 邀请adapter
 * 
 * @author admina
 *
 */
public class YaoQingAdapter extends BaseAdapter {
	private Context context;
	private JsonBean json;

	public YaoQingAdapter(Context context, JsonBean json) {
		this.context = context;
		this.json = json;
	}

	@Override
	public int getCount() {
		return json.getInfor().size();
	}

	@Override
	public Object getItem(int position) {
		return json.getInfor().size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.lv_item_yaoqing, null);
			holder.tv_1 = (TextView) view.findViewById(R.id.tv_1);
			holder.tv_2 = (TextView) view.findViewById(R.id.tv_2);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_1.setText("手机号   "+json.getInfor().get(position)
				.get("staff_mobile"));
		holder.tv_2
				.setText((String) json.getInfor().get(position).get("price"));
		return view;
	}

	class ViewHolder {
		TextView tv_1, tv_2;
	}

}
