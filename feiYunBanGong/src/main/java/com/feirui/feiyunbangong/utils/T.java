package com.feirui.feiyunbangong.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;

//Toast统一管理类
public class T extends Toast {
	/**
	 * @param context
	 */
	public T(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static boolean isShow = true;

	public static TextView textView;

	public static Toast result;

	/**
	 * 短时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {
		if (!isShow || "暂无数据".equals(message)) {
			return;
		}

		int duration = Toast.LENGTH_SHORT;
		result = new Toast(context);

		View view = LayoutInflater.from(context).inflate(R.layout.toast, null);

		textView = (TextView) view.findViewById(R.id.textView1);

		textView.setText(message.toString());
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);

		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				result.cancel();

			}
		});

		result.setGravity(Gravity.CENTER, 0, (int) context.getResources()
				.getDimension(R.dimen.x10));

		Log.e("way", "0000000000000");
		result.setDuration(duration);
		result.setView(view);

		result.show();

	}

	/**
	 * 短时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message) {
		if (isShow)
			NewToast.maketext(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (isShow)
			NewToast.maketext(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 长时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message) {
		if (isShow)
			NewToast.maketext(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 自定义显示Toast时间
	 *
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (isShow)
			NewToast.maketext(context, message, duration).show();
	}

	/**
	 * 自定义显示Toast时间
	 *
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration) {
		if (isShow)
			NewToast.maketext(context, message, duration).show();
	}

}

class NewToast extends Toast {

	public NewToast(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static Toast maketext(Context context, Object message, int duration) {

		Toast result = new Toast(context);

		ViewGroup.LayoutParams vpl = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setPadding(
				(int) context.getResources().getDimension(R.dimen.x100),
				(int) context.getResources().getDimension(R.dimen.x30),
				(int) context.getResources().getDimension(R.dimen.x100),
				(int) context.getResources().getDimension(R.dimen.x30));
		linearLayout.setLayoutParams(vpl);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		linearLayout.setBackgroundResource(R.drawable.toast);
		layoutParams.weight = 1;

		layoutParams.gravity = Gravity.CENTER;

		TextView textView = new TextView(context);
		textView.setText(message + "");
		textView.setLayoutParams(layoutParams);
		textView.setTextColor(Color.argb(255, 240, 240, 255));

		linearLayout.addView(textView);

		result.setView(linearLayout);
		result.setGravity(Gravity.CENTER, 0, (int) context.getResources()
				.getDimension(R.dimen.x500));

		result.setDuration(duration);

		return result;

	}
}
