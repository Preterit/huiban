package com.feirui.feiyunbangong.activity.tribe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.fundamental.widget.WXNetworkImageView;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.tribe.YWTribeCreationParam;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.BaseActivity;
import com.feirui.feiyunbangong.state.AppStore;

import java.util.ArrayList;
import java.util.List;


public class EditTribeInfoActivity extends BaseActivity {

    private YWIMKit mIMKit;
    private IYWTribeService mTribeService;
    private String mTribeOp;
    private long mTribeId;
    private String mTribeType;

    private EditText mTribeName;
    private EditText mTribeNotice;

    private String oldTribeName;
    private String oldTribeNotice;
    private ModifyTribeInfoCallback callback;

    private Button mChuangjian;  //创建群的按钮

    public ImageView leftIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tribe_info);
        Log.d("tag","_____---------");
        init();
    }

    private void init() {

        mChuangjian = (Button)findViewById(R.id.bt_add_qun);
        mChuangjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    createTribe(YWTribeType.CHATTING_TRIBE);
            }
        });

        mIMKit = AppStore.mIMKit;
        mTribeService = mIMKit.getTribeService();  //获取群管理器

//        mTribeOp = getIntent().getStringExtra(TribeConstants.TRIBE_OP);
//        Log.d("tag","群类型-------"+mTribeOp);
//        if (mTribeOp.equals(TribeConstants.TRIBE_CREATE)){  //创建群
//            mTribeType = getIntent().getStringExtra(TribeConstants.TRIBE_TYPE);  //群类型
//        }
//        else if(mTribeOp.equals(TribeConstants.TRIBE_EDIT)){ //编辑群信息
//            mTribeId = getIntent().getLongExtra(TribeConstants.TRIBE_ID, 0);   //群ID
//        }

        WXNetworkImageView headView = (WXNetworkImageView) findViewById(R.id.head);

        mTribeName = (EditText) findViewById(R.id.tribe_name);

//        mTribeNotice = (EditText) findViewById(R.id.tribe_description);

        //群接口
        YWTribe tribe = mTribeService.getTribe(mTribeId);
//        if (tribe != null) {
//            oldTribeName = tribe.getTribeName();  //群名称
//
//            oldTribeNotice = tribe.getTribeNotice(); //群公告
//
//            mTribeName.setText(tribe.getTribeName());
//
//            mTribeNotice.setText(tribe.getTribeNotice());
//        }
        initUI();

    }


        private void initUI() {
            initTitle();
            setLeftDrawable(R.drawable.arrows_left);
            setCenterString("创建群组");
            setRightVisibility(false);
//        RelativeLayout titleBar = (RelativeLayout) findViewById(R.id.title_bar);
//        titleBar.setBackgroundColor(Color.parseColor("#00b4ff"));
//        titleBar.setVisibility(View.VISIBLE);
//
//        TextView titleView = (TextView) findViewById(R.id.title_self_title);
//        TextView leftButton = (TextView) findViewById(R.id.left_button);
//        leftButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.demo_common_back_btn_white, 0, 0, 0);
//        leftButton.setTextColor(Color.WHITE);
//        leftButton.setText("返回");
//        leftButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        titleView.setTextColor(Color.WHITE);
//        if (TextUtils.isEmpty(mTribeType)){
//            titleView.setText("编辑群信息");
//        } else if (mTribeType.equals(YWTribeType.CHATTING_GROUP.toString())) {
//            titleView.setText("创建讨论组");
//        } else if (mTribeType.equals(YWTribeType.CHATTING_TRIBE.toString())) {
//            titleView.setText("创建群组");
//        }
//
//        TextView rightButton = (TextView) findViewById(R.id.right_button);
//        rightButton.setText("提交");
//        rightButton.setTextColor(Color.WHITE);
//        rightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mTribeOp.equals(TribeConstants.TRIBE_EDIT)){
//                    updateTribeInfo();
//                } else if (mTribeType.equals(YWTribeType.CHATTING_GROUP.toString())) {
//                    createTribe(YWTribeType.CHATTING_GROUP);
//                } else if (mTribeType.equals(YWTribeType.CHATTING_TRIBE.toString())) {
//                    createTribe(YWTribeType.CHATTING_TRIBE);
//                }
//            }
//        });
    }


    /**
     * 用于更新群信息
     */
    private void updateTribeInfo() {
        String name = mTribeName.getText().toString();
        String notice = mTribeNotice.getText().toString();
        if(name.equals(oldTribeName) && notice.equals(oldTribeNotice)) {
            //没有修改,不提交服务端
            finish();
            return;
        }
        if(callback == null) {
            callback = new ModifyTribeInfoCallback();
        }
        if(name.equals(oldTribeName) && !notice.equals(oldTribeNotice)) {
            mTribeService.modifyTribeInfo(callback, mTribeId, null, notice);
        } else if(!name.equals(oldTribeName) && notice.equals(oldTribeNotice)) {
            mTribeService.modifyTribeInfo(callback, mTribeId, name, null);
        } else {
            mTribeService.modifyTribeInfo(callback, mTribeId, name, notice);
        }
    }

    /**
     * 创建群
     * @param type
     */
    private void createTribe(final YWTribeType type) {
        List<String> users = new ArrayList<String>();
        users.add(mIMKit.getIMCore().getLoginUserId());
        YWTribeCreationParam param = new YWTribeCreationParam();
        param.setTribeType(type);
        param.setTribeName(mTribeName.getText().toString());

//        param.setNotice(mTribeNotice.getText().toString());


        mTribeService.createTribe(new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {
                if (result != null && result.length > 0) {
                    YWTribe tribe = (YWTribe) result[0];
                    if (type.equals(YWTribeType.CHATTING_TRIBE)) {
                        IMNotificationUtils.getInstance().showToast(EditTribeInfoActivity.this, "创建群组成功！");
                    }
                    else {
                        IMNotificationUtils.getInstance().showToast(EditTribeInfoActivity.this, "创建讨论组成功！");
                    }
//                      跳转到群名片
                    Intent intent = new Intent(EditTribeInfoActivity.this, TribeInfoActivity.class);
                    intent.putExtra(TribeConstants.TRIBE_ID, tribe.getTribeId());
                    startActivity(intent);
                    finish();
                    Log.d("TAG", "onSuccess:---------- ");
                }
            }

            @Override
            public void onError(int code, String info) {
                IMNotificationUtils.getInstance().showToast(EditTribeInfoActivity.this, "创建讨论组失败，code = " + code + ", info = " + info);
            }

            @Override
            public void onProgress(int progress) {

            }
        }, param);
    }


    /**
     * 修改群信息后的反馈
     */

    class ModifyTribeInfoCallback implements IWxCallback {
        @Override
        public void onSuccess(Object... result) {
            IMNotificationUtils.getInstance().showToast(EditTribeInfoActivity.this, "修改群信息成功！");
            finish();
        }

        @Override
        public void onError(int code, String info) {
            IMNotificationUtils.getInstance().showToast(EditTribeInfoActivity.this, "修改群信息失败，code = " + code + ", info = " + info);
        }

        @Override
        public void onProgress(int progress) {

        }
    }

}
