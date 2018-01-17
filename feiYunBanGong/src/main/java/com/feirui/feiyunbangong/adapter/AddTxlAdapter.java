package com.feirui.feiyunbangong.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.LianXiRen;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lice on 2018/1/11.
 */

public class AddTxlAdapter extends RecyclerView.Adapter<AddTxlAdapter.ViewHolder>{
    private ArrayList<LianXiRen> mList;
    private DisplayImageOptions mDisplayImageOptions;
    private List<String> strs = new ArrayList<>();
    public AddTxlAdapter(ArrayList<LianXiRen> list){
        mList=list;
        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.fragment_head) //
                .showImageOnFail(R.drawable.fragment_head) //
                .cacheInMemory(true)  //加载图片时会在内存中加载缓存
                .cacheOnDisc(true)   //加载图片时会在磁盘中加载缓存
                .displayer(new RoundedBitmapDisplayer(20))  //设置用户加载图片task(这里是圆角图片显示)
                .build();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new AddTxlAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shenpiren_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHead;
        TextView name;


        public ViewHolder(View itemView) {
            super(itemView);
            ivHead = (ImageView) itemView.findViewById(R.id.iv_shenpi_item);
            name = (TextView) itemView.findViewById(R.id.tv_shenpi_item);

            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    removeAt(getAdapterPosition());
                }
            });
        }
    }
}
