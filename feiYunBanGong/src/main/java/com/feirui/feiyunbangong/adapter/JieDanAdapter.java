package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JieDanRenBean;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by szh on 2017/12/5.
 */

public class JieDanAdapter extends BaseAdapter {
    private List<JieDanRenBean.InforBean> acctpt_list;
    private LayoutInflater inflater;

    public JieDanAdapter(List<JieDanRenBean.InforBean> acctpt_list, Context context) {
        this.acctpt_list = acctpt_list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return acctpt_list.size();
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
        View view=inflater.inflate(R.layout.item_jiedanren,null);
        ViewHolder holder=new ViewHolder();
        holder.iv_head= (ImageView) view.findViewById(R.id.iv_head);
        holder.tv_name= (TextView) view.findViewById(R.id.tv_name);
        holder.tv_chakan= (TextView) view.findViewById(R.id.tv_chakan);
        ImageLoader.getInstance().displayImage(acctpt_list.get(position).getStaff_head(), holder.iv_head, ImageLoaderUtils.getSimpleOptions());
        holder.tv_name.setText(acctpt_list.get(position).getStaff_name());
        if (acctpt_list.get(position).getState().equals("2")){
            holder.tv_chakan.setTextColor(R.color.red);
        }else {
            holder.tv_chakan.setTextColor(R.color.lanse);
        }
        return view;

    }
    class ViewHolder{
        ImageView iv_head;
        TextView tv_name,tv_chakan;
    }
}
