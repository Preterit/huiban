package com.feirui.feiyunbangong.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.ImagePagerActivity;
import com.feirui.feiyunbangong.entity.ItemEntity;
import com.feirui.feiyunbangong.utils.ImageLoaderUtils;
import com.feirui.feiyunbangong.view.CircleImageView;
import com.feirui.feiyunbangong.view.MyListView;
import com.feirui.feiyunbangong.view.NoScrollGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @author Administrator
 */
public class ListItemAdapter extends MyBaseAdapter<ItemEntity> {

    private Activity mContext;
    private ArrayList<ItemEntity> items;
    private OnTouchListener listener;
    private MyLongClickListener mMyLongClickListener;


    public interface MyLongClickListener {
        void onLongClick(ItemEntity itemEntity, int position);
    }

    public void setMyLongClickListener(MyLongClickListener myLongClickListener) {
        mMyLongClickListener = myLongClickListener;
    }

    public ListItemAdapter(OnTouchListener listener, Activity ctx,
                           ArrayList<ItemEntity> items) {
        this.mContext = ctx;
        this.listener = listener;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mContext.getLayoutInflater().inflate(
                    R.layout.ll_work_quan_item, null);
            holder.tv_zan = (TextView) convertView.findViewById(R.id.tv_zan);
            holder.iv_head = (CircleImageView) convertView
                    .findViewById(R.id.iv_head_work);
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name_work);
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content_work);
            holder.gridview = (NoScrollGridView) convertView
                    .findViewById(R.id.gridview_work);
            holder.iv_xinxi = (ImageView) convertView
                    .findViewById(R.id.iv_xinxi);
            holder.ll_show_hide_zan = (LinearLayout) convertView
                    .findViewById(R.id.ll_show_hide_zan);
            holder.lv_pinglun = (MyListView) convertView
                    .findViewById(R.id.lv_pinglun);// 显示所有评论信息
            holder.tv_zan_item = (TextView) convertView
                    .findViewById(R.id.tv_zan_item);// 显示赞信息；
            holder.ll_zan = (LinearLayout) convertView
                    .findViewById(R.id.ll_zan);

            holder.tv_pinglun_click = (TextView) convertView
                    .findViewById(R.id.tv_pinglun_click);// 评论字点击；
            holder.mll_quan = ((LinearLayout) convertView.findViewById(R.id.ll_item_quan));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ItemEntity itemEntity = items.get(position);

        holder.tv_name.setText(itemEntity.getTitle());
        holder.tv_content.setText(itemEntity.getContent());

        holder.ll_show_hide_zan.setTag(itemEntity);// 设置标记，根据findviewbytag找到该Linearlayout;
        holder.ll_show_hide_zan.setOnTouchListener(listener);
        // 所有赞的人信息；
        if (!TextUtils.isEmpty(itemEntity.getZan_name())) {
            holder.tv_zan_item.setText(itemEntity.getZan_name());
        }

        // 载头像：
        ImageLoader.getInstance().displayImage(itemEntity.getAvatar(),
                holder.iv_head, ImageLoaderUtils.getWholeOptions());

        if (!itemEntity.isZan()) {
            holder.tv_zan.setText("赞一下");
        } else {
            holder.tv_zan.setText("取消赞");
        }

        holder.tv_zan.setTag(position);
        holder.tv_zan.setOnTouchListener(listener);

        // 赞列表的显示与隐藏：
        if (TextUtils.isEmpty(itemEntity.getZan_name())) {
            holder.ll_zan.setVisibility(View.GONE);
        } else {
            holder.ll_zan.setVisibility(View.VISIBLE);
        }

        holder.tv_pinglun_click.setTag(position);
        holder.tv_pinglun_click.setOnTouchListener(listener);

        holder.iv_xinxi.setTag(position);
        holder.iv_xinxi.setOnTouchListener(listener);

        holder.lv_pinglun.setAdapter(new PingLunAdapter(mContext
                .getLayoutInflater(), itemEntity.getPlis()));

        // 发表的内容图片显示与隐藏：
        final ArrayList<String> imageUrls = itemEntity.getImageUrls();
        Log.d("mBeanList的值", "onSuccess: "+imageUrls.toString());
        if (imageUrls == null || imageUrls.size() == 0) {
            holder.gridview.setVisibility(View.GONE);
        } else {
            holder.gridview.setVisibility(View.VISIBLE);
            holder.gridview.setAdapter(new NoScrollGridAdapter(mContext,
                    imageUrls));
        }

        holder.mll_quan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mMyLongClickListener != null) {
                    mMyLongClickListener.onLongClick(itemEntity, position);
                }
                return true;
            }
        });

        holder.gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                imageBrower(position, imageUrls);
            }
        });

        return convertView;
    }

    /**
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
        mContext.overridePendingTransition(R.anim.aty_zoomin,
                R.anim.aty_zoomout);
    }

    /**
     * @author Administrator
     */
    class ViewHolder {
        private CircleImageView iv_head;
        private ImageView iv_xinxi;
        private TextView tv_name, tv_zan, tv_content, tv_zan_item,
                tv_pinglun_click;
        private NoScrollGridView gridview;
        private LinearLayout ll_show_hide_zan, ll_zan;
        private MyListView lv_pinglun;
        private LinearLayout mll_quan;
    }

}
