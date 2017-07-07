package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.LocalDisplay;
import com.feirui.feiyunbangong.entity.RenWuDan_QuanbuBean;

import java.util.List;

/**
 * Created by lice on 2017/7/7.
 */

public class QuanBuAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context context;
    private List<RenWuDan_QuanbuBean> list;
    private OnItemClickListener mOnItemClickListener = null;

    public QuanBuAdapter(Context context,List<RenWuDan_QuanbuBean> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_quan_bu_ren_wu, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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

}
