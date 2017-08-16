package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.mobileim.contact.IYWContact;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.im.MyUserProfileSampleHelper;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class BeiZhuActivity extends BaseActivity {
    @PView
    TextView tv_beizhu;
    @PView
    EditText et_beizhu_mc;
    @PView
    Button btn_ture;

    private String mStaffId;
    private String mTargetPhone;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bei_zhu);
        initUi();
        Bundle bundle = getIntent().getExtras();
        mStaffId = bundle.getString("mStaffId");
        mTargetPhone = bundle.getString("mTargetPhone");
        name = bundle.getString("mTargetName");
    }

    private void initUi() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("修改备注");
        setRightVisibility(false);

        btn_ture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xiugai();
            }
        });
    }

    private void xiugai() {
        // 如果内存缓存中存在该用户，则修改内存缓存中该用户的备注：
        if (MyUserProfileSampleHelper.mUserInfo.containsKey(mTargetPhone)) {
            IYWContact iywContact = MyUserProfileSampleHelper.mUserInfo.get(mTargetPhone);
            IYWContact contact = new MyUserProfileSampleHelper.UserInfo(name, iywContact
                    .getAvatarPath(), iywContact.getUserId(), iywContact.getAppKey());

            Log.e("联系人页面", "name: " + name);
            MyUserProfileSampleHelper.mUserInfo.remove(mTargetPhone); // 移除临时的IYWContact对象
            MyUserProfileSampleHelper.mUserInfo.put(mTargetPhone, contact); // 保存从服务器获取到的数据
        }


        RequestParams params = new RequestParams();
        Log.e("好友备注", "remark: "+et_beizhu_mc.getText());
        Log.e("好友备注", "person_id: "+Integer.parseInt(mStaffId));
        params.put("remark",et_beizhu_mc.getText());
        params.put("person_id",mStaffId);
        String url = UrlTools.url+UrlTools.XIUGAI_BEIZHU;
        AsyncHttpServiceHelper.post(url,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final JsonBean bean = JsonUtils.getMessage(new String(responseBody));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("200".equals(bean.getCode())) {
                            T.showShort(BeiZhuActivity.this,
                                    "修改成功！");
                            finish();
                        } else {
                            T.showShort(BeiZhuActivity.this,
                                    bean.getMsg());
                        }
                    }
                });
            }
        });
    }
}
