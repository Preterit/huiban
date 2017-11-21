package com.feirui.feiyunbangong.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.lang.reflect.Field;

/**
 * 解决的问题：GridView显示不全，只显示了一行的图片，比较奇怪，尝试重写GridView来解�?
 *
 * @author lichao
 * @since 2014-10-16 16:41
 */
public class NoScrollGridView extends GridView {

    public NoScrollGridView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}

