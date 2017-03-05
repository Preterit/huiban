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
 * 项目-进行中adapter
 * 
 * @author admina
 *
 */
public class FragmentProject1Adapter extends BaseAdapter {
	private Context context;
	private JsonBean json;
	public BitmapUtils bitmapUtils;

	public FragmentProject1Adapter(Context context, JsonBean json,
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
			view = LayoutInflater.from(context).inflate(
					R.layout.lv_item_xiangmu, null);
			holder.iv_head = (ImageView) view.findViewById(R.id.iv_head);
			holder.tv_xiangmuming = (TextView) view
					.findViewById(R.id.tv_xiangmuming);
			holder.tv_fuzeren = (TextView) view.findViewById(R.id.tv_fuzeren);
			holder.tv_renshu = (TextView) view.findViewById(R.id.tv_renshu);
			holder.tv_kaishishijian = (TextView) view
					.findViewById(R.id.tv_kaishishijian);
			holder.tv_jieshushijian = (TextView) view
					.findViewById(R.id.tv_jieshushijian);
			holder.tv_jindu = (TextView) view.findViewById(R.id.tv_jindu);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_xiangmuming.setText((String) json.getInfor().get(position)
				.get("project_name"));
		holder.tv_fuzeren.setText((String) json.getInfor().get(position)
				.get("principal_person"));
		holder.tv_renshu.setText(""
				+ json.getInfor().get(position).get("project_member"));
		holder.tv_kaishishijian.setText((String) json.getInfor().get(position)
				.get("begin_time"));
		holder.tv_jieshushijian.setText((String) json.getInfor().get(position)
				.get("end_time"));
		holder.tv_jindu.setText(""
				+ json.getInfor().get(position).get("average_days"));
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
					(String) json.getInfor().get(position).get("staff_img"),
					config);
		}
		return view;
	}

	class ViewHolder {
		// 头像，项目名，负责人,人数，开始时间，结束时间,进度
		ImageView iv_head;
		TextView tv_xiangmuming, tv_fuzeren, tv_renshu, tv_kaishishijian,
				tv_jieshushijian, tv_jindu;
	}

}
