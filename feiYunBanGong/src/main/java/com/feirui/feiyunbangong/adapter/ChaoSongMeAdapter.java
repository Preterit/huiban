package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.view.CircleImageView2;

import java.util.HashMap;
import java.util.List;

/**
 * Created by xy on 2017-12-13.
 */

public class ChaoSongMeAdapter extends RecyclerView.Adapter<ChaoSongMeAdapter.ViewHolder> {
    private Context mContext;
    private List<HashMap<String, Object>> mData;

    public ChaoSongMeAdapter(Context mContext,List<HashMap<String, Object>> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setList(List<HashMap<String, Object>> mData){
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.item_ry_send_mess,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        HashMap<String,Object> data = mData.get(position);
        if (!TextUtils.isEmpty(data.get("staff_head").toString())){
            Glide.with(mContext).load(data.get("staff_head")).placeholder(R.drawable.fragment_head);
        }
        if (!TextUtils.isEmpty(data.get("staff_name").toString())){
            holder.mItem_send_name.setText(data.get("staff_name") + "的请假申请");
        }
        if (!TextUtils.isEmpty(data.get("approval_type").toString())){
            holder.mItem_send_type.setText("类型：" + data.get("approval_type"));
        }
//        if (!TextUtils.isEmpty(data.get("approval_time").toString())){
//            holder.mItem_send_type.setText("开始时间：" + data.get("approval_time"));
//        }
        if (!TextUtils.isEmpty(data.get("approval_time").toString())){
            holder.mItem_send_type.setText(data.get("approval_time").toString());
        }

        //每一项的点击事件
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

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView2 mItem_send_head;
        private TextView mItem_send_name;
        private TextView mItem_send_type;
        private TextView mItem_send_starttime;
        private TextView mItem_send_time;

        public ViewHolder(View itemView) {
            super(itemView);
            mItem_send_head = (CircleImageView2) itemView.findViewById(R.id.item_send_head);
            mItem_send_name = (TextView) itemView.findViewById(R.id.item_send_name);
            mItem_send_type = (TextView) itemView.findViewById(R.id.item_send_type);
            mItem_send_starttime = (TextView) itemView.findViewById(R.id.item_send_starttime);
            mItem_send_time = (TextView) itemView.findViewById(R.id.item_send_time);
        }
    }

    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
