package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;

public class TuanDui_UpdateNameActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_update_name;
    private EditText et_update_name;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuan_dui__update_name);
        Intent intent = getIntent();
        id =intent.getStringExtra("id");
        Log.e("修改团队名称页面", "id: "+id );
        initView();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("修改团名称");
        setRightVisibility(false);
        btn_update_name=findViewById(R.id.btn_update_name);
        et_update_name=findViewById(R.id.et_update_name);
        btn_update_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_name:
                upDateName();
                break;
        }
    }

    private void upDateName() {
        String url = UrlTools.url + UrlTools.CHANGE_TEAM_NAME;
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("new_name", et_update_name.getText());
        Utils.doPost(LoadingDialog.getInstance(TuanDui_UpdateNameActivity.this),
                TuanDui_UpdateNameActivity.this, url, params,
                new Utils.HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        T.showShort(TuanDui_UpdateNameActivity.this, "修改团队名成功！");
                    }
                    @Override
                    public void failure(String msg) {
                        T.showShort(TuanDui_UpdateNameActivity.this, msg);
                    }
                    @Override
                    public void finish() {
                        // TODO Auto-generated method stub
                    }
                });
    }
}
