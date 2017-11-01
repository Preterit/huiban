package com.feirui.feiyunbangong.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.YWContactFactory;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.ChengYuanAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 查看已创建的团队：
 *
 * @author feirui1
 */
@SuppressLint("InflateParams")
public class TuanDui_DetailActivity extends BaseActivity implements
        OnItemClickListener, OnKeyListener, OnClickListener {

    private ListView lv_chengyuan;
    private ChengYuanAdapter adapter;
    private ArrayList<TuanDuiChengYuan> tdcys;
    private TuanDui td;
    private Button bt_add;// 添加成员
    private LinearLayout ll_tuanduigonggao, ll_tuanduiquan, ll_tuanduichengyuan;// 团队公告 、成员；
    private TextView tv_message_num, tv_chenyuan;// 团队公告消息数量；
    private View header_view;// 头部；
    private ImageView iv_tjcy;

    //群聊相关
    private TribeAndRoomList mTribeAndRoomList;  //群聊集合
    private List<YWTribe> mTribeList;
    private List<YWTribe> mRoomsList;
    private YWIMKit mIMKit;
    private IYWTribeService mTribeService;
    private long mTribeId; //团聊的ID

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tuan_dui);
        mIMKit = AppStore.mIMKit;
        initView();
        setListener();
        setListView();

    }

    /**
     * 动态注册广播
     */
    private void registReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ON_RECEIVE_NEW_MEMBER_ADD);
        receiver = new MyReceiver();
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onResume() {
        registReceiver();// 注册广播接收器
        getData();
        getMessageNum();// 获得消息数量；
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //销毁在onResume()方法中的广播
        if (receiver != null){
            unregisterReceiver(receiver);
        }

    }

    private void getMessageNum() {

        String url = UrlTools.url + UrlTools.TEAM_MESSAGE_NUM;
        RequestParams params = new RequestParams();
        params.put("teamid", td.getTid());
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
                new HttpCallBack() {

                    @Override
                    public void success(JsonBean bean) {
                        String count = "" + bean.getInfor().get(0).get("count");
                        if ("0".equals(count)) {
                            tv_message_num.setVisibility(View.INVISIBLE);
                        } else {
                            tv_message_num.setVisibility(View.VISIBLE);
                            tv_message_num.setText(count);
                        }
                    }

                    @Override
                    public void failure(String msg) {
                        tv_message_num.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void finish() {

                    }
                });

    }

    private void getData() {
        String url = UrlTools.url + UrlTools.DETAIL_TUANDUICHENGYUAN;

        RequestParams params = new RequestParams();
        params.put("id", td.getTid());
        L.e("获取团队成员url" + url + " params" + params);
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
                new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        ArrayList<HashMap<String, Object>> infor = bean
                                .getInfor();
                        tdcys.removeAll(tdcys);
                        for (int i = 0; i < infor.size(); i++) {
                            HashMap<String, Object> hm = infor.get(i);
                            Log.e("团队成员", "团队成员: "+hm.toString() );
                            TuanDuiChengYuan tdcy = new TuanDuiChengYuan(hm
                                    .get("id") + "", String.valueOf(hm
                                    .get("staff_id")), String.valueOf(hm
                                    .get("staff_name")), String.valueOf(hm
                                    .get("staff_head")), hm.get("type") + "",
                                    String.valueOf(hm.get("staff_mobile")),
                                    String.valueOf(hm.get("staff_email")),
                                    hm.get("tag_name") + "",
                                    String.valueOf(hm.get("introduction")),String.valueOf(hm.get("store_url")),
                                    String.valueOf(hm.get("sex")),String.valueOf(hm.get("birthday")),
                                     String.valueOf(hm.get("address")),String.valueOf(hm.get("staff_key1")),
                                    String.valueOf(hm.get("staff_key2")),String.valueOf(hm.get("staff_key3")));

                            tdcy.setTeam_member_list_id(hm.get("team_member_list_id") + "");

                            tdcy.setState(Integer.parseInt(hm.get("state") + ""));

                            tdcy.setFriendstate((int) hm.get("friendstate"));// 是否是好友；

                            tdcy.setRemark(hm.get("remark") + "");// 设置备注；

                            tdcy.setT_remark(hm.get("t_remark") + "");// 设置团队备注；
                            tv_chenyuan.setText("团队成员" + "("
                                    + (String) (hm.get("Allnum") + "") + ")");
                            // 团队设置到团队团长id:
                            if ("团长".equals(tdcy.getType())) {
                                td.setGuanli_id(tdcy.getStaff_id());
                            }
                            // 副团长添加到团队副团长id集合中；
                            if ("副团长".equals(tdcy.getType())) {
                                td.getDcmoes().add(tdcy.getStaff_id());
                            }
                            tdcys.add(tdcy);
                        }
                        Log.e("123", "success: ------------------" + tdcys );
                        adapter.add(tdcys);
                    }

                    @Override
                    public void failure(String msg) {
                        Toast.makeText(TuanDui_DetailActivity.this, msg,Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2:
                    break;
                case 3:
                    // 添加成功！
                    Toast.makeText(TuanDui_DetailActivity.this, "添加成员成功！", 0).show();
                    //团队群Id
                    getTuanLiaoId();
                    Log.e("chengyuan", "handleMessage: -----------------" + tdcy_add.get(0).getPhone() );
                    getData();// 更新数据；
                    break;
                case 4:
                    JsonBean bean03 = (JsonBean) msg.obj;
                    Toast.makeText(TuanDui_DetailActivity.this, bean03.getMsg(),0)
                            .show();
                    break;
                case 5:
                    JsonBean bean = (JsonBean) msg.obj;
                    Log.e("chengyuan", "JsonBean: -----------------" + bean.getInfor().get(0).get("team_talk") );
                    //添加团聊成员
                    if (!bean.getInfor().get(0).get("team_talk").equals("")){
                        addTuanLiaoChengYuan(bean.getInfor().get(0).get("team_talk") + "");
                    }
                    break;
            }
        }

        ;
    };

    private void setListView() {
        lv_chengyuan.setAdapter(adapter);
        adapter.add(tdcys);
    }

    private void setListener() {
        lv_chengyuan.setOnItemClickListener(this);
        //bt_add.setOnClickListener(this);
        iv_tjcy.setOnClickListener(this);
        ll_tuanduigonggao.setOnClickListener(this);
        ll_tuanduichengyuan.setOnClickListener(this);
        //跳转到团队加页面
        rightll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TuanDui_DetailActivity.this,
                        TuanDuiJiaActivity.class);
                //将整个团队传过去
                intent.putExtra("td", td);
                startActivity(intent);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
            }
        });

        ll_tuanduiquan.setOnClickListener(this);

    }

    private LinearLayout llTeamTask;

    private void initView() {

        AppStore.acts.add(this);

        //传过来的团队
        Intent intent = getIntent();
        td = (TuanDui) intent.getSerializableExtra("tuanDui");

        initTitle();
        setLeftDrawable(R.drawable.arrows_left);

        if (td.getName().length() > 10) {
            setCenterString(td.getName().substring(0, 9) + "...");
        } else {
            setCenterString(td.getName());
        }

        setRightDrawable(R.drawable.point);

        lv_chengyuan = (ListView) findViewById(R.id.lv_chengyuan);
        adapter = new ChengYuanAdapter(getLayoutInflater());
        tdcys = new ArrayList<>();

//        View footer_view = getLayoutInflater().inflate(R.layout.lv_footer_tuandui_chengyuan, null);
//        bt_add = (Button) footer_view.findViewById(R.id.bt_add);
//
//        lv_chengyuan.addFooterView(footer_view);

        header_view = getLayoutInflater().inflate(R.layout.lv_notice_team_item,
                null);

        lv_chengyuan.addHeaderView(header_view);

        ll_tuanduigonggao = (LinearLayout) header_view
                .findViewById(R.id.ll_tuanduigonggao);
        ll_tuanduichengyuan = (LinearLayout)header_view.findViewById(R.id.llChengYuan);

        tv_chenyuan = (TextView) header_view.findViewById(R.id.tv_chenyuan);
        // bt_out_team = (Button) findViewById(R.id.bt_out_team);

        llTeamTask = (LinearLayout) header_view.findViewById(R.id.llTeamTask);
        llTeamTask.setOnClickListener(this);

        tv_message_num = (TextView) header_view
                .findViewById(R.id.tv_message_num);

        ll_tuanduiquan = (LinearLayout) header_view
                .findViewById(R.id.ll_tuanduiquan);
        iv_tjcy = (ImageView)header_view.findViewById(R.id.iv_tjcy);

        mTribeList = new ArrayList<YWTribe>();
        mRoomsList = new ArrayList<YWTribe>();
        mTribeAndRoomList = new TribeAndRoomList(mTribeList,mRoomsList);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (position == 0) {
            return;
        }
        // 查看团队某个成员信息：
        TuanDuiChengYuan tdcy = tdcys.get(position - 1);
        Intent intent = new Intent(this, PersonDataActivity.class);
        Log.e("查看团队某个成员信息", "tdcy: "+tdcy.toString() );
        intent.putExtra("tdcy", tdcy);
        intent.putExtra("td", td);
        startActivity(intent);
        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.bt_add:
//                Intent intent = new Intent(this, AddChengYuanActivity.class);
//                startActivityForResult(intent, 300);
//                break;
            case R.id.iv_tjcy: //添加成员
                Intent intent = new Intent(this, AddChengYuanActivity.class);
                startActivityForResult(intent, 300);
                break;
            case R.id.ll_tuanduigonggao:
                Intent intent02 = new Intent(this, DetailGongGaoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tdcys", tdcys);
                bundle.putString("id", td.getTid());
                intent02.putExtras(bundle);
                startActivity(intent02);
                break;
            case R.id.ll_tuanduiquan:
                Intent intent03 = new Intent(this, WorkCircleActivity.class);
                intent03.putExtra("team_id", td.getTid());
                startActivity(intent03);
                break;
            case R.id.llTeamTask:
                Intent teamTaskIntent = new Intent(this, TasksListActivity.class);
                startActivity(teamTaskIntent);
                break;
            case R.id.llChengYuan: //打开团队聊天窗口

//                YWTribe tribe = ;
//                Intent talkInternt = AppStore.mIMKit.getTribeChattingActivityIntent();


                break;
        }
        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 300 && resultCode == 100) {
            @SuppressWarnings("unchecked")
            ArrayList<ChildItem> childItem = (ArrayList<ChildItem>) data
                    .getSerializableExtra("childs");

            ArrayList<TuanDuiChengYuan> tdcy = new ArrayList<>();

            for (int i = 0; i < childItem.size(); i++) {
                TuanDuiChengYuan tdc = new TuanDuiChengYuan(childItem.get(i)
                        .getId(), childItem.get(i).getTitle(), childItem.get(i)
                        .getMarkerImgId(), "", childItem.get(i).getPhone());
                tdcy.add(tdc);
            }

            tdcy_add = new ArrayList<>();

            if (tdcy != null && tdcy.size() > 0) {
                for (int i = 0; i < tdcy.size(); i++) {
                    boolean isHave = false;
                    if (adapter.getList().size() > 0) {
                        for (int j = 0; j < adapter.getList().size(); j++) {
                            if (adapter.getList().get(j).getId()
                                    .equals(tdcy.get(i).getId())) {
                                isHave = true;
                                break;
                            }
                        }
                    }
                    if (!isHave) {  //团队还没有该成员
                        tdcy_add.add(tdcy.get(i));
                    }
                }
                addChengYuan();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    List<TuanDuiChengYuan> tdcy_add;  //添加的新成员
    private void addChengYuan() {

        if (tdcy_add == null || tdcy_add.size() == 0) {
            return;
        }
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < tdcy_add.size(); i++) {
            sb.append(tdcy_add.get(i).getId());
            sb.append(",");
        }

        //团队
        String url = UrlTools.url + UrlTools.ADD_CHENGYUAN;
        RequestParams params = new RequestParams();
        params.put("team_id", td.getTid());
        params.put("staff_id", sb.toString());

        Log.e("TAG", "------------成员------" + params.toString());

        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                        JsonBean bean = JsonUtils.getMessage(new String(arg2));
                        if ("200".equals(bean.getCode())) {
                            if(bean.getMsg().equals("该成员已存在")){
                                Toast.makeText(TuanDui_DetailActivity.this, "该成员已经加入团队", 0).show();
                                return;
                            }
                            Message msg = handler.obtainMessage(3);
                            msg.obj = bean;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = handler.obtainMessage(4);
                            msg.obj = bean;
                            handler.sendMessage(msg);
                        }
                        super.onSuccess(arg0, arg1, arg2);
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        Message msg = handler.obtainMessage(5);
                        handler.sendMessage(msg);
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }
                });
    }

    /**
     * 团队团聊Id
     */
    public void getTuanLiaoId(){
        String url = UrlTools.url + UrlTools.GET_TUANLIAOID;
        RequestParams params = new RequestParams();
        params.put("team_id",td.getTid());
        AsyncHttpServiceHelper.post(url,params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                JsonBean bean = JsonUtils.getMessage(new String(arg2));
                if ("200".equals(bean.getCode())) {
                    Message msg = handler.obtainMessage(5);
                    msg.obj = bean;
                    handler.sendMessage(msg);
                } else {
                    Message msg = handler.obtainMessage(4);
                    msg.obj = bean;
                    handler.sendMessage(msg);
                }
                super.onSuccess(arg0, arg1, arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                Message msg = handler.obtainMessage(5);
                handler.sendMessage(msg);
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        });

    }

    /**
     * 团队聊天自动加人
     * @param qunID   团聊的ID
     */
    public void  addTuanLiaoChengYuan(String qunID){
        mTribeService = mIMKit.getTribeService();

        mTribeId = Long.valueOf(qunID) ;
        Log.e("chengyuan", "mTribeId: -----------------" + mTribeId );

        List<IYWContact> list = new ArrayList<>();
        for (int i = 0;i < tdcy_add.size();i++){
            IYWContact iywContact =  YWContactFactory.createAPPContact(tdcy_add.get(i).getPhone(), Happlication.APP_KEY);
            list.add(iywContact);
            Log.e("chengyuan", "iywContact: -----------------" + list.get(i) );
        }
        if(list != null || list.size() > 0){
            mTribeService.inviteMembers(mTribeId, list,new MyCallback() {
                @Override
                public void onSuccess(Object... result) {
//                finish();
                }
            });
        }

    }

    // 广播接收器，接收团队成员加入的广播：
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (!intent.getAction().equals(Constant.ON_RECEIVE_NEW_MEMBER_ADD)) {
                return;
            }
            Log.e("TAG", "接收到团队成员加入的广播");

            // 判断团队id是否和传过来的团队id一致，如果一致则刷新页面；否则不做处理；
            String id = intent.getStringExtra("id");

            if (td.getTid().equals(id)) {
                getData();// 更新数据；
            }
        }
    }

    /**
     * 请求回调
     *
     * @author zhaoxu
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
}
