package com.feirui.feiyunbangong.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
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

import static com.baidu.platform.comapi.util.e.w;

/**
 * Created by xy on 2017-10-20.
 */

public class PersonDataActivity extends BaseActivity implements OnClickListener {
    private CircleImageView mCir_head;
    private TextView mTv_name,mTv_key1,mTv_key2,mTv_key3;
    private ImageView mIv_sex,mShop;
    private TextView mTv_birthday;
    private LinearLayout mLl_er_wei_ma;
    private LinearLayout mLl_person_phone;
    private TextView mTv_bian_phone;
    private LinearLayout mLl_person_area;
    private TextView mTv_person_area;
    private Button mPerson_talk;
    private Button mPerson_add;

    private TuanDuiChengYuan mTdcy;
    private int code;
    private List<Group> groups = new ArrayList<>();// 分组信息
    private ArrayList<String> group_name = new ArrayList<>();// 组名
    // 二维码名片：
    public  Bitmap erweima;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_person_data);
        Intent intent = getIntent();
        mTdcy = (TuanDuiChengYuan) intent.getSerializableExtra("tdcy");
        code = intent.getIntExtra("friend",-1);

        initUi();
        setListener();
        if (code == 1){
            updateState();// 更改新团员状态；
        }
        createErWeiMa();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
        requestGroup();// 获取分组信息；
    }

    private void initUi() {
        initTitle();
        setCenterString("个人资料");

        centerTv.setTextSize(18);
        setLeftDrawable(R.drawable.arrows_left);
        setRightVisibility(false);
        top.setBackgroundColor(getResources().getColor(R.color.bar));

        mCir_head = (com.feirui.feiyunbangong.view.CircleImageView) findViewById(R.id.cir_head);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mIv_sex = (ImageView) findViewById(R.id.iv_sex);
        mShop = (ImageView) findViewById(R.id.person_shop);
        mTv_birthday = (TextView) findViewById(R.id.tv_birthday);
        mLl_er_wei_ma = (LinearLayout) findViewById(R.id.ll_er_wei_ma);
        mLl_person_phone = (LinearLayout) findViewById(R.id.ll_person_phone);
        mTv_bian_phone = (TextView) findViewById(R.id.tv_bian_phone);
        mLl_person_area = (LinearLayout) findViewById(R.id.ll_person_area);
        mTv_person_area = (TextView) findViewById(R.id.tv_person_area);
        mPerson_talk = (Button) findViewById(R.id.person_talk);
        mPerson_add = (Button) findViewById(R.id.person_add);
        mTv_key1 = (TextView) findViewById(R.id.tv_key1);
        mTv_key2 = (TextView) findViewById(R.id.tv_key2);
        mTv_key3 = (TextView) findViewById(R.id.tv_key3);
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

        if (!"".equals(mTdcy.getKey1()) && null != mTdcy.getKey1()){
           mTv_key1.setText(mTdcy.getKey1());
        }else {
            mTv_key1.setText("UI设计");
        }
        if (!"".equals(mTdcy.getKey2()) && null != mTdcy.getKey2()){
            mTv_key2.setText(mTdcy.getKey2());
        }else {
            mTv_key2.setText("平面设计");
        }
        if (!"".equals(mTdcy.getKey3()) && null != mTdcy.getKey3()){
            mTv_key3.setText(mTdcy.getKey3());
        }else {
            mTv_key3.setText("网页设计");
        }

        if (mTdcy.getFriendstate() == 1){
            mPerson_add.setEnabled(false);
            mPerson_add.setText("已是好友");
            mPerson_add.setBackgroundColor(getResources().getColor(R.color.huise));
            mTv_bian_phone.setText(mTdcy.getPhone());
        }else {
            mPerson_add.setEnabled(true);
            mPerson_add.setText("加好友");
            mPerson_add.setBackgroundColor(getResources().getColor(R.color.juse));
            mTv_bian_phone.setText("好友可查看");

        }

    }

    private void setListener() {
        mShop.setOnClickListener(this);
        mLl_er_wei_ma.setOnClickListener(this);
        mLl_person_phone.setOnClickListener(this);
        mLl_person_area.setOnClickListener(this);
        mPerson_add.setOnClickListener(this);
        mPerson_talk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.person_shop:  //个人小店
                Log.e("团队成员资料页面", "Store_url: "+ mTdcy.getStore_url() );
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
                clickShow();
                break;
            case R.id.ll_person_phone: //好友电话
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
        }
    }

    private void clickShow() {
        PopWindow popWindow = new PopWindow(this,mTdcy,erweima);

//        Dialog dialog = new Dialog(this);
//        View v = getLayoutInflater().inflate(R.layout.ll_dialog_erweima, null);
//        ImageView iv = (ImageView) v.findViewById(R.id.iv_erweima2);
//        if (erweima != null) {
//            iv.setImageBitmap(erweima);
//        }
//        dialog.setContentView(v);
//        dialog.setTitle("扫我加好友");
//        dialog.show();
        popWindow.showPopupWindow();
    }

    private void createErWeiMa() {
        String phone = mTdcy.getPhone();
        Log.e("TAG", phone);
        // 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
        try {
            erweima = EncodingHandler.createQRCode(phone, 350);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void updateState() {
        // 老成员不需要更新状态；
        if (mTdcy.getState() == 2) {
            return;
        }
        String url = UrlTools.url + UrlTools.UPDATE_TEAM_STATE;
        RequestParams params = new RequestParams();
        params.put("team_member_list_id", mTdcy.getTeam_member_list_id() + "");
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params,
                new Utils.HttpCallBack() {

                    @Override
                    public void success(JsonBean bean) {
                        Log.e("团队成员界面", "成功"+bean.toString());
                    }

                    @Override
                    public void failure(String msg) {
                        Log.e("TAG", msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });

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
                        LoadingDialog.getInstance(PersonDataActivity.this),
                        PersonDataActivity.this, url, params,
                        new Utils.HttpCallBack() {

                            @Override
                            public void success(JsonBean bean) {
                                T.showShort(PersonDataActivity.this,
                                        "好友申请已发出！");
                                mPerson_add.setBackgroundColor(getResources()
                                        .getColor(R.color.huise));
                                mPerson_add.setText("申请中");
                                mPerson_add.setEnabled(false);
                            }

                            @Override
                            public void failure(String msg) {
                                T.showShort(PersonDataActivity.this, msg);
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
                        T.showShort(PersonDataActivity.this, msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }


}
