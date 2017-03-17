package com.feirui.feiyunbangong.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rubing on 2017/3/17.
 * rubingem@163.com
 */


public class GoodDetailAdapter extends RecyclerView.Adapter<GoodDetailAdapter.ViewHolder> {

    private List<String> mGoodDetails;

    public GoodDetailAdapter() {
        mGoodDetails = new ArrayList<>();
    }

    public void setGoodDetails(List<String> goodDetails) {
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
        ImageLoader.getInstance().displayImage(mGoodDetails.get(position), holder.mImageView, ImageLoaderUtils.getSimpleOptions());
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
