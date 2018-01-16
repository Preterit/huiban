package com.feirui.feiyunbangong.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.ContactMember;
import com.feirui.feiyunbangong.view.TextImageView;

import java.util.ArrayList;

/**
 * 通讯录好友adapter
 */

public class TongXunLuAdapter extends BaseAdapter implements Checkable{
    private Context context;
    private ArrayList<ContactMember> strs = new ArrayList<>();

    // type 1为添加好友，2为短信邀请
    public TongXunLuAdapter(Context context, ArrayList<ContactMember> strs) {
        this.context = context;
        this.strs = strs;
    }

    @Override
    public int getCount() {
        return strs.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.ll_lianxiren_item, null);
            holder = new ViewHolder();
            holder.iv = convertView.findViewById(R.id.iv_shenpiren);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_phone = convertView.findViewById(R.id.tv_phone);
            holder.bt = convertView.findViewById(R.id.bt_tianjia);
            holder.bt.setVisibility(View.GONE);
            holder.ll_txl = convertView.findViewById(R.id.ll_txl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 解决异步加载图片造成listview滑动过程中的混乱；
        ContactMember spr = strs.get(position);
        holder.iv.setText(spr.getContact_name());
        holder.tv_name.setText(spr.getContact_name());
        holder.tv_phone.setText(spr.getContact_phone());
        Log.e("通讯录联系人", "name: " + spr.getContact_name());

    //        for (int i = 0; i < strs.size(); i++) {
    //            if (strs.get(i) == strs.get(position) ){
    //                holder.ll_txl.setBackgroundColor(Color.YELLOW);
    //            }
    //        }
        return convertView;
    }

    @Override
    public void setChecked(boolean b) {

    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void toggle() {

    }

    @SuppressLint("NewApi")
    public void updateBackground(int position, View view) {
        int backgroundId;
        ContactMember spr = strs.get(position);
        if (isChecked()) {
            backgroundId = R.drawable.list_selected_holo_light;
        } else {
            backgroundId = R.drawable.conversation_item_background_read;
        }
        Drawable background = context.getResources().getDrawable(backgroundId);
        view.setBackground(background);
    }




    class ViewHolder {
        LinearLayout ll_txl;
        TextImageView iv;
        TextView tv_name, tv_phone;
        Button bt;
    }


}
