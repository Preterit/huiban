package com.feirui.feiyunbangong.adapter;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter{

	public List<T> list=new ArrayList<>();
	public LayoutInflater mInflater;
	
	public MyBaseAdapter(LayoutInflater inflater) {
		this.mInflater=inflater;
	}
	
	public MyBaseAdapter() {
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
	
	
	
	public void add(List<T> ls){
		try{
			list.removeAll(list);
			if(ls!=null && ls.size()>0){
				list.addAll(ls);
			}
			notifyDataSetChanged();
		}catch(Exception e){
			Log.i("TAG", e.getMessage());
		}
		
	}
	
	
	
	
	
	
	
	
	
}
