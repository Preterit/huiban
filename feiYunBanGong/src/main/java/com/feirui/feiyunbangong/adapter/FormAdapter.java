package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
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
  private MyItemClickListener mItemClickListener;
  private List<InforBean> mBeanList;
  private Context mContext;

  public FormAdapter(List<InforBean> beanList, Context context) {
    mBeanList = beanList;
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.read_form_list_item, parent, false),mItemClickListener);
  }


  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    ImageLoader.getInstance().displayImage(mBeanList.get(position).getPic(), holder.ivHeadFormItem, ImageLoaderUtils.getSimpleOptions());

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
    try {
      mBeanList.addAll(data);
      Log.d("tag", "setData错误------------" + data.toString());
    } catch (Exception e) {
      Log.d("tag", "setData错误");
    }

    notifyDataSetChanged();
  }




  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private MyItemClickListener mListener;
    ImageView ivHeadFormItem;
    TextView tvNameFormItem;
    TextView tvTimeFormItem;
    TextView tvLabelFormItem;
    ConstraintLayout list_lookfrom_layout;

    public ViewHolder(View itemView , MyItemClickListener myItemClickListener) {
      super(itemView);
      initView(itemView);

      //将全局的监听赋值给接口
      this.mListener = myItemClickListener;
      itemView.setOnClickListener(this);
    }

    private void initView(View itemView) {
      ivHeadFormItem = (ImageView) itemView.findViewById(R.id.ivHeadFormItem);
      tvNameFormItem = (TextView) itemView.findViewById(R.id.tvNameFormItem);
      tvTimeFormItem = (TextView) itemView.findViewById(R.id.tvTimeFormItem);
      tvLabelFormItem = (TextView) itemView.findViewById(R.id.tvLabelFormItem);
      list_lookfrom_layout= (ConstraintLayout)itemView.findViewById(R.id.list_lookfrom_layout);
    }


    /**
     * 实现OnClickListener接口重写的方法
     * @param v
     */
    @Override
    public void onClick(View v) {
      if (mListener != null) {
        mListener.onItemClick(v, getPosition());
      }

    }
  }


  /**
   * 创建一个回调接口
   */
  public interface MyItemClickListener {
    void onItemClick(View view, int position);
  }
  /**
   * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
   *
   * @param myItemClickListener
   */
  public void setItemClickListener(MyItemClickListener myItemClickListener) {
    this.mItemClickListener = myItemClickListener;
  }
}

