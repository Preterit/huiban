package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;

/**
 * 打卡数据adapter
 * 
 * @author admina
 *
 */
public class ClockInDataAdapter extends BaseAdapter {
	private Context context;
	private JsonBean json;
	public BitmapUtils bitmapUtils;

	public ClockInDataAdapter(Context context, JsonBean json,
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
	public View getView(int position, View view, ViewGroup arg2) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.item_clock_in,
					null);
			holder.iv_head = (ImageView) view.findViewById(R.id.iv_head);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_morning = (TextView) view.findViewById(R.id.tv_morning);
			holder.tv_yichang = (TextView) view.findViewById(R.id.tv_yichang);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_name.setText((String) json.getInfor().get(position)
				.get("staff_name"));
		holder.tv_morning.setText((String) json.getInfor().get(position)
				.get("type"));
		if ("打卡异常".equals((String) json.getInfor().get(position)
				.get("abnormal"))) {
			holder.tv_yichang.setText((String) json.getInfor().get(position)
					.get("abnormal"));
		} else {
			holder.tv_yichang.setVisibility(View.GONE);
		}
		if (!"img/1_1.png".equals((String) json.getInfor().get(position)
				.get("pic"))) {

			// 1,实例化BitmapDisplayConfig
			BitmapDisplayConfig config = new BitmapDisplayConfig();
			// 2,设置bitmapConfig
			config.setBitmapConfig(Bitmap.Config.RGB_565);
			config.setAutoRotation(true);

			// // 3,设置加载失败时显示的图片
			config.setLoadFailedDrawable(context.getResources().getDrawable(
					R.drawable.fragment_head));
			// // 4,设置正在加载时显示的图片
			config.setLoadingDrawable(context.getResources().getDrawable(
					R.drawable.fragment_head));
			// 5,设置图片的最大宽高
			config.setBitmapMaxSize(new BitmapSize(100, 100));
			// 6,设置disk缓存开启,图片会缓存到sd卡,即DiskLruCache
			bitmapUtils.configDiskCacheEnabled(false);

			// 7,设置Memory缓存开启,即LruCache
			bitmapUtils.configMemoryCacheEnabled(false);

			bitmapUtils.configThreadPoolSize(2);
			// 8,根据上面设置的条件展示图片
			bitmapUtils.display(holder.iv_head,
					(String) json.getInfor().get(position).get("pic"), config);
		}
		return view;
	}

	class ViewHolder {
		// 头像，名字，签到签退
		ImageView iv_head;
		TextView tv_name, tv_morning, tv_yichang;
	}

}
