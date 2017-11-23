package com.feirui.feiyunbangong.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.testpic.PublishedActivity;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.SearchFriendsAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.Friend;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.BaiDuUtil;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SearchFriendsActivity extends BaseActivity implements View.OnClickListener{
    private ImageView mIv_search_left;
    private RecyclerView mRe_search_friends;
    private TextView mTv_more_friend,mTv_search;
    private TextView mTv_no_friend;
    private RecyclerView mRe_search_no_friends;
    private TextView mTv_more_no_friend;
    private EditText mSearch;

    private SearchFriendsAdapter mAdapter;
    private SearchFriendsAdapter mAdapter2;
    private List<Friend> listFriend = new ArrayList<>(); //防止java.util.ConcurrentModificationException异常
    private List<Friend> listNuFriend = new ArrayList<>();
    private RecyclerView.LayoutManager mManger,mManger2;
    private String mStr,string;
    private StringBuffer stringBuffer = new StringBuffer(256);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        Intent intent = getIntent();
        mStr = intent.getStringExtra("friend");
        string = intent.getStringExtra("location");
        initView();
        initLocation();
    }

    private void initLocation() {
        //启动的同时 定位
        handler.sendEmptyMessage(2);
        //获取定位
        initDate();

    }


    private void initView() {
        mIv_search_left = (ImageView) findViewById(R.id.iv_search_left);
        mRe_search_friends = (android.support.v7.widget.RecyclerView) findViewById(R.id.re_search_friends);
        mTv_more_friend = (TextView) findViewById(R.id.tv_more_friend);
        mTv_no_friend = (TextView) findViewById(R.id.tv_no_friend);
        mRe_search_no_friends = (android.support.v7.widget.RecyclerView) findViewById(R.id.re_search_no_friends);
        mTv_more_no_friend = (TextView) findViewById(R.id.tv_more_no_friend);
        mSearch = (EditText) findViewById(R.id.et_sousuolianxiren);
        mSearch.setHint("名字/手机号/关键词");
        mTv_search = (TextView) findViewById(R.id.tv_search);
        mTv_search.setOnClickListener(this);
        mIv_search_left.setOnClickListener(this);
        mTv_more_friend.setOnClickListener(this);
        mTv_more_no_friend.setOnClickListener(this);
        mSearch.setText(mStr);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initDate() {
        if (!"".equals(mSearch.getText().toString())){
            String url = UrlTools.url + UrlTools.FRIEND_SEARCH;
            RequestParams params = new RequestParams();
            params.put("staff_id", AppStore.user.getInfor().get(0).get("id") + ""); //个人id
            params.put("key",mSearch.getText().toString());
            params.put("location",string);
            Log.e("fail", "friend: =============stringBuffer.toString()==-----------------" + string);
            Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
                @Override
                public void success(JsonBean bean) {
                    ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                    listFriend.removeAll(listFriend);
                    listNuFriend.removeAll(listNuFriend);
                    if (infor.size() > 0){
                        for (int i = 0;i < infor.size();i++){
                            HashMap<String,Object> hm = infor.get(i);
                            Friend friend = new Friend(hm.get("staff_name") + "",hm.get("address") + "",
                                    hm.get("staff_head") + "",hm.get("sex") + "",hm.get("birthday") + "",
                                    hm.get("staff_key1")  + "",hm.get("staff_key2")  + "",hm.get("staff_key3")  + "",
                                    hm.get("staff_key4")  + "",hm.get("staff_key5")  + "",
                                    hm.get("distance") + "",hm.get("store_url") + "",hm.get("is_friend") + "",
                                    hm.get("staff_mobile") + "",hm.get("type") + "",hm.get("position") + "",hm.get("limit_position") + "");

                            if ("0".equals(friend.getState())){
                                listNuFriend.add(friend);
                            }else {
                                listFriend.add(friend);
                            }
                        }

                        handler.sendEmptyMessage(0);

                    }else {
                        T.showShort(SearchFriendsActivity.this,"没有找到您要搜索的~");
                    }
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                            hideSoftInputFromWindow(SearchFriendsActivity.this.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }

                @Override
                public void failure(String msg) {
                    T.showShort(SearchFriendsActivity.this,msg);
                    Log.e("fail", "failure: ===============" + msg );
                }

                @Override
                public void finish() {}
            });

        }else {
            T.showShort(this,"输入内容不能为空~");
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                setRecyclerView();
            }else if (msg.what == 1){
//                initDate();
            }else if (msg.what == 2){
                BaiDuUtil.initLocation(SearchFriendsActivity.this, new BDLocationListener() {
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
                        string = stringBuffer.toString();
                        Log.e("string", "onReceiveLocation: -------------------------" + string);
                    }

                    @Override
                    public void onConnectHotSpotMessage(String s, int i) {}
                });
            }
        }
    };
    private void setRecyclerView() {

        mManger = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mManger2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        //排序
        Collections.sort(listFriend,new Comparator<Friend>(){

            @Override
            public int compare(Friend lhs, Friend rhs) {
                return lhs.getDistence().compareTo(rhs.getDistence());
            }
        });

        Collections.sort(listNuFriend, new Comparator<Friend>() {
            @Override
            public int compare(Friend lhs, Friend rhs) {
                return lhs.getDistence().compareTo(rhs.getDistence());
            }
        });

        if (listFriend.size() > 3){
            mAdapter = new SearchFriendsAdapter(listFriend.subList(0,3));
        }else {
            mAdapter = new SearchFriendsAdapter(listFriend);
        }

        if (listNuFriend.size() > 3){
            mAdapter2 = new SearchFriendsAdapter(listNuFriend.subList(0,3));
        }else {
            mAdapter2 = new SearchFriendsAdapter(listNuFriend);
        }

        mRe_search_friends.setLayoutManager(mManger);
        mRe_search_no_friends.setLayoutManager(mManger2);
        mRe_search_no_friends.setAdapter(mAdapter2);
        mRe_search_friends.setAdapter(mAdapter);

        //每一项的点击事件
//        String name, String head,  String phone,String store_url,String sex,
//                String birthday,String address,String key1,String key2,String key3
        mAdapter.setOnItemClickListener(new SearchFriendsAdapter.OnItemClickListener() {
            @Override
            public void onClick(int postion) {
                if (listFriend.size() > 0){
                    startOther(postion,listFriend);
                }
            }

            @Override
            public void onLongClick(int postion) {}
        });
        mAdapter2.setOnItemClickListener(new SearchFriendsAdapter.OnItemClickListener() {
            @Override
            public void onClick(int postion) {
                if (listNuFriend.size() > 0){
                    startOther(postion,listNuFriend);
                }
            }

            @Override
            public void onLongClick(int postion) {}
        });
    }

    /*
    查看详细信息
     */
    private void startOther(int postion, List<Friend> friend2) {
        Intent intent = new Intent(SearchFriendsActivity.this,PersonDataActivity.class);
        Friend friend = friend2.get(postion);
        TuanDuiChengYuan tdcy = new TuanDuiChengYuan(friend.getName(),friend.getHead(),friend.getPhone(),
                friend.getShopUrl(),friend.getSex(),friend.getBrithday(),friend.getAddress(),friend.getKey1(),
                friend.getKey2(),friend.getKey3(),friend.getKey4(),friend.getKey5(),
                Integer.parseInt(friend.getState()),friend.getType(),friend.getPosition(),friend.getLimit_position());
        Log.e("search", "startOther: ----------------------------" + tdcy );
        intent.putExtra("tdcy",tdcy);
        intent.putExtra("friend", 1);
        startActivity(intent);
        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_search_left:
                finish();
                break;
            case R.id.tv_more_friend:
                findMore(listFriend);
                break;
            case R.id.tv_more_no_friend:
                findMore(listNuFriend);
                break;
            case R.id.tv_search:
                initLocation();
                break;
        }

    }

    /*
    更多好友或非好友的方法
     */
    private void findMore(List<Friend> more) {
        if (more.size() > 0){
            Intent intent = new Intent(this,MoreFriendsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("friendList", (Serializable) more);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
        }else {
            T.showShort(this,"没有找到您需要的哦~");
        }
    }


}
