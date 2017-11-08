package com.feirui.feiyunbangong.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xy on 2017-11-03.
 * 展示二维码
 */

public class PopWindow extends PopupWindow {
    private View conentView;
    private Activity context;
    private Bitmap erweima;
    private ImageView iv;
    private TuanDuiChengYuan mTdcy;

    public PopWindow(final Activity context,TuanDuiChengYuan mTdcy,Bitmap erweima){
        super(context);
        this.context = context;
        this.mTdcy = mTdcy;
        this.erweima = erweima;
        this.initPopupWindow(erweima);
    }

    private void initPopupWindow(Bitmap resource) {
        //使用view来引入布局
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.ll_dialog_erweima, null);
        //布局控件初始化
        LinearLayout ll_person = (LinearLayout) conentView.findViewById(R.id.ll_person);
        ImageView head = (ImageView) ll_person.findViewById(R.id.iv_pop_head);
        if (!"null".equals(mTdcy.getHead()) && null != mTdcy.getHead()
                && !"img/1_1.png".equals(mTdcy.getHead())) {
            ImageLoader.getInstance().displayImage(mTdcy.getHead(), head);

        } else {
            head.setImageResource(R.drawable.fragment_head);
        }
        TextView name = (TextView) ll_person.findViewById(R.id.tv_pop_name);
        name.setText(mTdcy.getName());
        TextView area = (TextView) ll_person.findViewById(R.id.tv_pop_area);
        if (!"null".equals(mTdcy.getAddress()) && !"".equals(mTdcy.getAddress())){
            area.setText(mTdcy.getAddress());
        }else {
            area.setText("北京 朝阳");
        }

        RelativeLayout ll_pop = (RelativeLayout) conentView
                .findViewById(R.id.ll_pop);
        iv = (ImageView) ll_pop.findViewById(R.id.iv_erweima2);
        //设置二维码
        if (erweima != null) {
            iv.setImageBitmap(erweima);
        }
        RelativeLayout rl_tv = (RelativeLayout) conentView.findViewById(R.id.rl_tv);

        //获取popupwindow的高度与宽度
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth( (w * 3)/ 4 );
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(h /2 + 100);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setBackgroundDrawable(new PaintDrawable());

    }

    public void showPopupWindow(){
        this.showAtLocation(iv, Gravity.CENTER,0,0);
    }

}
