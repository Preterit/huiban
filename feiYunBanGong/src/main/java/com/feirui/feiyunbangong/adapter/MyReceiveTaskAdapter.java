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
          inflater.inflate(R.layout.myteam_task_item,null);

        }


        return convertView;
    }

    class ViewHolder{
        TextView tvTaskTitle;
        TextView tvPubNameTaskItem;
        TextView tvPubTime;
        TextView tvContentTaskItem;


    }
}
