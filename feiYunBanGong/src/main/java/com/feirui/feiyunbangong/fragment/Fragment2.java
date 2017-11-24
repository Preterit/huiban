package com.feirui.feiyunbangong.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.AboutFriendActivity;
import com.feirui.feiyunbangong.activity.AddFriendActivity;
import com.feirui.feiyunbangong.activity.FenZuGuanLiActivity;
import com.feirui.feiyunbangong.activity.JiaRuTuanDuiActivity;
import com.feirui.feiyunbangong.activity.MainActivity;
import com.feirui.feiyunbangong.activity.MoreFriendsActivity;
import com.feirui.feiyunbangong.activity.NewFriendActivity;
import com.feirui.feiyunbangong.activity.SearchFriendsActivity;
import com.feirui.feiyunbangong.activity.WorkCircleActivity;
import com.feirui.feiyunbangong.activity.tribe.EditGroupInfoActivity;
import com.feirui.feiyunbangong.activity.tribe.EditTribeInfoActivity;
import com.feirui.feiyunbangong.activity.tribe.TribeActivity;
import com.feirui.feiyunbangong.adapter.MyBaseExpandableListAdapter;
import com.feirui.feiyunbangong.adapter.SearchFriendsAdapter;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog.CallBack;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog.AlertCallBack;
import com.feirui.feiyunbangong.dialog.SelectZTDialog;
import com.feirui.feiyunbangong.dialog.SelectZTDialog.MyDailogCallback;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog.AlertCallBack1;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.Friend;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.LianXiRen;
import com.feirui.feiyunbangong.im.MyUserProfileSampleHelper;
import com.feirui.feiyunbangong.im.MyUserProfileSampleHelper.UserInfo;
import com.feirui.feiyunbangong.myinterface.AllInterface.OnGroupStateChangedListener;
import com.feirui.feiyunbangong.myinterface.AllInterface.OnNewFriendNumChanged;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.BaiDuUtil;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.LianXiRenUtil;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.RequestParams;
import com.zxing.activity.CaptureActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联系人
 *
 * @author Lesgod
 */
public class Fragment2 extends BaseFragment implements OnGroupClickListener,
        OnTouchListener, OnLongClickListener,
        android.content.DialogInterface.OnClickListener, OnClickListener,
        OnKeyListener, OnRefreshListener, OnGroupStateChangedListener,
        MyDailogCallback {

    private View view;
    @PView
    private ExpandableListView expandlist;
    private MyBaseExpandableListAdapter adapter;
    private Map<Integer, List<ChildItem>> map;
    private List<Group> groups;
    private String[] str;// 存放联系人姓名和手机号的数组；
    private ArrayList<LianXiRen> lxrs01 = new ArrayList<>();// 已经注册过得联系人集合；
    private LinearLayout ll_tianjia, ll_qunliao, ll_saosao, inclue, ll_quntalk, ll_taolun;

    private boolean isShow = false;
    private EditText et_sousuo;
    private SwipeRefreshLayout swipe_container;
    // private ImageView iv_hongdian;// 红点；
    private TextView tv_num;
    private RelativeLayout rl_chuangjian, rl_newfriend, rl_qunliebiao;// 创建团队,新朋友，群组；
    private FrameLayout fl_main2;
    private View header_view;// 头部；朋友圈，新的朋友，群列表；
    private MyBroadCastReceiver receiver;
    private SelectZTDialog dialog;
    private OnNewFriendNumChanged friendNumListener;
    public int friend_size;
    public JsonBean josnbean1;
    private StringBuffer stringBuffer = new StringBuffer(256);
    private List<Friend> listFriend = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("string", "onReceiveLocation: --------ddddddd-----------------");
        try {
            view = setContentView(inflater, R.layout.fragment_main2);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            },100);
            Log.e("string", "onReceiveLocation: -------------------------" + stringBuffer.toString());
            initView();
            setListView();
            setListener();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setFriendNumListener(OnNewFriendNumChanged friendNumListener) {
        this.friendNumListener = friendNumListener;
    }


    public Fragment2() {
    }

    private void regist() {
        Log.e("tag", "regist: --------------添加好友--------------------");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.GET_BROADCAST_ABOUT_FRIEND);
        receiver = new MyBroadCastReceiver();
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onResume() {
        regist();// 注册广播接收器；
        requestGroup();// 获取分组信息；

        getNewFriendNum();// 获取新申请的朋友个数；

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // 写子线程中的操作
//                getNewFriendNum();// 获取新申请的朋友个数；
//            }
//        }).start();
        // 合上：
        for (int i = 0; i < groups.size(); i++) {
            expandlist.collapseGroup(i);
        }
        inclue.setVisibility(View.GONE);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //注销广播
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }

    }

    // 获取分组信息：
    private void requestGroup() {
        String url01 = UrlTools.url + UrlTools.GET_FRIEND_GROUP;
        Utils.doPost(LoadingDialog.getInstance(getActivity()), getActivity(),
                url01, null, new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        ArrayList<HashMap<String, Object>> infor = bean
                                .getInfor();

                        Log.e("orz", "success: " + bean.getInfor().toString());
                        groups.removeAll(groups);
                        /*
                         * for (int i = 0; i < groups.size(); i++) {
						 * expandlist.collapseGroup(i);// 合上； }
						 */
                        for (int i = 0; i < infor.size(); i++) {
                            int id = (int) infor.get(i).get("id");
                            String name = infor.get(i).get("name") + "";
                            int default_num = (int) infor.get(i).get("default");
                            int count = (int) infor.get(i).get("count");
                            Group group = new Group(id, name, default_num,
                                    count);
                            groups.add(group);
                        }
                        Log.e("TAG", groups + "");

                        for (int i = 0; i < groups.size(); i++) {
                            List<ChildItem> ci = new ArrayList<>();
                            map.put(i, ci);
                        }

                        adapter.notifyDataSetChanged();

                        swipe_container.setRefreshing(false);
                    }

                    @Override
                    public void failure(String msg) {
                        T.showShort(getActivity(), msg);
                        swipe_container.setRefreshing(false);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }

    // 获取申请好友个数：
    private void getNewFriendNum() {

        new Thread() {
            //        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //获取通讯录中的电话在会办中注册过的人
                str = LianXiRenUtil.readConnect(getActivity());
                //Log.e("通讯录联系人", str[0] + "姓名，电话" + str[1]);
                //Log.e("通讯录联系人", str[0] + "姓名，电话" + str[1]);
                RequestParams params = new RequestParams();
                if (str == null) {
                    //T.showShort(getActivity(),"没有找到您要搜索的~");
                    return;
                }
                params.put("phone", str[1]);
                params.put("name", str[0]);//之后传联系人姓名
                String url1 = UrlTools.url + UrlTools.SHOUJILIANXIREN;
                Utils.doPost(null, getActivity(), url1, params,
                        new HttpCallBack() {
                            @Override
                            public void success(JsonBean bean) {
                                josnbean1 = bean;
                                ArrayList<HashMap<String, Object>> info = bean.getInfor();
                                Log.e("通讯录好友列表", "info.size() " + info.size());
                                Log.e("通讯录好友列表", "info " + info);
                                Log.e("通讯录好友列表", "josnbean1 " + josnbean1);
                                friend_size = info.size();
//                                tv_num.setVisibility(View.VISIBLE);
//                                tv_num.setText(friend_size + "");
                            }

                            @Override
                            public void failure(String msg) {
                            }

                            @Override
                            public void finish() {
//                        dialog.dismiss();
                            }
                        });
                //获取申请好友的个数
                String url = UrlTools.url + UrlTools.HAOYOU_NUM;

                Utils.doPost(null, getActivity(),
                        url, null, new HttpCallBack() {

                            @Override
                            public void success(JsonBean bean) {
                                Object object = bean.getInfor().get(0).get("num");
                                int num = Integer.parseInt(String.valueOf(object));
                                Message msg = new Message();
                                msg.obj = num + friend_size;
                                handler.sendMessage(msg);
//                                if (num + friend_size > 0) {
//                                    tv_num.setVisibility(View.VISIBLE);
//                                    tv_num.setText(num + friend_size + "");
//
//                                } else {
//                                    tv_num.setVisibility(View.INVISIBLE);
//                                }
                                //friendNumListener.newFriendNumChanged(num + friend_size);
                            }

                            @Override
                            public void failure(String msg) {

                                T.showShort(getActivity(), msg);

                            }

                            @Override
                            public void finish() {
                                // TODO Auto-generated method stub

                            }
                        });
//                Looper.loop();
            }
        }.start();


    }

    private void setListener() {
        // 设置下拉刷新监听：
        swipe_container.setOnRefreshListener(this);
        swipe_container.setColorScheme(
                android.R.color.holo_orange_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        rightll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShow) {
                    inclue.setVisibility(View.VISIBLE);
                } else {
                    inclue.setVisibility(View.GONE);
                }
                isShow = !isShow;
            }
        });

        leftll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openLeft();
            }
        });

        rl_chuangjian.setOnClickListener(this);
        rl_qunliebiao.setOnClickListener(this);
        rl_newfriend.setOnClickListener(this);
        ll_saosao.setOnTouchListener(this);
        ll_qunliao.setOnTouchListener(this);
        ll_tianjia.setOnTouchListener(this);
        //创建群聊
        ll_quntalk.setOnTouchListener(this);
        ll_taolun.setOnTouchListener(this);

        et_sousuo.setOnKeyListener(this);
        //
        expandlist.setOnTouchListener(this);
        expandlist.setOnGroupClickListener(this);

        fl_main2.setOnTouchListener(this);
    }

    /**
     * 联系人搜索框点击事件
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.e("TAG", keyCode + "");
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.e("TAG", keyCode + "");
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.e("TAG", "keydown");
                search(et_sousuo.getText().toString());

            }
        }
        return false;
    }

    /**
     * 联系人搜索功能
     */
    private void search(String phone) {
        String url = UrlTools.url + UrlTools.SOUSUO_LIANXIREN;
        RequestParams params = new RequestParams();
        params.put("staff_id", AppStore.user.getInfor().get(0).get("id") + ""); //个人id
        params.put("key",phone);
        params.put("location",stringBuffer.toString());
        Log.e("string", "onReceiveLocation: -------------stringBuffer------------" + params.toString() + "---" );
        Utils.doPost(LoadingDialog.getInstance(getActivity()), getActivity(),
                url, params, new HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        try {
                            ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                            listFriend.removeAll(listFriend);
                            if (infor.size() > 0){
                                for (int i = 0;i < infor.size();i++){
                                    HashMap<String,Object> hm = infor.get(i);
                                    Friend friend = new Friend(hm.get("staff_name") + "",hm.get("address") + "",
                                            hm.get("staff_head") + "",hm.get("sex") + "",hm.get("birthday") + "",
                                            hm.get("staff_key1")  + "",hm.get("staff_key2")  + "",hm.get("staff_key3")  + "",
                                            hm.get("staff_key4")  + "",
                                            hm.get("staff_key5")  + "",
                                            hm.get("distance") + "",hm.get("store_url") + "",hm.get("is_friend") + "",
                                            hm.get("staff_mobile") + "",hm.get("type") + "",
                                            hm.get("position") + "",hm.get("limit_position") + "");

                                    listFriend.add(friend);
                                }

                            }else {
                                T.showShort(getActivity(),"没有找到您要搜索的~");
                                return;
                            }

                            //按距离排序
                            Collections.sort(listFriend, new Comparator<Friend>() {
                                @Override
                                public int compare(Friend lhs, Friend rhs) {
                                    return lhs.getDistence().compareTo(rhs.getDistence());
                                }
                            });

                            Log.e("string", "success: --------------------------" + listFriend.toString() );
                            Intent intent = new Intent(getActivity(), MoreFriendsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("friendList", (Serializable) listFriend);
                            bundle.putInt("code",1);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            getActivity().overridePendingTransition(
                                    R.anim.aty_zoomin, R.anim.aty_zoomout);
                        } catch (Exception e) {
                            Log.e("TAG", e.getMessage());
                        }
                    }

                    @Override
                    public void failure(String msg) {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private void setListView() {

        expandlist.addHeaderView(header_view);
        expandlist.setGroupIndicator(null);// 设置去掉小箭头
        map = new HashMap<>();
        groups = new ArrayList<>();
        adapter = new MyBaseExpandableListAdapter(getActivity(), groups, map,
                this, this, this);
        expandlist.setAdapter(adapter);
    }

    // 聊天：
    public void chat(String phone, String name) {
        try {
            // 打开聊天窗口：
            Intent intent = AppStore.mIMKit.getChattingActivityIntent(phone,
                    Happlication.APP_KEY);
            intent.putExtra("name", name);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.aty_zoomin,
                    R.anim.aty_zoomout);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v,
                                int groupPosition, long id) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.ll_tianjia:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startActivity(new Intent(getActivity(), AddFriendActivity.class));
                    inclue.setVisibility(View.GONE);
                    getActivity().overridePendingTransition(R.anim.aty_zoomin,
                            R.anim.aty_zoomout);
                }
                return true;// 消费该监听事件，不再传递；

            case R.id.ll_quntalk: //创建群
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startActivity(new Intent(getActivity(), EditTribeInfoActivity.class));
                    getActivity().overridePendingTransition(R.anim.aty_zoomclosein, R.anim.aty_zoomcloseout);
                }

                return true;

            case R.id.ll_taolun: //发起群聊
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(getActivity(), EditGroupInfoActivity.class);
                    intent.putExtra("code", 0);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.aty_zoomclosein, R.anim.aty_zoomcloseout);
                }

                return true;

            case R.id.ll_saoyisao:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    saoyisao();
                }
                return true;// 消费该监听事件，不再传递；
            case R.id.ll_qunliao:// 分组管理
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startActivity(new Intent(getActivity(),
                            FenZuGuanLiActivity.class));
                    getActivity().overridePendingTransition(R.anim.aty_zoomin,
                            R.anim.aty_zoomout);
                }
                return true;// 消费该监听事件，不再传递；
            case R.id.fl_main2:
                inclue.setVisibility(View.GONE);
            case R.id.expandlist:
                inclue.setVisibility(View.GONE);
        }
        return false;
    }

    private void saoyisao() {
        try {
            // 打开扫描界面扫描条形码或二维码
            Intent openCameraIntent = new Intent(getActivity(),
                    CaptureActivity.class);
            startActivityForResult(openCameraIntent, 123);
            getActivity().overridePendingTransition(R.anim.aty_zoomin,
                    R.anim.aty_zoomout);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }

    private ChildItem item;// 长按的item;
    private int groupId;// 长按选中的item的组的id;
    private int groupPosition;// 长按选中的item组的位置；

    @Override
    public boolean onLongClick(final View v) {

        dialog.show();
        Object[] obj = (Object[]) v.getTag();
        item = (ChildItem) obj[0];
        groupPosition = (int) obj[1];
        groupId = groups.get(groupPosition).getId();

        return true;
    }

    private void deleteFriend() {

        MyAleartDialog dialog = new MyAleartDialog("删除好友", "确定删除吗？",
                getActivity(), new AlertCallBack() {

            @Override
            public void onOK() {
                delete();
            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show();
    }

    // 删除好友：
    private void delete() {
        String url = UrlTools.url + UrlTools.DELETE_FRIEND;
        RequestParams params = new RequestParams();
        params.put("friend_id", item.getId());

        Utils.doPost(LoadingDialog.getInstance(getActivity()), getActivity(),
                url, params, new HttpCallBack() {

                    @Override
                    public void success(JsonBean bean) {
                        T.showShort(getActivity(), "删除好友成功！");
                        // TODO 获取该分组下的所有好友信息：
                        requestGroup();
                        requestFriend(groupPosition);
                    }

                    @Override
                    public void failure(String msg) {
                        T.showShort(getActivity(), msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == rl_chuangjian.getId()) {
            // 工作圈：
            inclue.setVisibility(View.GONE);
            startActivity(new Intent(getActivity(), WorkCircleActivity.class));
            getActivity().overridePendingTransition(R.anim.aty_zoomin,
                    R.anim.aty_zoomout);
        } else if (v.getId() == rl_qunliebiao.getId()) { //获取群列表
            inclue.setVisibility(View.GONE);
            startActivity(new Intent(getActivity(), TribeActivity.class));
            getActivity().overridePendingTransition(R.anim.aty_zoomin,
                    R.anim.aty_zoomout);
        } else if (v.getId() == rl_newfriend.getId()) {
            inclue.setVisibility(View.GONE);
            Intent intent = new Intent(getActivity(), NewFriendActivity.class);

            if (josnbean1 != null) {
                Log.e("联系人页面", "josnbean1: " + josnbean1.toString() + "");
                intent.putExtra("bean", josnbean1);
            }
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
        }else {
            int[] i = ((ChildItem) ((Object[]) v.getTag())[0]).getPosition();
            int gp = i[0];
            int cp = i[1];
            String phone = map.get(gp).get(cp).getPhone();
            String name = map.get(gp).get(cp).getTitle();
            chat(phone, name);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (requestCode == 123) {
                Toast.makeText(getActivity(), "扫描成功！", Toast.LENGTH_SHORT).show();
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("result");
                if (Utils.isPhone(scanResult)) {
                    search(scanResult);
                } else if (scanResult.charAt(0) == 'T') {
                    addTeam(scanResult.substring(1, scanResult.length()));// 加入团队；
                } else {
                    Toast.makeText(getActivity(), "无法识别该二维码！", Toast.LENGTH_SHORT).show();
                }
                Log.e("TAG", scanResult);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // 加入团队：
    @SuppressLint("ShowToast")
    private void addTeam(String id) {
        Intent intent = new Intent(getActivity(), JiaRuTuanDuiActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.aty_zoomin,
                R.anim.aty_zoomout);
    }

    // 下拉刷新时执行该方法：
    @Override
    public void onRefresh() {
        requestGroup();// 获取分组；
        // 合上：
        for (int i = 0; i < groups.size(); i++) {
            expandlist.collapseGroup(i);
        }
        getNewFriendNum();
    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("tag", "regist: --------------添加好友--------------------");
            requestGroup();// 获取分组；
            getNewFriendNum();// 获取新申请好友个数；
        }
    }

    private void initView() {
        // 设置头部
        initTitle(view);

        setCenterString("联系人");
        setRightDrawable(R.drawable.jia);
        setLeftDrawable(R.drawable.homepage_l);
        ll_tianjia = (LinearLayout) view.findViewById(R.id.ll_tianjia);

        ll_quntalk = (LinearLayout) view.findViewById(R.id.ll_quntalk); //不用
        ll_taolun = (LinearLayout) view.findViewById(R.id.ll_taolun);  //发起群聊
//    ll_taolun.setVisibility(View.INVISIBLE);

        inclue = (LinearLayout) view.findViewById(R.id.inclue_add);
        ll_qunliao = (LinearLayout) view.findViewById(R.id.ll_qunliao);
        ll_saosao = (LinearLayout) view.findViewById(R.id.ll_saoyisao);
        swipe_container = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        header_view = getActivity().getLayoutInflater().inflate(
                R.layout.lianxiren_lv_header, null);
        et_sousuo = (EditText) header_view
                .findViewById(R.id.et_sousuolianxiren);
        et_sousuo.setHint("名字/手机号/关键词");
        // iv_hongdian = (ImageView) view.findViewById(R.id.iv_hongdian);
        tv_num = (TextView) header_view.findViewById(R.id.tv_num);
        rl_chuangjian = (RelativeLayout) header_view.findViewById(R.id.rl_chuangjian);
        rl_qunliebiao = (RelativeLayout) header_view.findViewById(R.id.rl_qunliebiao);
        fl_main2 = (FrameLayout) view.findViewById(R.id.fl_main2);
        rl_newfriend = (RelativeLayout) header_view
                .findViewById(R.id.rl_newfriend);
        ArrayList<String> list = new ArrayList<>();
        list.add("删除好友");
        list.add("修改分组");
        list.add("修改备注");
        dialog = new SelectZTDialog(getActivity(), "好友管理", list, this);
    }

    private void addData(JsonBean bean) {
        ArrayList<HashMap<String, Object>> info = bean.getInfor();
        Log.e("TAG", info.size() + "info.size");
        if (info != null) {
            lxrs01.removeAll(lxrs01);
            for (int i = 0; i < info.size(); i++) {
                HashMap<String, Object> map = info.get(i);
                LianXiRen lxr = new LianXiRen((String) map.get("staff_name"),
                        (String) map.get("phone"), (String) map.get("type"),
                        (String) map.get("staff_head"));
                lxrs01.add(lxr);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // 组展开时：
    @Override
    public void onGroupExpanded(final int groupPosition) {
        // TODO 请求该分组下的好友信息：
        requestFriend(groupPosition);
    }

    // 当分组展开时，请求好友信息：
    private void requestFriend(final int groupPosition) {
        String url = UrlTools.url + UrlTools.GET_CHILD_OF_GROUP;
        RequestParams params = new RequestParams();
        params.put("group_id", ((Group) adapter.getGroup(groupPosition)).getId() + "");
        L.e("获取某组的好友url" + url + " params" + params);
        Utils.doPost(null, getActivity(), url, params, new HttpCallBack() {

            @Override
            public void success(JsonBean bean) {
                Log.e("TAG", "success");
                ArrayList<HashMap<String, Object>> infor = bean.getInfor();
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
                    if ("".equals(remark)) {
                        ci.setTitle(name);
                    } else {
                        ci.setTitle(remark);
                    }
                    ci.setPerson_id(person_id);
                    cis.add(ci);
                }
                map.put(groupPosition, cis);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(String msg) {
                T.showShort(getActivity(), msg);
            }

            @Override
            public void finish() {
                // TODO Auto-generated method stub

            }
        });
    }

    // 组合上时：
    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    private ArrayList<String> groupName = new ArrayList<>();

    @Override
    public void onOK(String s) {
        if ("修改备注".equals(s)) {
            Log.e("联系人页面修改备注", "item.getPhone(): " + item.getPhone().toString());
            Log.e("联系人页面修改备注", "item: " + item.toString());
            AppStore.phone = item.getPhone();
            Log.e("联系人页面", "item.getPhone(): " + item.getPerson_id());
            XiuGaiDialog tianjia = new XiuGaiDialog("修改备注", item.getId()
                    + "", "请修改备注名", getActivity(), new AlertCallBack1() {

                @Override
                public void onOK(final String name) {
                    Log.e("联系人页面", "item.getPhone(): " + item.getPhone().toString());

                    // 如果内存缓存中存在该用户，则修改内存缓存中该用户的备注：
                    if (MyUserProfileSampleHelper.mUserInfo.containsKey(item.getPhone())) {

                        IYWContact iywContact = MyUserProfileSampleHelper.mUserInfo.get(item.getPhone());

                        IYWContact contact = new UserInfo(name, iywContact.getAvatarPath(), iywContact.getUserId(), iywContact.getAppKey());

                        Log.e("联系人页面", "name: " + name);

                        MyUserProfileSampleHelper.mUserInfo.remove(item.getPhone()); // 移除临时的IYWContact对象
                    }

                    requestFriend(groupPosition);
                }

                @Override
                public void onCancel() {

                }
            });

            tianjia.show();
        } else if ("删除好友".equals(s)) {
            deleteFriend();
        } else if ("修改分组".equals(s)) {
            groupName.removeAll(groupName);
            for (int i = 0; i < groups.size(); i++) {
                groupName.add(groups.get(i).getName());
            }
            ChoiceGroupDialog dialog = new ChoiceGroupDialog(new CallBack() {
                @Override
                public void OnResultMsg(String res) {
                    int pos = 0;
                    for (int i = 0; i < groups.size(); i++) {
                        if (res.equals(groupName.get(i))) {
                            pos = i;
                            break;
                        }
                    }
                    // 修改分组：
                    String url = UrlTools.url + UrlTools.CHANGE_GROUP;
                    RequestParams params = new RequestParams();
                    params.put("person_id", item.getPerson_id() + "");
                    params.put("group_id", groups.get(pos).getId() + "");
                    Log.e("id", "OnResultMsg:------------------ "+ params );
                    Utils.doPost(null, getActivity(), url, params,
                            new HttpCallBack() {
                                @Override
                                public void success(JsonBean bean) {
                                    Toast.makeText(getActivity(), "转移成功！", Toast.LENGTH_SHORT)
                                            .show();
                                    requestGroup();// 获取分组信息；
                                }

                                @Override
                                public void failure(String msg) {
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT)
                                            .show();
                                }

                                @Override
                                public void finish() {
                                    // TODO Auto-generated method stub

                                }
                            });

                }
            }, getActivity(), groupName, "选择分组");
            dialog.show();
        }
    }

    // 添加分组：
    private void addGroup() {
        XiuGaiDialog tianjia = new XiuGaiDialog("添加分组", "添加", "输入新增组名",
                getActivity(), new AlertCallBack1() {
            @Override
            public void onOK(final String name) {
                requestGroup();
            }

            @Override
            public void onCancel() {
            }
        });
        tianjia.show();
    }

    @Override
    public void onCancel() {

    }

    Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0){
                BaiDuUtil.initLocation(getActivity(), new BDLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {

                        if (bdLocation != null && bdLocation.getLocType() != BDLocation.TypeServerError){
                            if (stringBuffer.length() > 0){
                                stringBuffer.delete(0,stringBuffer.length());
                            }
                            stringBuffer.append(bdLocation.getLatitude());//纬度
                            stringBuffer.append(",");
                            stringBuffer.append(bdLocation.getLongitude());//经度
                        }

                    }

                    @Override
                    public void onConnectHotSpotMessage(String s, int i) {}
                });
            }else {
                Log.e("好友数量", "msg.obj=" + msg.obj.toString());
                if ((int) msg.obj > 0) {
                    tv_num.setVisibility(View.VISIBLE);
                    tv_num.setText(msg.obj + "");
                    friendNumListener.newFriendNumChanged((int) msg.obj);
                } else {
                    tv_num.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

}
