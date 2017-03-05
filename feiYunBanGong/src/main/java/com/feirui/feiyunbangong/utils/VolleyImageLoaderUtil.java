package com.feirui.feiyunbangong.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;

public class VolleyImageLoaderUtil {

	private static ImageLoader mImageLoader;

	// 单例模式：
	public static ImageLoader getInstance(Context context) {
		RequestQueue queue = Volley.newRequestQueue(context);
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(queue, new BitmapCache());
		}
		return mImageLoader;
	}

	/**
	 * 使用LruCache来缓存图片
	 */
	public static class BitmapCache implements ImageCache {

		private LruCache<String, Bitmap> mCache;

		public BitmapCache() {
			// 获取应用程序最大可用内存
			int maxMemory = (int) Runtime.getRuntime().maxMemory();
			int cacheSize = maxMemory / 8;
			mCache = new LruCache<String, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getRowBytes() * bitmap.getHeight();
				}
			};
		}

		@Override
		public Bitmap getBitmap(String url) {
			return mCache.get(url);
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			mCache.put(url, bitmap);
		}

	}

}
