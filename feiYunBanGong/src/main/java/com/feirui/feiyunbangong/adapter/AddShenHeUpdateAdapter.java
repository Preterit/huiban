package com.feirui.feiyunbangong.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.BiaoQianDialog;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.HttpUtils;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.TextImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 * 添加审批人的适配器
 */

public class AddShenHeUpdateAdapter extends MyBaseAdapter<ChildItem> {
    private ArrayList<JsonBean> list1;
    private Context context;
    private List<String> strs = new ArrayList<>();

    public AddShenHeUpdateAdapter(LayoutInflater inflater,
                                    ArrayList<JsonBean> list1, Context context) {
        super(inflater);
        this.context = context;
        this.list1 = list1;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View v, ViewGroup parent) {

        final AddShenHeUpdateAdapter.ViewHolder holder;
        if (v == null) {
            holder = new AddShenHeUpdateAdapter.ViewHolder();
            v = mInflater.inflate(R.layout.lv_item_addshenpiren, null);
            holder.tv_name = (TextView) v.findViewById(R.id.tv_name);
            holder.iv_head = (TextImageView) v.findViewById(R.id.iv_head);
           // holder.iv_jianhao = (ImageView) v.findViewById(R.id.iv_jianhao);
            holder.tv_biaoqian = (TextView) v.findViewById(R.id.tv_biaoqian);
            holder.lay_shenpi = (LinearLayout) v.findViewById(R.id.lay_shenpi);
            v.setTag(holder);

            holder.tv_biaoqian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BiaoQianDialog biaoQian = new BiaoQianDialog("选择标签", list1,
                            context, new HttpUtils.CallBack() {
                        @Override
                        public void onRequestComplete(String result) {
                            int pos = (int) holder.tv_biaoqian.getTag();
                            strs.set(pos, result);
                            holder.tv_biaoqian.setText(strs.get(pos));
                        }
                    });
                    biaoQian.show();
                }
            });

        } else {
            holder = (AddShenHeUpdateAdapter.ViewHolder) v.getTag();
        }
        final ChildItem item = (ChildItem) getItem(position);

        holder.tv_name.setText(item.getTitle());

        holder.tv_biaoqian.setTag(position);

//        if (item.getPhone().equals(
//                AppStore.user.getInfor().get(0).get("staff_mobile"))) {
//            holder.iv_jianhao.setVisibility(View.INVISIBLE);
//        } else {
//            holder.iv_jianhao.setVisibility(View.VISIBLE);
//        }

        if (!strs.get(position).equals("其他")) {
            holder.tv_biaoqian.setText(strs.get(position));
        } else {
            holder.tv_biaoqian.setText("其他");
        }

        // 图片上加字：
        String name = Utils.getPicTitle(item.getTitle());
        if (item.getMarkerImgId() == null
                || "img/1_1.png".equals(item.getMarkerImgId())
                || "http://123.57.45.74/feiybg1/public/static/staff_head/19912/53da489597afc6f5abb2a1bae0d767ff.jpeg".equals(item.getMarkerImgId())
                || item.getMarkerImgId().equals("")) {
            holder.iv_head.setText(name);
        } else {
            ImageLoader.getInstance().displayImage(item.getMarkerImgId(),
                    holder.iv_head);
        }

        holder.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduce(item);
            }
        });

        return v;
    }


    public void reduce(ChildItem item) {
        if (list.contains(item)) {
            list.remove(item);
            notifyDataSetChanged();
        }
    }

    class ViewHolder {
        TextView tv_name, tv_biaoqian;
        TextImageView iv_head;
        //ImageView iv_jianhao;
        LinearLayout lay_shenpi;
    }

    public List<ChildItem> getList() {
        return list;
    }

    // 查重并累加：
    public void addList(List<ChildItem> items) {

        for (int i = 0; i < items.size(); i++) {
            boolean isHave = false;

            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getId().equals(items.get(i).getId())) {
                    isHave = true;
                    break;
                }
            }
            if (!isHave) {
                list.add(items.get(i));
            }
        }

        strs.removeAll(strs);
        for (int i = 0; i < list.size(); i++) {
            strs.add("其他");
        }

        notifyDataSetChanged();
    }

    public List<String> getTags() {

        return strs;
    }


}
