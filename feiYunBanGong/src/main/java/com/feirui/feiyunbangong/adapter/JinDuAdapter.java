package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JinDuBean;

import java.util.List;

/**
 * Created by szh on 2017/10/27.
 */

public class JinDuAdapter extends BaseAdapter {
    private Context context;
    private List<JinDuBean.Info1Bean> list;
    private LayoutInflater inflater;

    public JinDuAdapter(Context context, List<JinDuBean.Info1Bean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TimeLineHolder viewHolder = null;
        if (convertView == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_jindu, null);
            viewHolder = new TimeLineHolder();

            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TimeLineHolder) convertView.getTag();
        }

        viewHolder.title.setText(list.get(position).getMessage());
        viewHolder.time.setText(list.get(position).getTime());
        return convertView;


    }

    static class TimeLineHolder {
        private TextView title, time;
    }

}
