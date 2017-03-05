package com.feirui.feiyunbangong.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.utils.LoadImage;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShenPiRenAdapter extends MyBaseAdapter<ShenPiRen> {

	private Activity activity;

	public ShenPiRenAdapter(Activity activity, LayoutInflater inflater) {
		super(inflater);
		this.activity = activity;
	}

	public ShenPiRenAdapter() {
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		try {
			ViewHolder holder = null;
			if (v == null) {
				v = mInflater.inflate(R.layout.ll_shenpiren_item, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) v.findViewById(R.id.iv_shenpiren);
				holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
				holder.tv_bumen = (TextView) v.findViewById(R.id.tv_bumen);
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}

			// 解决异步加载图片造成listview滑动过程中的混乱；
			ShenPiRen spr = list.get(position);
			holder.tv_name.setText(spr.getName());
			
			if ("null".equals(spr.getDepartment())
					|| spr.getDepartment() == null) {
				holder.tv_bumen.setText("无");
			} else {
				holder.tv_bumen.setText(spr.getDepartment());
			}

			if ("img/1_1.png".equals(spr.getHead())) {
				holder.iv.setImageResource(R.drawable.moren_head_nv);
			} else {
				ImageLoader.getInstance()
						.displayImage(spr.getHead(), holder.iv);
			}

		} catch (Exception e) {
			Log.i("TAG", e.getMessage());
		}
		return v;
	}

	class ViewHolder {
		ImageView iv;
		TextView tv_name, tv_bumen;
	}

}
