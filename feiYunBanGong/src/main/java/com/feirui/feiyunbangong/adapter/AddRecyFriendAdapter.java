package com.feirui.feiyunbangong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szh on 2017/12/18.
 */

public class AddRecyFriendAdapter extends  RecyclerView.Adapter<AddRecyFriendAdapter.ViewHolder>{
    private ArrayList<ChildItem> mList;
    private DisplayImageOptions mDisplayImageOptions;
    private List<String> strs = new ArrayList<>();
    public AddRecyFriendAdapter(ArrayList<ChildItem> list) {
        mList = list;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.fragment_head) //
                .showImageOnFail(R.drawable.fragment_head) //
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
    }

    public void setList(ArrayList<ChildItem> list) {
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shenpiren_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(mList.get(position).getTitle());
        ImageLoader.getInstance().displayImage(mList.get(position).getMarkerImgId(), holder.ivHead, mDisplayImageOptions);
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

    public void addFriend(ChildItem child) {
        mList.add(child);
        notifyDataSetChanged();
    }

//    public ArrayList<ShenPiRen> getDataSet() {
//        return mList;
//    }

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
    public ArrayList<ChildItem> getDataSet() {
        return mList;
    }

    // 查重并累加：
    public void addList(List<ChildItem> items) {

        for (int i = 0; i < items.size(); i++) {
            boolean isHave = false;

            for (int j = 0; j < mList.size(); j++) {
                if (mList.get(j).getId().equals(items.get(i).getId())) {
                    isHave = true;
                    break;
                }
            }
            if (!isHave) {
                mList.add(items.get(i));
            }
        }

        strs.removeAll(strs);
        for (int i = 0; i <mList.size(); i++) {
            strs.add("其他");
        }

        notifyDataSetChanged();
    }

    public List<String> getTags() {

        return strs;
    }

}
