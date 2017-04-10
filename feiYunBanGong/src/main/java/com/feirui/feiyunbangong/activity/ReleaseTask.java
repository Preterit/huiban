package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddFriendAdapter;
import com.feirui.feiyunbangong.adapter.AddShenHeAdapter;
import com.feirui.feiyunbangong.adapter.AddTeamAdapter;
import com.feirui.feiyunbangong.adapter.HeaderViewRecyclerAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.entity.TuanDui;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;


public class ReleaseTask extends BaseActivity  implements OnClickListener {
    AddShenHeAdapter adapter;

    private HeaderViewRecyclerAdapter addFriendAdapterRc;
    private HeaderViewRecyclerAdapter addTeamAdapterRc;
    private SelectPicPopupWindow window;// 弹出图片选择框；
    private EditText tvTaskTitle;   //发布任务的标题
    private EditText tvTaskCount;   //发布任务的内容
    public AddFriendAdapter addFriendAdapter;  //添加好友的适配器
    public AddTeamAdapter addTeamAdapter; //添加团队的适配器
    private ImageView addPicFriend;  //添加朋友
    private ImageView addPicTeam;    //添加团队
    private RecyclerView team_pic_recycler;  //添加朋友的RecyclerView
    private RecyclerView friend_pic_recycler; //添加团队的RecyclerView
    private Button quedingButton;
    private Button quxiaoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_task);
        initView(); //初始化控件
        setlisteners();
    }

    public void initView(){
        tvTaskCount=(EditText) findViewById(R.id.tvTaskCount);
        tvTaskTitle=(EditText) findViewById(R.id.tvTaskTitle);
        quedingButton=(Button) findViewById(R.id.quedingButton);
        quxiaoButton=(Button) findViewById(R.id.quxiaoButton);
        friend_pic_recycler=(RecyclerView)findViewById(R.id.friend_pic_recycler1);
        team_pic_recycler=(RecyclerView)findViewById(R.id.team_pic_recycler);


        View footerFriendPic = LayoutInflater.from(this).inflate(R.layout.add_pic_footer_shenpi, null);
        View footerTeamPic = LayoutInflater.from(this).inflate(R.layout.add_pic_footer_shenpi, null);
     //添加好友
        addFriendAdapter=new AddFriendAdapter(new ArrayList<ShenPiRen>());
        addFriendAdapterRc = new HeaderViewRecyclerAdapter(addFriendAdapter);
        addFriendAdapterRc.addFooterView(footerFriendPic);
        //添加团队
       addTeamAdapter=new AddTeamAdapter(new ArrayList<TuanDui>());
        addTeamAdapterRc=new HeaderViewRecyclerAdapter(addTeamAdapter);
        addTeamAdapterRc.addFooterView(footerTeamPic);
        //把头像放进来

        addPicFriend = (ImageView) footerFriendPic.findViewById(R.id.iv_add_pic_footer);
        //点击加号
        footerFriendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseTask.this, ShenPiRenActivity.class);
                startActivityForResult(intent, 102);// 请求码；
            }
        });
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("写任务");
        setRightVisibility(false);
        adapter = new AddShenHeAdapter(getLayoutInflater(),ReleaseTask.this);


        friend_pic_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        team_pic_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        friend_pic_recycler.setAdapter(addFriendAdapterRc);
        team_pic_recycler.setAdapter(addTeamAdapterRc);
    }

//点击确定按钮

    private void setlisteners() {
        quedingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                determine();//确定按钮
            }
        });
    }

    private void determine() {
        if (TextUtils.isEmpty(tvTaskTitle.getText().toString().trim())) {
            T.showShort(ReleaseTask.this, "请输入任务的标题");
            return;
        }
        if (TextUtils.isEmpty(tvTaskCount.getText().toString().trim())) {

            T.showShort(ReleaseTask.this, "请输入任务的内容");
            return;
        }
        RequestParams params = new RequestParams();

        params.put("task_txt", tvTaskTitle.getText().toString().trim());
        params.put("subject", tvTaskCount.getText().toString().trim());

        ArrayList<ShenPiRen> friendList = addFriendAdapter.getDataSet();
        StringBuffer sb_id = new StringBuffer();
        for (int i = 0; i < friendList.size(); i++) {
            sb_id.append(friendList.get(i).getId() + ",");
        }
        if(sb_id!=null) {
            params.put("chooser", sb_id.deleteCharAt(sb_id.length() - 1)
                    .toString());
        }

        String url = UrlTools.pcUrl + UrlTools.TASK_ADDTASK;
        Utils.doPost(LoadingDialog.getInstance(ReleaseTask.this), ReleaseTask.this, url,
                params, new Utils.HttpCallBack() {
                    @Override
                    public void success(final JsonBean bean) {
                        T.showShort(ReleaseTask.this,"发布任务成功");
                        if ("200".equals(bean.getCode())) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    T.showShort(ReleaseTask.this,
                                            bean.getMsg());

                                    ReleaseTask.this.finish();
                                    overridePendingTransition(
                                            R.anim.aty_zoomclosein,
                                            R.anim.aty_zoomcloseout);
                                }
                            });

                        } else {
                            T.showShort(ReleaseTask.this,
                                    bean.getMsg());
                        }
                    }

                    @Override
                    public void failure(String msg) {
                        T.showShort(ReleaseTask.this, msg);
                    }

                    @Override
                    public void finish() {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){

            case 102:
                ShenPiRen spr = (ShenPiRen) data.getSerializableExtra("shenpiren");
                if (spr.getId() ==0) {
                    return;
                }
                addFriendAdapter.addFriend(spr);
                break;
        }

    }

    @Override
    public void onClick(View v) {


    }
}
