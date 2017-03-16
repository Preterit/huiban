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

    private ArrayList<String> uris;
    private DisplayImageOptions options;

    public PicAdapter(ArrayList<String> uris) {
        this.uris = uris;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
    }

    public void setUris(ArrayList<String> uris) {
        this.uris = uris;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(uris.get(position), holder.mIv, options);

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAt(position);
            }
        });

    }

    public void removeAt(int position) {
        uris.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, uris.size());
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    public void addPic(String uri) {
        uris.add(uri);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIv, mDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            mIv = (ImageView) itemView.findViewById(R.id.iv_item_choose_pic);
            mDelete = (ImageView) itemView.findViewById(R.id.pic_delete);
        }
    }
}

