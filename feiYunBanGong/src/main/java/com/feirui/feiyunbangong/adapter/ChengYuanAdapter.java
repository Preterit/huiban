package com.feirui.feiyunbangong.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ChengYuanAdapter extends MyBaseAdapter<TuanDuiChengYuan> {

    public ChengYuanAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public ChengYuanAdapter() {
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder = null;
        if (v == null) {
            v = mInflater.inflate(R.layout.lv_item_chengyuan, null);
            holder = new ViewHolder();
            holder.tv_guanliyuan = (TextView) v.findViewById(R.id.tv_guanliyuan);
            holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
            holder.iv_head = (TextImageView) v.findViewById(R.id.iv_head);
            holder.tv_biaoqian2 = (TextView) v.findViewById(R.id.tv_biaoqian2);
            holder.tv_tag = (TextView) v.findViewById(R.id.tv_biaoqian);
            holder.iv_new_member = (ImageView) v.findViewById(R.id.iv_new_member);
            v.setTag(holder);
        } else {
            //holder.iv_head.setText(name);
            holder = (ViewHolder) v.getTag();
        }

        TuanDuiChengYuan tdcy = (TuanDuiChengYuan) getItem(position);
        String name = Utils.getPicTitle(tdcy.getName());//获得该行人员的姓名

        if (!TextUtils.isEmpty(tdcy.getT_remark())) {
            holder.tv_name.setText(tdcy.getT_remark());
        } else if (!TextUtils.isEmpty(tdcy.getRemark())) {
            holder.tv_name.setText(tdcy.getRemark());
        } else {
            holder.tv_name.setText(tdcy.getName());
        }

        if (tdcy.getState() == 1) {
            holder.iv_new_member.setVisibility(View.VISIBLE);
        } else {
            holder.iv_new_member.setVisibility(View.INVISIBLE);
        }

          Log.e("团队成员页面", "tdcy.getTag()" + tdcy);
//        Log.e("团队成员页面", "name: " + name);
//        Log.e("团队成员页面", "tdcy.getHead(): " + tdcy.getHead());
        //关键词判断
        if (!"".equals(tdcy.getKey1()) && !"null".equals(tdcy.getKey1())) {
            holder.tv_tag.setVisibility(View.VISIBLE);
            holder.tv_tag.setText(tdcy.getKey1());
        } else {
            holder.tv_tag.setVisibility(View.GONE);
        }

        if (!"".equals(tdcy.getKey2()) && !"null".equals(tdcy.getKey2())) {
            holder.tv_biaoqian2.setVisibility(View.VISIBLE);
            holder.tv_biaoqian2.setText(tdcy.getKey2());
        } else {
            holder.tv_biaoqian2.setVisibility(View.GONE);
        }

        if ("团长".equals(tdcy.getType()) || "副团长".equals(tdcy.getType())) {
            holder.tv_guanliyuan.setVisibility(View.VISIBLE);
            holder.tv_guanliyuan.setText(tdcy.getType());
            holder.tv_biaoqian2.setVisibility(View.GONE);
        } else {
            holder.tv_guanliyuan.setVisibility(View.GONE);
        }

        if (tdcy.getHead() == null
                || "http://123.57.45.74/feiybg1/public/static/staff_head/19912/53da489597afc6f5abb2a1bae0d767ff.jpeg".equals(tdcy.getHead())
                || "http://123.57.45.74/feiybg1/public/static/staff_head/379/8fe02470a4eb202cf7643a86f31fa39f.jpeg".equals(tdcy.getHead())
                || "".equals(tdcy.getHead())) {
            //name = Utils.getPicTitle(tdcy.getName());
            holder.iv_head.setText(name);

        } else {
            ImageLoader.getInstance().displayImage(tdcy.getHead(), holder.iv_head);
        }

        return v;
    }

    public List<TuanDuiChengYuan> getList() {
        return list;
    }

    public void addList(List<TuanDuiChengYuan> items) {
        for (int i = 0; i < items.size(); i++) {

            boolean isHave = false;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getCId().equals(items.get(i).getCId())) {
                    isHave = true;
                    break;
                }
            }
            if (!isHave) {
                list.add(items.get(i));
            }
        }
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tv_name, tv_guanliyuan, tv_tag,tv_biaoqian2;
        TextImageView iv_head;
        ImageView iv_new_member;
    }

}
