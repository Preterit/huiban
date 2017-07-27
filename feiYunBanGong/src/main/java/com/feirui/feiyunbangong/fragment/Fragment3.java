package com.feirui.feiyunbangong.fragment;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.MainActivity;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.view.PView;

/**
 * 信息
 * 聊天记录
 * @author Lesgod
 */
public class Fragment3 extends BaseFragment implements OnClickListener {
    View view;
    @SuppressWarnings("deprecation")
    protected MyLocalActivityManager mLocalActivityManager;
    private FrameLayout mBoday;
    private Bundle savedInstanceState;
    private MyIMReceiver receiver;
    private Activity activity;
    @PView(click = "onClick")
    private LinearLayout leftll, rightll;

    /**
     * 注意：注册广播接收器要在这个构造方法中；
     */
    public Fragment3(Activity activity) {
        this.activity = activity;
        registBroadCast();// 注册广播即接收器；
    }

    public Fragment3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = setContentView(inflater, R.layout.fragment_main3);
        initView();
        this.savedInstanceState = savedInstanceState;
        return view;
    }

    private void registBroadCast() {
        receiver = new MyIMReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ON_LOGIN_ALI_SUCCESS);
        activity.registerReceiver(receiver, filter);
    }

    @SuppressWarnings("deprecation")
    private void open(Bundle savedInstanceState) {

        mLocalActivityManager = new MyLocalActivityManager(getActivity(), true);

        mLocalActivityManager.dispatchCreate(savedInstanceState);
        Log.e("TAG", "oncreate.....................");
        // 打开会话列表：
        Intent intent = AppStore.mIMKit.getConversationActivityIntent();
        intent.setAction("android.settings.SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        View v = mLocalActivityManager.startActivity("one", intent)
                .getDecorView();
        mBoday.removeAllViews();
        mBoday.addView(v);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPause() {
        if (mLocalActivityManager != null) {
            mLocalActivityManager.dispatchPause(true);
        }
        super.onPause();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onResume() {
        if (mLocalActivityManager != null) {
            mLocalActivityManager.dispatchResume();
        }
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.e("TAG", "onStart()");
        super.onStart();
    }

    private void initView() {
        mBoday = (FrameLayout) view.findViewById(R.id.frame);
    }

    class MyIMReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ON_LOGIN_ALI_SUCCESS)) {
                open(savedInstanceState);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    public class MyLocalActivityManager extends LocalActivityManager {

        @SuppressWarnings("deprecation")
        public MyLocalActivityManager(Activity parent, boolean singleMode) {
            super(parent, singleMode);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftll: // 个人信息侧滑
                ((MainActivity) getActivity()).openLeft();
                break;
            case R.id.rightll: // 消息侧滑
                ((MainActivity) getActivity()).openRight();
                break;
        }
    }

}
