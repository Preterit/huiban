package com.feirui.feiyunbangong.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.DetailChengYuanActivity;
import com.feirui.feiyunbangong.entity.GongGao;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;

public class GongGaoAdapter extends MyBaseAdapter<GongGao> {

	private Activity activity;
	private ArrayList<TuanDuiChengYuan> tdcys = new ArrayList<>();

	public GongGaoAdapter(LayoutInflater inflater) {
		this.mInflater = inflater;
	}

	public GongGaoAdapter(LayoutInflater inflater, Activity activity,
			ArrayList<TuanDuiChengYuan> tdcys) {
		this.mInflater = inflater;
		this.tdcys = tdcys;
		this.activity = activity;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {

		ViewHolder holder = null;
		if (v == null) {
			v = mInflater.inflate(R.layout.ll_item_tuandui_gonggao, null);
			holder = new ViewHolder();
			holder.iv_head = (TextImageView) v.findViewById(R.id.tiv_head);
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.tv_content = (TextView) v.findViewById(R.id.tv_content);
			holder.tv_time = (TextView) v.findViewById(R.id.tv_time);
			v.setTag(holder);

			if (activity != null) {

				holder.iv_head.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {

						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							int pos = (int) view.getTag();
							GongGao gg = (GongGao) getItem(pos);
							String phone = gg.getPhone();
							TuanDuiChengYuan tdcy = null;
							for (int i = 0; i < tdcys.size(); i++) {
								if (phone.equals(tdcys.get(i).getPhone())) {
									tdcy = tdcys.get(i);
								}
							}

							Intent intent = new Intent(activity,
									DetailChengYuanActivity.class);
							intent.putExtra("tdcy", tdcy);
							activity.startActivity(intent);
						}
						return true;
					}
				});
			}

		} else {
			holder = (ViewHolder) v.getTag();
		}
		GongGao gg = (GongGao) getItem(position);
		Utils.setImage(gg.getName(), holder.iv_head, gg.getHead());
		holder.tv_name.setText(gg.getName() + "");
		holder.tv_content.setText(gg.getContent() + "");
		holder.tv_time.setText(gg.getTime() + "");

		holder.iv_head.setTag(position);

		return v;
	}

	class ViewHolder {
		TextImageView iv_head;
		TextView tv_name, tv_time, tv_content;
	}

}
