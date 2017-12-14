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
 * Created by feirui1 on 2017-12-13.
 */

public class ChaoSongAdapter  extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, Object>> data;
    private ChaoSongAdapter.OnChakanClickListener mOnChakanClickListener;


    public interface OnChakanClickListener {
        void onChakanClick(HashMap<String, Object> data, int position);
    }

    public ChaoSongAdapter.OnChakanClickListener getOnChakanClickListener() {
        return mOnChakanClickListener;
    }

    public void setOnChakanClickListener(ChaoSongAdapter.OnChakanClickListener onChakanClickListener) {
        mOnChakanClickListener = onChakanClickListener;
    }

    public ChaoSongAdapter(Context context, ArrayList<HashMap<String, Object>> map) {
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

        final ChaoSongAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.ll_item_ti,
                    null);

            holder = new ChaoSongAdapter.ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ChaoSongAdapter.ViewHolder) convertView.getTag();

        }

//		holder.tv_shenpi.setText((String) data.get(position)
//				.get("appname"));
        holder.tv_leixing.setText((String) data.get(position)
                .get("approval_type"));
        switch ((int)data.get(position).get("status")) {
            case 0:
                holder.tv_pass.setText("待审批");
                break;
            case 1:
                holder.tv_pass.setText("通过");
                break;
            case 2:
                holder.tv_pass.setText("审核中");
                break;
            case 3:
                holder.tv_pass.setText("未通过");
                break;
        }
        //holder.tv_pass.setText(data.get(position).get("status")+"");
//		holder.tv_shenpiren.setText("审批");

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
        TextView tv_name,tv_pass, tv_shenpi, tv_leixing,tv_shenpiren;// 姓名，部门，类型
        Button bt_detail;

        public ViewHolder(View convertView) {

            tv_leixing = (TextView) convertView.findViewById(R.id.tv_leixing);
            tv_pass = (TextView) convertView.findViewById(R.id.tv_pass);
//			tv_shenpi = (TextView) convertView.findViewById(R.id.tv_shenpi);
//			tv_shenpiren = (TextView) convertView.findViewById(R.id.tv_shenpiren);
            bt_detail = (Button) convertView.findViewById(R.id.bt_detail);
        }
    }
}
