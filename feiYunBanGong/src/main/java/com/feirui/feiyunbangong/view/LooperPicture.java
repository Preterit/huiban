package com.feirui.feiyunbangong.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.PicturePagerAdapter;
import com.feirui.feiyunbangong.entity.JsonB;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.HttpRequestParamsUtil;
import com.feirui.feiyunbangong.utils.ImageUtil;
import com.feirui.feiyunbangong.utils.JsonUtil;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

public class LooperPicture {
	// viewpager底部的小圆点的集合
	private static ArrayList<View> viewList = new ArrayList<View>();
	// 轮播图片控件集合
	private static ArrayList<ImageView> imgList = new ArrayList<ImageView>();
	// 动态添加轮播图片底部圆点的布局属性
	private static LinearLayout.LayoutParams params;
	// 轮播的计时器
	private static Timer mTimer;
	private static TimerTask mTimerTask;
	private JsonBean jb;
	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				viewList.clear();
				// imageloader初始化，同时添加缓存权限
				// ImageLoaderConfiguration configuration =
				// ImageLoaderConfiguration.createDefault(LooperPicture.activity);
				ImageLoader imageLoader = ImageLoader.getInstance();

				List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) msg.obj;
				for (int i = 0; i < list.size(); i++) {
					final ImageView img = new ImageView(activity);
					img.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					img.setScaleType(ScaleType.FIT_XY);
					imageLoader.displayImage((String) list.get(i).get("pic"),
							img);
					img.setTag(list.get(i));
					img.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {

						}
					});
					imgList.add(img);

					View view = new View(activity);
					view.setBackgroundResource(R.drawable.unselected);
					fragment_vp_ll.addView(view, params);
					viewList.add(view);
				}

				PicturePagerAdapter adapter = new PicturePagerAdapter(imgList);
				vp.setAdapter(adapter);
				vp.setCurrentItem(0);
				viewList.get(0).setBackgroundResource(R.drawable.selected);

				// 回调homepager界面的获取服务类别的网络方法
				// if(AppStore.hotService==null){
				// homePagerFragment.initNetData();
				// }
				break;

			case 2:
				if (AppStore.mpList != null) {
					if (vp.getCurrentItem() == AppStore.mpList.size() - 1) {
						vp.setCurrentItem(0);
					} else {
						vp.setCurrentItem(vp.getCurrentItem() + 1);
					}
				}
				break;

			case 3:
				// 写死的：
				viewList.clear();
				for (int i = 0; i < 3; i++) {
					final ImageView img = new ImageView(activity);
					img.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					img.setScaleType(ScaleType.FIT_XY);

					img.setTag(i);

					if (i == 0) {
						// 图片压缩！！
						Bitmap bitmap = ImageUtil
								.decodeSampledBitmapFromResource(
										activity.getResources(),
										R.drawable.ban1, 180, 300);
						img.setImageBitmap(bitmap);
					} else if (i == 1) {
						// 图片压缩！！
						Bitmap bitmap = ImageUtil
								.decodeSampledBitmapFromResource(
										activity.getResources(),
										R.drawable.ban2, 180, 300);
						img.setImageBitmap(bitmap);
					} else {
						// 图片压缩！！
						Bitmap bitmap = ImageUtil
								.decodeSampledBitmapFromResource(
										activity.getResources(),
										R.drawable.ban3, 180, 300);
						img.setImageBitmap(bitmap);
					}
					img.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {

						}
					});

					imgList.add(img);
					View view = new View(activity);
					view.setBackgroundResource(R.drawable.unselected);
					fragment_vp_ll.addView(view, params);
					viewList.add(view);
				}

				PicturePagerAdapter a = new PicturePagerAdapter(imgList);
				vp.setAdapter(a);
				vp.setCurrentItem(0);
				viewList.get(0).setBackgroundResource(R.drawable.selected);

				startTimerTask();

				// 回调homepager界面的获取服务类别的网络方法
				// if(AppStore.hotService==null){
				// homePagerFragment.initNetData();
				// }
				break;

			case 4:
				if (vp.getCurrentItem() == 3 - 1) {
					vp.setCurrentItem(0);
				} else {
					vp.setCurrentItem(vp.getCurrentItem() + 1);
				}
				break;
			}
		}

		private Bitmap getHttpBitmap(String string) {
			// TODO Auto-generated method stub
			return null;
		};
	};
	private static ViewPager vp;
	private static LinearLayout fragment_vp_ll;
	private static FragmentActivity activity;

	/**
	 * 添加轮播功能
	 * 
	 * @param fragmentActivity
	 * @param fragment_vp_ll
	 * @param vp
	 * @param homePagerFragment
	 */
	public static void addLooperPicture(ViewPager vp,
			LinearLayout fragment_vp_ll, FragmentActivity activity) {
		LooperPicture.vp = vp;
		LooperPicture.fragment_vp_ll = fragment_vp_ll;
		LooperPicture.activity = activity;

		initData();
		addListener();
		handler.sendEmptyMessage(3);
		/*
		 * if (AppStore.mpList == null) { // getNetData();//不用从网络获取； } else {
		 * handler.sendEmptyMessage(3); }
		 */
	}

	/**
	 * 添加监听事件
	 */
	private static void addListener() {
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (arg0 == 0) {
					for (int i = 0; i < viewList.size(); i++) {
						if (i == vp.getCurrentItem()) {
							viewList.get(i).setBackgroundResource(
									R.drawable.selected);
						} else {
							viewList.get(i).setBackgroundResource(
									R.drawable.unselected);
						}
					}
				}
			}
		});
	}

	/**
	 * 初始化圆点属性
	 */
	private static void initData() {
		params = new LinearLayout.LayoutParams(28, 28);
		params.leftMargin = 10;
		params.rightMargin = 10;
	}

	/**
	 * 联网获取轮播图片路径
	 */
	private static void getNetData() {
		String url = UrlTools.wheelpic;
		RequestParams params = HttpRequestParamsUtil
				.getParams(UrlTools.wheelpic1);
		L.e("url=" + url + "---" + "params=" + params);
		// HttpUtils.doGetAsyn(url, new HttpUtils.CallBack() {
		//
		// @Override
		// public void onRequestComplete(String result) {
		// // TODO Auto-generated method stub
		// // L.e(result);
		// // L.e("轮播图结果" + result);
		// Type type = new TypeToken<ArrayList<MainPicture>>() {
		// }.getType();
		// AppStore.mpList = AsyncHttpServiceHelper.gson.fromJson(result,
		// type);
		// handler.sendEmptyMessage(1);
		// }
		// });

		AsyncHttpServiceHelper.post(url, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						super.onSuccess(arg0, arg1, arg2);
						L.e("轮播图结果" + new String(arg2));
						final JsonB json = JsonUtil
								.getMessage(new String(arg2));
						AppStore.mpList = json.getData();
						Message message = new Message();
						message.what = 1;
						message.obj = json.getData();
						handler.sendMessage(message);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1, arg2, arg3);
						L.e(new String(arg2));
					}
				});
	}

	/**
	 * 添加一个计时器 每隔x秒发一个handler 轮训图片 启动计时器
	 */
	public static void startTimerTask() {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					handler.sendEmptyMessage(4);
				}
			};
		}
		// 执行计时器
		if (mTimer != null && mTimerTask != null) {
			mTimer.schedule(mTimerTask, 3000, 3000);
		}
	}

	/**
	 * 停止计时器
	 */
	public static void stopTimerTask() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}

}
