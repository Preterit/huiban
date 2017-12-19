package com.feirui.feiyunbangong.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.TuanDuiGuanLiActivity;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 管理团队成员适配器：
 * 
 * @author feirui1
 *
 */
public class GuanLiChengYuanAdapter extends MyBaseAdapter<TuanDuiChengYuan> {

	private OnClickListener listener;

	public GuanLiChengYuanAdapter(LayoutInflater inflater) {
		super(inflater);
	}

	public GuanLiChengYuanAdapter(LayoutInflater inflater,
			OnClickListener listener) {
		super(inflater);
		this.listener = listener;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View v, ViewGroup parent) {

		ViewHolder holder = null;
		if (v == null) {
			holder = new ViewHolder();
			v = mInflater.inflate(R.layout.lv_item_chuangjiantuandui, null);
			holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
			holder.iv_head = (TextImageView) v.findViewById(R.id.iv_head);
			holder.iv_jianhao = (ImageView) v.findViewById(R.id.iv_jianhao);
			holder.iv_manager = (ImageView) v.findViewById(R.id.iv_set_manager);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		final TuanDuiChengYuan item = (TuanDuiChengYuan) getItem(position);

		holder.tv_name.setText(item.getName());
		holder.iv_manager.setVisibility(View.VISIBLE);

		// 图片上加字：
		String name = Utils.getPicTitle(item.getName());
		if (item.getHead() == null || "img/1_1.png".equals(item.getHead())
				|| item.getHead().equals("")) {
			holder.iv_head.setText(name);
		} else {
			ImageLoader.getInstance().displayImage(item.getHead(),
					holder.iv_head);
		}

		holder.iv_jianhao.setTag(item);

		holder.iv_jianhao.setOnClickListener(listener);

		holder.iv_manager.setTag(item);
		holder.iv_manager.setOnClickListener(listener);

		return v;
	}

	public void reduce(TuanDuiChengYuan item) {
		if (list.contains(item)) {
			list.remove(item);
			notifyDataSetChanged();
		}
	}

	class ViewHolder {
		TextView tv_name;
		TextImageView iv_head;
		ImageView iv_jianhao, iv_manager;// 删除和移交管理员
	}

	public List<TuanDuiChengYuan> getList() {
		return list;
	}

	public void addList(List<TuanDuiChengYuan> items) {
		for (int i = 0; i < items.size(); i++) {

			boolean isHave = false;
			for (int j = 0; j < list.size(); j++) {
				if (list.get(j).getCId().equals(items.get(i).getCId())) {
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
}
