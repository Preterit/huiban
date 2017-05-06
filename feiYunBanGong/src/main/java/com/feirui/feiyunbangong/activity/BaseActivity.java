package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.view.EventListener;
import com.feirui.feiyunbangong.view.PView;

import java.lang.reflect.Field;

public class BaseActivity extends FragmentActivity {
	// 左部 中间 右部
	public TextView centerTv;
	public ImageView leftIv, rightIv;
	public LinearLayout leftll, rightll;
	public RelativeLayout top;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		// 内侧版本：初始化testin;发布版本将这个去掉；
//		TestinUtils.initTestin(this);
//		 配置testin:发布版本将这个去掉；
//		TestinUtils.configTestin(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Happlication.getInstance().addActivity(this);
		// 禁止横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		initInjectedView(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initInjectedView(this);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		initInjectedView(this);
	}

	/**
	 * 初始化头部
	 */
	public void initTitle() {
		top = (RelativeLayout) findViewById(R.id.top);
		leftll = (LinearLayout) findViewById(R.id.leftll);
		rightll = (LinearLayout) findViewById(R.id.rightll);
		leftIv = (ImageView) findViewById(R.id.leftIv);
		centerTv = (TextView) findViewById(R.id.centerTv);
		rightIv = (ImageView) findViewById(R.id.rightIv);
		if (leftIv != null) {
			leftll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();

					Log.d("tag","_____---------___________");
					overridePendingTransition(R.anim.aty_zoomclosein,
							R.anim.aty_zoomcloseout);
				}
			});
		}

	}

	/**
	 * 设置头部是否可见
	 */
	public void setTopVisibility(boolean visibilty) {
		if (visibilty) {
			top.setVisibility(View.VISIBLE);

		} else {
			top.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置头部 的中间部分是否可见 默认是可见的
	 */
	public void setCenterVisibility(boolean visibilty) {
		if (visibilty) {
			centerTv.setVisibility(View.VISIBLE);
		} else {
			centerTv.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置头部 的左部分是否可见 默认是可见的
	 */
	public void setLeftVisibility(boolean visibilty) {
		if (visibilty) {
			leftIv.setVisibility(View.VISIBLE);
		} else {
			leftIv.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置头部 的右部分是否可见 默认是可见的
	 */
	public void setRightVisibility(boolean visibilty) {
		if (visibilty) {
			rightIv.setVisibility(View.VISIBLE);
		} else {
			rightIv.setVisibility(View.GONE);
		}

	}

	/**
	 * 设置头部的中间的文字
	 */
	public void setCenterString(String title) {
		if (title == null) {
			centerTv.setText("");
		}
		centerTv.setText(title);

	}

	/**
	 * 设置头部 的左部分的图片
	 */
	public void setLeftDrawable(int drawable) {
		leftIv.setImageResource(drawable);

	}

	/**
	 * 设置头部 的右部分的图片
	 */
	public void setRightDrawable(int drawable) {
		rightIv.setImageResource(drawable);

	}


	private void initInjectedView(Activity activity) {
		initInjectedView(activity, activity.getWindow().getDecorView());

	}

	private void initInjectedView(Activity activity, View sourceView) {
		Field[] fields = activity.getClass().getDeclaredFields(); // 获取字段
		// 返回 Field 对象的一个数组，这些对象反映此 Class
		// 对象所表示的类或接口所声明的所有字段，包括公共、保护、默认（包）访问和私有字段，但不包括继承的字段。
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				try {
					field.setAccessible(true); // 设为可访问

					if (field.get(activity) != null) // 返回指定对象上此 Field 表示的字段的值
						continue;

					PView d3View = field.getAnnotation(PView.class);
					// 如果存在该元素的指定类型的注释，则返回这些注释，否则返回 null。
					if (d3View != null) {

						int viewId = d3View.id();
						if (viewId == 0)
							viewId = getResources().getIdentifier(
									field.getName(), "id", getPackageName());
						if (viewId == 0)
							Log.e("D3Activity", "field " + field.getName()
									+ "not found");
						// 关键,注解初始化，相当于 backBtn = (TextView)
						// findViewById(R.id.back_btn);
						field.set(activity, sourceView.findViewById(viewId));
						// 事件
						setListener(activity, field, d3View.click(),
								Method.Click);
						setListener(activity, field, d3View.longClick(),
								Method.LongClick);
						setListener(activity, field, d3View.itemClick(),
								Method.ItemClick);
						setListener(activity, field, d3View.itemLongClick(),
								Method.itemLongClick);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void setListener(Object activity, Field field, String methodName,
			Method method) throws Exception {
		if (methodName == null || methodName.trim().length() == 0)
			return;

		Object obj = field.get(activity);

		switch (method) {
		case Click:
			if (obj instanceof View) {
				((View) obj).setOnClickListener(new EventListener(activity)
						.click(methodName));
			}
			break;
		case ItemClick:
			if (obj instanceof AbsListView) {
				((AbsListView) obj).setOnItemClickListener(new EventListener(
						activity).itemClick(methodName));
			}
			break;
		case LongClick:
			if (obj instanceof View) {
				((View) obj).setOnLongClickListener(new EventListener(activity)
						.longClick(methodName));
			}
			break;
		case itemLongClick:
			if (obj instanceof AbsListView) {
				((AbsListView) obj)
						.setOnItemLongClickListener(new EventListener(activity)
								.itemLongClick(methodName));
			}
			break;

		}
	}

	public enum Method {
		Click, LongClick, ItemClick, itemLongClick
	}

}
