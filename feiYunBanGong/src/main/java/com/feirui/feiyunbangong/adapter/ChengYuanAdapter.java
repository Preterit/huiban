package com.feirui.feiyunbangong.adapter;

import java.util.List;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChengYuanAdapter extends MyBaseAdapter<TuanDuiChengYuan> {

	public ChengYuanAdapter(LayoutInflater inflater) {
		super(inflater);
	}

	public ChengYuanAdapter() {
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {

		ViewHolder holder = null;
		if (v == null) {
			v = mInflater.inflate(R.layout.lv_item_chengyuan, null);
			holder = new ViewHolder();
			holder.tv_guanliyuan = (TextView) v
					.findViewById(R.id.tv_guanliyuan);
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.tv_biaoqian = (TextView) v.findViewById(R.id.tv_biaoqian);
			holder.iv_head = (TextImageView) v.findViewById(R.id.iv_head);
			holder.tv_tag = (TextView) v.findViewById(R.id.tv_biaoqian);
			holder.iv_new_member = (ImageView) v
					.findViewById(R.id.iv_new_member);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		TuanDuiChengYuan tdcy = (TuanDuiChengYuan) getItem(position);

		if ("团长".equals(tdcy.getType()) || "副团长".equals(tdcy.getType())) {
			holder.tv_guanliyuan.setVisibility(View.VISIBLE);
			holder.tv_guanliyuan.setText(tdcy.getType());
		} else {
			holder.tv_guanliyuan.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(tdcy.getT_remark())) {
			holder.tv_name.setText(tdcy.getT_remark());
		} else if (!TextUtils.isEmpty(tdcy.getRemark())) {
			holder.tv_name.setText(tdcy.getRemark());
		} else {
			holder.tv_name.setText(tdcy.getName());
		}

		if (tdcy.getState() == 1) {
			holder.iv_new_member.setVisibility(View.VISIBLE);
		} else {
			holder.iv_new_member.setVisibility(View.INVISIBLE);
		}

		Log.e("TAG", tdcy.getTag() + "");

		if (!TextUtils.isEmpty(tdcy.getTag())) {
			holder.tv_tag.setVisibility(View.INVISIBLE);
			holder.tv_tag.setText(tdcy.getTag());
		} else {
			holder.tv_tag.setVisibility(View.INVISIBLE);
		}

		if (tdcy.getHead() == null || "img/1_1.png".equals(tdcy.getHead())
				|| "".equals(tdcy.getHead())) {
			String name = Utils.getPicTitle(tdcy.getName());
			holder.iv_head.setText(name);
		} else {
			ImageLoader.getInstance().displayImage(tdcy.getHead(),
					holder.iv_head);
		}

		return v;
	}

	public List<TuanDuiChengYuan> getList() {
		return list;
	}

	public void addList(List<TuanDuiChengYuan> items) {
		for (int i = 0; i < items.size(); i++) {

			boolean isHave = false;
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getId().equals(items.get(i).getId())) {
					isHave = true;
					break;
				}
			}
			if (!isHave) {
				list.add(items.get(i));
			}
		}
		notifyDataSetChanged();
	}

	class ViewHolder {
		TextView tv_name, tv_guanliyuan, tv_biaoqian, tv_tag;
		TextImageView iv_head;
		ImageView iv_new_member;
	}

}
