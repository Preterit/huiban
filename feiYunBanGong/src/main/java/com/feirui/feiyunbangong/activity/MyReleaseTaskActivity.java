package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import static com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper.post;

public class MyReleaseTaskActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_right;
    private RadioGroup rg_btn;
    private RadioButton rbt_complete, rbt_fankui;
    private EditText et_jindu;
    int button;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release_task);
        id = getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("反馈任务单情况");
        setRightVisibility(false);
        tv_right = (TextView) findViewById(R.id.righttv);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("确定");
        tv_right.setOnClickListener(this);
        rg_btn = (RadioGroup) findViewById(R.id.rg_bt);
        rbt_complete = (RadioButton) findViewById(R.id.rbt_complete);
        rbt_fankui = (RadioButton) findViewById(R.id.rbt_fankui);
        et_jindu = (EditText) findViewById(R.id.et_jindu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.righttv:
                if (rbt_complete.isChecked()) {
                    button = 0;
                }else if (rbt_fankui.isChecked()) {
                    button = 1;
                } else if (rbt_complete.isChecked()==false&&rbt_fankui.isChecked()==false){
                    Toast.makeText(this, "请选择任务进度", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_jindu.getText().toString().trim() == null||et_jindu.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "任务进度不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestParams params = new RequestParams();
                String url = "http://123.57.45.74/feiybg1/public/index.php/home_api/task/feedback";
                params.put("button", button + "");
                params.put("id", id + "");
                params.put("message", et_jindu.getText().toString().trim());
                post(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        final JsonBean json = JsonUtils.getMessage(new String(responseBody));
                        if ("200".equals(json.getCode())) {
                            Toast.makeText(MyReleaseTaskActivity.this, "提交反馈成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }
                });

                break;
        }
    }
}
