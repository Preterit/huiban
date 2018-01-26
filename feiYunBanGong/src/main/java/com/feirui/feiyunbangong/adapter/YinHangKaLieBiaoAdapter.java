package com.feirui.feiyunbangong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.ShowCardbeen;

import java.util.List;

/**
 * Created by 志恒 on 2018/1/22.
 */

public class YinHangKaLieBiaoAdapter extends BaseAdapter{

    Context context;
    List<ShowCardbeen.InfoBean> cardinfo;
    LayoutInflater inflater;
    public  YinHangKaLieBiaoAdapter(Context context,List<ShowCardbeen.InfoBean> cardinfo){
          this.context=context;
          this.cardinfo=cardinfo;
          this.inflater=LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return cardinfo.size();
    }

    @Override
    public ShowCardbeen.InfoBean getItem(int i) {
        return cardinfo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

         final ViewHolder holder;
        if(view==null){
            holder=new ViewHolder();
            view=inflater.inflate(R.layout.yinhangkaliebiao_layout,viewGroup,false);
            holder.cardlayout=(RelativeLayout)view.findViewById(R.id.Card_layout);
            holder.tvyinhangName=(TextView)view.findViewById(R.id.yinhangName);
            holder.tvyinhangcard=(TextView)view.findViewById(R.id.yinhangCard);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
            ShowCardbeen.InfoBean info = getItem(i);
            holder.tvyinhangName.setText(info.getBank_type());
            holder.tvyinhangcard.setText(fenge(info.getBank_number()));

           if(i==0){
            holder.cardlayout.setBackgroundResource(R.drawable.cardbackground1);
           }else if(i==1){
               holder.cardlayout.setBackgroundResource(R.drawable.cardbackground2);
           }else if(i==2){
               holder.cardlayout.setBackgroundResource(R.drawable.cardbackground3);
           }else if(i==3){
               holder.cardlayout.setBackgroundResource(R.drawable.cardbackground4);
           }else if(i==4){
               holder.cardlayout.setBackgroundResource(R.drawable.cardbackground5);
           }else if(i==5){
               holder.cardlayout.setBackgroundResource(R.drawable.cardbackground6);
           }else if(i==6){
               holder.cardlayout.setBackgroundResource(R.drawable.cardbackground7);
           }else{
               holder.cardlayout.setBackgroundResource(R.drawable.cardbackground1);
           }


        return view;
    }



     class ViewHolder{

        RelativeLayout cardlayout;
        TextView tvyinhangName;
        TextView tvyinhangcard;


    }

 public String fenge(String str){

     str=str.replaceAll("\\d{4}(?!$)", "$0 ");

     return str;
 }


}
