package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.Good;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class GoodsAdapter extends MyBaseAdapter<Good> {
    private Context mContext;
    private boolean mEdit;

    public GoodsAdapter(Context context, LayoutInflater inflater) {
        super(inflater);
        mContext = context;
    }

    public boolean isEdit() {
        return mEdit;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {


        ViewHolder holder = null;
        if (v == null) {
            v = mInflater.inflate(R.layout.item_shop_good, null);
            holder = new ViewHolder();
            holder.iv_good_head = (ImageView) v.findViewById(R.id.iv_good_head);
            holder.tv_good_name = (TextView) v.findViewById(R.id.tv_good_name);
            holder.tv_price = (TextView) v.findViewById(R.id.tv_good_price);
            holder.iv_left = (ImageView) v.findViewById(R.id.iv_left);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder = (ViewHolder) v.getTag();

        if (list.get(position).getImgUrl() != null) {

            if (position + 1 == list.size()) {
                Log.e("orz", list.get(position).getImgUrl());
                ImageLoader.getInstance().displayImage(list.get(position).getImgUrl(), holder.iv_good_head);

                holder.tv_price.setVisibility(View.GONE);
                holder.tv_good_name.setVisibility(View.GONE);
            } else {
                holder.tv_price.setVisibility(View.VISIBLE);
                holder.tv_good_name.setVisibility(View.VISIBLE);
                if (list.get(position).getGood_name() != null) {
                    holder.tv_good_name.setText(list.get(position).getGood_name());
                }
                if (list.get(position).getPrivce() != null) {
                    holder.tv_price.setText(list.get(position).getPrivce());
                }
                ImageLoader.getInstance().displayImage(list.get(position).getImgUrl(), holder.iv_good_head);
            }

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
            if (position + 1 == list.size()) {
                holder.iv_left.setVisibility(View.GONE);
            }
        } else {
            holder.iv_left.setVisibility(View.INVISIBLE);
        }

        return v;
    }

    public void setEdit(boolean edit) {
        mEdit = edit;
        notifyDataSetChanged();
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

    public void remove(int selectedPosition) {
        list.remove(selectedPosition);
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView iv_good_head, iv_left;// 商品缩略图；
        TextView tv_good_name, tv_price;// 商品名称；
    }

    @Override
    public void add(List<Good> ls) {
        // TODO Auto-generated method stub
        Good addGood = new Good();
        addGood.setImgUrl("drawable://" + R.drawable.add_pic);
        addGood.setGood_name("添加商品");
        addGood.setPrivce("");
        ls.add(addGood);
        super.add(ls);
    }

    public void setData(List<Good> data) {
        list.clear();
        list.addAll(data);
        Good addGood = new Good();
        addGood.setImgUrl("drawable://" + R.drawable.add_pic);
        addGood.setGood_name("添加商品");
        addGood.setPrivce("");
        list.add(addGood);

        notifyDataSetChanged();
    }
}
