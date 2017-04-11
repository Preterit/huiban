package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.lidroid.xutils.BitmapUtils;

/**
 * 提现记录adapter
 * 
 * @author admina
 *
 */
public class TiXian2Adapter extends BaseAdapter {
	private Context context;
	private JsonBean json;
	public BitmapUtils bitmapUtils;

	public TiXian2Adapter(Context context, JsonBean json,
			BitmapUtils bitmapUtils) {
		this.context = context;
		this.json = json;
		this.bitmapUtils = bitmapUtils;
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
					R.layout.item_tixianjilu, null);
			holder.tv_num = (TextView) view.findViewById(R.id.tv_num);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_form_time);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_num.setText(json.getInfor().get(position).get("money") + "元");
		holder.tv_time.setText((String) json.getInfor().get(position)
				.get("time"));
		return view;
	}

	class ViewHolder {
		// 钱数，时间
		TextView tv_num, tv_time;
	}

}
