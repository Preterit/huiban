package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddShenHeUpdateAdapter;
import com.feirui.feiyunbangong.dialog.LoadingDialog;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.PView;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 审批-报销
 *
 * @author admina
 */
public class BaoXiaoActivity extends BaseActivity implements OnClickListener {
    Button btn_submit;// 提交
    @PView
    EditText et_1, et_2, et_3,et_4;// 完成，未完成，明天计划,备注
    private SelectPicPopupWindow window;// 弹出图片选择框；
    private int select;// 点击上传图片位置；
    private Bitmap bitmap1, bitmap2, bitmap3;
    public int totle = 0;
    // 添加审批人
    @PView(click = "onClick")
    ImageView iv_add,iv_add_chaosong, iv_01, iv_tupian1, iv_tupian2, iv_tupian3;
    private ArrayList<JsonBean> list1 ;

    @PView(click = "onClick")
    TextView mTvAdd,tv_title;
    //    @PView

    @PView
    LinearLayout ll_add_mingxi;
    private View v_add_mingxi;

    @SuppressWarnings("unchecked")
    private ArrayList<ChildItem> childs ; //添加的审批人员
    @PView
    ListView lv_add_shenpiren;
    @PView
    ListView lv_add_chaosong;
    //    AddShenHeAdapter adapter;
    AddShenHeUpdateAdapter adapter;
    AddShenHeUpdateAdapter adapter1;
    @PView
    ScrollView sv_caigou;
    private Button jisuanBtn;
    private int num=0;
    private int p;

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

        list1 = new ArrayList<>();
//        adapter = new AddShenHeAdapter(getLayoutInflater(),
//                BaoXiaoActivity.this);
        adapter = new AddShenHeUpdateAdapter(getLayoutInflater(),list1,BaoXiaoActivity.this);
        jisuanBtn=(Button)findViewById(R.id.jisuanBtn);
        jisuanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              jisuan();
            }
        });
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
        //抄送人列表
        adapter1 = new AddShenHeUpdateAdapter(getLayoutInflater(),list1,BaoXiaoActivity.this);
        lv_add_chaosong.setAdapter(adapter1);
        lv_add_chaosong.setOnTouchListener(new OnTouchListener() {

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
        mTvAdd = (TextView) findViewById(R.id.tv_add_mingxi);
        mTvAdd.setOnClickListener(this);

    }

    private void jisuan() {
        Log.e("tag", "有几个布局 ----------" + ll_add_mingxi.getChildCount());
        for (int i = 0; i < ll_add_mingxi.getChildCount(); i++) {
            View v = ll_add_mingxi.getChildAt(i);
            EditText et_1 = (EditText) v.findViewById(R.id.et_1);
            //计算总金额
            String numText=et_1.getText().toString().trim();
            Log.e("tag", "用户输入的钱数 ----------" + numText );
            try{
                if("".equals(numText)){
                    et_4.setText("0.0");
                    return;
                }
                p = Integer.parseInt(numText);
            }catch (NumberFormatException e){
                Log.e("tag", "jisuan: -------------------" + e.getMessage());
            }
            Log.e("tag", "转换后的数据 ----------" + p );

            num = num + p;

            Log.e("tag", "获的总额 ----------" + num );
        }
        et_4.setText(num+"");
        num=0;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 200 && resultCode == 100) {
            childs = (ArrayList<ChildItem>) data
                    .getSerializableExtra("childs");
            HashMap<String, Object> hm = AppStore.user.getInfor().get(0);
            childs.add(
                    0,
                    new ChildItem(hm.get("staff_name") + "", hm
                            .get("staff_head") + "", hm.get("staff_mobile")
                            + "", hm.get("id") + "", 0));
            //去掉自己
            childs.remove(0);
            if (childs != null && childs.size() > 0) {
                adapter.addList(childs);
            }
            Log.d("mytag","添加人员："+childs.get(0).toString());
        }

        if (requestCode == 300 && resultCode == 100 ) {
            ArrayList<ChildItem> childs1 = (ArrayList<ChildItem>)data.getSerializableExtra("childs");
            Log.e("tag", "onActivityResult: ----------" + childs1 );
            HashMap<String, Object> hm = AppStore.user.getInfor().get(0);
            Log.e("tag", "onActivityResult: ----------" + hm );
            childs1.add(0, new ChildItem(hm.get("staff_name") + "", hm.get("staff_head") + "", hm.get("staff_mobile")
                    + "", hm.get("id") + "", 0));
            //去掉自己
            childs1.remove(0);
            if (childs1 != null && childs1.size() > 0) {
                adapter1.addList(childs1);
            }
            Log.d("mytag","添加人员："+childs1.get(0).toString());
        }

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
        super.onActivityResult(requestCode, resultCode, data);
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
//                adapter.reduce(((AddShenHe) view.getTag()));// 删除审批人；
                break;
            case R.id.iv_add:
                Intent intent = new Intent(this, AddChengYuanActivity.class);
                startActivityForResult(intent, 200);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
//                startActivityForResult(intent, 101);// 请求码；
                break;
            case R.id.iv_add_chaosong:
                final Intent intent1 = new Intent(this, AddChengYuanActivity.class);
                startActivityForResult(intent1, 300);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
//			startActivityForResult(intent, 101);// 请求码；
                break;
            case R.id.tv_add_mingxi:
                addMingXi(); //添加明细
                break;
            case R.id.tv_title:
                ll_add_mingxi.removeView((View) view.getTag());// 移除明细；
                break;
        }
    }

    private void addMingXi() {
        v_add_mingxi = getLayoutInflater().inflate(R.layout.add_mingxi_baoxiao_shenpi,
                null);
        tv_title = (TextView) v_add_mingxi.findViewById(R.id.tv_title);
        tv_title.setTag(v_add_mingxi);
        tv_title.setOnClickListener(this);
        ll_add_mingxi.addView(v_add_mingxi);
        tv_title.setText("删除明细");
        tv_title.setTextColor(Color.RED);
    }

    public void commit() {
        Log.e("开始提交", "start");

        StringBuffer sb_price = new StringBuffer();
        StringBuffer sb_leixing = new StringBuffer();
        StringBuffer sb_mingxi = new StringBuffer();

//        int totle = 0;
        // 采购明细：

        for (int i = 0; i < ll_add_mingxi.getChildCount(); i++) {
            View v = ll_add_mingxi.getChildAt(i);
            final EditText et_1 = (EditText) v.findViewById(R.id.et_1);
            EditText et_2 = (EditText) v.findViewById(R.id.et_2);
            EditText et_3 = (EditText) v.findViewById(R.id.et_3);

            if (TextUtils.isEmpty(et_1.getText().toString().trim())) {
                Toast.makeText(this, "请输入报销金额！",  Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(et_2.getText().toString().trim())) {
                Toast.makeText(this, "请输入报销类型！",  Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(et_3.getText().toString().trim())) {
                Toast.makeText(this, "请输入报销明细！",  Toast.LENGTH_SHORT).show();
                return;
            }
            sb_price.append(et_1.getText().toString().trim() + ",");
            sb_leixing.append(et_2.getText().toString().trim() + ",");
            sb_mingxi.append(et_3.getText().toString().trim() + ",");

            Log.e("报销类型", "sb_price: "+sb_price.toString());
            Log.e("报销金额", "sb_leixing: "+sb_leixing.toString() );
            Log.e("报销明细", "sb_mingxi: "+sb_mingxi.toString() );

//            totle += Integer.parseInt(et_1.getText().toString().trim());
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

        //从适配器中取出审批人集合
        List<ChildItem> shenPi = adapter.getList();
        StringBuffer sb_id = new StringBuffer();
        // 循环拼接添加成员id,每个id后加逗号
        for (int i = 0; i < shenPi.size(); i++) {
            sb_id.append(shenPi.get(i).getId());
            sb_id.append(",");
            Log.d("adapterTag", "适配器上的数据" + sb_id);
        }
        //从适配器中取出抄送人集合
        List<ChildItem> chaoSong = adapter1.getList();
        StringBuffer cs_id = new StringBuffer();

        // 循环拼接添加成员id,每个id后加逗号
        for (int i = 0; i < chaoSong.size(); i++) {
            cs_id.append(chaoSong.get(i).getId());
            cs_id.append(",");
            Log.d("adapterTag","适配器上的数据"+cs_id);
        }
        params.put("ccuser_id", cs_id.deleteCharAt(cs_id.length() - 1).toString());
        params.put("approvers", sb_id.deleteCharAt(sb_id.length() - 1).toString());
        params.put("expense_money", et_1.getText().toString().trim());
        params.put("expense_type", et_2.getText().toString().trim());
        params.put("expense_detail", et_3.getText().toString().trim());
        String url = UrlTools.url1 + UrlTools.EXPENSE_ADD_EXPENSE1;
        Log.e("tag", "报销的---------"+  params.toString());

        Utils.doPost(LoadingDialog.getInstance(this), this, url, params, new Utils.HttpCallBack() {
            @Override
            public void success(final JsonBean bean) {
                Log.e("tag", "报销的---------"+ bean.getCode());
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
                    T.showShort(BaoXiaoActivity.this, bean.getMsg());
                }
                Log.e("结束", "success ");
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