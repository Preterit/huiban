package com.feirui.feiyunbangong.utils;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.feirui.feiyunbangong.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;

public class LoadImage {

	public static void loadImage(Activity activity,ImageView iv,String path,int size){
		
		BitmapUtils bitmapUtils = new BitmapUtils(activity);
		// 1,实例化BitmapDisplayConfig
		BitmapDisplayConfig config = new BitmapDisplayConfig();
		// 2,设置bitmapConfig
		config.setBitmapConfig(Bitmap.Config.RGB_565);
		config.setAutoRotation(true);
		// // 3,设置加载失败时显示的图片
		config.setLoadFailedDrawable(activity.getResources().getDrawable(
		R.drawable.fragment_head));
		// 4,设置正在加载时显示的图片
		config.setLoadingDrawable(activity.getResources().getDrawable(R.drawable.fragment_head));
		// 5,设置图片的最大宽高
		config.setBitmapMaxSize(new BitmapSize(size, size));
		// 6,设置disk缓存开启,图片会缓存到sd卡,即DiskLruCache
		bitmapUtils.configDiskCacheEnabled(true);
		// 7.设置Memory缓存开启,即LruCache
		bitmapUtils.configMemoryCacheEnabled(false);
		bitmapUtils.configThreadPoolSize(10);
		// 8.根据上面设置的条件展示图片
		bitmapUtils.display(iv, path, config);
	}
}
