package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

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
            convertView = View.inflate(context, R.layout.ll_item_daishenqi,
                    null);

            holder = new MyShenPiAdapter.ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (MyShenPiAdapter.ViewHolder) convertView.getTag();

        }

        holder.tv_name.setText((String)
                data.get(position).get("staff_name"));

        holder.tv_shenhe.setText((String) data.get(position).get("status"));

        holder.tv_leixing.setText((String) data.get(position)
                .get("approval_type"));
//        holder.bt_detail = (Button) convertView
//                .findViewById(R.id.bt_detail);

        holder.bt_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChakanClickListener != null) {
                    mOnChakanClickListener.onChakanClick(data.get(position), position);
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
        data.clear();
        data.addAll(map);
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView tv_name, tv_shenhe, tv_leixing;// 姓名，类型
        Button bt_detail;

        public ViewHolder(View convertView) {

            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_shenhe = (TextView) convertView.findViewById(R.id.tv_shenhe);
            tv_leixing = (TextView) convertView.findViewById(R.id.tv_leixing);
            bt_detail = (Button) convertView.findViewById(R.id.bt_detail);
        }
    }

}
