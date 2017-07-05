package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 操作记录adapter
 * 
 * @author feirui1
 *
 */
public class CaoZuoJiLuAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, Object>> data;
	private OnChakanClickListener mOnChakanClickListener;

	public interface OnChakanClickListener {
		void onChakanClick(HashMap<String, Object> data, int position);
	}

	public OnChakanClickListener getOnChakanClickListener() {
		return mOnChakanClickListener;
	}

	public void setOnChakanClickListener(OnChakanClickListener onChakanClickListener) {
		mOnChakanClickListener = onChakanClickListener;
	}

	public CaoZuoJiLuAdapter(Context context, ArrayList<HashMap<String, Object>> map) {
		this.context = context;
		this.data = map;
	}

	@Override
	public int getCount() {

		return data.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		final ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.ll_item_ti,
					null);

			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}

//		holder.tv_shenpi.setText((String) data.get(position)
//				.get("appname"));
		holder.tv_leixing.setText((String) data.get(position)
				.get("approval_type"));

		holder.tv_pass.setText((String) data.get(position)
				.get("status"));
//		holder.tv_shenpiren.setText("审批");

		holder.bt_detail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnChakanClickListener != null) {
					mOnChakanClickListener.onChakanClick(data.get(position), position);
				}
			}
		});

		return convertView;
	}

	public void add(ArrayList<HashMap<String, Object>> map) {
		data.addAll(map);
		notifyDataSetChanged();
	}

	public void addAll(ArrayList<HashMap<String, Object>> map) {
		data.clear();
		data.addAll(map);
		notifyDataSetChanged();
	}


	class ViewHolder {
		TextView tv_name,tv_pass, tv_shenpi, tv_leixing,tv_shenpiren;// 姓名，部门，类型
		Button bt_detail;

		public ViewHolder(View convertView) {

			tv_leixing = (TextView) convertView.findViewById(R.id.tv_leixing);
			tv_pass = (TextView) convertView.findViewById(R.id.tv_pass);
//			tv_shenpi = (TextView) convertView.findViewById(R.id.tv_shenpi);
//			tv_shenpiren = (TextView) convertView.findViewById(R.id.tv_shenpiren);
			bt_detail = (Button) convertView.findViewById(R.id.bt_detail);
		}
	}
}