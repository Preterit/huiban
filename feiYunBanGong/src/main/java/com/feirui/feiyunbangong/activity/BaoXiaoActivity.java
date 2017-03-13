package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddShenHeAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.AddShenHe;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.RequestParams;

import java.io.File;

/**
 * 审批-报销
 *
 * @author admina
 */
public class BaoXiaoActivity extends BaseActivity implements OnClickListener {
    Button btn_submit;// 提交
    @PView
    EditText et_1, et_2, et_3, et_4;// 完成，未完成，明天计划,备注
    private SelectPicPopupWindow window;// 弹出图片选择框；
    private int select;// 点击上传图片位置；
    private Bitmap bitmap1, bitmap2, bitmap3;
    // 添加审批人
    @PView(click = "onClick")
    ImageView iv_add, iv_01, iv_tupian1, iv_tupian2, iv_tupian3;
    @PView
    ListView lv_add_shenpiren;
    AddShenHeAdapter adapter;
    @PView
    ScrollView sv_caigou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baoxiao);
        initView();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("报销");
        setRightVisibility(false);
        adapter = new AddShenHeAdapter(getLayoutInflater(),
                BaoXiaoActivity.this);
        lv_add_shenpiren.setAdapter(adapter);
        lv_add_shenpiren.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    sv_caigou.requestDisallowInterceptTouchEvent(false);
                } else {
                    sv_caigou.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        iv_tupian1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 0;
                toastChoicePic();
            }
        });
        iv_tupian2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 1;
                toastChoicePic();
            }
        });
        iv_tupian3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 2;
                toastChoicePic();
            }
        });

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 101:
                ShenPiRen spr = (ShenPiRen) data.getSerializableExtra("shenpiren");
                if (spr.getId() == 0) {
                    return;
                }
                AddShenHe ash = new AddShenHe(spr.getName(), spr.getId());
                adapter.add(ash);
                break;
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
                        switch (select) {
                            case 0:
                                bitmap1 = extras.getParcelable("data");
                                iv_tupian1.setImageBitmap(bitmap1);
                                iv_tupian2.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                bitmap2 = extras.getParcelable("data");
                                iv_tupian2.setImageBitmap(bitmap2);
                                iv_tupian3.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                bitmap3 = extras.getParcelable("data");
                                iv_tupian3.setImageBitmap(bitmap3);
                                break;
                        }

                    }
                }
                break;
        }
    }

    ;

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

    // 图片选择：
    public void toastChoicePic() {
        window = new SelectPicPopupWindow(this, this);
        // 显示窗口
        //隐藏软键盘
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                hideSoftInputFromWindow(BaoXiaoActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        window.showAtLocation(findViewById(R.id.activity_baoxiao),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_camero_rl_takephoto:
                window.dismiss();
                Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory(), "caigou.jpg")));
                startActivityForResult(i1, 2);
                break;
            case R.id.view_camero_rl_selectphoto:
                window.dismiss();
                Intent i2 = new Intent(Intent.ACTION_PICK, null);
                i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(i2, 1);
                break;
            case R.id.iv_01:
                adapter.reduce(((AddShenHe) view.getTag()));// 删除审批人；
                break;
            case R.id.iv_add:
                Intent intent = new Intent(this, ShenPiRenActivity.class);
                startActivityForResult(intent, 101);// 请求码；
                break;
        }
    }

    public void commit() {

        if (TextUtils.isEmpty(et_1.getText().toString().trim())) {
            T.showShort(this, "请输入报销金额");
            return;
        }
        if (TextUtils.isEmpty(et_2.getText().toString().trim())) {
            T.showShort(this, "请输入报销类型");
            return;
        }
        if (TextUtils.isEmpty(et_3.getText().toString().trim())) {
            T.showShort(this, "请输入报销明细");
            return;
        }
        if (TextUtils.isEmpty(et_4.getText().toString().trim())) {
            T.showShort(this, "请输入报销总金额");
            return;
        }
        if (adapter.getCount() == 0) {
            T.showShort(this, "请选择审批人");
            return;
        }


        RequestParams params = new RequestParams();
        params.put("total_money", et_4.getText().toString().trim());
        StringBuffer sb_pic = new StringBuffer();
        if (bitmap1 != null) {
            sb_pic.append(BitmapToBase64.bitmapToBase64(bitmap1) + ",");
        }
        if (bitmap2 != null) {
            sb_pic.append(BitmapToBase64.bitmapToBase64(bitmap2) + ",");
        }
        if (bitmap3 != null) {
            sb_pic.append(BitmapToBase64.bitmapToBase64(bitmap3) + ",");
        }
        if (bitmap1 == null && bitmap2 == null && bitmap3 == null) {
            params.put("pic", "");
        } else {
            params.put("pic", sb_pic.deleteCharAt(sb_pic.length() - 1)
                    .toString());
        }
        StringBuffer sb_id = new StringBuffer();

        for (int i = 0; i < adapter.getCount(); i++) {
            AddShenHe ash = (AddShenHe) adapter.getItem(i);
            sb_id.append(ash.getId() + ",");
        }

        params.put("approvers", sb_id.deleteCharAt(sb_id.length() - 1)
                .toString());
        params.put("expense_money", et_1.getText().toString().trim());
        params.put("expense_type", et_2.getText().toString().trim());
        params.put("expense_detail", et_3.getText().toString().trim());
        String url = UrlTools.url + UrlTools.EXPENSE_ADD_EXPENSE;

        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(final JsonBean bean) {
                if ("200".equals(bean.getCode())) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            T.showShort(BaoXiaoActivity.this,
                                    bean.getMsg());
                            BaoXiaoActivity.this.finish();
                            overridePendingTransition(
                                    R.anim.aty_zoomclosein,
                                    R.anim.aty_zoomcloseout);
                        }
                    });
                } else {

                }
            }

            @Override
            public void failure(String msg) {
                T.showShort(BaoXiaoActivity.this, msg);
            }

            @Override
            public void finish() {

            }
        });
    }
}