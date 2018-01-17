package com.feirui.feiyunbangong.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

/**
 * Created by xy on 2017-12-25.
 */

public class TypePopupWindow extends PopupWindow {
    private View mView;
    private TextView mTv_all, mTv_leave,mTv_submit,mTv_out,mTv_spend,mTv_purchase,mTv_else;

    public TypePopupWindow(Activity activity, View.OnClickListener listener){
        super(activity);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.ll_type_handle,null);
        mTv_all = mView.findViewById(R.id.tv_all);
        mTv_leave = mView.findViewById(R.id.tv_leave);
        mTv_submit = mView.findViewById(R.id.tv_submit);
        mTv_out = mView.findViewById(R.id.tv_out);
        mTv_spend = mView.findViewById(R.id.tv_spend);
        mTv_purchase = mView.findViewById(R.id.tv_purchase);
        mTv_else = mView.findViewById(R.id.tv_else);

        //各个类型的点击事件
        mTv_all.setOnClickListener(listener);
        mTv_leave.setOnClickListener(listener);
        mTv_submit.setOnClickListener(listener);
        mTv_out.setOnClickListener(listener);
        mTv_spend.setOnClickListener(listener);
        mTv_purchase.setOnClickListener(listener);
        mTv_else.setOnClickListener(listener);
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
        ColorDrawable dw = new ColorDrawable(0x0ffffff);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mView.findViewById(R.id.type_handle).getTop(); //包裹内容的布局
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
