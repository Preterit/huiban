package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.bumptech.glide.Glide;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.SPUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import static com.feirui.feiyunbangong.state.AppStore.mIMKit;

public class TuanDui_UpdateNameActivity extends BaseActivity implements View.OnClickListener {
    private static final int MAX_NUM = 40;
    private Button btn_update_name;
    private EditText et_update_name,et_update_introduction;
    private ImageView update_head;
    private String id;
    private IYWTribeService mTribeService;
    private ModifyTribeInfoCallback callback;
    private TextView tv_num_update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuan_dui__update_name);
        mTribeService = mIMKit.getTribeService();  //获取群管理器
        Intent intent = getIntent();
        id =intent.getStringExtra("id");
        Log.e("修改团队名称页面", "id: "+id );
        initView();
        getTeamInfor();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("修改团信息");
        setRightVisibility(false);
        btn_update_name=findViewById(R.id.btn_update_name);
        et_update_name=findViewById(R.id.et_update_name);
        et_update_introduction = findViewById(R.id.et_update_introduction);
        update_head = findViewById(R.id.update_head);
        tv_num_update = findViewById(R.id.tv_num_update);
        update_head.setOnClickListener(this);
        btn_update_name.setOnClickListener(this);
        et_update_introduction.addTextChangedListener(watcher);
    }

    TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //只要编辑框内容有变化就会调用该方法，s为编辑框变化后的内容
            Log.i("onTextChanged", s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //编辑框内容变化之前会调用该方法，s为编辑框内容变化之前的内容
            Log.i("beforeTextChanged", s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            //编辑框内容变化之后会调用该方法，s为编辑框内容变化后的内容
            Log.i("afterTextChanged", s.toString());
            if (s.length() > MAX_NUM) {
                s.delete(MAX_NUM, s.length());
            }
            String num = String.valueOf(s.length());
            tv_num_update.setText(num + "/40");
        }
    };

    /**
     * 获取团队信息
     * @return
     */
    private String name;
    public void getTeamInfor() {
        String url = UrlTools.url + UrlTools.TEAM_INFOR;
        RequestParams params = new RequestParams();
        params.put("team_id",id);
        AsyncHttpServiceHelper.post(url,params,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
                JsonBean bean = JsonUtils.getMessage(new String(responseBody));
                if ("200".equals(bean.getCode())){
                    Log.e("infor", "onSuccess:------------------------- " +bean.getInfor().get(0));
                    ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                    name = infor.get(0).get("team_name") + "";
                    String pic = infor.get(0).get("pic") + "";
                    String introduction = infor.get(0).get("team_introduction") + "";
                    if (!TextUtils.isEmpty(name)){
                        et_update_name.setText(name);
                    }
                    if (!TextUtils.isEmpty(introduction)){
                        et_update_introduction.setText(introduction);
                    }
                    if (!TextUtils.isEmpty(pic)){
                        Glide.with(TuanDui_UpdateNameActivity.this).load(pic).into(update_head);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_name:
                upDateName();
                break;
            case R.id.update_head:
                shangchuantouxiang();
                break;
            // 拍照；
            case R.id.view_camero_rl_takephoto:
                window.dismiss();
                paiZhao();
                break;
            // 从相册选择：
            case R.id.view_camero_rl_selectphoto:
                window.dismiss();
                xiangCe();
                break;
        }
    }

    private SelectPicPopupWindow window;// 弹出图片选择框；
    private Bitmap bitmap1;
    private void shangchuantouxiang() {
        window = new SelectPicPopupWindow(this, this);
        // 显示窗口
        window.showAtLocation(findViewById(R.id.ll_update), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    // 拍照：
    private void paiZhao() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "caigou.jpg")));
        startActivityForResult(i, 2);
    }

    // 从相册选择：
    private void xiangCe() {
        Intent i2 = new Intent(Intent.ACTION_PICK, null);
        i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(i2, 1);
    }

    /**
     * 调节图片大小工具
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        // intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("crop", "true");
        // // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    startPhotoZoom(data.getData());
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/caigou.jpg");
                    startPhotoZoom(Uri.fromFile(temp));
                }
                break;

            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        bitmap1 = extras.getParcelable("data");
                        update_head.setImageBitmap(bitmap1);
                    }
                }
                break;
        }
    }

    /**
     * 修改团队信息
     */
    private void upDateName() {
//        String url = UrlTools.url + UrlTools.CHANGE_TEAM_NAME;
        String url = UrlTools.url + UrlTools.UPDATE_TEAM_INFOR;
        RequestParams params = new RequestParams();
        params.put("team_id", id);
        if (!TextUtils.isEmpty(et_update_name.getText().toString().trim())){
            params.put("new_name", et_update_name.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(et_update_introduction.getText().toString().trim())){
            params.put("team_introduction", et_update_introduction.getText().toString().trim());
        }
        if (bitmap1 != null){
            params.put("pic", BitmapToBase64.bitmapToBase64(bitmap1));
        }

        Log.e("修改团队名称", "params: "+params.toString()+"name"+et_update_name.getText() );
        Utils.doPost(null, TuanDui_UpdateNameActivity.this, url, params,
                new Utils.HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        ArrayList<HashMap<String,Object>> infor = bean.getInfor();
                        if ("200".equals(bean.getCode())){
                            //保存团队名称
                            if (!name.equals(et_update_name.getText().toString().trim())){
                                SPUtils.put(TuanDui_UpdateNameActivity.this,id,et_update_name.getText().toString().trim() + "");
                            }
                           if (!TextUtils.isEmpty(infor.get(0).get("team_talk") + "")
                                   && !name.equals(et_update_name.getText().toString().trim())){
                               mTribeService.modifyTribeInfo(callback,
                                       Long.parseLong(infor.get(0).get("team_talk") + ""),
                                       et_update_name.getText().toString(), null);
                           }
                            T.showShort(TuanDui_UpdateNameActivity.this, "修改团队信息成功！");
                            TuanDui_UpdateNameActivity.this.finish();
                        }else {
                            T.showShort(TuanDui_UpdateNameActivity.this, "没有修改团队信息！");
                            TuanDui_UpdateNameActivity.this.finish();
                        }

                    }
                    @Override
                    public void failure(String msg) {
                        T.showShort(TuanDui_UpdateNameActivity.this, msg);
                    }
                    @Override
                    public void finish() {
                    }
                });
    }

    /**
     * 修改群信息后的反馈
     */

    class ModifyTribeInfoCallback implements IWxCallback {
        @Override
        public void onSuccess(Object... result) {
            finish();
        }

        @Override
        public void onError(int code, String info) {
            Log.e("yyy", "onError:修改群信息失败，code = " + code + ", info = " + info);
        }

        @Override
        public void onProgress(int progress) {
        }
    }
}
