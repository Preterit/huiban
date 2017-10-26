package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.SearchFriendsAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.Friend;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchFriendsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIv_search_left;
    private RecyclerView mRe_search_friends;
    private TextView mTv_more_friend;
    private TextView mTv_no_friend;
    private RecyclerView mRe_search_no_friends;
    private TextView mTv_more_no_friend;
    private EditText mSearch;

    private SearchFriendsAdapter mAdapter;
    private SearchFriendsAdapter mAdapter2;
    private List<Friend> listFriend = new ArrayList<>();
    private List<Friend> listNuFriend = new ArrayList<>();
    private RecyclerView.LayoutManager mManger,mManger2;
    private String mStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        Intent intent = getIntent();
        mStr = intent.getStringExtra("friend");
        initView();
    }


    private void initView() {
        mIv_search_left = (ImageView) findViewById(R.id.iv_search_left);
        mRe_search_friends = (android.support.v7.widget.RecyclerView) findViewById(R.id.re_search_friends);
        mTv_more_friend = (TextView) findViewById(R.id.tv_more_friend);
        mTv_no_friend = (TextView) findViewById(R.id.tv_no_friend);
        mRe_search_no_friends = (android.support.v7.widget.RecyclerView) findViewById(R.id.re_search_no_friends);
        mTv_more_no_friend = (TextView) findViewById(R.id.tv_more_no_friend);
        mSearch = (EditText) findViewById(R.id.et_sousuolianxiren);
        mIv_search_left.setOnClickListener(this);
        mSearch.setText(mStr);

        if (!"".equals(mSearch.getText())){
            initDate();
        }else {
            T.showShort(this,"输入内容不能为空~");
        }

    }

    private void initDate() {
        String url = UrlTools.url + UrlTools.FRIEND_SEARCH;
        RequestParams params = new RequestParams();
        params.put("staff_id", AppStore.user.getInfor().get(0).get("id") + ""); //个人id
        params.put("key",mSearch.getText().toString());
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                listFriend.removeAll(listFriend);
                listNuFriend.removeAll(listNuFriend);
                if (infor.size() > 0){
                    for (int i = 0;i < infor.size();i++){
//                        String name,String address,String head,String sex,String birthday,
//                                String key1,String key2,String key3,String distence,String shopUrl,String state
                        HashMap<String,Object> hm = infor.get(i);
                        Friend friend = new Friend(hm.get("staff_name") + "",hm.get("address") + "",
                                hm.get("staff_head") + "",hm.get("sex") + "",hm.get("birthday") + "",
                                hm.get("staff_key1")  + "",hm.get("staff_key2")  + "",hm.get("staff_key3")  + "",
                                "1.5km",hm.get("store_url") + "",hm.get("is_friend") + "");
                        Log.e("fail", "friend: ===============-----------------" + friend.toString() );
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
            }

            @Override
            public void failure(String msg) {
                Log.e("fail", "failure: ===============" + msg );
            }

            @Override
            public void finish() {}
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                setRecyclerView();
            }
        }
    };
    private void setRecyclerView() {
        Log.e("fail", "failure: ===============" + listFriend.toString() + "uuuuuuuuuuuuuu" + listNuFriend.toString() );
        mManger = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mManger2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
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
    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
