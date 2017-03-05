package com.feirui.feiyunbangong.adapter;

import java.util.List;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NoticeIsReadAdapter extends MyBaseAdapter<TuanDuiChengYuan> {

	public NoticeIsReadAdapter(LayoutInflater inflater) {
		super(inflater);
	}

	public NoticeIsReadAdapter() {
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {

		ViewHolder holder = null;
		if (v == null) {
			v = mInflater.inflate(R.layout.lv_notice_isread_item, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.iv_head = (TextImageView) v.findViewById(R.id.iv_head);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		TuanDuiChengYuan tdcy = (TuanDuiChengYuan) getItem(position);

		if (!TextUtils.isEmpty(tdcy.getT_remark())
				&& !"null".equals(tdcy.getT_remark())) {
			holder.tv_name.setText(tdcy.getT_remark());
		} else if (!TextUtils.isEmpty(tdcy.getRemark())
				&& !"null".equals(tdcy.getRemark())) {
			holder.tv_name.setText(tdcy.getRemark());
		} else {
			holder.tv_name.setText(tdcy.getName());
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

	class ViewHolder {
		TextView tv_name;
		TextImageView iv_head;
	}

}
