package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddFriendAdapter;
import com.feirui.feiyunbangong.adapter.AddShenHeAdapter;
import com.feirui.feiyunbangong.adapter.AddTeamAdapter;
import com.feirui.feiyunbangong.adapter.HeaderViewRecyclerAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.entity.TeamList_entity.Infor;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.feirui.feiyunbangong.view.SwitchView;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 发布任务
 * */
public class ReleaseTask extends BaseActivity  implements OnClickListener {
    AddShenHeAdapter adapter;

    private HeaderViewRecyclerAdapter addFriendAdapterRc;
    private HeaderViewRecyclerAdapter addTeamAdapterRc;
    private HeaderViewRecyclerAdapter addFenZuAdapterRc;
    private SelectPicPopupWindow window;// 弹出图片选择框；
    private EditText tvTaskTitle;   //发布任务的标题
    private EditText tvTaskCount;   //发布任务的内容
    private EditText et_xsje;//悬赏金额
    private EditText et_xzrs;//限制人数
    private TextView tv_kssj;//textview开始时间
    //选择时间的属性
    private Calendar mCalendar;
    private Date startTime;

    public AddFriendAdapter addFriendAdapter;  //添加好友的适配器
    public AddTeamAdapter addTeamAdapter; //添加团队的适配器
    private ImageView addPicFriend;  //添加朋友
    private ImageView addPicTeam;    //添加团队
    private RecyclerView team_pic_recycler;  //添加朋友的RecyclerView
    private RecyclerView friend_pic_recycler; //添加团队的RecyclerView
    private RecyclerView fenzu_pic_recycler; //添加分组的RecyclerView
    private Button quedingButton;
    private Button quxiaoButton;
    private SwitchView switch_wzjl;
    private SwitchView switch_xs;
    private SwitchView switch_rsxz;
    private ConstraintLayout layout_xsje;
    private ConstraintLayout layout_rsxz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_task);
        initView(); //初始化控件
        setlisteners();
    }

    public void initView(){
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        switch_wzjl = (SwitchView) findViewById(R.id.switch_wzjl);
        switch_xs = (SwitchView) findViewById(R.id.switch_xs);
        switch_rsxz = (SwitchView) findViewById(R.id.switch_rsxz);
        layout_xsje = (ConstraintLayout) findViewById(R.id.layout_xsje);
        layout_rsxz = (ConstraintLayout) findViewById(R.id.layout_rsxz);
        tvTaskCount=(EditText) findViewById(R.id.tvTaskCount);
        tvTaskTitle=(EditText) findViewById(R.id.tvTaskTitle);
        et_xsje= (EditText) findViewById(R.id.et_xsje);
        et_xzrs= (EditText) findViewById(R.id.et_xzrs);
        tv_kssj = (TextView) findViewById(R.id.tv_kssj);
        quedingButton=(Button) findViewById(R.id.quedingButton);
        friend_pic_recycler=(RecyclerView)findViewById(R.id.friend_pic_recycler1);
        team_pic_recycler=(RecyclerView)findViewById(R.id.team_pic_recycler);
        fenzu_pic_recycler=(RecyclerView)findViewById(R.id.team_pic_recycler);

        View footerFriendPic = LayoutInflater.from(this).inflate(R.layout.add_pic_footer_shenpi, null);
        View footerTeamPic = LayoutInflater.from(this).inflate(R.layout.add_pic_footer_shenpi, null);
        //添加好友
        addFriendAdapter=new AddFriendAdapter(new ArrayList<ShenPiRen>());
        addFriendAdapterRc = new HeaderViewRecyclerAdapter(addFriendAdapter);
        addFriendAdapterRc.addFooterView(footerFriendPic);
        //添加团队
       addTeamAdapter=new AddTeamAdapter(new ArrayList<Infor>());
        addTeamAdapterRc=new HeaderViewRecyclerAdapter(addTeamAdapter);
        addTeamAdapterRc.addFooterView(footerTeamPic);
        //把头像放进来
        addPicFriend = (ImageView) footerFriendPic.findViewById(R.id.iv_add_pic_footer);
        //把团队的头像添加进来
        addPicTeam=(ImageView) footerTeamPic.findViewById(R.id.iv_add_pic_footer);
        //点击加号
        footerFriendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseTask.this, ShenPiRenActivity.class);
                startActivityForResult(intent, 102);// 请求码；
            }
        });
        footerTeamPic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseTask.this, SelectorTeamActivity.class);
                startActivityForResult(intent, 200);// 请求码；
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

        switch_rsxz.setOnClickListener(this);
        switch_xs.setOnClickListener(this);
        tv_kssj.setOnClickListener(this);

////        取消按钮
////               quxiaoButton=(Button) findViewById(R.id.quxiaoButton);
//                footerTeamPic.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ReleaseTask.this,SelectorTeamActivity.class);
//                startActivityForResult(intent,103);
//            }
//        });
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
//        if (TextUtils.isEmpty(tvTaskCount.getText().toString().trim())) {
//            T.showShort(ReleaseTask.this, "请输入任务的内容");
//            return;
//        }
        RequestParams params = new RequestParams();

        params.put("subject", tvTaskTitle.getText().toString().trim());
        params.put("task_txt", tvTaskCount.getText().toString().trim());
        //时间
        params.put("time", tv_kssj.getText().toString().trim());

        //是否开启位置记录
        if(switch_wzjl.isOpened()){
            params.put("address", "金隅国际");
            params.put("address_limit","1" );
        }else {
            params.put("address_limit","0");
        }
        //是否开启悬赏
        if(switch_xs.isOpened()){
            params.put("reward",  et_xsje.getText().toString().trim());
            params.put("reward_limit","1");
        }else {
            params.put("reward", "0");
            params.put("reward_limit","0");
        }
        //是否开启人数限制
        if(switch_rsxz.isOpened()){
            params.put("number", et_xzrs.getText().toString().trim());
            params.put("number_limit", "1");
        } else {
            params.put("number", "0");
            params.put("number_limit", "0");
        }

        ArrayList<ShenPiRen> friendList = addFriendAdapter.getDataSet();
        StringBuffer sb_id = new StringBuffer();
        for (int i = 0; i < friendList.size(); i++) {
            sb_id.append(friendList.get(i).getId() + ",");
        }
        if (friendList.size()==0){
            T.showShort(ReleaseTask.this, "请选择好友或分组");
            return;
        }
        if(sb_id!=null) {
            params.put("chooser", sb_id.deleteCharAt(sb_id.length() - 1)
                    .toString());
        }
        ArrayList<Infor> teamList=addTeamAdapter.getDataSet();
        StringBuffer team_id=new StringBuffer();
        for(int i=0;i<teamList.size();i++){
            team_id.append(teamList.get(i).getTeam_id()+",");
        }
        if(team_id!=null){
             // params.put("choose_team",team_id.deleteCharAt(team_id.length()-1));
        }
        Log.e("任务单界面参数", "params: "+params.toString());
        //http://123.57.45.74/feiybg1/public/index.php/home_api/Task/add_task
        String url = UrlTools.pcUrl + UrlTools.TASK_ADDTASK;
        Utils.doPost(LoadingDialog.getInstance(ReleaseTask.this), ReleaseTask.this, url,
                params, new Utils.HttpCallBack() {
                    @Override
                    public void success(final JsonBean bean) {
                        T.showShort(ReleaseTask.this,"发布任务成功");
                        if ("200".equals(bean.getCode())) {
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    T.showShort(ReleaseTask.this, bean.getMsg());
//                                    ReleaseTask.this.finish();
//                                    overridePendingTransition(R.anim.aty_zoomclosein, R.anim.aty_zoomcloseout);
//                                }
//                            });
                            ReleaseTask.this.finish();
                            overridePendingTransition(R.anim.aty_zoomclosein, R.anim.aty_zoomcloseout);

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
                if (spr.getId()==null) {
                    return;
                }
                addFriendAdapter.addFriend(spr);
                break;
            case 200:
                Infor infor=(Infor)data.getSerializableExtra("Team");
                if(infor.getTeam_id()==0){
                    return;
                }
                addTeamAdapter.addTeam(infor);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_kssj:
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());

                new TimePickerView.Builder(ReleaseTask.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String str = getDateStr(date);
                        tv_kssj.setText(str);
                        startTime = date;
                    }
                })
                        .setRange(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.YEAR) + 1)
                        .setDate(calendar)
                        .setSubmitColor(R.color.main_color)
                        .setCancelColor(R.color.main_color)
                        .setType(TimePickerView.Type.ALL)
                        .build().show();


                tv_kssj.setText(startTime + "");
            case R.id.switch_xs:
                if(switch_xs.isOpened()){
                    layout_xsje.setVisibility(View.VISIBLE);
                }else {
                    layout_xsje.setVisibility(View.GONE);
                }
            case R.id.switch_rsxz:
                if(switch_rsxz.isOpened()){
                    layout_rsxz.setVisibility(View.VISIBLE);
                }else {
                    layout_rsxz.setVisibility(View.GONE);
                }
        }


    }

    public String getDateStr(Date date) {
        // 获得日历实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String format = sdf.format(date);

        return format;
    }
}
