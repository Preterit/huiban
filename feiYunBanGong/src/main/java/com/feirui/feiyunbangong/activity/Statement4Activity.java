package com.feirui.feiyunbangong.activity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testpic.Bimp;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.ShenPiRen;
import com.feirui.feiyunbangong.utils.BitMapUtils;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

/**
 * 报表-业务报表
 *
 * @author admina
 */
public class Statement4Activity extends Statement1Activity implements OnClickListener {
    @Override
    public void initView() {
        super.initView();
        setCenterString("写业务报表");
        TextView tv1 = (TextView) findViewById(R.id.tv_1);
        TextView tv3 = (TextView) findViewById(R.id.tv_3);
        TextView tv2 = (TextView) findViewById(R.id.tv_2);
        tv1.setText("今日营业额");
        tv2.setText("今日用户数");
        tv3.setText("明日目标");

        et_1 = (EditText) findViewById(R.id.et_1);
        et_2 = (EditText) findViewById(R.id.et_2);
        et_3 = (EditText) findViewById(R.id.et_3);
        et_beizhu = (EditText) findViewById(R.id.et_beizhu);
    }


    @Override
    public void commit() {
        if (TextUtils.isEmpty(et_1.getText().toString().trim())) {
            T.showShort(this, "请输入今日营业额");
            return;
        }
        if (TextUtils.isEmpty(et_3.getText().toString().trim())) {
            T.showShort(this, "请输入明日目标");
            return;
        }
        if (shenpi==false) {
            T.showShort(this, "请选择审批人");
            return;
        }
        RequestParams params = new RequestParams();

        params.put("type_id", "4");
        params.put("option_one", et_1.getText().toString().trim());
        params.put("option_two", et_2.getText().toString().trim());
        params.put("option_three", et_3.getText().toString().trim());
        params.put("remarks", et_beizhu.getText().toString().trim());
        StringBuffer sb_pic = new StringBuffer();

//        ArrayList<String> dataSet = mPicAdapter.getDataSet();
//        for (int i = 0; i < dataSet.size(); i++) {
//            sb_pic.append(BitmapToBase64.bitmapToBase64(BitMapUtils.getBitmap(dataSet.get(i))) + ",");
//        }
//        if (dataSet.size() == 0) {
//            params.put("picture", "");
//        } else {
//            params.put("picture", sb_pic.deleteCharAt(sb_pic.length() - 1)
//                    .toString());
//        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < Bimp.bmp.size(); i++) {
            Bitmap bt = Bimp.bmp.get(i);
            sb.append(BitmapToBase64.bitmapToBase64(bt));
            sb.append(",");
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        params.put("picture", sb.toString());
        ArrayList<ShenPiRen> shenPiRenList = mShenPiRecAdapter.getDataSet();
        StringBuffer sb_id = new StringBuffer();
        for (int i = 0; i < shenPiRenList.size(); i++) {
            sb_id.append(shenPiRenList.get(i).getId() + ",");
        }
        params.put("form_check", sb_id.deleteCharAt(sb_id.length() - 1)
                .toString());

        String url = UrlTools.url + UrlTools.FORM_REPORT;
        Utils.doPost(LoadingDialog.getInstance(Statement4Activity.this), Statement4Activity.this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(final JsonBean bean) {
                if ("200".equals(bean.getCode())) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            T.showShort(Statement4Activity.this,
                                    bean.getMsg());

                            Statement4Activity.this.finish();
                            overridePendingTransition(
                                    R.anim.aty_zoomclosein,
                                    R.anim.aty_zoomcloseout);
                        }
                    });

                } else {
                    T.showShort(Statement4Activity.this,
                            bean.getMsg());
                }
            }

            @Override
            public void failure(String msg) {
                T.showShort(Statement4Activity.this, msg);
            }

            @Override
            public void finish() {

            }
        });
    }
}
