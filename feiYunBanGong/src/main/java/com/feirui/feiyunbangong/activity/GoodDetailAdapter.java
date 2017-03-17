package com.feirui.feiyunbangong.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.GoodDetail;

import java.util.ArrayList;

/**
 * Created by rubing on 2017/3/17.
 * rubingem@163.com
 */


public class GoodDetailAdapter extends RecyclerView.Adapter<GoodDetailAdapter.ViewHolder> {

    private ArrayList<GoodDetail> mGoodDetails;

    public void setGoodDetails(ArrayList<GoodDetail> goodDetails) {
        mGoodDetails.clear();
        mGoodDetails.addAll(goodDetails);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.good_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mGoodDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_good_detail_item);
        }
    }
}
