package com.feirui.feiyunbangong.fragment;

import java.lang.reflect.Field;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.view.EventListener;
import com.feirui.feiyunbangong.view.PView;

public class BaseFragment extends Fragment {
	// 左部 中间 右部
	public TextView centerTv;
	public ImageView leftIv, rightIv;
	public LinearLayout leftll, rightll;
	public RelativeLayout top;

	public View setContentView(LayoutInflater inflater, int layoutResID) {
		View view = inflater.inflate(layoutResID, null);
		initInjectedView(this, view);
		return view;
	}

	/**
	 * 初始化头部
	 */
	public void initTitle(View view) {
		top = (RelativeLayout) view.findViewById(R.id.top);
		leftll = (LinearLayout) view.findViewById(R.id.leftll);
		rightll = (LinearLayout) view.findViewById(R.id.rightll);
		leftIv = (ImageView) view.findViewById(R.id.leftIv);
		centerTv = (TextView) view.findViewById(R.id.centerTv);
		rightIv = (ImageView) view.findViewById(R.id.rightIv);
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
	 * 设置头部 的右部分的图片
	 */
	public void setLeftDrawable(int drawable) {
		leftIv.setBackgroundResource(drawable);

	}

	/**
	 * 设置头部 的右部分的图片
	 */
	public void setRightDrawable(int drawable) {
		rightIv.setBackgroundResource(drawable);

	}

	private void initInjectedView(Object framgent, View sourceView) {
		Field[] fields = framgent.getClass().getDeclaredFields(); // 获取字段
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				try {
					field.setAccessible(true); // 设为可访问

					if (field.get(framgent) != null)
						continue;

					PView d3View = field.getAnnotation(PView.class);
					if (d3View != null) {

						int viewId = d3View.id();
						if (viewId == 0)
							viewId = getResources().getIdentifier(
									field.getName(), "id",
									getActivity().getPackageName());
						if (viewId == 0)
							Log.e("D3Activity", "field " + field.getName()
									+ "not found");

						// 关键,注解初始化，相当于 backBtn = (TextView)
						// findViewById(R.id.back_btn);
						field.set(framgent, sourceView.findViewById(viewId));
						// 事件
						setListener(framgent, field, d3View.click(),
								Method.Click);
						setListener(framgent, field, d3View.longClick(),
								Method.LongClick);
						setListener(framgent, field, d3View.itemClick(),
								Method.ItemClick);
						setListener(framgent, field, d3View.itemLongClick(),
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
		case focusChange:
			if (obj instanceof View) {
				((View) obj).setOnFocusChangeListener(new EventListener(
						activity).focusChange(methodName));
			}
			break;
		default:
			break;
		}
	}

	public enum Method {
		Click, LongClick, ItemClick, itemLongClick, focusChange
	}

}
