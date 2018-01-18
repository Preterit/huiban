package com.feirui.feiyunbangong.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.feirui.feiyunbangong.view.RefreshLayout;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xw.repo.refresh.PullListView;

import org.apache.http.Header;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 查看已创建的团队：
 *
 * @author feirui1
 */
@SuppressLint("InflateParams")
public class TuanDui_DetailActivity extends AppCompatActivity implements
        OnItemClickListener, OnKeyListener, OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,RefreshLayout.OnLoadListener{

    private static PullListView lv_chengyuan;
    private static ChengYuanAdapter adapter;
    private static ArrayList<TuanDuiChengYuan> tdcys ; //所有的团队成员
    private List<TuanDuiChengYuan> allTuan = new ArrayList<>();
    private static List<TuanDuiChengYuan> tuanDui = new ArrayList<>();
    private static TuanDui td;
    private Button bt_add;// 添加成员
    private LinearLayout ll_tuanduigonggao, ll_tuanduiquan, ll_tuanduichengyuan;// 团队公告 、成员；
    private static TextView tv_message_num;
    private static TextView tv_chenyuan;
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
    private static RefreshLayout swipe_container;
    private String count;//公告数
    static ArrayList<Activity> activitys = Happlication.getActivities();
    private static Activity activity;
    private static int position = 1;//当前页
    private View m_listViewFooter;
    public TextView righttv;
    private int numMem = 0;
    // 左部 中间 右部
    public TextView centerTv,right_tv;
    public ImageView leftIv, rightIv;
    private static String guanli_id;// 团长id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tuan_dui);
        Happlication.getActivities().add(this);
        mIMKit = AppStore.mIMKit;
        //传过来的团队
        Intent intent = getIntent();
        td = (TuanDui) intent.getSerializableExtra("tuanDui");
        count = intent.getStringExtra("count");
        initView();
        setListener();
        initData();//从数据库获取数据
        getNum(1 + "");
        setListView();
    }

    /**
     * 动态注册广播
     */
    private void registReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ON_RECEIVE_NEW_MEMBER_ADD);
//        filter.addAction(Constant.ON_RECEIVE_NEW_MEMBER_DELETE);
        receiver = new MyTeamReceiver();
        registerReceiver(receiver, filter);

        //静态广播
        Intent intent = new Intent();
        intent.setAction(Constant.ON_RECEIVE_NEW_MEMBER_DELETE);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        registReceiver();// 注册广播接收器
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Happlication.getActivities().remove(this);
    }

    /**
     *团队公告数
     */
    private static void getMessageNum() {
        String url = UrlTools.url + UrlTools.TEAM_MESSAGE_NUM;
        RequestParams params = new RequestParams();
        params.put("teamid", td.getTid());
        if (activitys.size() == 0 || activitys == null){
            return;
        }else {
            activity = activitys.get(0);
        }
        Utils.doPost(LoadingDialog.getInstance(activity), activity, url, params,
                new HttpCallBack() {

                    @Override
                    public void success(JsonBean bean) {
                      String nm = "" + bean.getInfor().get(0).get("count");
                        if ("0".equals(nm)) {
                            tv_message_num.setVisibility(View.INVISIBLE);
                        } else {
                            tv_message_num.setVisibility(View.VISIBLE);
                            tv_message_num.setText(nm);
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

    /**
     * 获取团队人数
     */
    private  void getNum(final String num){
        String url = UrlTools.url + UrlTools.DETAIL_TUANDUICHENGYUAN_TEAM;
        RequestParams params = new RequestParams();
        params.put("id", td.getTid());
        params.put("curpage",num);
        if (activitys.size() == 0 || activitys == null){
            return;
        }else {
            activity = activitys.get(0);
        }
        Utils.doPost(LoadingDialog.getInstance(activity), activity, url, params,
                new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        ArrayList<HashMap<String, Object>> infor = bean
                                .getInfor();
                        HashMap<String, Object> hm = infor.get(0);
                        if (infor.size() > 0){
                            if ("1".equals(num)){
                                tv_chenyuan.setText("团队成员" + "("
                                        + infor.size() + "/" + (hm.get("Allnum") + "") + ")");
                                numMem = infor.size();
                            }else {
                                numMem += infor.size();
                                Log.e("num", "success:---------------------------- " + numMem );
                                tv_chenyuan.setText("团队成员" + "("
                                        + numMem + "/" + (hm.get("Allnum") + "") + ")");
                            }

                        }else {
                            tv_chenyuan.setText("团队成员" + "("
                                    + (0 + "") + ")");
                        }

                    }

                    @Override
                    public void failure(String msg) {
                    }

                    @Override
                    public void finish() {
                    }
                });
    }

    /**
     * 从网络获取数据
     * 当前页数  加载类型 0 下拉刷新 1 上拉加载
     */
    private static void getData(String num, final int type) {
        String url = UrlTools.url + UrlTools.DETAIL_TUANDUICHENGYUAN_TEAM;
        RequestParams params = new RequestParams();
        params.put("id", td.getTid());
        params.put("curpage",num);
        if (activitys.size() == 0 || activitys == null){
            return;
        }else {
            activity = activitys.get(0);
        }
        L.e("获取团队成员url" + url + " params" + params);
        Utils.doPost(LoadingDialog.getInstance(activity), activity, url, params,
                new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        ArrayList<HashMap<String, Object>> infor = bean
                                .getInfor();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipe_container.setLoading(false);
                            }
                        }, 1000);

                        swipe_container.setRefreshing(false);
                        if (type == 0){
                            tdcys.removeAll(tdcys);
                            position = 2 ;
                        }
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
                                    String.valueOf(hm.get("staff_key2")),String.valueOf(hm.get("staff_key3"))
                                    ,String.valueOf(hm.get("staff_key4")),String.valueOf(hm.get("staff_key5")),
                                    String.valueOf(hm.get("type_position")),String.valueOf(hm.get("position")),
                                    String.valueOf(hm.get("limit_position")));

                            tdcy.setTeam_member_list_id(hm.get("team_member_list_id") + "");

                            tdcy.setState(Integer.parseInt(hm.get("state") + ""));

                            tdcy.setFriendstate((int) hm.get("friendstate"));// 是否是好友；

                            tdcy.setRemark(hm.get("remark") + "");// 设置备注；

                            tdcy.setT_remark(hm.get("t_remark") + "");// 设置团队备注；
//                            tv_chenyuan.setText("团队成员" + "("
//                                    + infor.size() + "/" + (hm.get("Allnum") + "") + ")");
                            // 团队设置到团队团长id:
                            if ("团长".equals(tdcy.getType())) {
                                guanli_id = tdcy.getStaff_id();
                                td.setGuanli_id(tdcy.getStaff_id());
                            }
                            // 副团长添加到团队副团长id集合中；
                            if ("副团长".equals(tdcy.getType())) {
                                td.getDcmoes().add(tdcy.getStaff_id());
                            }
                            tdcys.add(tdcy);
                        }
                        // 上拉加载：
                        if (type == 1) {
                            position ++;
                            Log.e("tdcys", " 保存数据库的 ===========" + tdcys.get(25).getCId() + "======" + tdcys.get(0).getCId() );
                            if (tdcys.get(25).getCId().equals(tdcys.get(0).getCId())){
                                tdcys.removeAll(tuanDui);
                            }
                        }
                        adapter.add(tdcys);
                        // 下拉刷新：
                        if (type == 0) {
                            //保存数据  考虑团队成员 关键字的改变
                            DataSupport.deleteAll(TuanDuiChengYuan.class,"tuandui_id = ?", td.getTid());
                            Log.e("tdcys", " 保存数据库的 ===========" + tdcys );
                            //多对一数据保存
                            for (int i = 0; i < tdcys.size(); i++){
                                TuanDuiChengYuan tuan = new TuanDuiChengYuan();
                                tuan.setCId(tdcys.get(i).getCId());
                                tuan.setStaff_id(tdcys.get(i).getStaff_id());
                                tuan.setHead(tdcys.get(i).getHead());
                                tuan.setName(tdcys.get(i).getName());
                                tuan.setType(tdcys.get(i).getType());
                                tuan.setPhone(tdcys.get(i).getPhone());
                                tuan.setEmail(tdcys.get(i).getEmail());
                                tuan.setStore_url(tdcys.get(i).getStore_url());
                                tuan.setSex(tdcys.get(i).getSex());
                                tuan.setBirthday(tdcys.get(i).getBirthday());
                                tuan.setAddress(tdcys.get(i).getAddress());
                                tuan.setKey1(tdcys.get(i).getKey1());
                                tuan.setKey2(tdcys.get(i).getKey2());
                                tuan.setKey3(tdcys.get(i).getKey3());
                                tuan.setKey4(tdcys.get(i).getKey4());
                                tuan.setKey5(tdcys.get(i).getKey5());
                                tuan.setPosition(tdcys.get(i).getPosition());
                                tuan.setLimit_position(tdcys.get(i).getLimit_position());
                                tuan.setType2(tdcys.get(i).getType2());
                                tuan.setTuanDui_id(td.getTid());
                                tuan.setTeam_member_list_id(tdcys.get(i).getTeam_member_list_id());

                                tuan.setState(tdcys.get(i).getState());

                                tuan.setFriendstate(tdcys.get(i).getFriendstate());// 是否是好友；

                                tuan.setRemark(tdcys.get(i).getRemark());// 设置备注；

                                tuan.setT_remark(tdcys.get(i).getT_remark());// 设置团队备注；
                                tuan.save();
                            }

                        }

                    }

                    @Override
                    public void failure(String msg) {
                        Toast.makeText(activity, msg,Toast.LENGTH_SHORT)
                                .show();
                        swipe_container.setRefreshing(false);
                        swipe_container.setLoading(false);
                    }

                    @Override
                    public void finish() {}
                });
    }

    /**
     * 从数据库获取数据
     */
    private void initData() {
        //从数据库获取所有数据
        allTuan = DataSupport.findAll(TuanDuiChengYuan.class);
        if (allTuan == null || allTuan.size() == 0){
            Log.e("tdcys", "initData:-------------allTuan---------------- " );
            //从网络获取数据
            getData(1 + "", 0);
        }else {
            tdcys.removeAll(tdcys);
            for (int i = 0; i < allTuan.size(); i++){
                if ((td.getTid()).equals(allTuan.get(i).getTuanDui_id())){
                    tdcys.add(allTuan.get(i));
                }
            }
            Log.e("tdcys", " 5555555555555 " + tdcys + "------" + tdcys.size() + "----" + count);
            if (tdcys.size() == 0 || Integer.parseInt(count) != 0){
                //这个最好判断团队最新的人数与数据库中的人数 但未做
                Log.e("tdcys", "initData:-------------从网络获取数据---------------- " );
                //从网络获取数据
                getData(1 + "", 0);
            }else {
                Log.e("tdcys", "initData:------------数据库获取---------------- " );
                for (int i = 0; i < tdcys.size(); i++){
                    if ("团长".equals(tdcys.get(i).getType())) {
                        guanli_id = tdcys.get(i).getStaff_id();
                        td.setGuanli_id(tdcys.get(i).getStaff_id());
                    }
                    // 副团长添加到团队副团长id集合中；
                    if ("副团长".equals(tdcys.get(i).getType())) {
                        td.getDcmoes().add(tdcys.get(i).getStaff_id());
                    }
                }
                adapter.add(tdcys);
                tuanDui.removeAll(tuanDui);
                tuanDui = tdcys;
                position = 2;
                swipe_container.setLoading(false);
                swipe_container.setRefreshing(false);
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2:
                    break;
                case 3:
                    // 添加成功！
                    Toast.makeText(TuanDui_DetailActivity.this, "添加成员成功！",Toast.LENGTH_SHORT).show();
                    //团队群Id
                    getTuanLiaoId();
                    Log.e("chengyuan", "handleMessage: -----------------" + tdcy_add.get(0).getPhone() );
                    break;
                case 4:
                    JsonBean bean03 = (JsonBean) msg.obj;
                    Toast.makeText(TuanDui_DetailActivity.this, bean03.getMsg(),Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 5:
                    final JsonBean bean = (JsonBean) msg.obj;
                    getData(1 + "",0);// 更新数据；
                    getNum(1 + "");
                    //添加团聊成员
                    if (TextUtils.isEmpty(bean.getInfor().get(0).get("team_talk") + "")){
                        return;
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addTuanLiaoChengYuan(bean.getInfor().get(0).get("team_talk") + "");
                            }
                        });
                    }
                    break;
                case 6:
//                    listenerSearch(); //搜索成员
                    break;
            }
        }

        ;
    };

    private void setListView() {
        lv_chengyuan.setAdapter(adapter);
//        adapter.add(tdcys);
    }

    private void setListener() {
        swipe_container.setOnRefreshListener(this);
        swipe_container.setOnLoadListener(this);
        lv_chengyuan.setOnItemClickListener(this);
        //bt_add.setOnClickListener(this);
        iv_tjcy.setOnClickListener(this);
        ll_tuanduigonggao.setOnClickListener(this);
        ll_tuanduichengyuan.setOnClickListener(this);
        //跳转到团队加页面
        rightIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TuanDui_DetailActivity.this,
                        TuanDuiJiaActivity.class);
                Log.e("td", "onClick:-------------------- " + td.toString());
                //将整个团队传过去
                intent.putExtra("td", td);
                startActivity(intent);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
            }
        });

        ll_tuanduiquan.setOnClickListener(this);

        righttv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TuanDui_DetailActivity.this,
                        SearchMemberActivity.class);
                //将整个团队传过去
                intent.putExtra("td", td);
                startActivity(intent);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
            }
        });

    }


    private LinearLayout llTeamTask;

    private void initView() {
        AppStore.acts.add(this);
        leftIv = (ImageView) findViewById(R.id.leftIv);
        centerTv = (TextView) findViewById(R.id.centerTv);
        right_tv= (TextView) findViewById(R.id.righttv);
        rightIv = (ImageView) findViewById(R.id.rightIv);
		righttv = (TextView) findViewById(R.id.righttv);
        leftIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (td.getName().length() > 10) {
            centerTv.setText(td.getName().substring(0, 9) + "...");
        } else {
            centerTv.setText(td.getName());
        }
        righttv.setBackgroundResource(R.drawable.bai_fangdajing);


        lv_chengyuan = (PullListView) findViewById(R.id.lv_chengyuan);
        lv_chengyuan.setVisibility(View.VISIBLE);
        adapter = new ChengYuanAdapter(getLayoutInflater());
        tdcys = new ArrayList<>();


        header_view = getLayoutInflater().inflate(R.layout.lv_notice_team_item,
                null);

        lv_chengyuan.addHeaderView(header_view);
        swipe_container = (RefreshLayout)findViewById(R.id.swipe_container);
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
        m_listViewFooter = LayoutInflater.from(this).inflate(R.layout.listview_foot, null, false);
        lv_chengyuan.addFooterView(m_listViewFooter);

        mTribeList = new ArrayList<YWTribe>();
        mRoomsList = new ArrayList<YWTribe>();
        mTribeAndRoomList = new TribeAndRoomList(mTribeList,mRoomsList);
        if ("0".equals(count)) {
            tv_message_num.setVisibility(View.INVISIBLE);
        } else {
            tv_message_num.setVisibility(View.VISIBLE);
            tv_message_num.setText(count);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Log.e("查看团队某个成员信息", "tdcy:----------------- "+ position);
        if (position == 0) {
            return;
        }
        // 查看团队某个成员信息：
        TuanDuiChengYuan tdcy = tdcys.get(position - 1);
        Intent intent = new Intent(this, TeamPersonActivity.class);
        Log.e("查看团队某个成员信息", "tdcy: "+tdcy.toString() );
        intent.putExtra("tdcy", tdcy);
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
                intent03.putExtra("guanli_id", guanli_id);
                intent03.putExtra("team_id", td.getTid());
                startActivity(intent03);
                break;
            case R.id.llTeamTask:
                Intent teamTaskIntent = new Intent(this, RenWuListActivity.class);
                startActivity(teamTaskIntent);
                break;
            case R.id.llChengYuan: //打开团队聊天窗口

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
                            if (adapter.getList().get(j).getCId()
                                    .equals(tdcy.get(i).getCId())) {
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
            sb.append(tdcy_add.get(i).getCId());
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
                                Toast.makeText(TuanDui_DetailActivity.this, "该成员已经加入团队", Toast.LENGTH_SHORT).show();
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

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        getMessageNum();//公告数
        getData( 1 +  "",0); //获取原始数据
        getNum(1 + ""); //团队人数
    }

    @Override
    public void onLoad() {
        Log.e("load", "onLoad: =========================" + position );
        if (tdcys.size() >= 25){
            getData( position +  "",1);
            getNum(position + "");
        }else {
            swipe_container.setLoading(false);
        }

    }

    @Override
    public void setFooterView(boolean isLoading) {
        if (isLoading) {
            lv_chengyuan.removeFooterView(m_listViewFooter);
            lv_chengyuan.addFooterView(m_listViewFooter);
        } else {
            lv_chengyuan.removeFooterView(m_listViewFooter);
        }
    }

    // 广播接收器，接收团队成员加入的广播：
    public  class MyTeamReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("tdys", "接收到团队成员加入的广播");
            if (intent.getAction().equals(Constant.ON_RECEIVE_NEW_MEMBER_ADD)) {
                // 判断团队id是否和传过来的团队id一致，如果一致则刷新页面；否则不做处理；
                String id = intent.getStringExtra("id");
                if (td.getTid().equals(id)) {
                    getMessageNum();
                    getData( 1 +  "",0);
                    getNum(1 + "");
                }
            }
        }
    }

    public static class mBroadcastReceiver extends BroadcastReceiver {
        //接收到广播后自动调用该方法
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("删除".equals(intent.getStringExtra("guangbo"))){
                Log.e("tdys", "接收到团队成员加入的广播---------" + intent.getStringExtra("teamId")+ "===" + intent.getStringExtra("id"));
//                getData( 1 +  "",0);
                DataSupport.deleteAll(TuanDuiChengYuan.class,"tuandui_id = ? and staff_id = ?",
                        intent.getStringExtra("teamId"),intent.getStringExtra("id"));
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
