package com.feirui.feiyunbangong.activity.tribe;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.kit.common.YWAsyncBaseAdapter;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.state.AppStore;

/**
 * Created by xy on 2017/5/10.
 * 群组的适配器
 */

public class TribeAdapterSample extends YWAsyncBaseAdapter {
    private static final String TAG = "TribeAdapterSample";
    private Context context;
    private int max_visible_item_count;
    private LayoutInflater inflater;
    private YWContactHeadLoadHelper mContactHeadLoadHelper;
    private TribeAndRoomList mList;

    public TribeAdapterSample(Activity context, TribeAndRoomList list) {
        this.context = context;
        this.mList = list;
        mContactHeadLoadHelper = new YWContactHeadLoadHelper(context, this,
               AppStore.mIMKit.getUserContext());
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    private class ViewHolder {
        TextView titleView;
        ImageView headView;
        TextView name;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= 0 && position < mList.size()) {
            return mList.getItem(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void loadAsyncTask() {
        mContactHeadLoadHelper.setMaxVisible(max_visible_item_count);
        mContactHeadLoadHelper.loadAyncHead();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tribe_item, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.group_title);
            holder.headView = (ImageView) convertView.findViewById(R.id.head);
            holder.name = (TextView) convertView
                    .findViewById(R.id.select_name);
            convertView.setTag(holder);

            holder.headView.setImageResource(R.drawable.aliwx_tribe_head_default);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.headView.setImageResource(R.drawable.aliwx_tribe_head_default);
        }
        if (mList != null) {
            if (position == 0 && mList.getTribeList().size() > 0) {
                holder.titleView.setVisibility(View.VISIBLE);
                holder.titleView.setText("群");
            } else if (position == mList.getTribeList().size()) {
                holder.titleView.setVisibility(View.VISIBLE);
                holder.titleView.setText("讨论组");
            } else {
                holder.titleView.setVisibility(View.GONE);
            }
            YWTribe tribe = (YWTribe) mList.getItem(position);
            if (tribe != null) {
                String name = tribe.getTribeName();
                holder.headView.setTag(R.id.head, position);
                if (tribe.getTribeType() == YWTribeType.CHATTING_TRIBE) {
                    mContactHeadLoadHelper.setTribeDefaultHeadView(holder.headView); //不用网络中获取到的图片，全部本地默认。
                } else {
                    mContactHeadLoadHelper.setRoomDefaultHeadView(holder.headView);
                }
                holder.name.setVisibility(View.VISIBLE);
                holder.name.setText(name);
            }
        }
        return convertView;
    }

    public void setMax_visible_item_count(int max_visible_item_count) {
        this.max_visible_item_count = max_visible_item_count;
    }

}
