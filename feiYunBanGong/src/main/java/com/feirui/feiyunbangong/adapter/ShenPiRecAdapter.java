package com.feirui.feiyunbangong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.ShenPiRen;

import java.util.ArrayList;

/**
 * Created by rubing on 2017/3/15.
 * rubingem@163.com
 */

public class ShenPiRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ShenPiRen> mList;

    public ShenPiRecAdapter(ArrayList<ShenPiRen> list) {
        mList = list;
    }

    public void setList(ArrayList<ShenPiRen> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shenpiren_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHead;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
