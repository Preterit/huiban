package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.FormAdapter.ViewHolder;
import com.feirui.feiyunbangong.entity.ReadFormEntity.InforBean;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.feirui.feiyunbangong.R.color.d6a5f4;
import static com.feirui.feiyunbangong.R.color.ff6f5c;
import static com.feirui.feiyunbangong.R.color.ff99cb;
import static com.feirui.feiyunbangong.R.color.ffa44b;

/**
 * Created by rubing on 2017/3/25.
 * rubingem@163.com
 */

public class FormAdapter extends Adapter<ViewHolder> {

  private List<InforBean> mBeanList;
  private Context mContext;

  public FormAdapter(List<InforBean> beanList, Context context) {
    mBeanList = beanList;
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.read_form_list_item, parent, false));
  }


  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    ImageLoader.getInstance().displayImage(mBeanList.get(position).getPic(), holder.ivHeadFormItem,
        ImageLoaderUtils.getSimpleOptions());

    holder.tvNameFormItem.setText(mBeanList.get(position).getName());
    holder.tvTimeFormItem.setText(mBeanList.get(position).getForm_time());
    switch (mBeanList.get(position).getType_id()) {
      case 1:
        //日报
        holder.tvLabelFormItem.setBackgroundColor(mContext.getResources().getColor(ff99cb));
        holder.tvLabelFormItem.setText("日报");
        break;
      case 2:
        holder.tvLabelFormItem.setBackgroundColor(mContext.getResources().getColor(ffa44b));
        holder.tvLabelFormItem.setText("周报");
        //周报
        break;
      case 3:
        holder.tvLabelFormItem.setBackgroundColor(mContext.getResources().getColor(d6a5f4));
        holder.tvLabelFormItem.setText("月报");
        //月报
        break;
      case 4:
        holder.tvLabelFormItem.setBackgroundColor(mContext.getResources().getColor(ff6f5c));
        holder.tvLabelFormItem.setText("业绩报表");
        //业绩报表
        break;
    }
  }

  @Override
  public int getItemCount() {
    return mBeanList.size();
  }

  public void setData(List<InforBean> data) {
    mBeanList.clear();
    //////////////报错2222222222222,第二处,添加了一个try catch
    try{mBeanList.addAll(data);}catch(Exception e){
      Log.d("tag","setData错误");
    }

    notifyDataSetChanged();
  }


  class ViewHolder extends RecyclerView.ViewHolder {

    ImageView ivHeadFormItem;
    TextView tvNameFormItem;
    TextView tvTimeFormItem;
    TextView tvLabelFormItem;

    public ViewHolder(View itemView) {
      super(itemView);
      initView(itemView);
    }

    private void initView(View itemView) {
      ivHeadFormItem = (ImageView) itemView.findViewById(R.id.ivHeadFormItem);
      tvNameFormItem = (TextView) itemView.findViewById(R.id.tvNameFormItem);
      tvTimeFormItem = (TextView) itemView.findViewById(R.id.tvTimeFormItem);
      tvLabelFormItem = (TextView) itemView.findViewById(R.id.tvLabelFormItem);
    }
  }
}
