package com.feirui.feiyunbangong;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.activity.BaseActivity;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 好友资料页面
 *
 * @author rubing
 */
public class FriendInfoActivity extends BaseActivity {

    private ImageView mIvHeadActivityFriendInfo;
    private TextView mTvNameFriendInfo;
    private ImageView mIvSexFriendInfo;
    private TextView mTvNumberFriendInfo;
    private TextView mTvBZFriendInfo;
    private RelativeLayout mRlBzFriendInfo;
    private RelativeLayout mRlShopFriendInfo;
    private Button deleteFriendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        // TODO Auto-generated method stub
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("好友资料");
        setRightVisibility(false);
        mIvHeadActivityFriendInfo = (ImageView) findViewById(R.id.iv_head_activity_friend_info);
        mTvNameFriendInfo = (TextView) findViewById(R.id.tv_name_friendInfo);
        mIvSexFriendInfo = (ImageView) findViewById(R.id.ivSexFriendInfo);
        mTvNumberFriendInfo = (TextView) findViewById(R.id.tvNumberFriendInfo);
        mTvBZFriendInfo = (TextView) findViewById(R.id.tvBZFriendInfo);
        mRlBzFriendInfo = (RelativeLayout) findViewById(R.id.rlBzFriendInfo);
        mRlShopFriendInfo = (RelativeLayout) findViewById(R.id.rlShopFriendInfo);
        deleteFriendInfo = (Button) findViewById(R.id.deleteFriendInfo);
        /**
         * 去聊天
         */
        deleteFriendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String phone;

    private void initData() {
        RequestParams params = new RequestParams();
        phone = getIntent().getStringExtra("phone");
        params.put("staff_mobile", phone + "");
        String url = UrlTools.url + UrlTools.USER_SEARCH_MOBILE;
        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(JsonBean bean) {
                ArrayList<HashMap<String, Object>> infor = bean.getInfor();
                HashMap<String, Object> map = infor.get(0);

                mTvNameFriendInfo.setText(String.valueOf(map.get("staff_name")));
                mTvNumberFriendInfo.setText(String.valueOf("手机号 :" + map.get("staff_mobile")));
                mTvBZFriendInfo.setText(String.valueOf("地址: " + map.get("address")));
                ImageLoader.getInstance().displayImage(map.get("staff_head") + "", mIvHeadActivityFriendInfo);
            }

            @Override
            public void failure(String msg) {

            }

            @Override
            public void finish() {

            }
        });

    }
}
