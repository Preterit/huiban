package com.feirui.feiyunbangong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by 郭新胜 on 2017/4/7.
 */

public class AddTeamAdapter extends  RecyclerView.Adapter<AddTeamAdapter.ViewHolder> {


    private ArrayList<TuanDui> mList;
    private DisplayImageOptions mDisplayImageOptions;

    public AddTeamAdapter(ArrayList<TuanDui> list) {
        mList = list;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.fragment_head) //
                .showImageOnFail(R.drawable.fragment_head) //
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
    }

    public void setList(ArrayList<TuanDui> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shenpiren_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(mList.get(position).getName());

       // ImageLoader.getInstance().displayImage(mList.get(position).getHead(), holder.ivHead, mDisplayImageOptions);
    }


    public void removeAt(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addShenPiRen(TuanDui spr) {
        mList.add(spr);
        notifyDataSetChanged();
    }

    public ArrayList<TuanDui> getDataSet() {
        return mList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHead;
        TextView name;



        public ViewHolder(View itemView) {
            super(itemView);
            ivHead = (ImageView) itemView.findViewById(R.id.iv_shenpi_item);
            name = (TextView) itemView.findViewById(R.id.tv_shenpi_item);

            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(getAdapterPosition());
                }
            });
        }
    }
}
