package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.Happlication;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.ChoiceGroupDialog;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.Group;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.MyUser;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.PopWindow;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.CircleImageView;
import com.google.zxing.WriterException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zxing.encoding.EncodingHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamPersonActivity extends BaseActivity implements View.OnClickListener {
    private CircleImageView mCir_head;
    private TextView mTv_name,mTv_key1,mTv_key2,mTv_key3,mTv_key4,mTv_key5,mTv_postion,mTv_change_area;
    private ImageView mIv_sex,mShop;
    private TextView mTv_birthday;
    private LinearLayout mLl_er_wei_ma,mPerson_btn;
    private LinearLayout mLl_person_phone;
    private TextView mTv_bian_phone;
    private LinearLayout mLl_person_area;
    private TextView mTv_person_area;
    private Button mPerson_talk;
    private Button mPerson_add;

    private TuanDuiChengYuan mTdcy;
    private List<Group> groups = new ArrayList<>();// 分组信息
    private ArrayList<String> group_name = new ArrayList<>();// 组名
    // 二维码名片：
    private Bitmap erweima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_person);
        Intent intent = getIntent();
        //从团队成员跳转过来的
        mTdcy = (TuanDuiChengYuan) intent.getSerializableExtra("tdcy");
        initUi();
        setListener();
        updateState();// 更改新团员状态；
    }


    @Override
    protected void onResume() {
        super.onResume();
        requestGroup();// 获取分组信息；
        initData();
    }


    private void initUi() {
        initTitle();
        setCenterString("个人资料");

        centerTv.setTextSize(18);
        setLeftDrawable(R.drawable.arrows_left);

        top.setBackgroundColor(getResources().getColor(R.color.juse));

        mCir_head = (com.feirui.feiyunbangong.view.CircleImageView) findViewById(R.id.cir_head);
        mCir_head.setOnClickListener(this);
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
        mTv_key4 = (TextView) findViewById(R.id.tv_key4);
        mTv_key5 = (TextView) findViewById(R.id.tv_key5);
        mTv_postion = (TextView) findViewById(R.id.tv_postion);
        mTv_change_area = (TextView) findViewById(R.id.tv_change_area);
        mPerson_btn = (LinearLayout) findViewById(R.id.person_btn);

        setRightVisibility(false);
        mPerson_btn.setVisibility(View.VISIBLE);
        createErWeiMa(mTdcy.getPhone());
    }


    private void initData() {
        Log.e("postion", "getLimit_position: -------------------------" + mTdcy.getPosition() );
        if (!"null".equals(mTdcy.getHead()) && null != mTdcy.getHead()
                && !"img/1_1.png".equals(mTdcy.getHead())) {
            ImageLoader.getInstance().displayImage(mTdcy.getHead(), mCir_head);

        } else {
            mCir_head.setImageResource(R.drawable.fragment_head);
        }

        mTv_name.setText(mTdcy.getName());
        if (!"null".equals(mTdcy.getSex()) && null != mTdcy.getSex()){
            if ("女".equals(mTdcy.getSex())){
                mIv_sex.setImageResource(R.drawable.team_gril);
            }else {
                mIv_sex.setImageResource(R.drawable.team_boy);
            }
        }else {
            mIv_sex.setImageResource(R.drawable.team_boy);
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
            mPerson_add.setText("加好友");
            mPerson_add.setBackgroundColor(getResources().getColor(R.color.juse));
            mTv_bian_phone.setText("好友可查看");

        }

        if("0".equals(mTdcy.getType2())){
            mTv_postion.setText("暂时无法查看");
        }else if ("1".equals(mTdcy.getType2())){
            if ("0".equals(mTdcy.getLimit_position())){//实时位置
                Log.e("postion", "getLimit_position: -------------------------" + mTdcy.getPosition());
                mTv_change_area.setText("实时位置");
            }else if ("1".equals(mTdcy.getLimit_position())){//固定位置
                mTv_change_area.setText("固定位置");
            }
            mTv_postion.setText(mTdcy.getPosition());
        }
    }


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
        Intent intent = new Intent(TeamPersonActivity.this, ImagePagerActivity.class);
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
                        LoadingDialog.getInstance(TeamPersonActivity.this),
                        TeamPersonActivity.this, url, params,
                        new Utils.HttpCallBack() {

                            @Override
                            public void success(JsonBean bean) {
                                T.showShort(TeamPersonActivity.this,
                                        "好友申请已发出！");
                                mPerson_add.setBackgroundColor(getResources()
                                        .getColor(R.color.huise));
                                mPerson_add.setText("申请中");
                                mPerson_add.setEnabled(false);
                            }

                            @Override
                            public void failure(String msg) {
                                T.showShort(TeamPersonActivity.this, msg);
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
                        T.showShort(TeamPersonActivity.this, msg);
                    }

                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub

                    }
                });
    }
}
