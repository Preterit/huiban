package com.feirui.feiyunbangong.activity.tribe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.fundamental.widget.refreshlist.YWPullToRefreshBase;
import com.alibaba.mobileim.fundamental.widget.refreshlist.YWPullToRefreshListView;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.BaseActivity;
import com.feirui.feiyunbangong.state.AppStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2017/5/10.
 * 展示群列表
 * 注释掉的是添加的权限群
 */

public class TribeActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener{

        protected static final long POST_DELAYED_TIME = 300;
        private static final String TAG = "TribeFragment";
        private final Handler handler = new Handler();

        public IYWTribeService getTribeService() {
            mIMKit = AppStore.mIMKit;
            mTribeService = mIMKit.getTribeService();
            return mTribeService;
        }

        private IYWTribeService mTribeService;
        private ListView mMessageListView; // 消息列表视图
        private TribeAdapterSample adapter;
        private int max_visible_item_count = 0; // 当前页面列表最多显示个数
        private TribeAndRoomList mTribeAndRoomList;
        private List<YWTribe> mList;
        private List<YWTribe> mTribeList;
        private List<YWTribe> mRoomsList;
        private TextView righttv;


        private YWIMKit mIMKit;
        private String mUserId;
        private View mProgress;
        private YWPullToRefreshListView mPullToRefreshListView;   //下拉刷新的
        private Runnable cancelRefresh = new Runnable() {
            @Override
            public void run() {
                if (mPullToRefreshListView != null) {
                    mPullToRefreshListView.onRefreshComplete(false, false,
                            R.string.aliwx_sync_failed);
                }
            }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tribe);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mProgress = findViewById(R.id.progress);
        mIMKit = AppStore.mIMKit;
        mUserId = mIMKit.getIMCore().getLoginUserId();
        if (TextUtils.isEmpty(mUserId)) {
            YWLog.i(TAG, "user not login");
        }
        mTribeService = mIMKit.getTribeService();
        initView();

    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("群列表");
        setRightVisibility(false);
        righttv = (TextView) findViewById(R.id.righttv);
        righttv.setVisibility(View.VISIBLE);
        righttv.setText("搜索群");//群聊界面右上角搜索群
        righttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TribeActivity.this,SearchTribeActivity.class));
            }
        });

        mList = new ArrayList<YWTribe>();
        mTribeList = new ArrayList<YWTribe>();
        mRoomsList = new ArrayList<YWTribe>();
        mTribeAndRoomList = new TribeAndRoomList(mTribeList, mRoomsList);
        adapter = new TribeAdapterSample(this, mTribeAndRoomList);
        mPullToRefreshListView = (YWPullToRefreshListView) findViewById(R.id.message_list);
        mMessageListView = mPullToRefreshListView.getRefreshableView();
        mPullToRefreshListView.setMode(YWPullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        mPullToRefreshListView.setShowIndicator(false);
        mPullToRefreshListView.setDisableScrollingWhileRefreshing(false);
        mPullToRefreshListView.setRefreshingLabel("同步群组");
        mPullToRefreshListView.setReleaseLabel("松开同步群组");

        mPullToRefreshListView.setDisableRefresh(false);
        mPullToRefreshListView.setOnRefreshListener(new YWPullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(cancelRefresh);
                        //
                        IYWTribeService tribeService = mIMKit.getTribeService();  //获取群管理器;
                        if (tribeService != null) {
                            tribeService.getAllTribesFromServer(new IWxCallback() {  //从服务器获取群列表
                                @Override
                                public void onSuccess(Object... arg0) {
                                    // 返回值为列表
                                    mList.clear();
                                    mList.addAll((ArrayList<YWTribe>) arg0[0]);
                                    if (mList.size() > 0) {
                                        mTribeList.clear();
                                        mRoomsList.clear();
                                        for (YWTribe tribe : mList) {
                                            if (tribe.getTribeType() == YWTribeType.CHATTING_TRIBE) {
//                                                mTribeList.add(tribe);
                                            } else {
                                                mRoomsList.add(tribe);
                                            }
                                        }
                                    }
                                    mPullToRefreshListView.onRefreshComplete(false, true,
                                            R.string.aliwx_sync_success);
                                    refreshAdapter();
                                }

                                @Override
                                public void onError(int code, String info) {
                                    mPullToRefreshListView.onRefreshComplete(false, false,
                                            R.string.aliwx_sync_failed);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        }
                    }
                }, POST_DELAYED_TIME);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        initTribeListView();
        Intent intent = this.getIntent();
        if (intent != null) {
            if (!TextUtils.isEmpty(intent.getStringExtra(TribeConstants.TRIBE_OP))) {
                mList = mTribeService.getAllTribes();   //从本地获取群列表
                updateTribeList();
            }
        }
    }

    /**
     * 初始化群列表行为
     */
    private void initTribeListView() {
        if (mMessageListView != null) {
            mMessageListView.setAdapter(adapter);
            mMessageListView.setOnItemClickListener(this);
            mMessageListView.setOnScrollListener(this);
        }

        getAllTribesFromServer();
    }

    private void getAllTribesFromServer() {
        getTribeService().getAllTribesFromServer(new IWxCallback() {
            @Override
            public void onSuccess(Object... arg0) {
                // 返回值为列表
                mList.clear();
                mList.addAll((ArrayList<YWTribe>) arg0[0]);
                updateTribeList();
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void onError(int code, String info) {
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    private void updateTribeList() {
        mTribeList.clear();
        mRoomsList.clear();
        if (mList.size() > 0) {
            for (YWTribe tribe : mList) {
                if (tribe.getTribeType() == YWTribeType.CHATTING_TRIBE) {
//                    mTribeList.add(tribe);
                } else {
                    mRoomsList.add(tribe);
                }
            }
        }
        refreshAdapter();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position1,
                            long id) {
        final int position = position1 - mMessageListView.getHeaderViewsCount();
        if (position >= 0 && position < mTribeAndRoomList.size()) {

            YWTribe tribe = (YWTribe) mTribeAndRoomList.getItem(position);
            YWIMKit imKit = AppStore.mIMKit;
            //参数为群ID号
            Intent intent = imKit.getTribeChattingActivityIntent(tribe.getTribeId());
            startActivity(intent);
        }
    }

    /**
     * 刷新当前列表
     */
    private void refreshAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChangedWithAsyncLoad();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && adapter != null) {
            adapter.loadAsyncTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        max_visible_item_count = visibleItemCount > max_visible_item_count ? visibleItemCount
                : max_visible_item_count;
        if (adapter != null) {
            adapter.setMax_visible_item_count(max_visible_item_count);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mList = mTribeService.getAllTribes();
        updateTribeList();
    }
}
