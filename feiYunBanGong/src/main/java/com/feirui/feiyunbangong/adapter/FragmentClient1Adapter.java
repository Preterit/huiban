package com.feirui.feiyunbangong.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.BaifangKehuActivity;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.lidroid.xutils.BitmapUtils;

/**
 * 客户管理-进行中adapter
 * 
 * @author admina
 *
 */
public class FragmentClient1Adapter extends BaseAdapter {
	private Context context;
	private JsonBean json;
	public BitmapUtils bitmapUtils;

	public FragmentClient1Adapter(Context context, JsonBean json,
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
	public View getView(final int position, View view, ViewGroup arg2) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.lv_item_kehuguanli_jinxingzhong, null);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_shijian = (TextView) view.findViewById(R.id.tv_shijian);
			holder.tv_dizhi = (TextView) view.findViewById(R.id.tv_dizhi);
			holder.ll_chakan = (LinearLayout) view.findViewById(R.id.ll_chakan);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_name.setText((String) json.getInfor().get(position)
				.get("customer_name"));
		holder.tv_shijian.setText((String) json.getInfor().get(position)
				.get("visit_time"));
		holder.tv_dizhi.setText((String) json.getInfor().get(position)
				.get("visit_places"));
		holder.ll_chakan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, BaifangKehuActivity.class);
				intent.putExtra("id",
						"" + json.getInfor().get(position).get("id"));

				context.startActivity(intent);
				((Activity) context).overridePendingTransition(
						R.anim.aty_zoomin, R.anim.aty_zoomout);
			}
		});
		return view;
	}

	class ViewHolder {
		// 名字，时间，地址
		TextView tv_name, tv_shijian, tv_dizhi;
		LinearLayout ll_chakan;// 查看
	}

}
