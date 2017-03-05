package com.feirui.feiyunbangong.adapter;

/*
 * 项目名:     JulyProject
 * 包名:       com.zsy.words.adapter
 * 文件名:     MyAdapter
 * 创建者:     阿钟
 * 创建时间:   2016/11/17 19:22
 * 描述:       ListView列表适配器
 */

import java.util.List;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.TuanDui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private List<TuanDui> list;
	private LayoutInflater inflater;

	public MyAdapter(Context context, List<TuanDui> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_item, null);
			holder.tv_word = (TextView) convertView.findViewById(R.id.tv_word);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_number = (TextView) convertView
					.findViewById(R.id.tv_number);
			holder.tv_message = (TextView) convertView
					.findViewById(R.id.tv_message);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String word = list.get(position).getHeadword();

		// 如果是#的话则分到其他组里；
		holder.tv_word.setText(word);

		holder.tv_name.setText(list.get(position).getName());
		holder.tv_number.setText(list.get(position).getId() + "");
		TuanDui td = (TuanDui) getItem(position);

		if (td.isHave()) {
			holder.tv_message.setVisibility(View.VISIBLE);
			if (td.getNotice_number() > 99) {
				holder.tv_message.setText(99 + "+");
			} else {
				holder.tv_message.setText(td.getNotice_number() + "");
			}
		} else {
			holder.tv_message.setVisibility(View.INVISIBLE);
		}

		// 将相同字母开头的合并在一起
		if (position == 0) {
			// 第一个是一定显示的
			holder.tv_word.setVisibility(View.VISIBLE);
		} else {
			// 后一个与前一个对比,判断首字母是否相同，相同则隐藏
			String headerWord = list.get(position - 1).getHeadword();
			if (word.equals(headerWord)) {
				holder.tv_word.setVisibility(View.GONE);
			} else {
				holder.tv_word.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	private class ViewHolder {
		private TextView tv_word;
		private TextView tv_name;
		private TextView tv_number;
		private TextView tv_message;
	}

	public void add(List<TuanDui> tds) {
		this.list.removeAll(list);
		if (tds != null && tds.size() > 0) {
			this.list.addAll(tds);
		}
		notifyDataSetChanged();
	}

}
