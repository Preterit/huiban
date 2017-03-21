package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.utils.BitMapUtils;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.ArrayList;


public class AddGoodActivity extends BaseActivity {
    private int select;
    private Bitmap mBitMapMain;
    private Bitmap mBitMap01;
    private Bitmap mBitMap02;
    private Bitmap mBitMap03;
    private File photoDir;

    private boolean mBitMap1IsSelect = false;
    private boolean mBitMap2IsSelect = false;
    private boolean mBitMap3IsSelect = false;
    private boolean mBitMap4IsSelect = false;


    private String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_good);
        initTop();
        initView();

        storeId = getIntent().getStringExtra("id");
    }


    private void initTop() {
        initTitle();
        setCenterString("添加商品");
        setLeftDrawable(R.drawable.arrows_left);
    }

    private Button mCommit;
    private EditText et_name, et_price, et_description;
    private ImageView ivMain, iv1, iv2, iv3;


    private void initView() {
        photoDir = new File(getExternalCacheDir(), "pics");
        if (!photoDir.exists()) {
            photoDir.mkdirs();//创建目录
        }


        mCommit = (Button) findViewById(R.id.btn_add_good_commit);
        et_name = (EditText) findViewById(R.id.et_new_good_name);
        et_price = (EditText) findViewById(R.id.et_new_good_price);
        et_description = (EditText) findViewById(R.id.et_new_good_description);

        ivMain = (ImageView) findViewById(R.id.iv_addMainImage);
        iv1 = (ImageView) findViewById(R.id.iv_addDetailImage1);
        iv2 = (ImageView) findViewById(R.id.iv_addDetailImage2);
        iv3 = (ImageView) findViewById(R.id.iv_addDetailImage3);

        ivMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 4;
                toastChoicePic();
            }
        });

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 1;
                toastChoicePic();
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 2;
                toastChoicePic();
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 3;
                toastChoicePic();
            }
        });

        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_name.getText())) {
                    T.showShort(AddGoodActivity.this, "请填写商品名");
                    return;
                }
                if (TextUtils.isEmpty(et_price.getText())) {
                    T.showShort(AddGoodActivity.this, "请填写价格");
                    return;
                }
                if (TextUtils.isEmpty(et_description.getText())) {
                    T.showShort(AddGoodActivity.this, "请填写商品描述");
                    return;
                }

                if (!mBitMap1IsSelect) {
                    T.showShort(AddGoodActivity.this, "请选择主图");
                    return;
                }
                if (!(mBitMap2IsSelect || mBitMap3IsSelect || mBitMap4IsSelect)) {
                    T.showShort(AddGoodActivity.this, "至少选择一张商品图片");
                    return;
                }
                RequestParams params = new RequestParams();
                params.put("store_id", storeId);
                params.put("goods_name", et_name.getText().toString().trim());
                params.put("goods_price", et_price.getText().toString().trim());
                params.put("goods_content", et_description.getText().toString().trim());
                params.put("main_pic", BitmapToBase64.bitmapToBase64(mBitMapMain));

//                params.put("main_pic", "44");
                ArrayList<String> list = new ArrayList<String>();
                if (mBitMap1IsSelect) {
                    list.add(BitmapToBase64.bitmapToBase64(mBitMap01));
//                    list.add("1");
                }
                if (mBitMap2IsSelect) {
                    list.add(BitmapToBase64.bitmapToBase64(mBitMap02));
//                    list.add("2");
                }
                if (mBitMap3IsSelect) {
                    list.add(BitmapToBase64.bitmapToBase64(mBitMap03));
//                    list.add("3");
                }

                StringBuffer sp = new StringBuffer("");
                for (String bitmap :
                        list) {
                    sp.append(bitmap + ",");
                }
                if (list.size() == 0) {
                    params.put("figure", "");
                } else {
                    params.put("figure", sp.deleteCharAt(sp.length() - 1)
                            .toString());
                }


                String url = UrlTools.url + UrlTools.ADD_GOOD;

//                Log.e("orz", "onClick: " + params);
                Utils.doPost(LoadingDialog.getInstance(AddGoodActivity.this), AddGoodActivity.this, url, params, new Utils.HttpCallBack() {
                    @Override
                    public void success(JsonBean bean) {
                        Log.e("orz", "success: " + bean.toString());
                        if (bean.getCode().equals("200")) {
                            T.showShort(AddGoodActivity.this, bean.getMsg());

                            AddGoodActivity.this.finish();
                        } else {
                            T.showShort(AddGoodActivity.this, bean.getMsg());
                        }
                    }

                    @Override
                    public void failure(String msg) {

                    }

                    @Override
                    public void finish() {

                    }
                });
            }
        });
    }

    private SelectPicPopupWindow window;// 弹出图片选择框；

    // 图片选择：
    public void toastChoicePic() {
        window = new SelectPicPopupWindow(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.view_camero_rl_takephoto) {//拍照
                    window.dismiss();
                    paiZhao();
                } else if (v.getId() == R.id.view_camero_rl_selectphoto) {//相册
                    window.dismiss();
                    xiangCe();
                }
            }
        });
        // 显示窗口
        window.showAtLocation(findViewById(R.id.et_new_good_description), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    private void xiangCe() {
        Intent i2 = new Intent(Intent.ACTION_PICK, null);
        i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(i2, 1);
    }

    private void paiZhao() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "shangpin.jpg")));
        startActivityForResult(i, 2);
    }

    /**
     * 调节图片大小工具
     *
     * @param uri
     */
    /*
     * public void startPhotoZoom(Uri uri) { Intent intent = new
	 * Intent("com.android.camera.action.CROP"); intent.setDataAndType(uri,
	 * "image/*"); // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
	 * intent.putExtra("crop", "true"); // aspectX aspectY 是宽高的比例
	 * intent.putExtra("aspectX", 1); intent.putExtra("aspectY", 1); // outputX
	 * outputY 是裁剪图片宽高 intent.putExtra("outputX", 300);
	 * intent.putExtra("outputY", 300); intent.putExtra("return-data", true);
	 * startActivityForResult(intent, 3); }
	 */
    public void startPhotoZoom(Uri uri, File outPutFile) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        // intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("crop", "true");
        // // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
//        intent.putExtra("return-data", true);//返回bitmap
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));//返回file
        startActivityForResult(intent, 3);
    }

    private String photoName = "goodPic";

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    startPhotoZoom(data.getData(), new File(photoDir, photoName + select));
                }
                break;

            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/shangpin.jpg");
                    startPhotoZoom(Uri.fromFile(temp), new File(photoDir, photoName + select));
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        switch (select) {
                            case 1:
                                mBitMap01 = BitMapUtils.getBitmap(new File(photoDir, photoName + select).getAbsolutePath());
                                iv1.setImageBitmap(mBitMap01);
                                mBitMap1IsSelect = true;
                                break;
                            case 2:
                                mBitMap02 = BitMapUtils.getBitmap(new File(photoDir, photoName + select).getAbsolutePath());
                                iv2.setImageBitmap(mBitMap02);
                                mBitMap2IsSelect = true;
                                break;
                            case 3:
                                mBitMap03 = BitMapUtils.getBitmap(new File(photoDir, photoName + select).getAbsolutePath());
                                iv3.setImageBitmap(mBitMap03);
                                mBitMap3IsSelect = true;
                                break;
                            case 4://main pic
                                mBitMapMain = BitMapUtils.getBitmap(new File(photoDir, photoName + select).getAbsolutePath());
                                ivMain.setImageBitmap(mBitMapMain);
                                mBitMap4IsSelect = true;
                                break;
                        }
                    }
                }
                break;
        }
    }

}
