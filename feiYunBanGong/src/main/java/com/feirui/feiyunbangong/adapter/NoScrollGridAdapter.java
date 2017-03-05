package com.feirui.feiyunbangong.adapter;

import java.util.ArrayList;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.utils.LoadImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NoScrollGridAdapter extends BaseAdapter {

	/** 图片Url集合 */
	private ArrayList<String> imageUrls;
	private Activity activity;

	public NoScrollGridAdapter(Activity ctx, ArrayList<String> urls) {
		this.activity = ctx;
		this.imageUrls = urls;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls == null ? 0 : imageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(activity, R.layout.item_gridview, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
		ImageLoader.getInstance().displayImage(imageUrls.get(position), imageView);
		return view;
	}

}
