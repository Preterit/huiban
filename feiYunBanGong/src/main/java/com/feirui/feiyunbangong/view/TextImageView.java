package com.feirui.feiyunbangong.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

public class TextImageView extends CircleImageView {

	// 颜色画板集
	private static final int[] colors = { 0xff42baff, 0xff16a085, 0xfff1c40f,
			0xfff39c12, 0xff2ecc71, 0xff27ae60, 0xffe67e22, 0xffd35400,
			0xff3498db, 0xff2980b9, 0xffe74c3c, 0xffc0392b, 0xff9b59b6,
			0xff8e44ad, 0xffbdc3c7, 0xff34495e, 0xff2c3e50, 0xff95a5a6,
			0xff7f8c8d, 0xffec87bf, 0xffd870ad, 0xfff69785, 0xff9ba37e,
			0xffb49255, 0xffb49255, 0xffa94136 };

	private Paint mPaintBackground;
	private Paint mPaintText;
	private Rect mRect;
	private String text;
	private int charHash;

	public TextImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TextImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TextImageView(Context context) {
		super(context);
	}

	private void init() {
		mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG); //没有锯齿
		mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		mRect = new Rect();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (text == null) {
			return;
		}

		//int color = colors[new Random().nextInt(25)];
		int color = colors[0];
		init();

		mPaintBackground.setColor(color);
		// 画圆
		canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2,
				mPaintBackground);
		// 写字
		mPaintText.setColor(Color.WHITE);
		mPaintText.setTextSize(getWidth() / 3);
		mPaintText.setStrokeWidth(3); //设置线条宽度
		// mPaintText.getTextBounds(text, 0, 1, mRect);
		// 垂直居中
		Paint.FontMetricsInt fontMetrics = mPaintText.getFontMetricsInt();
		int baseline = (getMeasuredHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
		// 左右居中
		mPaintText.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(text, getWidth() / 2, baseline, mPaintText);
		removeText();// 注意！！！！！！！！！！！！！必须将text置空，否则会出现紊乱！！！！！！！！
	}

	/**
	 * @param content
	 *            传入字符内容 只会取内容的第一个字符,如果是字母转换成大写
	 */
	public void setText(String content) {
		if (content == null) {
			return;
		}
		this.text = content;
		/*
		 * this.text = String.valueOf(content.toCharArray()[0]); this.text =
		 * text.toUpperCase();
		 */
		charHash = this.text.hashCode();
		// 重绘
		invalidate();// view硬件加速会失效！！！！如何解决》？？？？
	}

	public void removeText() {
		this.text = null;
	}

}
