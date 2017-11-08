package com.feirui.feiyunbangong.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.feirui.feiyunbangong.R;


public class AddWordPopupWindow extends PopupWindow {
    private EditText mWord_add;
    private Button mBtn_add_word;
    private View mView;

    public AddWordPopupWindow(Activity context, View.OnClickListener itemOnClick){
        super(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.activity_add_word_popup_window,null);

        mWord_add = (EditText)mView.findViewById(R.id.word_add);
        mBtn_add_word = (Button) mView.findViewById(R.id.btn_add_word);

        //动态跟随键盘输入的监听方式
        mWord_add.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mWord_add.getText().toString() != null){
                    mBtn_add_word.setTag(mWord_add.getText().toString());
                }
            }
        });

        mBtn_add_word.setOnClickListener(itemOnClick);

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

                int height = mView.findViewById(R.id.ll_add_word).getTop(); //包裹内容的布局
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
