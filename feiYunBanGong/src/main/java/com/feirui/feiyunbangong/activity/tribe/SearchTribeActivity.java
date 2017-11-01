package com.feirui.feiyunbangong.activity.tribe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.BaseActivity;
import com.feirui.feiyunbangong.adapter.TuanDuiAdapter;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.state.AppStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xy on 2017-09-20.
 */

public class SearchTribeActivity extends BaseActivity implements
        View.OnKeyListener{

    private EditText et_sousuolianxiren;
    private ListView lv_tuandui;
    private TuanDuiAdapter adapter;
    List<TuanDui> tds = new ArrayList<>();
    private YWIMKit mIMKit;
    private IYWTribeService mTribeService;
    private YWTribe mTribe;
    private long mTribeId;


    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_qun);
        initView();
        setListener();
        setListView();
    }

    private void setListView() {
        lv_tuandui.setAdapter(adapter);
    }

    private void setListener() {
        et_sousuolianxiren.setOnKeyListener(this);
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("搜索群");
        setRightVisibility(false);
        et_sousuolianxiren = (EditText) findViewById(R.id.et_sousuolianxiren);
        lv_tuandui = (ListView) findViewById(R.id.lv_tuandui);
        adapter = new TuanDuiAdapter(getLayoutInflater());
        mIMKit = AppStore.mIMKit;
        mTribeService = mIMKit.getTribeService();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.e("TAG", keyCode + "");
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.e("TAG", keyCode + "");
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.e("TAG", "keydown");
                if (TextUtils.isEmpty(et_sousuolianxiren.getText().toString()
                        .trim())) {
                    Toast.makeText(this, "请输入搜索内容！", 0).show();
                    return false;
                }
                search(et_sousuolianxiren.getText().toString());
            }
        }
        return false;
    }

    private void search(String id) {

        try {
            mTribeId = Long.valueOf(id);
        } catch (Exception e) {
            IMNotificationUtils.getInstance().showToast(SearchTribeActivity.this, "请输入群id");
        }
        mTribe = mTribeService.getTribe(mTribeId);
        if (mTribe == null || mTribe.getTribeRole() == null) {
            mTribeService.getTribeFromServer(new IWxCallback() {
                @Override
                public void onSuccess(Object... result) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            startTribeInfoActivity(TribeConstants.TRIBE_JOIN);
                        }
                    });
                }

                @Override
                public void onError(int code, String info) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            IMNotificationUtils.getInstance().showToast(SearchTribeActivity.this, "没有搜索到该群，请确认群id是否正确！");
                        }
                    });
                }

                @Override
                public void onProgress(int progress) {

                }
            }, mTribeId);
        } else {
            startTribeInfoActivity(null);
        }

    }

    private void startTribeInfoActivity(String tribeOp) {
        Intent intent = new Intent(this, TribeInfoActivity.class);
        intent.putExtra(TribeConstants.TRIBE_ID, mTribeId);
        if (!TextUtils.isEmpty(tribeOp)) {
            intent.putExtra(TribeConstants.TRIBE_OP, tribeOp);
        }
        startActivity(intent);
    }

}
