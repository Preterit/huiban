package com.feirui.feiyunbangong.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.CaoZuoJiLuActivity;
import com.feirui.feiyunbangong.entity.JsonBean;

/**
 * 操作记录adapter
 * 
 * @author feirui1
 *
 */
public class CaoZuoJiLuAdapter extends BaseAdapter {

	private CaoZuoJiLuActivity context;
	private JsonBean json;

	public CaoZuoJiLuAdapter(CaoZuoJiLuActivity context, JsonBean json) {
		this.context = context;
		this.json = json;
	}

	@Override
	public int getCount() {

		return json.getInfor().size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.ll_item_daishenqi,
					null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_bumen = (TextView) convertView
					.findViewById(R.id.tv_bumen);
			holder.tv_leixing = (TextView) convertView
					.findViewById(R.id.tv_leixing);
			holder.bt_detail = (Button) convertView
					.findViewById(R.id.bt_detail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.tv_name.setText((String) json.getInfor().get(position)
					.get("staff_name"));
			holder.tv_bumen.setText(""
					+ json.getInfor().get(position).get("staff_department"));
			holder.tv_leixing.setText((String) json.getInfor().get(position)
					.get("approval_type"));
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_name, tv_bumen, tv_leixing;// 姓名，部门，类型
		Button bt_detail;
	}
}
