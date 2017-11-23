package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.mobileim.contact.IYWContact;
import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.dialog.XiuGaiDialog;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.MyUser;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.im.MyUserProfileSampleHelper;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.PopWindow;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.CircleImageView;
import com.google.zxing.WriterException;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zxing.encoding.EncodingHandler;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendInforDetailActivity extends BaseActivity implements View.OnClickListener {
    private CircleImageView mCir_head;
    private TextView mTv_name,mTv_key1,mTv_key2,mTv_key3,mTv_key4,mTv_key5,mTv_revise,mTv_revise_group;
    private ImageView mIv_sex,mShop;
    private TextView mTv_birthday;
    private LinearLayout mLl_er_wei_ma,mPerson_btn;
    private LinearLayout mLl_person_phone;
    private TextView mTv_bian_phone;
    private LinearLayout mLl_person_area,mLl_revise;
    private TextView mTv_person_area;
    private Button mPerson_talk;
    private Button mPerson_add;

    private List<Group> groups = new ArrayList<>();// 分组信息
    private ArrayList<String> group_name = new ArrayList<>();// 组名
    // 二维码名片：
    private Bitmap erweima;
    private String phone;//个人id
    private TuanDuiChengYuan mTdcy;
    private String mStaffId;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_friend_infor_detail);
        Intent intent = getIntent();
        //从聊天跳转过来
        phone = intent.getStringExtra("phone");
        Log.e("phone", "onCreate: --------------------" + phone );
        initUi();
        setListener();

    }


    @Override
    protected void onResume() {
        super.onResume();
        requestGroup();// 获取分组信息；
        getPersonDetail();
    }


    private void initUi() {
        initTitle();
        setCenterString("个人资料");

        centerTv.setTextSize(18);
        setLeftDrawable(R.drawable.arrows_left);

        top.setBackgroundColor(getResources().getColor(R.color.bar));

        mCir_head = (com.feirui.feiyunbangong.view.CircleImageView) findViewById(R.id.cir_head);
        mCir_head.setOnClickListener(this);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mIv_sex = (ImageView) findViewById(R.id.iv_sex);
        mShop = (ImageView) findViewById(R.id.person_shop);
        mTv_birthday = (TextView) findViewById(R.id.tv_birthday);
        mLl_er_wei_ma = (LinearLayout) findViewById(R.id.ll_er_wei_ma);
        mLl_person_phone = (LinearLayout) findViewById(R.id.ll_person_phone);

        mLl_revise = (LinearLayout) findViewById(R.id.ll_friend);

        mTv_bian_phone = (TextView) findViewById(R.id.tv_bian_phone);
        mLl_person_area = (LinearLayout) findViewById(R.id.ll_person_area);
        mTv_person_area = (TextView) findViewById(R.id.tv_person_area);
        mPerson_talk = (Button) findViewById(R.id.person_talk);
        mPerson_add = (Button) findViewById(R.id.person_add);
        mTv_key1 = (TextView) findViewById(R.id.tv_key1);
        mTv_key2 = (TextView) findViewById(R.id.tv_key2);
        mTv_key3 = (TextView) findViewById(R.id.tv_key3);
        mTv_key4 = (TextView) findViewById(R.id.tv_key4);
        mTv_key5 = (TextView) findViewById(R.id.tv_key5);

        mTv_revise = (TextView) findViewById(R.id.tv_revise);
        mTv_revise.setOnClickListener(this);
        mTv_revise_group = (TextView) findViewById(R.id.tv_revise_group);

        mPerson_btn = (LinearLayout) findViewById(R.id.person_btn);

        setRightVisibility(false);
        mPerson_btn.setVisibility(View.VISIBLE);
        mLl_revise.setVisibility(View.GONE);
    }


    private void initData() {
        if (!"null".equals(mTdcy.getHead()) && null != mTdcy.getHead()
                && !"img/1_1.png".equals(mTdcy.getHead())) {
            ImageLoader.getInstance().displayImage(mTdcy.getHead(), mCir_head);

        } else {
            mCir_head.setImageResource(R.drawable.fragment_head);
        }

        mTv_name.setText(mTdcy.getName());
        if (!"null".equals(mTdcy.getSex()) && null != mTdcy.getSex()){
            if ("女".equals(mTdcy.getSex())){
                mIv_sex.setImageResource(R.drawable.girl);
            }else {
                mIv_sex.setImageResource(R.drawable.boy);
            }
        }else {
            mIv_sex.setImageResource(R.drawable.boy);
        }

        if (!"null".equals(mTdcy.getBirthday()) && null != mTdcy.getBirthday()){
            mTv_birthday.setText(mTdcy.getBirthday());
        }else {
            mTv_birthday.setText("2000-01-02");
        }
        if (!"null".equals(mTdcy.getAddress()) && !"".equals(mTdcy.getAddress())){
            mTv_person_area.setText(mTdcy.getAddress());
        }else {
            mTv_person_area.setText("北京 朝阳");
        }

        if (!"".equals(mTdcy.getKey1()) && !"null".equals(mTdcy.getKey1())){
            mTv_key1.setText(mTdcy.getKey1());
            mTv_key1.setVisibility(View.VISIBLE);
        }else {
            mTv_key1.setVisibility(View.GONE);
        }
        if (!"".equals(mTdcy.getKey2()) && !"null".equals(mTdcy.getKey2())){
            mTv_key2.setText(mTdcy.getKey2());
            mTv_key2.setVisibility(View.VISIBLE);
        }else {
            mTv_key2.setVisibility(View.GONE);
        }
        if (!"".equals(mTdcy.getKey3()) && !"null".equals(mTdcy.getKey3())){
            mTv_key3.setText(mTdcy.getKey3());
            mTv_key3.setVisibility(View.VISIBLE);
        }else {
            mTv_key3.setVisibility(View.GONE);
        }

        if (!"".equals(mTdcy.getKey4()) && !"null".equals(mTdcy.getKey4())){
            mTv_key4.setText(mTdcy.getKey4());
            mTv_key4.setVisibility(View.VISIBLE);
        }else {
            mTv_key4.setVisibility(View.GONE);
        }
        if (!"".equals(mTdcy.getKey5()) && !"null".equals(mTdcy.getKey5())){
            mTv_key5.setText(mTdcy.getKey5());
            mTv_key5.setVisibility(View.VISIBLE);
        }else {
            mTv_key5.setVisibility(View.GONE);

        }
        if (mTdcy.getFriendstate() == 1){
            mPerson_add.setEnabled(false);
            mPerson_add.setText("已是好友");
            mLl_revise.setVisibility(View.VISIBLE);
            mPerson_add.setBackgroundColor(getResources().getColor(R.color.huise));
            mTv_bian_phone.setText(mTdcy.getPhone());
            mLl_person_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent3 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mTdcy.getPhone()));
                        startActivity(intent3);
                        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            });
        }else {
            mPerson_add.setEnabled(true);
            mLl_revise.setVisibility(View.GONE);
            mPerson_add.setText("加好友");
            mPerson_add.setBackgroundColor(getResources().getColor(R.color.juse));
            mTv_bian_phone.setText("好友可查看");

        }

    }

    /*
    获取个人信息
     */
    private void getPersonDetail() {

        RequestParams params = new RequestParams();
        params.put("key", phone + "");
        params.put("location", "");
        String url = UrlTools.url + UrlTools.USER_SEARCH_MOBILE;
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {

                if ("200".equals(bean.getCode())){
                    Message message = new Message();
                    message.obj = bean;
                    message.what = 0;
                    handler.handleMessage(message);
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

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                JsonBean bean = (JsonBean) msg.obj;
                HashMap<String,Object> infor = bean.getInfor().get(0);
                mStaffId = String.valueOf(infor.get("id"));
                String name = String.valueOf(infor.get("staff_name"));
                String head =String.valueOf(infor.get("staff_head"));
                String birth =String.valueOf(infor.get("birthday"));
                String sex =String.valueOf(infor.get("sex"));
                String shop_url =String.valueOf(infor.get("store_url"));
                String address =String.valueOf(infor.get("address"));
                String phone =String.valueOf(infor.get("staff_mobile"));
                String key1 =String.valueOf(infor.get("staff_key1"));
                String key2 =String.valueOf(infor.get("staff_key2"));
                String key3 =String.valueOf(infor.get("staff_key3"));
                String key4 =String.valueOf(infor.get("staff_key4"));
                String key5 =String.valueOf(infor.get("staff_key5"));
                String friendstate = String.valueOf(infor.get("is_friend"));
                String type = String.valueOf(infor.get("type"));
                String position = String.valueOf(infor.get("position"));
                String limit_position = String.valueOf(infor.get("limit_position"));
                mTdcy = new TuanDuiChengYuan(name,head,phone,shop_url,sex,birth,address,key1,key2,
                        key3,key4,key5,Integer.parseInt(friendstate),type,position,limit_position);
                initData();
                createErWeiMa(phone);
            }
        }
    };

    private void setListener() {
        mShop.setOnClickListener(this);
        mLl_er_wei_ma.setOnClickListener(this);
        mLl_person_area.setOnClickListener(this);
        mPerson_add.setOnClickListener(this);
        mPerson_talk.setOnClickListener(this);
        //修改
        rightIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.person_shop:  //个人小店
                if ( !"0".equals(mTdcy.getStore_url()) && !"null".equals(mTdcy.getStore_url())){
                    Intent intent1=new Intent();
                    intent1.putExtra("uri",mTdcy.getStore_url());//
                    intent1.putExtra("TAG","1");
                    intent1.setClass(getApplicationContext(),WebViewActivity.class);
                    startActivity(intent1);
                }else {
                    T.showShort(this,"还没有小店哦~");
                }
                break;
            case R.id.ll_er_wei_ma: //二维码
                clickShow(mTdcy);
                break;
            case R.id.tv_revise: //好友电话
                AppStore.phone = phone;
                Log.e("好友资料", "phone: "+phone.toString());
                Log.e("好友资料", "phone: "+ mStaffId);
                XiuGaiDialog tianjia = new XiuGaiDialog("修改备注", mStaffId
                        + "", "请输入新备注", FriendInforDetailActivity.this, new XiuGaiDialog.AlertCallBack1() {

                    @Override
                    public void onOK(final String name) {
                        Log.e("联系人页面", "name:--------------------- "+ MyUserProfileSampleHelper.mUserInfo.get(phone) );

                        // 如果内存缓存中存在该用户，则修改内存缓存中该用户的备注：
                        if (MyUserProfileSampleHelper.mUserInfo.containsKey(phone)) {
                            IYWContact iywContact = MyUserProfileSampleHelper.mUserInfo
                                    .get(phone);

                            IYWContact contact = new MyUserProfileSampleHelper.UserInfo(name, iywContact
                                    .getAvatarPath(), iywContact.getUserId(),
                                    iywContact.getAppKey());

                            Log.e("联系人页面", "name: "+name );

                            MyUserProfileSampleHelper.mUserInfo.remove(phone); // 移除临时的IYWContact对象
                            MyUserProfileSampleHelper.mUserInfo.put(phone, contact); // 保存从服务器获取到的数据
                        }

                    }

                    @Override
                    public void onCancel() {

                    }
                });

                tianjia.show();

                break;
            case R.id.ll_person_area: //所属地区
                break;
            case R.id.person_talk: //去聊天
                // 打开聊天窗口：
                Intent intent = AppStore.mIMKit.getChattingActivityIntent(
                        mTdcy.getPhone(), Happlication.APP_KEY);
                startActivity(intent);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                break;
            case R.id.person_add: //添加好友
                addFriend();// 添加好友；
                break;
            case R.id.cir_head: //点击查看头像
                final ArrayList<String> imageUrls = new ArrayList<String>();
                    if (!"null".equals(mTdcy.getHead()) && null != mTdcy.getHead()
                            && !"img/1_1.png".equals(mTdcy.getHead())) {
                        imageUrls.add(mTdcy.getHead());
                        imageBrower(0,imageUrls);

                    } else {
                        T.showShort(this,"主人还没有设置头像~");
                    }
                break;
            case R.id.rightIv://修改个人资料
                break;

        }
    }

    /*
    查看头像，点击放大的
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(FriendInforDetailActivity.this, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
        overridePendingTransition(R.anim.aty_zoomin,
                R.anim.aty_zoomout);
    }

    /*
    查看生成个人的二维码
     */
    private void clickShow(TuanDuiChengYuan tdcy) {
        PopWindow popWindow = new PopWindow(this,tdcy,erweima);
        popWindow.showPopupWindow();
    }

    private void createErWeiMa(String phone) {
        Log.e("TAG", phone);
        // 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
        try {
            erweima = EncodingHandler.createQRCode(phone, 350);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private void addFriend() {

        ChoiceGroupDialog dialog = new ChoiceGroupDialog(new ChoiceGroupDialog.CallBack() {

            @Override
            public void OnResultMsg(String res) {

                int pos = 0;
                for (int i = 0; i < groups.size(); i++) {
                    if (res.equals(group_name.get(i))) {
                        pos = i;
                        break;
                    }
                }

                String url = UrlTools.url + UrlTools.TIANJIAHAOYOU;
                RequestParams params = new RequestParams();
                params.put("group_id", groups.get(pos).getId() + "");
                L.e("添加好友xxxxxx：" + groups.get(pos).getId());
                params.put("group_id", groups.get(pos).getId() + "");
                params.put("staff_mobile", mTdcy.getPhone());


                L.e("添加好友：url " + url + "  params:" + params);
                Utils.doPost(
                        LoadingDialog.getInstance(FriendInforDetailActivity.this),
                        FriendInforDetailActivity.this, url, params,
                        new Utils.HttpCallBack() {

                            @Override
                            public void success(JsonBean bean) {
                                T.showShort(FriendInforDetailActivity.this,
                                        "好友申请已发出！");
                                mPerson_add.setBackgroundColor(getResources()
                                        .getColor(R.color.huise));
                                mPerson_add.setText("申请中");
                                mPerson_add.setEnabled(false);
                            }

                            @Override
                            public void failure(String msg) {
                                T.showShort(FriendInforDetailActivity.this, msg);
                            }

                            @Override
                            public void finish() {
                                // TODO Auto-generated method stub

                            }
                        });

            }
        }, this, group_name, "选择分组");

        dialog.show();
    }

    // 获取分组信息：
    private void requestGroup() {
        String url01 = UrlTools.url + UrlTools.GET_FRIEND_GROUP;
        Utils.doPost(LoadingDialog.getInstance(this), this, url01, null,
                new Utils.HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        ArrayList<HashMap<String, Object>> infor = bean
                                .getInfor();

                        groups.removeAll(groups);
                        group_name.removeAll(group_name);

                        for (int i = 0; i < infor.size(); i++) {
                            int id = (int) infor.get(i).get("id");
                            String name = infor.get(i).get("name") + "";
                            int default_num = (int) infor.get(i).get("default");
                            int count = (int) infor.get(i).get("count");
                            Group group = new Group(id, name, default_num,
                                    count);
                            groups.add(group);
                            group_name.add(group.getName());
                        }
                    }

                    @Override
                    public void failure(String msg) {
                        T.showShort(FriendInforDetailActivity.this, msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }

}
