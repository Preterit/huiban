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
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.RequestParams;

import java.io.File;

/**
 * 报表-日报
 *
 * @author admina
 */
public class Statement1Activity extends BaseActivity implements OnClickListener {
    @PView
    EditText et_1, et_2, et_3, et_beizhu;// 完成，未完成，明天计划,备注
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

    private Button mBtnCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement1);
        initView();
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("日报");
        setRightVisibility(false);
        adapter = new AddShenHeAdapter(getLayoutInflater(),
                Statement1Activity.this);
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
        mBtnCommit = (Button) findViewById(R.id.btn_submit_dialy);
        mBtnCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
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
    }

    private void commit() {
        if (TextUtils.isEmpty(et_1.getText().toString().trim())) {
            T.showShort(this, "请输入今日完成工作的内容");
            return;
        }
        if (TextUtils.isEmpty(et_3.getText().toString().trim())) {
            T.showShort(this, "请输入明日计划");
            return;
        }


        RequestParams params = new RequestParams();

        params.put("type_id", "1");
        params.put("option_one", et_1.getText().toString().trim());
        params.put("option_two", et_2.getText().toString().trim());
        params.put("option_three", et_3.getText().toString().trim());
        params.put("remarks", et_beizhu.getText().toString().trim());
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
            params.put("picture", "");
        } else {
            params.put("picture", sb_pic.deleteCharAt(sb_pic.length() - 1)
                    .toString());
        }
        StringBuffer sb_id = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); i++) {
            AddShenHe ash = (AddShenHe) adapter.getItem(i);
            sb_id.append(ash.getId() + ",");
        }
        params.put("form_check", sb_id.deleteCharAt(sb_id.length() - 1)
                .toString());
        String url = UrlTools.url + UrlTools.FORM_REPORT;
        L.e("上传日报url" + url + " params" + params);

        Utils.doPost(LoadingDialog.getInstance(Statement1Activity.this), Statement1Activity.this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(final JsonBean bean) {
                if ("200".equals(bean.getCode())) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            T.showShort(Statement1Activity.this,
                                    bean.getMsg());
                            finish();
                            overridePendingTransition(
                                    R.anim.aty_zoomclosein,
                                    R.anim.aty_zoomcloseout);
                        }
                    });

                } else {
                    T.showShort(Statement1Activity.this,
                            bean.getMsg());
                }
            }

            @Override
            public void failure(String msg) {

                T.showShort(Statement1Activity.this, msg);
            }

            @Override
            public void finish() {

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

        // 在显示窗口的 同时，将虚拟键盘关闭：
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                getWindow().getDecorView().getWindowToken(), 0);

        window = new SelectPicPopupWindow(this, this);
        // 显示窗口
        window.showAtLocation(findViewById(R.id.activity_statement1),
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
}