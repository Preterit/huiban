package com.feirui.feiyunbangong.activity.tribe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.tribe.YWTribeCreationParam;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.AddChengYuanActivity;
import com.feirui.feiyunbangong.activity.BaseActivity;
import com.feirui.feiyunbangong.adapter.ChuangJianTuanDuiAdapter;
import com.feirui.feiyunbangong.adapter.GridAdapter;
import com.feirui.feiyunbangong.dialog.TianJiaBIaoQianDialog;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xy on 2017/5/9.
 */

public class EditGroupInfoActivity extends BaseActivity implements
        View.OnClickListener {

    //引的创建团队的
    private ImageView iv_jia;
    private ListView lv_zuyuan;
    private ChuangJianTuanDuiAdapter adapter;
    private EditText et_name_tuandui;// 团队名称！
    private LinearLayout ll_add_chengyuan;// 添加成员；
    private Button bt_submit;
    private GridView gv;
    private GridAdapter adapter1;
    private ArrayList<JsonBean> list1 = new ArrayList<>();
    private List<TextView> tvs = new ArrayList<>();

    //群聊的
    private YWIMKit mIMKit;
    private IYWTribeService mTribeService;
    private ModifyTribeInfoCallback callback;

    private ArrayList<ChildItem> childs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_info);
        initView();
        setListener();
        setListView();
    }
    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("发起群聊");
        setRightVisibility(false);
        gv = (GridView) findViewById(R.id.gridView);
        iv_jia = (ImageView) findViewById(R.id.iv_jia);
        lv_zuyuan = (ListView) findViewById(R.id.lv_zuyuan);
        et_name_tuandui = (EditText) findViewById(R.id.et_name_tuandui);
        ll_add_chengyuan = (LinearLayout) findViewById(R.id.ll_add_chengyuan);
        bt_submit = (Button) findViewById(R.id.bt_submit);
    }

    private void setListener() {
        ll_add_chengyuan.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        iv_jia.setOnClickListener(this);
    }

    private void setListView() {

        list1 = new ArrayList<JsonBean>();
        list1.add(new JsonBean("其他"));
        adapter1 = new GridAdapter(EditGroupInfoActivity.this, list1);
        gv.setAdapter(adapter1);

        adapter = new ChuangJianTuanDuiAdapter(getLayoutInflater(), list1,
                EditGroupInfoActivity.this);
        lv_zuyuan.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_chengyuan:
                Intent intent = new Intent(this, AddChengYuanActivity.class);
                startActivityForResult(intent, 200);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                break;
            case R.id.bt_submit:
//                submit();
                createTribe(YWTribeType.CHATTING_GROUP);
                break;
            case R.id.iv_jia:// 加标签
                TianJiaBIaoQianDialog tianjia = new TianJiaBIaoQianDialog("新增标签",
                        "添加", "例如：北京", EditGroupInfoActivity.this,
                        new TianJiaBIaoQianDialog.AlertCallBack1() {

                            @Override
                            public void onOK(final String name) {
                                list1.add(new JsonBean(name));
                                adapter1.notifyDataSetChanged();
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                tianjia.show();
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == 100) {
                    childs = (ArrayList<ChildItem>) data
                    .getSerializableExtra("childs");
            HashMap<String, Object> hm = AppStore.user.getInfor().get(0);
            childs.add(
                    0,
                    new ChildItem(hm.get("staff_name") + "", hm
                            .get("staff_head") + "", hm.get("staff_mobile")
                            + "", hm.get("id") + "", 0));
            if (childs != null && childs.size() > 0) {
                adapter.addList(childs);
            }
        }
//        for (int i = 0;i < childs.size();i++){
//            Log.d("tag","获取到的群成员--------"+childs.get(i));
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 创建讨论组
     * @param type
     */
    private void createTribe(final YWTribeType type) {
        mIMKit = AppStore.mIMKit;
        mTribeService = mIMKit.getTribeService();  //获取群管理器

        if (TextUtils.isEmpty(et_name_tuandui.getText().toString())) {
            Toast.makeText(this, "请输入群名称！", 0).show();
            return;
        }
//        param.setNotice(mTribeNotice.getText().toString());

        List<ChildItem> list = adapter.getList();
        if (list.size() < 2) {
            Toast.makeText(this, "至少添加一名成员哦！", 0).show();
            return;
        }

        if (mTribeService == null) {
            return;
        }

        YWTribeCreationParam tribeCreationParam = new YWTribeCreationParam();
        tribeCreationParam.setTribeName(et_name_tuandui.getText().toString());
        tribeCreationParam.setNotice("notice");
        tribeCreationParam.setTribeType(type);

        if (type == YWTribeType.CHATTING_GROUP){
            //讨论组需要指定用户
            final List<String> userList = new ArrayList<String>();
            final YWIMCore core = mIMKit.getIMCore();

            userList.add(core.getLoginUserId());// 当前登录的用户ID，这个必须要传

            for (int i = 0;i < childs.size();i++){
                userList.add(childs.get(i).getPhone());
                Log.d("tag","获取到的群成员--------"+childs.get(i).getPhone());
            }

            tribeCreationParam.setUsers(userList);
        }

        mTribeService.createTribe(new MyCallback() {
            @Override
            public void onSuccess(Object... result) {
                // 返回值为刚刚成功创建的群
                YWTribe tribe = (YWTribe) result[0];
                tribe.getTribeId();// 群ID，用于唯一标识一个群
             //        跳转到群名片
                Intent intent = new Intent(EditGroupInfoActivity.this, TribeInfoActivity.class);
                intent.putExtra(TribeConstants.TRIBE_ID, tribe.getTribeId());
                startActivity(intent);
                finish();
            }
        }, tribeCreationParam);


    }

    /**
     * 请求回调
     *
     * @author zhaoxu
     *
     */
    private static abstract class MyCallback implements IWxCallback {

        @Override
        public void onError(int arg0, String arg1) {
            YWLog.e("TribeSampleHelper", "code=" + arg0 + " errInfo=" + arg1);
        }

        @Override
        public void onProgress(int arg0) {

        }
    }

    /**
     * 修改群信息后的反馈
     */

    class ModifyTribeInfoCallback implements IWxCallback {
        @Override
        public void onSuccess(Object... result) {
            IMNotificationUtils.getInstance().showToast(EditGroupInfoActivity.this, "修改群信息成功！");
            finish();
        }

        @Override
        public void onError(int code, String info) {
            IMNotificationUtils.getInstance().showToast(EditGroupInfoActivity.this, "修改群信息失败，code = " + code + ", info = " + info);
        }

        @Override
        public void onProgress(int progress) {

        }
    }

}
