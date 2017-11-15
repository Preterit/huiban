package com.feirui.feiyunbangong.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.Friend;
import com.feirui.feiyunbangong.view.CircleImageView;
import com.feirui.feiyunbangong.view.TextImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by xy on 2017-10-25.
 */

public class SearchFriendsAdapter extends RecyclerView.Adapter<SearchFriendsAdapter.ViewHolder> {
    private List<Friend> friendList;

    public SearchFriendsAdapter(List<Friend> friendList){
        this.friendList = friendList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_search_friend,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //绑定数据
        if (!TextUtils.isEmpty(friendList.get(position).getHead())){
            ImageLoader.getInstance().displayImage(friendList.get(position).getHead(),holder.mCir_item_head);
        }else {
            holder.mCir_item_head.setText(friendList.get(position).getName());
        }

        holder.mTv_item_name.setText(friendList.get(position).getName());

        if (!TextUtils.isEmpty(friendList.get(position).getBrithday()) && !"null".equals(friendList.get(position).getBrithday())){
            String birth = friendList.get(position).getBrithday().substring(0,4);

            holder.mTv_item_birth.setText(birth + "年");
        }else {
            holder.mTv_item_birth.setText("无");
        }
        if (!TextUtils.isEmpty(friendList.get(position).getSex()) && !"null".equals(friendList.get(position).getSex())) {
            if ("女".equals(friendList.get(position).getSex())){
                holder.mIv_item_sex.setImageResource(R.drawable.person_women);
            }else {
                holder.mIv_item_sex.setImageResource(R.drawable.person_man);
            }

        }else {
            holder.mIv_item_sex.setImageResource(R.drawable.person_man);
        }

        if (!TextUtils.isEmpty(friendList.get(position).getKey1()) && !"null".equals(friendList.get(position).getKey1())){
            holder.mTv_item_key1.setText(friendList.get(position).getKey1());
            holder.mTv_item_key1.setVisibility(View.VISIBLE);
        }else {
            holder.mTv_item_key1.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(friendList.get(position).getKey2()) && !"null".equals(friendList.get(position).getKey2())){
            holder.mTv_item_key2.setText(friendList.get(position).getKey2());
            holder.mTv_item_key2.setVisibility(View.VISIBLE);
        }else {
            holder.mTv_item_key2.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(friendList.get(position).getKey3()) && !"null".equals(friendList.get(position).getKey3())){
            holder.mTv_item_key3.setText(friendList.get(position).getKey3());
            holder.mTv_item_key3.setVisibility(View.VISIBLE);
        }else {
            holder.mTv_item_key3.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(friendList.get(position).getAddress()) && !"null".equals(friendList.get(position).getAddress())){
            holder.mTv_item_area.setText(friendList.get(position).getAddress());
        }else {
            holder.mTv_item_area.setText("北京 朝阳");
        }
        if (!TextUtils.isEmpty(friendList.get(position).getDistence()) && !"null".equals(friendList.get(position).getDistence())){
            double dist = Double.parseDouble(friendList.get(position).getDistence());
            if (dist > 1.0 ){
                holder.mTv_item_distence.setText(friendList.get(position).getDistence() + " km");
            }else {
                holder.mTv_item_distence.setText((int)(dist * 1000) + " m");
            }
        }else {
            holder.mTv_item_distence.setText(" ");
        }
//        if (!TextUtils.isEmpty(friendList.get(position).getShopUrl())){
//            ImageLoader.getInstance().displayImage(friendList.get(position).getShopUrl());
//        }else {
//            holder.mTv_item_distence.setText("");
//        }

        if ("0".equals(friendList.get(position).getState())){
            holder.mIv_item_add.setVisibility(View.VISIBLE);
        }else {
            holder.mIv_item_add.setVisibility(View.GONE);
        }

        //调用点击事件
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }



    }

    //每一项item的点击事件
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onClick(int postion);
        void onLongClick(int postion);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextImageView mCir_item_head;
        private TextView mTv_item_name;
        private TextView mTv_item_birth;
        private ImageView mIv_item_sex;
        private TextView mTv_item_key1;
        private TextView mTv_item_key2;
        private TextView mTv_item_key3;
        private TextView mTv_item_area;
        private TextView mTv_item_distence;
        private ImageView mIv_item_shop;
        private ImageView mIv_item_add;

        public ViewHolder(View itemView) {
            super(itemView);
            mCir_item_head = (TextImageView)itemView.findViewById(R.id.cir_item_head);
            mTv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
            mTv_item_birth = (TextView) itemView.findViewById(R.id.tv_item_birth);
            mIv_item_sex = (ImageView) itemView.findViewById(R.id.iv_item_sex);
            mTv_item_key1 = (TextView) itemView.findViewById(R.id.tv_item_key1);
            mTv_item_key2 = (TextView) itemView.findViewById(R.id.tv_item_key2);
            mTv_item_key3 = (TextView) itemView.findViewById(R.id.tv_item_key3);
            mTv_item_area = (TextView) itemView.findViewById(R.id.tv_item_area);
            mTv_item_distence = (TextView) itemView.findViewById(R.id.tv_item_distence);
            mIv_item_shop = (ImageView) itemView.findViewById(R.id.iv_item_shop);
            mIv_item_add = (ImageView) itemView.findViewById(R.id.iv_item_add);
        }
    }
}
