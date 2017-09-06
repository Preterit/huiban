package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.LocalDisplay;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lice on 2017/8/31.
 */

public class JieDanRenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context context;
    private OnItemClickListener onItemClickListener = null;
    private ArrayList<HashMap<String, Object>> data;

    public JieDanRenAdapter(Context context,ArrayList<HashMap<String,Object>> map){
        this.context = context;
        this.data = map;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_renwudan_jiedanren,null);
        Log.e("详细页面adapter", "data: "+data.toString());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("接单人adapter", "staff_name: "+ data.get(position).get("staff_name"));
        Log.e("接单人adapter", "staff_head: "+ data.get(position).get("staff_head"));
        ((ViewHolder) holder).rwd_tv_name.setText(data.get(position).get("staff_name")+"");
        ImageLoader.getInstance().displayImage("http://123.57.45.74/feiybg/"+data.get(position).get("staff_head"), ((ViewHolder) holder).rwd_im_head, ImageLoaderUtils.getSimpleOptions());

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView rwd_tv_name;
        private ImageView rwd_im_head;

        public ViewHolder(View itemView) {
            super(itemView);
            LocalDisplay.init(itemView.getContext());
            itemView.setOnClickListener(this);
            rwd_tv_name = (TextView) itemView.findViewById(R.id.rwd_tv_name);
            rwd_im_head = (ImageView) itemView.findViewById(R.id.rwd_im_head);

        }

        @Override
        public void onClick(View v) {

        }
    }
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(View v) {

    }
    public void setOnItemClickListener(JieDanRenAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void addAll(ArrayList<HashMap<String, Object>> map) {//有值
        data.clear();
        data.addAll(map);
        notifyDataSetChanged();
    }
}
