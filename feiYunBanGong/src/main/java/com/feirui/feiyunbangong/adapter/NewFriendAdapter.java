package com.feirui.feiyunbangong.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.LianXiRen;
import com.feirui.feiyunbangong.view.TextImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
/**
 * 添加新朋友adapter
 * */
public class NewFriendAdapter extends MyBaseAdapter<LianXiRen> {

	private Activity activity;
	private OnTouchListener listener;
	private ArrayList<String> strGroups = new ArrayList<>();

	public NewFriendAdapter() {
	}

	public NewFriendAdapter(OnTouchListener listener, Activity activity,
			LayoutInflater inflater) {
		super(inflater);
		this.activity = activity;
		this.listener = listener;
		strGroups.add("sdfds");
		strGroups.add("dsdfsfdfdf");
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {

		ViewHolder holder = null;
		if (v == null) {
			v = mInflater.inflate(R.layout.ll_newfriend_item, null);
			holder = new ViewHolder();
			holder.iv_head = (TextImageView) v.findViewById(R.id.iv_head);
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.tv_phone = (TextView) v.findViewById(R.id.tv_phone);
			holder.jieshou = (Button) v.findViewById(R.id.bt_jieshou);
			holder.jvjue = (Button) v.findViewById(R.id.bt_jvjue);
			holder.tv_choice = (TextView) v.findViewById(R.id.tv_choice);
			holder.tv_jieshou = (TextView) v.findViewById(R.id.tv_jishou);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		LianXiRen lianXiRen = list.get(position);
		Log.e("新朋友", "lianXiRen: "+lianXiRen.getName() );
		Log.e("新朋友", "lianXiRen: "+lianXiRen.getId() );
		Log.e("新朋友", "lianXiRen: "+lianXiRen.getHead() );

		if ("http://123.57.45.74/feiybg1/public/static/staff_head/19912/53da489597afc6f5abb2a1bae0d767ff.jpeg".equals(lianXiRen.getHead())
				||"http://123.57.45.74/feiybg1/public/static/staff_head/379/8fe02470a4eb202cf7643a86f31fa39f.jpeg".equals(lianXiRen.getHead())) {
			//holder.iv_head.setImageResource(R.drawable.fragment_head);
			holder.iv_head.setText(lianXiRen.getName());
		} else {
			ImageLoader.getInstance().displayImage(lianXiRen.getHead(),
					holder.iv_head);
		}
		holder.tv_name.setText(lianXiRen.getName());
		holder.tv_phone.setText(lianXiRen.getPhone());

		if ("已接受".equals(lianXiRen.getState())) {
			holder.tv_jieshou.setVisibility(View.VISIBLE);
			holder.jieshou.setVisibility(View.GONE);
			holder.jvjue.setVisibility(View.GONE);
		} else if ("拒绝".equals(lianXiRen.getState())) {
			holder.tv_jieshou.setText("拒绝");
			holder.tv_jieshou.setVisibility(View.VISIBLE);
			holder.jieshou.setVisibility(View.GONE);
			holder.jvjue.setVisibility(View.GONE);
		} else {
			holder.tv_jieshou.setVisibility(View.GONE);
			holder.jieshou.setVisibility(View.VISIBLE);
			holder.jvjue.setVisibility(View.VISIBLE);
		}

		holder.tv_choice.setTag(position);
		holder.tv_choice.setTag(position);
		holder.tv_choice.setOnTouchListener(listener);
		Object[] obj = new Object[] { lianXiRen.getId(), holder.tv_choice,
				lianXiRen.getPhone() };
		holder.jvjue.setOnTouchListener(listener);
		holder.jieshou.setOnTouchListener(listener);
		holder.jvjue.setTag(obj);
		holder.jieshou.setTag(obj);

		return v;
	}

	class ViewHolder {
		TextView tv_name, tv_phone, tv_jieshou, tv_choice;
		TextImageView iv_head;
		Button jieshou, jvjue;
	}

}
