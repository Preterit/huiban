package com.feirui.feiyunbangong.activity;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.IYWPushListener;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.feirui.feiyunbangong.AddShopActivity;
import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog;
import com.feirui.feiyunbangong.dialog.MyAleartDialog.AlertCallBack;
import com.feirui.feiyunbangong.dialog.UseMessageDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.MyUser;
import com.feirui.feiyunbangong.entity.ShowAppCountBean;
import com.feirui.feiyunbangong.fragment.Fragment1;
import com.feirui.feiyunbangong.fragment.Fragment2;
import com.feirui.feiyunbangong.fragment.Fragment3;
import com.feirui.feiyunbangong.fragment.Fragment4;
import com.feirui.feiyunbangong.im.MyUserProfileSampleHelper;
import com.feirui.feiyunbangong.myinterface.AllInterface.OnNewFriendNumChanged;
import com.feirui.feiyunbangong.myinterface.AllInterface.OnTeamNoticeNumChanged;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.ImmersedStatusbarUtils;
import com.feirui.feiyunbangong.utils.JPushUtil;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.NumberBadger.BadgeNumberManager;
import com.feirui.feiyunbangong.utils.NumberBadger.BadgeNumberManagerXiaoMi;
import com.feirui.feiyunbangong.utils.NumberBadger.MobileBrand;
import com.feirui.feiyunbangong.utils.SPUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UpdateManager;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.utils.Utils.HttpCallBack;
import com.feirui.feiyunbangong.view.CircleImageView;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.view.SelectRemindPopupWindow;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.feirui.feiyunbangong.state.AppStore.mIMKit;

/**
 * 主页面设置四个fragment跳转F
 *
 * @author feirui1
 */
public class MainActivity extends BaseActivity
        implements OnItemClickListener, OnTeamNoticeNumChanged, OnNewFriendNumChanged ,OnClickListener{
    LinearLayout[] btnArray = new LinearLayout[4];

    private Fragment work, linkman, message, circle;
    Fragment[] fragmentArray = null;

    UpdateManager manager;

    private ImageView iv_erweima;
    private SelectRemindPopupWindow window;// 弹出图片选择框；
    /**
     * 当前显示的fragment
     */
    int currentIndex = 0;
    /**
     * 选中的button,显示下一个fragment
     */
    int selectedIndex;

    @PView
    public DrawerLayout drawerlayout;
    @PView
    private ListView lv_left;
    //private ArrayAdapter adapter;
    private SimpleAdapter adapter;

    @PView
    private LinearLayout inclue;

    @PView
    private ListView lv_right;

    private TextView tv_name;
    private CircleImageView iv_head;

    private TextView tv_num, tv_num_team, tv_num_contact,tv_num_hb;// 会话列表会话数量；团队公告数量；联系人消息数量；
    private int num;//

    public void openLeft() {
        this.drawerlayout.openDrawer(Gravity.LEFT);
    }

    public void openRight() {
        // this.drawerlayout.openDrawer(Gravity.RIGHT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置titlebar;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        // 设置沉浸式状态栏：
        ImmersedStatusbarUtils.initAfterSetContentView(this, findViewById(R.id.rl));

        JPushUtil.jOnCreate(this);
        JPushUtil.setJPSH(this);

        initUI();
        try {
            setupView();
            addListener();
            setListView();
            loginALi();
            getWork_Num();//设置工作页面角标

        } catch (Exception e) {
            Log.e("Main页面", e.getMessage() + "/登录阿里百川等方法/");
        }
        //检查有没有设置头像
        check();

    }

    private void getUser() {
        RequestParams params=new RequestParams();
        AsyncHttpServiceHelper.post(UrlTools.url + UrlTools.DETAIL_ME, params,new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                JsonBean bean = JsonUtils.getMessage(new String(arg2));
                if ("200".equals(bean.getCode())) {
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = bean;
                    handler.sendMessage(msg);
                } else {
                }
                super.onSuccess(arg0, arg1, arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        });
    }

    private void initUI() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_head = (CircleImageView) findViewById(R.id.iv_head);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_num_team = (TextView) findViewById(R.id.tv_num_team);
        tv_num_contact = (TextView) findViewById(R.id.tv_num_contact);
        tv_num_hb= (TextView) findViewById(R.id.tv_num_hb);

    }

    @Override
    protected void onResume() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUser();
            }
        }).start();

        //极光推送
        JPushUtil.jOnResume(this);
        super.onResume();
        getWork_Num();//设置工作页面角标
    }

    @Override
    protected void onPause() {
        JPushUtil.jOnPause(this);
        super.onPause();
    }

    /**
     *登录阿里百川
     */
    private void loginALi() {
        // 此实现不一定要放在Application onCreate中
        final String userid = (String) AppStore.user.getInfor().get(0).get("staff_mobile");

        // 此对象获取到后，保存为全局对象，供APP使用
        // 此对象跟用户相关，如果切换了用户，需要重新获取
        mIMKit = YWAPI.getIMKitInstance(userid, Happlication.APP_KEY);
        // 自定义用户事件：
        MyUserProfileSampleHelper.activity = this;
        MyUserProfileSampleHelper.initProfileCallback();

        // 开始登陆IM:
        String password = (String) AppStore.user.getInfor().get(0).get("staff_password");
        Log.e("tag", "loginALi: =-------------------" + password);

        IYWLoginService loginService = mIMKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
        Log.e("tag", "loginALi: =-------------------" + loginParam );
        loginService.login(loginParam, new IWxCallback() {
            @Override
            public void onSuccess(Object... arg0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", "登录阿里百川成功！！！！！！！！！！！！！！！！！！！！！");
                        // 发出登录成功的广播：
                        MainActivity.this.sendBroadcast(new Intent().setAction(Constant.ON_LOGIN_ALI_SUCCESS));
                    }
                });
            }

            @Override
            public void onProgress(int arg0) {
            }

            @Override
            public void onError(int errCode, String description) {
            }
        });

        // 监听消息接收。。。。。。。显示在导航栏的消息处
        IYWPushListener msgPushListener = new IYWPushListener() {
            @Override
            public void onPushMessage(IYWContact arg0, YWMessage arg1) {
                // 未读消息数；
                int num = mIMKit.getUnreadCount();
                if (num > 0) {
                    tv_num.setVisibility(View.VISIBLE);
                    tv_num.setText("" + num);
                } else {
                    tv_num.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPushMessage(YWTribe arg0, YWMessage arg1) {

            }
        };

       final IYWConversationService conversationService = mIMKit.getConversationService();
        // 如果之前add过，请清除
        conversationService.removePushListener(msgPushListener);
        // 增加新消息到达的通知
        conversationService.addPushListener(msgPushListener);

        // 未读数量变化监听：
        conversationService.addTotalUnreadChangeListener(new IYWConversationUnreadChangeListener() {
            @Override
            public void onUnreadChange() {
                getNum();
            }
        });

//        getNum();
    }


    //获取工作页面的待审批未读数
    private void getWork_Num(){
        RequestParams params = new RequestParams();
        String url = UrlTools.url + UrlTools.APPROVAL_TASK;
        AsyncHttpServiceHelper.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                Gson gson = new Gson();
                ShowAppCountBean count = gson.fromJson(new String(responseBody), ShowAppCountBean.class);
                Log.e("审批界面--count", "Infor: "+count.getInfo().getNum());
                Message message = new Message();
                message.obj = count.getInfo().getNum();
                message.what = 2;
                handler.sendMessage(message);

    }
});
        Log.e("主页面", "Work_Numk: "+num );
//        return num;
    }

    // 获取未读消息数：
    private void getNum() {
        // 未读消息数；
        int num = mIMKit.getUnreadCount();
        Message message = new Message();
        message.obj = num;
        message.what = 1;
        handler.sendMessage(message);

        if (num > 0) {
            String str = "";
            if (num > 99) {
                str = "99+";
            } else {
                str = num + "";
            }
            tv_num.setVisibility(View.VISIBLE);
            tv_num.setText("" + str);
        } else {
            tv_num.setVisibility(View.INVISIBLE);
        }
    }
    //侧滑菜单的item设置
    private void setListView() {
        adapter = new SimpleAdapter(this,getData(), R.layout.lv_item_gerenzhongxin,
                new String[]{"tv_grzx","img_grzx"},
                new int[]{R.id.tv_grzx,R.id.img_grzx});
        lv_left.setAdapter(adapter);
		/*
         * adapter = new ArrayAdapter<>(this, R.layout.lv_item_gerenzhongxin,
		 * R.id.tv, new String[] { "消息通知" }); lv_right.setAdapter(adapter);
		 */
    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tv_grzx", "个人资料");
        map.put("img_grzx", R.drawable.gerenziliao);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("tv_grzx", "邀请注册");
        map.put("img_grzx", R.drawable.yaoqingjiangli);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("tv_grzx", "电话帮助");
        map.put("img_grzx", R.drawable.shiliang);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("tv_grzx", "我的钱包");
        map.put("img_grzx", R.drawable.wallet_icon);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("tv_grzx", "系统设置");
        map.put("img_grzx", R.drawable.shezhi);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("tv_grzx", "退出登录");
        map.put("img_grzx", R.drawable.icon_exit);
        list.add(map);

        return list;
    }

    private void setupView() {

        btnArray[0] = (LinearLayout) findViewById(R.id.fragment1);
        btnArray[1] = (LinearLayout) findViewById(R.id.fragment2);
        btnArray[2] = (LinearLayout) findViewById(R.id.fragment3);
        btnArray[3] = (LinearLayout) findViewById(R.id.fragment4);
        btnArray[0].setSelected(true);

        work = new Fragment1();
        linkman = new Fragment2();
        ((Fragment2) linkman).setFriendNumListener(this);

        message = new Fragment3(this);
        circle = new Fragment4(this);

        fragmentArray = new Fragment[]{message, circle, linkman, work};

        // 一开始，显示第一个fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.fragment_container, message);
        transaction.show(message);
        transaction.commit();
    }



    class MyButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            //关闭手势滑动
            drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            try {
                switch (v.getId()) {
                    case R.id.fragment1:
                        selectedIndex = 0;
                        //打开手势滑动
                        drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        break;
                    case R.id.fragment2:
                        selectedIndex = 1;
                        //打开手势滑动
                        drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        break;
                    case R.id.fragment3:
                        selectedIndex = 2;
                        //打开手势滑动
                        drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                        break;
                    case R.id.fragment4:
                        selectedIndex = 3;
                        //打开手势滑动
                        drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                        break;
                }

                // 判断单击是不是当前的
                if (selectedIndex != currentIndex) {
                    // 不是当前的
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    // 当前hide
                    transaction.hide(fragmentArray[currentIndex]);
                    // show你选中

                    if (!fragmentArray[selectedIndex].isAdded()) {
                        // 以前没添加过
                        transaction.add(R.id.fragment_container, fragmentArray[selectedIndex]);
                    }
                    // 事务
                    transaction.show(fragmentArray[selectedIndex]);
                    transaction.commit();
                    btnArray[currentIndex].setSelected(false);
                    btnArray[selectedIndex].setSelected(true);
                    currentIndex = selectedIndex;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addListener() {
        MyButtonListener myButtonListener = new MyButtonListener();
        for (int i = 0; i < btnArray.length; i++) {
            btnArray[i].setOnClickListener(myButtonListener);
        }
        lv_left.setOnItemClickListener(this);
        inclue.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_left:
                switch (position) {
                    case 0:
                        // 个人资料：
                        Intent intent2 = new Intent(MainActivity.this, PersonDataActivity.class);
                        intent2.putExtra("friend",2);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                        break;
                    case 1:
                        // 邀请注册
                        startActivity(new Intent(MainActivity.this, YaoQingActivity.class));
                        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);

                        break;
                    case 2:
                        // 电话帮助
                        try {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:13366664598"));
                            startActivity(intent);
                            overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        break;
                    case 3:
                        // 意见反馈：
//                        startActivity(new Intent(MainActivity.this, YiJianActivity.class));
//                        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, SheZhiActivity.class));
                        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                        break;
                    case 5:
                        // 退出账号登录：
                        outLogin();
                        break;
                    case 6:
                        // 添加员工：改为代注册
                        startActivity(new Intent(MainActivity.this, AddYuanGongActivity.class));
                        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                        break;
                    case 7:
                        // 关于我们
                        UseMessageDialog dialog1 = new UseMessageDialog(this);
                        dialog1.show();
                        break;
                    case 8:
                        //修改密码
                        startActivity(new Intent(MainActivity.this, ForgetPasswordActivity.class));
                        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                        break;
                    case 9:
                        // 清理缓存：
                        ImageLoader.getInstance().clearMemoryCache();// 清除内存图片；
                        ImageLoader.getInstance().clearDiscCache();// 清除sd卡图片；
                        T.showShort(this, "清理完成！");
                        break;

                }
                break;

            case R.id.lv_right:
                break;

        }
    }



    private void  toMyShpOrAddShop() {
        // TODO Auto-generated method stub

        String postUrl = UrlTools.url + UrlTools.XIAODIAN_MY_SHOP;
        RequestParams requestParams = new RequestParams();

        Utils.doPost(LoadingDialog.getInstance(this), this, postUrl, requestParams, new HttpCallBack() {

            @Override
            public void success(JsonBean bean) {
                // TODO Auto-generated method stub
                if (bean.getInfor() == null) {
                    // 没有小店
                    Intent intent = new Intent(MainActivity.this, AddShopActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                } else {
                    // 有小店
                    Intent intent = new Intent(MainActivity.this, MyShopActivity.class);
                    Log.e("个人小店json_bean", "json_bean: "+bean );
                    intent.putExtra("json_bean", bean);
                    startActivity(intent);
                    overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);

                }
            }

            @Override
            public void finish() {
                // TODO Auto-generated method stub

            }

            @Override
            public void failure(String msg) {
                // TODO Auto-generated method stub

            }
        });
    }

    // 退出登录：
    private void outLogin() {

        MyAleartDialog dialog = new MyAleartDialog("退出登录", "确定退出吗？", this, new AlertCallBack() {

            @Override
            public void onOK() {
                // 阿里百川退出：
                IYWLoginService loginService = mIMKit.getLoginService();
                loginService.logout(new IWxCallback() {

                    @Override
                    public void onSuccess(Object... arg0) {
                        Log.e("TAG", "退出阿里成功！");
                        //必须结束 否则在登录后会再出现主页面
                        finish();
                    }

                    @Override
                    public void onProgress(int arg0) {
                    }

                    @Override
                    public void onError(int errCode, String description) {
                    }
                });

                // 退出自己的服务器：
                String url = UrlTools.url + UrlTools.OUT;
                Utils.doPost(LoadingDialog.getInstance(MainActivity.this), MainActivity.this, url, null,
                        new HttpCallBack() {
                            @Override
                            public void success(JsonBean bean) {
                                SPUtils.remove(MainActivity.this, Constant.SP_ALREADYUSED);// 移除偏好设置文件对应的键值对；
                                T.showShort(MainActivity.this, "退出成功！");
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                                finish();
//                                Happlication.getActivities().remove(this);
                            }

                            @Override
                            public void finish() {

                            }

                            @Override
                            public void failure(String msg) {
                                T.showShort(MainActivity.this, msg);
                            }
                        });
            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show();
    }

    long waitTime = 3000;
    long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            onkey();
        }
        return false;
    }

    public void onkey() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            T.showShort(this, "再按一次退出！");
            touchTime = currentTime;
        } else {
            out();
        }
    }

    // 退出阿里百川：
    private void outALI() {
    }

    // 退出登录：
    private void out() {
        String url = UrlTools.url + UrlTools.OUT;
        RequestParams params=new RequestParams();
        AsyncHttpServiceHelper.post(url, params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                finish();
                super.onSuccess(arg0, arg1, arg2);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        });
    }

    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    final ArrayList<String> imageUrls = new ArrayList<String>();
                    JsonBean bean = (JsonBean) msg.obj;
                    ArrayList<HashMap<String, Object>> infor = bean.getInfor();
                    HashMap<String, Object> hashMap = infor.get(0);
                    Log.e("tag", "handleMessage: ---------------------" + hashMap );

                    String name = String.valueOf(hashMap.get("staff_name"));
                    String duty = String.valueOf(hashMap.get("staff_duties"));
                    String department = String.valueOf(hashMap.get("staff_department"));
                    if (duty != null && duty.length() > 0 && !"null".equals(duty)) {
                        tv_name.setText(name + "(" + duty + ")");
                    } else {
                        tv_name.setText(name);
                    }

                    String head = String.valueOf(hashMap.get("staff_head"));
                    String sex = String.valueOf(hashMap.get("sex"));
                    String address = String.valueOf(hashMap.get("address"));
                    String birthday = String.valueOf(hashMap.get("birthday"));
                    String phone = String.valueOf(hashMap.get("staff_mobile"));

                    String id = String.valueOf(hashMap.get("id"));

                    // TODO: 2017/3/15 id

                    if ("img/1_1.png".equals(head) || "1".equals(head)) {
                        iv_head.setImageResource(R.drawable.fragment_head);
                    } else {
                        ImageLoader.getInstance().displayImage(head, iv_head);
                    }

                    imageUrls.add(head);
                    //侧滑菜单头像的点击事件
                    iv_head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(MainActivity.this, PersonDataActivity.class);
                            intent2.putExtra("friend",2);
                            startActivity(intent2);
                            overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                        }
                    });


                    MyUser user = new MyUser(name, duty, head, department, sex, birthday, address, phone);
                    user.setId(id);
                    AppStore.myuser = user;
                    break;
                case 1:
                    Log.e("获取未读消息数----------------", "getNum:-------------- "+(int) msg.obj);
                    //设置桌面图标提示

                    //设置应用在桌面上显示的角标
                    if (!Build.MANUFACTURER.equalsIgnoreCase(MobileBrand.XIAOMI)) {
                        BadgeNumberManager.from(MainActivity.this).setBadgeNumber((int) msg.obj);
                    }else {
                        if ((int) msg.obj > 0){
                            setXiaomiBadgeNumber((int) msg.obj - 1);
                        }

                        mIMKit.setShortcutBadger((int) msg.obj);
                    }
                    break;
                case 2:

                    Log.e("设置工作页面数字", "getNum_work:-------- "+msg.obj);
                    if(!(msg.obj).equals("0")){
                        tv_num_hb.setVisibility(View.VISIBLE);
                        tv_num_hb.setText(msg.obj+"");
                    }else{
                        tv_num_hb.setVisibility(View.INVISIBLE);
                    }

                    break;
            }
        }


    };

    private void setXiaomiBadgeNumber(int num) {
        NotificationManager notificationManager = (NotificationManager) MainActivity.this.
                getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(MainActivity.this.getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("")
                .setContentText("收到消息啦~")
                .setTicker("ticker")
                .setAutoCancel(true)  //点击通知后自动清除
                .build();
        BadgeNumberManagerXiaoMi.setBadgeNumber(notification, num);
        notificationManager.notify(500, notification);

    }

    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(MainActivity.this, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
        overridePendingTransition(R.anim.aty_zoomin,
                R.anim.aty_zoomout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void changed(int number) {
        Log.e("TAG", number + "number");
        if (number > 0) {
            tv_num_team.setVisibility(View.VISIBLE);
            if (number > 99) {
                tv_num_team.setText(99 + "+");
            } else {
                tv_num_team.setText(number + "");
            }
        } else {
            tv_num_team.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void newFriendNumChanged(int number) {
        if (number > 0) {
            tv_num_contact.setVisibility(View.VISIBLE);
            tv_num_contact.setText(number + "");
        } else {
            tv_num_contact.setVisibility(View.INVISIBLE);
        }

    }

    private void check(){
        final String[] head = new String[6];
        String url = UrlTools.url + UrlTools.DETAIL_ME;
        RequestParams params = new RequestParams();
        AsyncHttpServiceHelper.post(url,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                JsonBean bean = JsonUtils.getMessage(new String (responseBody));
                Log.e("主页面", "bean: "+bean.toString() );
                if ("200".equals(bean.getCode())){
                    HashMap<String,Object> infor = bean.getInfor().get(0);
                    head[0] =String.valueOf(infor.get("staff_head"));
                    head[1] =String.valueOf(infor.get("staff_key1"));
                    head[2] =String.valueOf(infor.get("staff_key2"));
                    head[3] =String.valueOf(infor.get("staff_key3"));
                    head[4] =String.valueOf(infor.get("staff_key4"));
                    head[5] =String.valueOf(infor.get("staff_key5"));
                    setWindow(head);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
            }
        });


    }

    private void setWindow(String[] head){
        if(head[0].isEmpty()||(head[1].isEmpty()&&head[2].isEmpty()&&head[3].isEmpty()&&head[4].isEmpty()&&head[5].isEmpty())){
            window = new SelectRemindPopupWindow(MainActivity.this,this);
            // 显示窗口
            window.showAtLocation(findViewById(R.id.rl),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gosit:
                window.dismiss();
                Intent intent2 = new Intent(MainActivity.this, PersonDataActivity.class);
                intent2.putExtra("friend",2);
                startActivity(intent2);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);

                break;
            case R.id.noremind:
                window.dismiss();
                break;
        }

    }
}
