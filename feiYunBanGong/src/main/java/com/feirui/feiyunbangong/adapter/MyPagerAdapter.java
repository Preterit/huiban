package com.feirui.feiyunbangong.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by lice on 2018/1/11.
 * 为了解决引导页ANR写的adapter
 */

public class MyPagerAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<ImageView> viewList;
    private int[] imgs;
    public MyPagerAdapter (Activity activity, ArrayList<ImageView> viewList, int[] imgs){
        this.activity=activity;
        this.viewList=viewList;
        this.imgs=imgs;
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
        //回收图片
        ImageView imageView = viewList.get(position);
        imageView.setImageBitmap(null);
        releaseImageViewResouce(imageView);
        //移除页面
        ((ViewPager)container).removeView(imageView);
    }
    @Override
    public Object instantiateItem(View container, int position) {
        //图片压缩
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(activity.getResources(), imgs[position], opts);
        DisplayMetrics outMetrics=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int x=opts.outWidth/outMetrics.widthPixels;
        int y=opts.outHeight/outMetrics.heightPixels;
        if(x>y&&x>1){
            opts.inSampleSize=x;
        }else if(y>x&&y>1){
            opts.inSampleSize=y;
        }
        opts.inJustDecodeBounds=false;
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), imgs[position], opts);
        //加载图片
        ImageView imageView = viewList.get(position);
        imageView.setImageBitmap(bitmap);
        //加载页面
        ((ViewPager)container).addView(imageView);
        return viewList.get(position);
    }
    @Override
    public int getCount() {
        return viewList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    /**
     * 释放图片资源的方法
     * @param imageView
     */
    public void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap=null;
            }
        }
        System.gc();
    }
}
