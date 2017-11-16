package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class Release_FanKuiActivity extends BaseActivity {
    public Button rwd_btn_qrjd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release__fan_kui);
        initView();
    }

    private void initView() {
        rwd_btn_qrjd = (Button) findViewById(R.id.rwd_btn_qrjd1);
        rwd_btn_qrjd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params2 = new RequestParams();
                String url2 = UrlTools.pcUrl + UrlTools.RENWU_QRJD;
                //String url22 = "http://123.57.45.74/feiybg1/public/index.php/home_api/Task/task_accept";
                params2.put("id", 11);
                params2.put("button", 1);
                //Log.e("全部任务获取成功-确认接单", "id: " + id);
                AsyncHttpServiceHelper.post(url2,params2,new AsyncHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        super.onSuccess(statusCode, headers, responseBody);
                        final JsonBean json = JsonUtils.getMessage(new String(responseBody));
                        if ("-400".equals(json.getCode())) {
                            Log.e("详细任务-确认接单按钮", "成功"+json.toString());
                        }else {
                            Log.e("详细任务-确认接单按钮", "失败"+json.toString());
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }
                });

//                Utils.doPost(LoadingDialog.getInstance(this), this, url2, params2, new Utils.HttpCallBack() {
//                    @Override
//                    public void success(JsonBean bean) {
//
//                        ArrayList<HashMap<String, Object>> infor = bean.getInfor();
//                        Log.e("详细任务-确认接单按钮", "成功");
//                    }
//
//                    @Override
//                    public void failure(String msg) {
//                        Log.e("详细任务-确认接单按钮", "失败，返回值: " + msg);
//                    }
//
//                    @Override
//                    public void finish() {
//                        //finish();
//                    }
//                });
            }
        });
        //setCenterString("反馈任务单情况");
    }


}
