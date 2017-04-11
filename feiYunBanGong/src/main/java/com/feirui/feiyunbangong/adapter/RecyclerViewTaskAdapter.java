package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.TaskListEntity.InfoBean;

import java.util.List;


public class RecyclerViewTaskAdapter extends RecyclerView.Adapter<RecyclerViewTaskAdapter.ViewHolder> {

    private MyItemClickListener mItemClickListener;
    private Context context;
    private List<InfoBean> mList;

    public RecyclerViewTaskAdapter(Context context, List<InfoBean> list) {
        this.context = context;
        this.mList = list;
    }

    public void setStatus(int position) {
        InfoBean infoBean = mList.get(position);

        infoBean.setType(1);

        notifyDataSetChanged();

    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.team_task_item, null);
        //将全局的监听传递给holder
        ViewHolder holder = new ViewHolder(view, mItemClickListener);
        return holder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        //给控件赋值

        holder.tvContentTaskItem.setText(mList.get(position).getTask_txt());
        holder.tvTaskTitle.setText(mList.get(position).getSubject());
        holder.tvPubNameTaskItem.setText(mList.get(position).getName());
        holder.tvPubTime.setText(mList.get(position).getTime());

        if (mList.get(position).getType()== 1) {
            holder.tvBgTaskItem.setBackgroundColor(Color.GRAY);
            holder.ivLabelTaskItem.setImageResource(R.drawable.label_grey);
            holder.tvBgTaskItem.setText("接收完成");
        }else{
            holder.tvBgTaskItem.setBackgroundColor(Color.GREEN);
            holder.ivLabelTaskItem.setImageResource(R.drawable.label_blue);
            holder.tvBgTaskItem.setText("任务接收中");
        }
    }

    public int getItemCount() {

        return mList.size();
    }

    public void setData(List<InfoBean> data) {
        mList = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView ivLabelTaskItem;
        TextView tvTaskTitle;
        TextView tvPubNameTaskItem;
        View divTaskItem;
        TextView tvBgTaskItem;
        TextView tvContentTaskItem;
        TextView tvPubTime;

        public ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            ivLabelTaskItem = (ImageView) itemView.findViewById(R.id.ivLabelTaskItem);
            tvTaskTitle = (TextView) itemView.findViewById(R.id.tvTaskTitle);
            tvPubNameTaskItem = (TextView) itemView.findViewById(R.id.tvPubNameTaskItem);
            divTaskItem = (View) itemView.findViewById(R.id.divTaskItem);
            tvBgTaskItem = (TextView) itemView.findViewById(R.id.tvBgTaskItem);
            tvContentTaskItem = (TextView) itemView.findViewById(R.id.tvContentTaskItem);
            tvPubTime = (TextView) itemView.findViewById(R.id.tvPubTime);

            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * 实现OnClickListener接口重写的方法
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(), mList);
            }

        }
    }
    //http://blog.csdn.net/dl10210950/article/details/52918019

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position, List<InfoBean> infotask);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }


}
