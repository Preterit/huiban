package com.feirui.feiyunbangong.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 轮播图adapter
 * 
 * @author feirui1
 *
 */
public class PicturePagerAdapter extends PagerAdapter {

	ArrayList<ImageView> list;

	public PicturePagerAdapter(ArrayList<ImageView> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		
		if(list.get(position).getParent()!=null){
			((ViewPager) list.get(position).getParent()).removeView((list.get(position)));
		}
		
		((ViewPager) container).addView(list.get(position));
		
		return list.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

}
