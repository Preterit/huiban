package com.feirui.feiyunbangong;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.activity.BaseActivity;
import com.feirui.feiyunbangong.activity.FriendShop;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.FriendShopBean;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.Constant;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

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
    private Button contactFriendInfo;

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
        contactFriendInfo = (Button) findViewById(R.id.contactFriendInfo);
        /**
         *
         * 删除好友
         */
        deleteFriendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        contactFriendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 跳转到好友的小店
         */
        mRlShopFriendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先查看是否有小店
                String url = UrlTools.url + UrlTools.FRIEND_SHOP;
                RequestParams requestParams = new RequestParams();
                requestParams.put("staff_id", mStaffId);
                if (mStaffId == null) {
                    return;
                }
                Log.e("orz", "onClick: " + mStaffId);
                AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Gson gson = new Gson();
                        final FriendShopBean friendShopBean = gson.fromJson(new String(responseBody), FriendShopBean.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (friendShopBean.getCode() == 200) {
                                    if (friendShopBean.getInfo() == null) {
                                        T.showShort(FriendInfoActivity.this, friendShopBean.getMsg());
                                    } else {
                                        Intent intent = new Intent(FriendInfoActivity.this, FriendShop.class);
                                        FriendShopBean.InfoBean infoBean = friendShopBean.getInfo().get(0);
                                        if (infoBean == null) {
                                            return;
                                        }
                                        infoBean.setTargetAddress(mTargetAddress);
                                        infoBean.setTargetHead(mTargetHead);
                                        infoBean.setTargetName(mTargetName);
                                        infoBean.setTargetPhoe(mTargetPhone);
                                        intent.putExtra(Constant.INTENT_SERIALIZABLE_DATA, infoBean);
                                        startActivity(intent);
                                    }
                                } else {
                                    T.showShort(FriendInfoActivity.this, friendShopBean.getMsg());
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private String phone;
    private String mStaffId;
    private String mTargetName;
    private String mTargetPhone;
    private String mTargetAddress;
    private String mTargetHead;

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


                mTargetHead = String.valueOf(map.get("staff_head"));
                mTargetName = String.valueOf(map.get("staff_name"));
                mTargetAddress = String.valueOf(map.get("address"));
                mTargetPhone = String.valueOf(map.get("staff_mobile"));


                mTvNameFriendInfo.setText(mTargetName);
                mTvNumberFriendInfo.setText(String.valueOf("手机号 :" + mTargetPhone));
                mTvBZFriendInfo.setText(mTargetAddress);

                mStaffId = String.valueOf(map.get("id"));
                ImageLoader.getInstance().displayImage(mTargetHead + "", mIvHeadActivityFriendInfo);
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
