package com.feirui.feiyunbangong.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.TaskListAdapter.ViewHolder;
import com.feirui.feiyunbangong.entity.TaskListEntity.InfoBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rubing on 2017/3/23.
 * rubingem@163.com
 */

public class TaskListAdapter extends Adapter<ViewHolder> {

  private List<InfoBean> mList;

  public TaskListAdapter(ArrayList<InfoBean> list) {
    mList = list;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.team_task_item, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.tvContentTaskItem.setText(mList.get(position).getTask_txt());
    holder.tvTaskTitle.setText(mList.get(position).getSubject());
    holder.tvPubNameTaskItem.setText(mList.get(position).getName());
    holder.tvPubTime.setText(mList.get(position).getTime());

  }

  @Override
  public int getItemCount() {
    return mList.size();
  }

  public void setData(List<InfoBean> data) {
    mList = data;
    notifyDataSetChanged();
  }

  private void initView() {
  }


  class ViewHolder extends RecyclerView.ViewHolder {

    ImageView ivLabelTaskItem;
    TextView tvTaskTitle;
    TextView tvPubNameTaskItem;
    View divTaskItem;
    TextView tvBgTaskItem;
    TextView tvContentTaskItem;
    TextView tvPubTime;

    public ViewHolder(View itemView) {
      super(itemView);
      initView(itemView);
    }

    private void initView(View itemView) {
      ivLabelTaskItem = (ImageView) itemView.findViewById(R.id.ivLabelTaskItem);
      tvTaskTitle = (TextView) itemView.findViewById(R.id.tvTaskTitle);
      tvPubNameTaskItem = (TextView) itemView.findViewById(R.id.tvPubNameTaskItem);
      divTaskItem = (View) itemView.findViewById(R.id.divTaskItem);
      tvBgTaskItem = (TextView) itemView.findViewById(R.id.tvBgTaskItem);
      tvContentTaskItem = (TextView) itemView.findViewById(R.id.tvContentTaskItem);
      tvPubTime = (TextView) itemView.findViewById(R.id.tvPubTime);
    }
  }
}
