package com.feirui.feiyunbangong.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.TeamList_entity.Infor;

import java.util.List;

public class TeamListAdapter extends BaseAdapter {

    private Context context;
    private List<Infor> teaminfo;
    private LayoutInflater inflater;
    public TeamListAdapter(Context context ,List<Infor> teaminfo){
        this.context=context;
        this.teaminfo=teaminfo;
        this.inflater=LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return teaminfo.size();
    }

    @Override
    public Infor getItem(int position) {
        return teaminfo.get(position);
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
            convertView=inflater.inflate(R.layout.teamlist_item,null);
            holder.tvTeamId=(TextView)convertView.findViewById(R.id.teamId);
            holder.tvTeamName=(TextView)convertView.findViewById(R.id.teamName);
            holder.ivTeamImage=(ImageView)convertView.findViewById(R.id.team_pic);
            convertView.setTag(holder);
        }
       holder=(ViewHolder) convertView.getTag();
        Infor team = getItem(position);
        holder.tvTeamId.setText(team.getTeam_id()+"");
        holder.tvTeamName.setText(team.getTeam_name());

        return convertView;
    }

    class ViewHolder{

        TextView tvTeamId;
        TextView tvTeamName;
        ImageView ivTeamImage;


    }
}
