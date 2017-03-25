package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddShenHeAdapter;
import com.feirui.feiyunbangong.adapter.HeaderViewRecyclerAdapter;
import com.feirui.feiyunbangong.adapter.PicAdapter;
import com.feirui.feiyunbangong.adapter.ShenPiRecAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.AddShenHe;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.utils.BitMapUtils;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


/**
 * 报表-日报
 *
 * @author admina
 */
public class Statement1Activity extends BaseActivity implements OnClickListener {
    @PView
    public EditText et_1, et_2, et_3, et_beizhu;// 完成，未完成，明天计划,备注
    private SelectPicPopupWindow window;// 弹出图片选择框；

    AddShenHeAdapter adapter;

    private Button mBtnCommit;
    private RecyclerView mRecPic;
    private RecyclerView mRecShenPi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement1);
        initView();
    }

    private HeaderViewRecyclerAdapter mFooterAdapter;
    public PicAdapter mPicAdapter;
    private HeaderViewRecyclerAdapter mShenpiFooterAdapter;
    public ShenPiRecAdapter mShenPiRecAdapter;
    private ImageView addPicDetail;
    private ImageView addShenPiRen;

    public void initView() {
        View footerAddPic = LayoutInflater.from(this).inflate(R.layout.add_pic_footer, null);
        View footerShenpi = LayoutInflater.from(this).inflate(R.layout.add_pic_footer_shenpi, null);
        mBtnCommit = (Button) findViewById(R.id.btn_submit_dialy);

        mPicAdapter = new PicAdapter(new ArrayList<String>());
        mFooterAdapter = new HeaderViewRecyclerAdapter(mPicAdapter);
        mFooterAdapter.addFooterView(footerAddPic);


        mShenPiRecAdapter = new ShenPiRecAdapter(new ArrayList<ShenPiRen>());
        mShenpiFooterAdapter = new HeaderViewRecyclerAdapter(mShenPiRecAdapter);
        mShenpiFooterAdapter.addFooterView(footerShenpi);

        addPicDetail = (ImageView) footerAddPic.findViewById(R.id.iv_add_pic_footer);
        addShenPiRen = (ImageView) footerShenpi.findViewById(R.id.iv_add_shenpi_ren_footer);

        addPicDetail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toastChoicePic();
            }
        });
        addShenPiRen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Statement1Activity.this, ShenPiRenActivity.class);
                startActivityForResult(intent, 101);// 请求码；
            }
        });

        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("写日报");
        setRightVisibility(false);
        adapter = new AddShenHeAdapter(getLayoutInflater(),
                Statement1Activity.this);
        mRecPic = (RecyclerView) findViewById(R.id.dialy_pic_recycler);
        mRecShenPi = (RecyclerView) findViewById(R.id.rec_add_shenpiren);

        mRecPic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecShenPi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mRecPic.setAdapter(mFooterAdapter);
        mRecShenPi.setAdapter(mShenpiFooterAdapter);

        mBtnCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }


    public void commit() {
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

        ArrayList<String> dataSet = mPicAdapter.getDataSet();
        for (int i = 0; i < dataSet.size(); i++) {
            sb_pic.append(BitmapToBase64.bitmapToBase64(BitMapUtils.getBitmap(dataSet.get(i))) + ",");
        }
        if (dataSet.size() == 0) {
            params.put("picture", "");
        } else {
            params.put("picture", sb_pic.deleteCharAt(sb_pic.length() - 1)
                    .toString());
        }

        ArrayList<ShenPiRen> shenPiRenList = mShenPiRecAdapter.getDataSet();
        StringBuffer sb_id = new StringBuffer();
        for (int i = 0; i < shenPiRenList.size(); i++) {
            sb_id.append(shenPiRenList.get(i).getId() + ",");
        }
        params.put("form_check", sb_id.deleteCharAt(sb_id.length() - 1)
                .toString());

        String url = UrlTools.url + UrlTools.FORM_REPORT;
        Utils.doPost(LoadingDialog.getInstance(Statement1Activity.this), Statement1Activity.this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(final JsonBean bean) {
                if ("200".equals(bean.getCode())) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            T.showShort(Statement1Activity.this,
                                    bean.getMsg());

                            Statement1Activity.this.finish();
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

    private String mFileName;


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 101:
                ShenPiRen spr = (ShenPiRen) data.getSerializableExtra("shenpiren");
                if (spr.getId() == 0) {
                    return;
                }
                mShenPiRecAdapter.addShenPiRen(spr);
                break;
            case 1://选择相片
                if (resultCode == Activity.RESULT_OK) {

                    mFileName = getFileName();
                    startPhotoZoom(data.getData(), new File(getExternalCacheDir(), mFileName));
                }
                break;

            case 2://照相
                if (resultCode == Activity.RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/caigou.jpg");

                    mFileName = getFileName();
                    startPhotoZoom(Uri.fromFile(temp), new File(getExternalCacheDir(), mFileName));
                }
                break;

            case 3:
                mPicAdapter.addPic(Uri.fromFile(new File(getExternalCacheDir(), mFileName)).toString());
                break;
        }
    }

    private String getFileName() {
        return "dialyPic" + new Date().getTime();
    }

    /**
     * 调节图片大小工具
     *
     * @param uri
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
//        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));//返回file
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

                break;

        }
    }
}