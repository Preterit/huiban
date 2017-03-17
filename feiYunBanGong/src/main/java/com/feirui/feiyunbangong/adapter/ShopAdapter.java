package com.feirui.feiyunbangong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.Good;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rubing on 2017/3/17.
 * rubingem@163.com
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    private ArrayList<Good> mGoods;
    private boolean mEdit;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public Good getItem(int position) {
        return mGoods.get(position);
    }

    public void remove(int position) {
        mGoods.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public boolean isHeader(int position) {
        return position == 0;
    }

    public interface OnItemClickListener {
        void onItemClick(ShopAdapter.ViewHolder holder, int position);
    }

    public boolean isEdit() {
        return mEdit;
    }

    public void setEdit(boolean edit) {
        mEdit = edit;
        notifyDataSetChanged();
    }

    public ShopAdapter(ArrayList<Good> goods) {
        mGoods = goods;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_good, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (mGoods.get(position).getImgUrl() != null) {
            ImageLoader.getInstance().displayImage(mGoods.get(position).getImgUrl(), holder.iv_good_head);
        }

        if (mGoods.get(position).getGood_name() != null) {
            holder.tv_good_name.setText(mGoods.get(position).getGood_name());
        }

        if (mGoods.get(position).getPrivce() != null) {
            holder.tv_price.setText(mGoods.get(position).getPrivce());
        }

        if (position == selectedPosition) {
            //修改选中的图标
            holder.iv_left.setImageResource(R.drawable.left_selected);
        } else {
            //重置为未选中icon
            holder.iv_left.setImageResource(R.drawable.left_unselect);
        }

        if (mEdit) {
            holder.iv_left.setVisibility(View.VISIBLE);
        } else {
            holder.iv_left.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder, position);
                }
            }
        });
    }

    private int selectedPosition = -1;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * 设置选中的条目
     */
    public void setSelectedItem(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mGoods.size();
    }

    public void setData(List<Good> data) {
        mGoods.clear();
        mGoods.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_left, iv_good_head;
        private TextView tv_price;
        private TextView tv_good_name;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_left = (ImageView) itemView.findViewById(R.id.iv_left);
            iv_good_head = (ImageView) itemView.findViewById(R.id.iv_good_head);
            tv_price = (TextView) itemView.findViewById(R.id.tv_good_price);
            tv_good_name = (TextView) itemView.findViewById(R.id.tv_good_name);
        }
    }

}
