package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.view.CircleImageView2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by feirui1 on 2017-07-04.
 */

public class MyShenPiAdapter  extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, Object>> data;
    private MyShenPiAdapter.OnChakanClickListener mOnChakanClickListener;

    public interface OnChakanClickListener {
        void onChakanClick(HashMap<String, Object> data, int position);
    }

    public MyShenPiAdapter.OnChakanClickListener getOnChakanClickListener() {
        return mOnChakanClickListener;
    }

    public void setOnChakanClickListener(MyShenPiAdapter.OnChakanClickListener onChakanClickListener) {
        mOnChakanClickListener = onChakanClickListener;
    }

    public MyShenPiAdapter(Context context, ArrayList<HashMap<String, Object>> map) {
        this.context = context;
        this.data = map;
    }

    @Override
    public int getCount() {

        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final MyShenPiAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_ry_send_mess,
                    null);

            holder = new MyShenPiAdapter.ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (MyShenPiAdapter.ViewHolder) convertView.getTag();

        }

        if (!TextUtils.isEmpty(data.get(position).get("staff_head").toString())){
            Glide.with(context).load(data.get(position).get("staff_head"))
                    .into(holder.mItem_send_head);
        }
        if (!TextUtils.isEmpty(data.get(position).get("staff_name").toString())){
            holder.mItem_send_name.setText(data.get(position).get("staff_name") + "的申请");
        }
        if (!TextUtils.isEmpty(data.get(position).get("approval_type").toString())){
            holder.mItem_send_type.setText("类型：" + data.get(position).get("approval_type"));
        }

        holder.mItem_send_state.setVisibility(View.VISIBLE);
        switch ((int)data.get(position).get("status")){
            case 0:
                holder.mItem_send_state.setText("");
                break;
            case 1:
                holder.mItem_send_state.setText("通过");
                holder.mItem_send_state.setTextColor(Color.parseColor("#ff8736"));
                holder.mItem_send_state.setBackgroundResource(R.drawable.shape_item_tag);
                break;
            case 2:
                holder.mItem_send_state.setText("审核中");
                break;
            case 3:
                holder.mItem_send_state.setText("未通过");
                holder.mItem_send_state.setTextColor(Color.parseColor("#3686ff"));
                holder.mItem_send_state.setBackgroundResource(R.drawable.shape_person_tag);
                break;

        }

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChakanClickListener != null){
                    mOnChakanClickListener.onChakanClick(data.get(position),position);
                }
            }
        });

        return convertView;
    }

    public void add(ArrayList<HashMap<String, Object>> map) {
        data.addAll(map);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<HashMap<String, Object>> map) {
        Log.e("操作记录-我审批的adapter", "map: "+map.toString() );
        data.clear();
        data.addAll(map);
        notifyDataSetChanged();
    }


    class ViewHolder {
        private CircleImageView2 mItem_send_head;
        private TextView mItem_send_name;
        private TextView mItem_send_type,mItem_send_state;
        private TextView mItem_send_starttime;
        private TextView mItem_send_time;
        private LinearLayout item_layout;

        public ViewHolder(View itemView) {
            item_layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
            mItem_send_head = (CircleImageView2) itemView.findViewById(R.id.item_send_head);
            mItem_send_name = (TextView) itemView.findViewById(R.id.item_send_name);
            mItem_send_type = (TextView) itemView.findViewById(R.id.item_send_type);
//            mItem_send_starttime = (TextView) itemView.findViewById(R.id.item_send_starttime);
            mItem_send_time = (TextView) itemView.findViewById(R.id.item_send_time);
            mItem_send_state = itemView.findViewById(R.id.item_send_state);
        }
    }

}
