package com.feirui.feiyunbangong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feirui.feiyunbangong.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by rubing on 2017/3/15.
 * rubingem@163.com
 */

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

    private ArrayList<String> mUris;
    private DisplayImageOptions options;

    public PicAdapter(ArrayList<String> uris) {
        this.mUris = uris;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
    }

    public void setUris(ArrayList<String> uris) {
        this.mUris = uris;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(mUris.get(position), holder.mIv, options);


    }

    public void removeAt(int position) {
        mUris.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUris.size();
    }

    public void addPic(String uri) {
        mUris.add(uri);
        notifyDataSetChanged();
    }

    public ArrayList<String> getDataSet() {
        return mUris;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIv, mDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            mIv = (ImageView) itemView.findViewById(R.id.iv_item_choose_pic);
            mDelete = (ImageView) itemView.findViewById(R.id.pic_delete);
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(getAdapterPosition());
                }
            });
        }
    }
}

