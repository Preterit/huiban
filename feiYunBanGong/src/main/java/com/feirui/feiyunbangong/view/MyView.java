package com.feirui.feiyunbangong.view;

import java.util.ArrayList;
import java.util.List;

import com.feirui.feiyunbangong.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

@SuppressLint("Recycle")
public class MyView extends View {

    private List<String> x_content = new ArrayList<>();// x轴坐标点；
    private List<String> y_content = new ArrayList<>();// y轴坐标点；
    private int x_interval;// x轴间隔；
    private int y_interval;// y轴间隔；
    private int x_point;// 原点横坐标；
    private int y_point;// 原点纵坐标；
    private int xylinecolor;// 颜色；
    private List<Integer> strengths = new ArrayList<>();// 信号强度；
    private int xyTextSize;// 字体；

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 属性：
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.LineChar);
        x_interval = typedArray.getLayoutDimension(
                R.styleable.LineChar_x_interval, 50);
        x_point = typedArray.getLayoutDimension(R.styleable.LineChar_x_point,
                120);
        y_point = typedArray.getLayoutDimension(R.styleable.LineChar_y_point,
                1050);
        y_interval = typedArray.getLayoutDimension(
                R.styleable.LineChar_y_interval, 50);
        xylinecolor = typedArray.getColor(R.styleable.LineChar_xylinecolor,
                R.color.white);
        xyTextSize = typedArray.getLayoutDimension(
                R.styleable.LineChar_xytextsize, 30);
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        Log.e("TAG", "onLayout()...........");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Log.e("TAG", "onDraw()...........");

        // 如果没有画坐标则开始画；
        drawXPoint(canvas);// 画x轴及x轴坐标点；
        drawYPoint(canvas);// 画y轴及y轴坐标点；

        Log.e("orz", "onDraw: " + strengths.size());
        if (strengths.size() == 0) {
            return;
        }
        // 画折线：
        drawBrokenLine(canvas);

    }

    // 画折线：
    private void drawBrokenLine(Canvas canvas) {

        Paint b_paint = new Paint();
        b_paint.setStyle(Paint.Style.FILL);// 设置画笔样式；
        b_paint.setTextSize(xyTextSize);// 设置画笔对象字体大小；
        b_paint.setStrokeWidth(4);// 设置线段粗细；

        for (int i = 0; i < strengths.size(); i++) {
            b_paint.setColor(Color.parseColor(colors[i % 10]));
            int startX1 = x_point + x_interval * i * 2;
            int startY1 = y_point;
            int stopX1 = startX1 + 2 * x_interval;
            int stopY1 = (int) (y_point - (strengths.get(i) + 100) / 10.0
                    * y_interval);
            canvas.drawLine(startX1, startY1, stopX1, stopY1, b_paint);

            int startX2 = stopX1;
            int startY2 = stopY1;
            int stopX2 = stopX1 + 2 * x_interval;
            int stopY2 = y_point;
            canvas.drawLine(startX2, startY2, stopX2, stopY2, b_paint);

            // 画信号强度值；
            canvas.drawText(strengths.get(i) + "", stopX1 - 20, stopY1 - 20,
                    b_paint);
        }

    }

    /**
     * 画y轴及y轴上的坐标点：
     */
    private void drawYPoint(Canvas canvas) {

        Paint y_paint = new Paint();// y轴坐标点画笔对象；

        y_paint.setStyle(Paint.Style.FILL);// 设置画笔样式；
        y_paint.setTextSize(xyTextSize);// 设置画笔对象字体大小；
        y_paint.setColor(xylinecolor);// 设置画笔颜色；
        String text = null;

        // 画y轴坐标点：
        for (int i = 0; i < y_content.size(); i++) {
            text = y_content.get(i);
            canvas.drawText(text,
                    (int) (x_point - y_paint.measureText(text) * 1.2), y_point
                            - y_interval * i, y_paint);
        }

        // 画y轴坐标点处的小圆圈：
        for (int i = 0; i < y_content.size(); i++) {
            canvas.drawCircle(x_point, y_point - y_interval * i,
                    xyTextSize / 4, y_paint);
        }

        // 画竖线：
        for (int i = 0; i < x_content.size(); i++) {
            canvas.drawLine(x_point + x_interval * i, y_point, x_point
                    + x_interval * i, y_point - y_interval
                    * (y_content.size() - 1), y_paint);
        }

        // 写坐标描述：
        canvas.drawText("信", (int) (x_point - xyTextSize * 3),
                (int) (getHeight() / 2 - xyTextSize * 2.5), y_paint);
        canvas.drawText("号", (int) (x_point - xyTextSize * 3),
                (int) (getHeight() / 2 - xyTextSize), y_paint);
        canvas.drawText("强", (int) (x_point - xyTextSize * 3),
                (int) (getHeight() / 2 + xyTextSize * 0.5), y_paint);
        canvas.drawText("度", (int) (x_point - xyTextSize * 3),
                (int) (getHeight() / 2 + xyTextSize * 2), y_paint);

    }

    /**
     * 画x轴上的点和直线：
     */
    public void drawXPoint(Canvas canvas) {

        Paint x_paint = new Paint();// x轴坐标点画笔对象；

        x_paint.setStyle(Paint.Style.FILL);// 设置画笔样式；
        x_paint.setTextSize(xyTextSize);// 设置画笔对象字体大小；
        x_paint.setColor(xylinecolor);// 设置画笔颜色；
        String text = null;

        // 画x轴坐标点：
        for (int i = 0; i < x_content.size(); i++) {
            text = x_content.get(i);
            canvas.drawText(text, x_point + i * x_interval,
                    (int) (y_point + xyTextSize * 1.2), x_paint);
        }

        // 画x轴坐标点处的小圆圈：
        for (int i = 0; i < x_content.size(); i++) {
            canvas.drawCircle(x_point + x_interval * i, y_point,
                    xyTextSize / 4, x_paint);
        }

        // 画横线：
        for (int i = 0; i < y_content.size(); i++) {
            canvas.drawLine(x_point, y_point - y_interval * i, x_point
                    + x_interval * (x_content.size() - 1), y_point - y_interval
                    * i, x_paint);
        }

        // 画x轴描述：
        canvas.drawText("WiFi 编 号", getWidth() / 2 - xyTextSize * 2,
                (int) (y_point + xyTextSize * 2.4), x_paint);

    }

    // 设置x轴和y轴坐标值：
    public void setContent(List<String> x_content, List<String> y_content) {
        this.x_content = x_content;
        this.y_content = y_content;
        invalidate();
    }

    // 设置信号强度：
    public void setStrength(List<Integer> strengths) {
        this.strengths = strengths;
        Log.e("TAG", this.strengths.toString());
        invalidate();
    }

    // 表示各个信道的颜色值
    public String[] colors = new String[]{"#ff784a", "#8fc31f", "#ae5da1",
            "#00a0e9", "#FF00FF", "#f19ec2", "#cfa972"};

}
