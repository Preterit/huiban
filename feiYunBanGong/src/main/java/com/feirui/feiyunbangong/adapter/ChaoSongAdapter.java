package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.view.CircleImageView2;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by feirui1 on 2017-12-13.
 */

public class ChaoSongAdapter  extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, Object>> mData;
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
        this.mData = map;
    }

    @Override
    public int getCount() {

        return mData.size();
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
            convertView = View.inflate(context, R.layout.item_ry_send_mess,
                    null);

            holder = new ChaoSongAdapter.ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ChaoSongAdapter.ViewHolder) convertView.getTag();

        }
        final HashMap<String,Object> data = mData.get(position);
        Log.e("adapter", "getView:------------------ " + mData.toString() );

        if (!TextUtils.isEmpty(data.get("staff_head").toString())){
            Glide.with(context).load(data.get("staff_head"))
                    .into(holder.mItem_send_head);
        }
        if (!TextUtils.isEmpty(data.get("staff_name").toString())){
            holder.mItem_send_name.setText(data.get("staff_name") + "的申请");
        }
        if (!TextUtils.isEmpty(data.get("approval_type").toString())){
            holder.mItem_send_type.setText("类型：" + data.get("approval_type"));
        }
//        if (!TextUtils.isEmpty(data.get("approval_time").toString())){
//            holder.mItem_send_type.setText("开始时间：" + data.get("approval_time"));
//        }
        if (!TextUtils.isEmpty(data.get("approval_time").toString())){
            SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressWarnings("unused")
            long lcc = Long.valueOf(data.get("approval_time").toString());
            int i = Integer.parseInt(data.get("approval_time").toString());
            String times = sdr.format(new Date(i * 1000L));
            holder.mItem_send_time.setText(times);
        }

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChakanClickListener != null){
                    mOnChakanClickListener.onChakanClick(data,position);
                }
            }
        });
        return convertView;
    }

    public void add(ArrayList<HashMap<String, Object>> map) {
        mData.addAll(map);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<HashMap<String, Object>> map) {
        mData.clear();
        mData.addAll(map);
        notifyDataSetChanged();
    }


    class ViewHolder {
        private CircleImageView2 mItem_send_head;
        private TextView mItem_send_name;
        private TextView mItem_send_type;
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
        }
    }
}
