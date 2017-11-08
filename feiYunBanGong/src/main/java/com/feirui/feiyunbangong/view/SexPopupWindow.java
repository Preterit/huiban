package com.feirui.feiyunbangong.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

/**
 * Created by xy on 2017-11-08.
 */

public class SexPopupWindow extends PopupWindow {
    private ImageView mMan_sex_pop;
    private ImageView mWo_sex_pop;
    private TextView mTv_sex_no;
    private TextView mTv_sex_ok;
    private View mView;

    public SexPopupWindow(Activity context, View.OnClickListener itemsOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.sex_popup,null); //布局
        mMan_sex_pop = (ImageView) mView.findViewById(R.id.man_sex_pop);
        mWo_sex_pop = (ImageView) mView.findViewById(R.id.wo_sex_pop);
        mTv_sex_no = (TextView) mView.findViewById(R.id.tv_sex_no);
        mTv_sex_ok = (TextView) mView.findViewById(R.id.tv_sex_ok);

        mTv_sex_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mMan_sex_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMan_sex_pop.setImageResource(R.drawable.check_ok);
                mWo_sex_pop.setImageResource(R.drawable.check_no);
                mTv_sex_ok.setTag("男");
            }
        });
        mWo_sex_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMan_sex_pop.setImageResource(R.drawable.check_no);
                mWo_sex_pop.setImageResource(R.drawable.check_ok);
                mTv_sex_ok.setTag("女");
            }
        });

        //监听按钮
        mTv_sex_ok.setOnClickListener(itemsOnClick);

        this.setContentView(mView);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mView.findViewById(R.id.re_sex).getTop(); //包裹内容的布局
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

}
