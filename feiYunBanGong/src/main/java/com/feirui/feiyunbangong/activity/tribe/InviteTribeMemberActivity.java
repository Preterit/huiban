package com.feirui.feiyunbangong.activity.tribe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.ui.contact.ContactsFragment;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.BaseActivity;
import com.feirui.feiyunbangong.adapter.AddChengYuanExpandableListAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.im.MyUserProfileSampleHelper;
import com.feirui.feiyunbangong.myinterface.AllInterface.OnGroupStateChangedListener;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2017/5/8.
 * 添加群成员
 */

public class InviteTribeMemberActivity extends BaseActivity implements
        OnClickListener, OnGroupStateChangedListener{

    private static final String TAG = "InviteTribeMemberActivity";

    private YWIMKit mIMKit;
    private IYWTribeService mTribeService;
    private long mTribeId;

    private ContactsFragment mFragment;

    private ExpandableListView expandlist;
    private AddChengYuanExpandableListAdapter adapter;
    @PView(click = "onClick")
    private Button bt_submit;
    private List<Group> groups;
    private Map<Integer, List<ChildItem>> map;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_invite_tribe_member);
        setContentView(R.layout.activity_add_cheng_yuan);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        type = intent.getStringExtra("type");

        mIMKit = AppStore.mIMKit;
        mTribeService = mIMKit.getTribeService();
        mTribeId = getIntent().getLongExtra(TribeConstants.TRIBE_ID, 0);

        initUI();
        setListView();
        setListener();
        YWLog.i(TAG, "onCreate");
    }

    private void initUI(){
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        if (type != null) {
            setCenterString("添加名片");
        } else {
            setCenterString("选择人员");
        }
        setRightVisibility(false);


//        final YWTribeType tribeType = mTribeService.getTribe(mTribeId).getTribeType();
//        TextView rightButton = (TextView) findViewById(R.id.right_button);
//        rightButton.setText("完成");
//        rightButton.setTextColor(Color.WHITE);
//        rightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ContactsAdapter adapter = mFragment.getContactsAdapter();
//                List<IYWContact> list = adapter.getSelectedList();
//                if (list != null && list.size() > 0){
//                    mTribeService.inviteMembers(mTribeId, list, new IWxCallback() {
//                        @Override
//                        public void onSuccess(Object... result) {
//                            Integer retCode = (Integer) result[0];
//                            if (retCode == 0){
//                                if (tribeType == YWTribeType.CHATTING_GROUP) {
//                                    IMNotificationUtils.getInstance().showToast(InviteTribeMemberActivity.this, "添加群成员成功！");
//                                } else {
//                                    IMNotificationUtils.getInstance().showToast(InviteTribeMemberActivity.this, "群邀请发送成功！");
//                                }
//                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void onError(int code, String info) {
//                            IMNotificationUtils.getInstance().showToast(InviteTribeMemberActivity.this, "添加群成员失败，code = " + code + ", info = " + info);
//                        }
//
//                        @Override
//                        public void onProgress(int progress) {
//
//                        }
//                    });
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                submit();
                break;
        }
    }

    // 提交：
    private void submit() {
        List<Integer> gp = adapter.getGp();
        List<Integer> cp = adapter.getCp();

        ArrayList<ChildItem> childs = new ArrayList<>();
        for (int i = 0; i < gp.size(); i++) {
            childs.add(map.get(gp.get(i)).get(cp.get(i)));
            Log.d("tag","添加的群成员-------"+childs.get(i));
        }

        if (type != null) {
            AppStore.listener.onSendCard(childs);
            finish();
            return;
        }

        final YWTribeType tribeType = mTribeService.getTribe(mTribeId).getTribeType();
        Log.d("tag","添加的群成员-------"+tribeType);
        List<IYWContact> list = new ArrayList<>();
        for (int i = 0;i < childs.size();i++){
            IYWContact iywContact = MyUserProfileSampleHelper.mUserInfo.get(childs.get(i).getPhone());
            list.add(iywContact);
            Log.d("tag","添加的群成员-------"+list.get(i)+childs.get(i).getPhone());
        }

        mTribeService.inviteMembers(mTribeId, list, new IWxCallback() {
            @Override
            public void onSuccess(Object... objects) {
                Integer retCode = (Integer)objects[0];
                Log.d("tag","添加的群成员-------");
                if (retCode == 0){
                    if (tribeType == YWTribeType.CHATTING_TRIBE) {
                        IMNotificationUtils.getInstance().showToast(InviteTribeMemberActivity.this, "添加群成员成功！");
                    }
                    finish();
                }
            }

            @Override
            public void onError(int i, String s) {
                IMNotificationUtils.getInstance().showToast(InviteTribeMemberActivity.this, "添加群成员失败，code = " + i + ", info = " + s);
            }

            @Override
            public void onProgress(int i) {

            }
        });

        finish();
    }

    private void setListView() {
        groups = new ArrayList<>();
        map = new HashMap<>();
        adapter = new AddChengYuanExpandableListAdapter(groups, this, map, this);
        expandlist = (ExpandableListView) findViewById(R.id.expandlist);
        expandlist.setGroupIndicator(null);// 设置去掉小箭头
        expandlist.setAdapter(adapter);
    }

    private void setListener() {

        expandlist.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                adapter.setPos(groupPosition, childPosition);
                return true;
            }
        });
    }

    @Override
    public void onGroupExpanded(final int groupPosition) {
        // TODO 请求该分组下的好友信息：
        String url = UrlTools.url + UrlTools.GET_CHILD_OF_GROUP;
        RequestParams params = new RequestParams();
        params.put("group_id",
                ((Group) adapter.getGroup(groupPosition)).getId() + "");
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
                new HttpCallBack() {

                    @Override
                    public void success(JsonBean bean) {
                        ArrayList<HashMap<String, Object>> infor = bean
                                .getInfor();
                        List<ChildItem> cis = new ArrayList<>();

                        for (int i = 0; i < infor.size(); i++) {
                            HashMap<String, Object> hm = infor.get(i);
                            ChildItem ci = new ChildItem();
                            int friend_id = (int) hm.get("friend_id");
                            String head = "" + hm.get("staff_head");
                            int person_id = (int) hm.get("person_id");
                            String remark = "" + hm.get("remark");
                            String phone = "" + hm.get("staff_mobile");
                            String name = hm.get("staff_name") + "";
                            ci.setId(friend_id + "");
                            ci.setMarkerImgId(head);
                            ci.setPhone(phone);
                            ci.setTitle(name);

                            cis.add(ci);
                        }
                        map.put(groupPosition, cis);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(String msg) {
                        T.showShort(InviteTribeMemberActivity.this, msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }
    @Override
    protected void onResume() {
        requestGroup();// 获取分组信息：
        super.onResume();
    }
    // 获取分组信息：
    private void requestGroup() {
        String url01 = UrlTools.url + UrlTools.GET_FRIEND_GROUP;
        Utils.doPost(LoadingDialog.getInstance(this), this, url01, null,
                new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        ArrayList<HashMap<String, Object>> infor = bean
                                .getInfor();

                        groups.removeAll(groups);
                        Log.e("TAG", infor.size() + "info.size()");

                        for (int i = 0; i < infor.size(); i++) {
                            int id = (int) infor.get(i).get("id");
                            String name = infor.get(i).get("name") + "";
                            int default_num = (int) infor.get(i).get("default");
                            int count = (int) infor.get(i).get("count");
                            Group group = new Group(id, name, default_num,
                                    count);
                            Log.e("TAG", group.toString());
                            groups.add(group);
                        }

                        for (int i = 0; i < groups.size(); i++) {
                            List<ChildItem> ci = new ArrayList<>();
                            map.put(i, ci);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(String msg) {
                        T.showShort(InviteTribeMemberActivity.this, msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }



}

