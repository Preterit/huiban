package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.Good;

import java.util.List;

public class GoodsAdapter extends MyBaseAdapter<Good> {
    private Context mContext;


    public GoodsAdapter(Context context, LayoutInflater inflater) {
        super(inflater);
        mContext = context;
    }


    @Override
    public View getView(int position, View v, ViewGroup parent) {




        return v;
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
