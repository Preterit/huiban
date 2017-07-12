package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
 * Created by lice on 2017/7/7.任务单完成adapter
 */

public class TaskWanChengAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context context;
    private OnItemClickListener mOnItemClickListener = null;
    private ArrayList<HashMap<String, Object>> data;


    public TaskWanChengAdapter(Context context, ArrayList<HashMap<String, Object>> map){
        this.context = context;
        this.data = map;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_quan_bu_ren_wu, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (!data.get(position).get("state").equals(2)){
//            return;
//        }
        ((ViewHolder) holder).tv_qbrw_name.setText(data.get(position).get("staff_name")+"");
        String a = (String) data.get(position).get("time");
        String[] a1 =  a.split(" ");
        ((ViewHolder) holder).tv_qbrw_time.setText(a1[0]);
        ((ViewHolder) holder).tv_qbrw_title.setText(data.get(position).get("task_txt")+"");
        ImageLoader.getInstance().displayImage("http://123.57.45.74/feiybg/"+data.get(position).get("staff_head"), ((ViewHolder) holder).iv_qbrw_tx, ImageLoaderUtils.getSimpleOptions());

        if(data.get(position).get("state").equals(0)){
            ((ViewHolder) holder).tv_qbrw_state.setText("待接单");
            ((ViewHolder) holder).tv_qbrw_state.setTextColor(Color.parseColor("#50B3FD"));
            ((ViewHolder) holder).iv_qbrw_state.setImageResource(R.drawable.icon_daijiedan);
        }else if (data.get(position).get("state").equals(1)){
            ((ViewHolder) holder).tv_qbrw_state.setText("进行中");
            ((ViewHolder) holder).tv_qbrw_state.setTextColor(Color.parseColor("#FB4475"));
            ((ViewHolder) holder).iv_qbrw_state.setImageResource(R.drawable.ongoing);
        }else if (data.get(position).get("state").equals(2)){
            ((ViewHolder) holder).tv_qbrw_state.setText("完成");
            ((ViewHolder) holder).tv_qbrw_state.setTextColor(Color.parseColor("#A4A4A4"));
            ((ViewHolder) holder).iv_qbrw_state.setImageResource(R.drawable.over);
        }else if (data.get(position).get("state").equals(3)){
            ((ViewHolder) holder).tv_qbrw_state.setTextColor(Color.parseColor("#A4A4A4"));
            ((ViewHolder) holder).tv_qbrw_state.setText("拒绝");
            ((ViewHolder) holder).iv_qbrw_state.setImageResource(R.drawable.over);
        }
        ((ViewHolder) holder).tv_qbrw_target.setText(data.get(position).get("task_time")+"完成");
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_qbrw_name,tv_qbrw_time,tv_qbrw_title,tv_qbrw_target,tv_qbrw_state;
        private ImageView iv_qbrw_tx,iv_qbrw_state;
        public ViewHolder(View itemView) {
            super(itemView);
            LocalDisplay.init(itemView.getContext());
            itemView.setOnClickListener(this);
            tv_qbrw_name= (TextView) itemView.findViewById(R.id.tv_qbrw_name);
            tv_qbrw_time= (TextView) itemView.findViewById(R.id.tv_qbrw_time);
            tv_qbrw_title= (TextView) itemView.findViewById(R.id.tv_qbrw_title);
            tv_qbrw_target= (TextView) itemView.findViewById(R.id.tv_qbrw_target);
            tv_qbrw_state= (TextView) itemView.findViewById(R.id.tv_qbrw_state);
            iv_qbrw_tx=(ImageView) itemView.findViewById(R.id.iv_qbrw_tx);
            iv_qbrw_state=(ImageView) itemView.findViewById(R.id.iv_qbrw_state);

        }

        @Override
        public void onClick(View v) {

        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void addAll(ArrayList<HashMap<String, Object>> map) {
        data.clear();
        data.addAll(map);
        notifyDataSetChanged();
    }
}