package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.SearchFriendsAdapter;
import com.feirui.feiyunbangong.entity.Friend;
import com.feirui.feiyunbangong.entity.TuanDuiChengYuan;
import com.feirui.feiyunbangong.utils.UrlTools;

import java.util.ArrayList;
import java.util.List;

/**
 * create by xy 2017/10/27
 */
public class MoreFriendsActivity extends AppCompatActivity {
    private List<Friend> friendList = new ArrayList<>();

    private ImageView mIv_search_left;
    private RecyclerView mRe_search_friends;
    private RecyclerView.LayoutManager mManager;
    private SearchFriendsAdapter mAdapter;
    private int code;
    private TextView mTvtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_friends);
        //接收对象数组
        friendList = (List<Friend>) getIntent().getSerializableExtra("friendList");
        code = getIntent().getIntExtra("code",-1);
        initView();
    }

    private void initView() {
        mIv_search_left = (ImageView) findViewById(R.id.iv_search_left);
        mTvtitle = (TextView) findViewById(R.id.tv_title_more);
        if (code == 1){
            mTvtitle.setText("相关好友");
        }
        mIv_search_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRe_search_friends = (android.support.v7.widget.RecyclerView) findViewById(R.id.re_search_friends);
        //管理布局
        mManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRe_search_friends.setLayoutManager(mManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDate();
    }

    private void initDate() {
        mAdapter = new SearchFriendsAdapter(friendList);
        mRe_search_friends.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SearchFriendsAdapter.OnItemClickListener() {
            @Override
            public void onClick(int postion) {
                if (friendList.size() > 0){
                    startOther(postion,friendList);
                }
            }

            @Override
            public void onLongClick(int postion) {}
        });
    }

    //由于调用的团队成员的 ，所以好友要先转化为团队成员的
    private void startOther(int postion, List<Friend> friend2) {
        Intent intent = new Intent(MoreFriendsActivity.this,PersonDataActivity.class);
        Friend friend = friend2.get(postion);
        TuanDuiChengYuan tdcy = new TuanDuiChengYuan(friend.getName(),friend.getHead(),friend.getPhone(),
                friend.getShopUrl(),friend.getSex(),friend.getBrithday(),friend.getAddress(),friend.getKey1(),
                friend.getKey2(),friend.getKey3(),friend.getKey4(),friend.getKey5(),Integer.parseInt(friend.getState()));
        intent.putExtra("tdcy",tdcy);
        intent.putExtra("friend", 1);
        startActivity(intent);
        overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
    }
}
