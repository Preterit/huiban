package com.feirui.feiyunbangong.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;

/**
 * Created by xy on 2017-12-25.
 */

public class TypePopupWindow extends PopupWindow {
    private View mView;
    private TextView mTv_leave,mTv_submit,mTv_out,mTv_spend,mTv_purchase,mTv_else;

    public TypePopupWindow(Activity activity, View.OnClickListener listener){
        super(activity);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.ll_type_handle,null);
        mTv_leave = (TextView) mView.findViewById(R.id.tv_leave);
        mTv_submit = (TextView) mView.findViewById(R.id.tv_submit);
        mTv_out = (TextView) mView.findViewById(R.id.tv_out);
        mTv_spend = (TextView) mView.findViewById(R.id.tv_spend);
        mTv_purchase = (TextView) mView.findViewById(R.id.tv_purchase);
        mTv_else = (TextView) mView.findViewById(R.id.tv_else);

        activity.addContentView(mView,null);

    }
}
