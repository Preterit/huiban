package com.feirui.feiyunbangong.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.MyTaskEntity.MyTaskInfo;

import java.util.List;

public class MyReceiveTaskAdapter extends BaseAdapter {
    private Context context;
    private List<MyTaskInfo> list;
    private LayoutInflater inflater;


    public MyReceiveTaskAdapter(Context context,List<MyTaskInfo> list){

        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MyTaskInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder holder=null;
        if(convertView==null){
                holder=new ViewHolder();
          convertView=inflater.inflate(R.layout.myteam_task_item,null);
            holder.tvContentTaskItem=(TextView) convertView.findViewById(R.id.tvContentTaskItem);
            holder.tvTaskTitle=(TextView)convertView.findViewById(R.id.tvTaskTitle);
            holder.tvPubNameTaskItem=(TextView) convertView.findViewById(R.id.tvPubNameTaskItem);
            holder.tvPubTime=(TextView)convertView.findViewById(R.id.tvPubTime);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        MyTaskInfo task = getItem(position);
        holder.tvContentTaskItem.setText(task.getTask_txt());
        holder.tvPubNameTaskItem.setText(task.getName());
        holder.tvPubTime.setText(task.getRelease_time());
        holder.tvTaskTitle.setText(task.getSubject());

        return convertView;
    }

    class ViewHolder{
        TextView tvTaskTitle;     //发布的任务标题
        TextView tvPubNameTaskItem; //发任务的人名
        TextView tvPubTime;     //发任务的时间
        TextView tvContentTaskItem;  //发任务的具体内容

    }
}
