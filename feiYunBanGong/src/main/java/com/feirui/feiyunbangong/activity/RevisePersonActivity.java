package com.feirui.feiyunbangong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bigkoo.pickerview.TimePickerView;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.activity.BaiDuPoi.PoiSearchActivity;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.entity.MyUser;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.BaiDuUtil;
import com.feirui.feiyunbangong.utils.BitmapToBase64;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.utils.Utils;
import com.feirui.feiyunbangong.view.AddWordPopupWindow;
import com.feirui.feiyunbangong.view.CircleImageView;
import com.feirui.feiyunbangong.view.SelectPicPopupWindow;
import com.feirui.feiyunbangong.view.SexPopupWindow;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 修改个人资料
 * create by xy
 */
public class RevisePersonActivity extends BaseActivity implements View.OnClickListener {
    private TextView righttv;
    private EditText mRevise_name;
    private TextView mRevise_sex;
    private TextView mRevise_birth;
    private EditText mRevise_area,mEv_share_area;
    private GridView mRevise_gd;
    private CircleImageView mRevise_head;
    private List<String> tags = new ArrayList<>();
    private MyUser user;
    private GridViewAdapter tagsAdapter;
    private SexPopupWindow sexPopupWindow; //性别选择
    private AddWordPopupWindow addPopupWindow;
    private ImageView mIv_share;
    private Button mBtn_time_area,mBtn_stone_area;
    private String limit_position,type;//（0是实时位置1是固定位置）  是否开启共享位置（0不共享1共享）
    private boolean flag = true;
    private SharedPreferences mShareds;
    private double longitude,latitude;//经度 纬度
    private String city,address,fromAddress,stoneLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_person);
        mShareds = getSharedPreferences("position",Context.MODE_PRIVATE);
        //设置在activity启动的时候输入法默认是不开启的
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        user = (MyUser)getIntent().getSerializableExtra("user");
        initView();
        setListener();
        BaiDuUtil.initLocation(this, new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
               longitude = bdLocation.getLongitude();
               latitude = bdLocation.getLatitude();//
               city = bdLocation.getCity();
               address = bdLocation.getAddrStr();
            }
        });
    }


    private void initView() {
        initTitle();
        setCenterString("修改个人资料");
        setLeftDrawable(R.drawable.arrows_left);
        rightIv.setVisibility(View.GONE);
        righttv = (TextView) findViewById(R.id.righttv);
        righttv.setVisibility(View.VISIBLE);
        righttv.setText("保存");

        mRevise_head = (CircleImageView) findViewById(R.id.revise_head);
        mRevise_name = (EditText) findViewById(R.id.revise_name);
        mRevise_sex = (TextView) findViewById(R.id.revise_sex);
        mRevise_birth = (TextView) findViewById(R.id.revise_birth);
        mRevise_area = (EditText) findViewById(R.id.revise_area);
        mRevise_gd = (GridView) findViewById(R.id.revise_gd);

        mIv_share = (ImageView) findViewById(R.id.iv_share);
        mBtn_time_area = (Button) findViewById(R.id.btn_time_area);
        mBtn_stone_area = (Button) findViewById(R.id.btn_stone_area);
        mEv_share_area = (EditText) findViewById(R.id.tv_share_area);
        getData();
    }

    private void setListener() {
        mRevise_head.setOnClickListener(this);
        mRevise_sex.setOnClickListener(this);
        mRevise_birth.setOnClickListener(this);
        righttv.setOnClickListener(this);
        mIv_share.setOnClickListener(this);
        mBtn_time_area.setOnClickListener(this);
        mBtn_stone_area.setOnClickListener(this);
    }

    private void getData() {
        if (user.getHead() != null){
            ImageLoader.getInstance().displayImage(user.getHead(),mRevise_head);
        }else {
            mRevise_head.setImageResource(R.drawable.fragment_head);
        }
        mRevise_name.setText(user.getName());
        if (!"null".equals(user.getSex()) && !TextUtils.isEmpty(user.getSex())){
            mRevise_sex.setText(user.getSex());
        }
        if (!"null".equals(user.getBirthday()) && !TextUtils.isEmpty(user.getBirthday())){
            mRevise_birth.setText(user.getBirthday());
        }
        Log.e("user", "onCreate: ======================" + user.toString() );
        if (!"null".equals(user.getAddress()) && !TextUtils.isEmpty(user.getAddress())){
            mRevise_area.setText(user.getAddress());
        }

        if (!TextUtils.isEmpty(user.getKey1())){
            tags.add(user.getKey1());
        }
        if (!TextUtils.isEmpty(user.getKey2())){
            tags.add(user.getKey2());
        }
        if (!TextUtils.isEmpty(user.getKey3())){
            tags.add(user.getKey3());
        }
        if (!TextUtils.isEmpty(user.getKey4())){
            tags.add(user.getKey4());
        }
        if (!TextUtils.isEmpty(user.getKey5())){
            tags.add(user.getKey5());
        }

        if (!"".equals(user.getType())){
            if ("0".equals(user.getType())){
                mIv_share.setImageResource(R.drawable.check_no);
            }else if("1".equals(user.getType())){
                mIv_share.setImageResource(R.drawable.check_ok);
            }

            if ("0".equals(user.getLimit_position())) {
                limit_position = "0";
                mBtn_time_area.setBackgroundColor(Color.parseColor("#3686ff"));
                mBtn_stone_area.setBackgroundColor(Color.parseColor("#ebebeb"));
                mEv_share_area.setVisibility(View.GONE);
            }else if ("1".equals(user.getLimit_position())){
                limit_position = "1";
                mBtn_stone_area.setBackgroundColor(Color.parseColor("#3686ff"));
                mBtn_time_area.setBackgroundColor(Color.parseColor("#ebebeb"));
                mEv_share_area.setVisibility(View.VISIBLE);
                mEv_share_area.setText(user.getPosition());
            }
        }

        tagsAdapter = new GridViewAdapter(this);
        mRevise_gd.setAdapter(tagsAdapter);
        Utils.reMesureGridViewHeight(mRevise_gd);

        mRevise_gd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                 if (position == tags.size()){
                     addPopupWindow = new AddWordPopupWindow(RevisePersonActivity.this, new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             if (v.getTag() != null){
                                 Log.e("tag", "onClick:---------------- " +  v.getTag() + "");
                                 tags.add(position,v.getTag() + "");
                                 tagsAdapter.notifyDataSetChanged();
                                 Utils.reMesureGridViewHeight(mRevise_gd);
                                 Log.e("tag", "onClick:---------------- " +  v.getTag() + "");

                             }
                             addPopupWindow.dismiss();
                         }
                     });
                     //添加关键词
                     addPopupWindow.showAtLocation(findViewById(R.id.ll_revise), Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
                 }
            }
        });

    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.revise_head:
                 shangchuantouxiang();
                 break;
             case R.id.revise_sex:
                 sexPopupWindow = new SexPopupWindow(this,this);
                 // 显示窗口
                 sexPopupWindow.showAtLocation(findViewById(R.id.ll_revise), Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
                 break;
             case R.id.revise_birth:
                 // 点击了选择日期按钮
                 Calendar selectedDate = Calendar.getInstance();
                 Calendar startDate = Calendar.getInstance();
                 startDate.set(1900, 1, 1);
                 Calendar endDate = Calendar.getInstance();
                 endDate.set(2080, 1, 1);

                 Calendar now = Calendar.getInstance();
                 now.setTime(new Date());
                 new TimePickerView.Builder(RevisePersonActivity.this, new TimePickerView.OnTimeSelectListener() {
                     @Override
                     public void onTimeSelect(Date date, View v) {
                         mRevise_birth.setText(getTime(date));
                     }
                 })
                         .setRange(startDate.get(Calendar.YEAR), endDate.get(Calendar.YEAR))
                         .setDate(now)
                         .setSubmitColor(R.color.main_color)
                         .setCancelColor(R.color.main_color)
                         .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                         .build().show();
                 break;
             case R.id.tv_sex_ok:
                 if (v.getTag()  != null){
                     mRevise_sex.setText(v.getTag() + "");
                 }
                 sexPopupWindow.dismiss();
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
             case R.id.righttv: //保存修改的信息
                 savePersonData();
                 break;
             case R.id.iv_share://可选按钮
                 if (flag == false){
                     mIv_share.setImageResource(R.drawable.check_no);
                     type = "0";
                     flag = true;
                 }else{
                     mIv_share.setImageResource(R.drawable.check_ok);
                     type = "1";
                     flag = false;
                 }
                 break;
             case R.id.btn_time_area://实时位置
                 limit_position = "0";
                 mBtn_time_area.setBackgroundColor(Color.parseColor("#3686ff"));
                 mBtn_stone_area.setBackgroundColor(Color.parseColor("#ebebeb"));
                 mEv_share_area.setVisibility(View.GONE);
                 Utils.reMesureGridViewHeight(mRevise_gd);
                 break;
             case R.id.btn_stone_area://固定位置
                 limit_position = "1";
                 mBtn_stone_area.setBackgroundColor(Color.parseColor("#3686ff"));
                 mBtn_time_area.setBackgroundColor(Color.parseColor("#ebebeb"));
                 Intent intent = new Intent(this, PoiSearchActivity.class);
                 intent.putExtra("longitude",longitude);
                 intent.putExtra("latitude",latitude);
                 intent.putExtra("city",city);
                 intent.putExtra("address",address);
                 startActivityForResult(intent,100);
                 break;

         }
    }

    /*
    保存用户信息
     */
    private void savePersonData() {
        RequestParams params = new RequestParams();

        if (bitmap1 != null) {
            params.put("pic", BitmapToBase64.bitmapToBase64(bitmap1));
        }

        String name = mRevise_name.getText().toString();
        if (TextUtils.isEmpty(mRevise_name.getText())) {
            T.showShort(this, "请输入姓名！");
            return;
        }
        params.put("staff_name", name);

        String sex = mRevise_sex.getText().toString();
        if (!TextUtils.isEmpty(sex)) {
            params.put("sex", sex);
        }
        String birthday = mRevise_birth.getText().toString();
        if (!TextUtils.isEmpty(birthday)) {
            params.put("birthday", birthday);
        }
        String address = mRevise_area.getText().toString();
        if (!TextUtils.isEmpty(address)) {
            params.put("address", address);
        }

        //staff_key1
        if (tags.size() == 1){
            params.put("staff_key1", tags.get(0));
            params.put("staff_key2", "");
            params.put("staff_key3", "");
            params.put("staff_key4", "");
            params.put("staff_key5", "");
        }else if (tags.size() == 2){
            params.put("staff_key1", tags.get(0));
            params.put("staff_key2", tags.get(1));
            params.put("staff_key3", "");
            params.put("staff_key4", "");
            params.put("staff_key5", "");
        }else if (tags.size() == 3){
            params.put("staff_key1", tags.get(0));
            params.put("staff_key2", tags.get(1));
            params.put("staff_key3", tags.get(2));
            params.put("staff_key4", "");
            params.put("staff_key5", "");
        }else if (tags.size() == 4) {
            params.put("staff_key1", tags.get(0));
            params.put("staff_key2", tags.get(1));
            params.put("staff_key3", tags.get(2));
            params.put("staff_key4", tags.get(3));
            params.put("staff_key5", "");
        }else if (tags.size() == 5){
            params.put("staff_key1", tags.get(0));
            params.put("staff_key2", tags.get(1));
            params.put("staff_key3", tags.get(2));
            params.put("staff_key4", tags.get(3));
            params.put("staff_key5", tags.get(4));
        }else {
            params.put("staff_key1", "");
            params.put("staff_key2", "");
            params.put("staff_key3", "");
            params.put("staff_key4", "");
            params.put("staff_key5", "");
        }

        params.put("limit_position",limit_position);
        params.put("type",type);
        if ("1".equals(limit_position)){
            if (TextUtils.isEmpty(mEv_share_area.getText().toString().trim())){
                T.showShort(this,"请输入固定位置~");
                return;
            }
            params.put("position",mEv_share_area.getText().toString().trim());
        }else {
            //从SplashActivity传递来的
            Log.e("position", "savePersonData: -----------------------" + mShareds.getString("mPostion","") + mShareds.getString("mLocation","" ));
            params.put("position",mShareds.getString("mPostion",""));
            stoneLocation = mShareds.getString("mLocation","");
        }

        params.put("location",stoneLocation);

        Log.e("position", "savePersonData: ------------------------------" + params );

        String url = UrlTools.url + UrlTools.XIUGAI_GERENZILIAO;

        AsyncHttpServiceHelper.post(url, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                        final JsonBean bean = JsonUtils.getMessage(new String(
                                arg2));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if ("200".equals(bean.getCode())) {
                                    T.showShort(RevisePersonActivity.this,
                                            "修改成功！");
//                                    getUser();
                                    finish();
                                } else {
                                    T.showShort(RevisePersonActivity.this,
                                            bean.getMsg());
                                }
                            }
                        });

                        super.onSuccess(arg0, arg1, arg2);
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                T.showShort(RevisePersonActivity.this, "修改失败！");
                            }
                        });
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }

                });
    }

    private SelectPicPopupWindow window;// 弹出图片选择框；
    private Bitmap bitmap1;
    private void shangchuantouxiang() {
        window = new SelectPicPopupWindow(this, this);
        // 显示窗口
        window.showAtLocation(findViewById(R.id.ll_revise), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    private String getTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    // 从相册选择：
    private void xiangCe() {
        Intent i2 = new Intent(Intent.ACTION_PICK, null);
        i2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(i2, 1);
    }

    // 拍照：
    private void paiZhao() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "caigou.jpg")));
        startActivityForResult(i, 2);
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
                        mRevise_head.setImageBitmap(bitmap1);
                    }
                }
                break;
            case 100:
                if (resultCode == RESULT_OK){
                    mEv_share_area.setVisibility(View.VISIBLE);
                    Utils.reMesureGridViewHeight(mRevise_gd);
                    fromAddress = data.getStringExtra("fromAdd");
                    stoneLocation = data.getStringExtra("location");
                    Log.e("result", "onActivityResult:-------------------- " + fromAddress);
                    mEv_share_area.setText(fromAddress);
                }
                break;
        }
    }
    /*
    添加标签的adapter
     */
    public class GridViewAdapter extends BaseAdapter{
        private LayoutInflater inflater;

        public GridViewAdapter(Context context){
            inflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            if (tags != null){
                return tags.size() + 1;
            }else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return tags.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.revise_item_tag,null);
                //初始化组件
                viewHolder.mTag_tv = (TextView)convertView.findViewById(R.id.tag_tv);
                viewHolder.mTag_delete = (ImageView) convertView.findViewById(R.id.tag_delete);
                //附加对象
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (position == tags.size()){
                viewHolder.mTag_tv.setText("+添加");
                viewHolder.mTag_tv.setTag("+添加");
                viewHolder.mTag_tv.setTextColor(Color.parseColor("#3686ff"));
                viewHolder.mTag_delete.setVisibility(View.GONE);
                if (position == 5){
                    viewHolder.mTag_tv.setVisibility(View.GONE);
                }
            }else {
                viewHolder.mTag_tv.setText(tags.get(position));
                viewHolder.mTag_delete.setVisibility(View.VISIBLE);
                viewHolder.mTag_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tags.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }

            return convertView;
        }

        public class ViewHolder{
            public  TextView mTag_tv;
            public  ImageView mTag_delete;
        }
    }
}
