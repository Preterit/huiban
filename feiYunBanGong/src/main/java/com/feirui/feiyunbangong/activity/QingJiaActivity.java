package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.AddShenHeUpdateAdapter;
import com.feirui.feiyunbangong.dialog.SelectZTDialog;
import com.feirui.feiyunbangong.entity.ChildItem;
import com.feirui.feiyunbangong.entity.JsonBean;
import com.feirui.feiyunbangong.state.AppStore;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.JsonUtils;
import com.feirui.feiyunbangong.utils.L;
import com.feirui.feiyunbangong.utils.T;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.HorizontalListView;
import com.feirui.feiyunbangong.view.PView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.feirui.feiyunbangong.R.id.et_tianshu;

/**
 * 审批-请假
 *
 * @author admina
 */
public class QingJiaActivity extends BaseActivity implements OnClickListener {
    @PView(click = "onClick")
    Button btn_submit;// 提交
    @PView(click = "onClick")
    TextView tv_leixing, tv_kaishishijian, tv_jieshushijian;// 类型，开始间，结束时间
    @PView
    EditText et_shiyou;// 请假天数，请假事由
    // 添加审批人
    @PView(click = "onClick")
    ImageView iv_add,iv_add_chaosong, iv_01;
    private ArrayList<JsonBean> list1 = new ArrayList<>();


    @PView
    HorizontalListView lv_add_shenpiren;
    @PView
    HorizontalListView lv_add_chaosong;
    AddShenHeUpdateAdapter adapter1;
    AddShenHeUpdateAdapter adapter;

    @PView
    ScrollView sv_caigou;


    private Calendar mCalendar;
    private String mDateStr;
    private Date startTime;
    private Date endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qingjia_update);
        initView();
    }

    private TextView mTianshu;

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("请假");
        setRightVisibility(false);
//        adapter = new AddShenHeAdapter(getLayoutInflater(),
//                QingJiaActivity.this);
        list1 = new ArrayList<>();
        list1.add(new JsonBean("其他"));
        //审批人列表
        adapter = new AddShenHeUpdateAdapter(getLayoutInflater(),list1,QingJiaActivity.this);//修改之前的listviewadapter
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
        adapter1 = new AddShenHeUpdateAdapter(getLayoutInflater(),list1,QingJiaActivity.this);
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

        mCalendar = Calendar.getInstance();
        mCalendar.setTime(new Date());
        mTianshu = (TextView) findViewById(et_tianshu);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == 100) {
            @SuppressWarnings("unchecked")
             ArrayList<ChildItem> childs = (ArrayList<ChildItem>) data
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

            super.onActivityResult(requestCode, resultCode, data);
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
    }
    public static String getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_01:
//                adapter.reduce(((AddShenHe) view.getTag()));// 删除审批人；
                break;
            case R.id.iv_add:
                //跳转到添加成员页面
                final Intent intent = new Intent(this, AddChengYuanActivity.class);
                startActivityForResult(intent, 200);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
                //startActivityForResult(intent, 101);// 请求码；

                break;

            case R.id.iv_add_chaosong:
                final Intent intent1 = new Intent(this, AddChengYuanActivity.class);
                startActivityForResult(intent1, 300);
                overridePendingTransition(R.anim.aty_zoomin, R.anim.aty_zoomout);
//			startActivityForResult(intent, 101);// 请求码；
                break;

            case R.id.tv_leixing:// 请假类型
                ArrayList<String> list = new ArrayList<>();
                list.add("事假");
                list.add("病假");
                list.add("工伤假");
                list.add("婚假");
                list.add("丧假");
                list.add("产假");
                list.add("探亲家");
                list.add("公假");
                list.add("年休假");
                list.add("其他");
                SelectZTDialog dialog = new SelectZTDialog(this, "请选择请假类型", list,
                        new SelectZTDialog.MyDailogCallback() {
                            @Override
                            public void onOK(String s) {
                                tv_leixing.setText(s);
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                dialog.show();
                break;
            case R.id.tv_kaishishijian:

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());

                new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String str = getDateStr(date);
                        tv_kaishishijian.setText(str);
                        startTime = date;
                    }
                })
                        .setRange(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.YEAR) + 1)
                        .setDate(calendar)
                        .setSubmitColor(R.color.main_color)
                        .setCancelColor(R.color.main_color)
                        .setType(TimePickerView.Type.ALL)
                        .build().show();


                break;
            case R.id.tv_jieshushijian:
                if (TextUtils.isEmpty(tv_kaishishijian.getText())) {
                    T.showShort(this, "请先选择开始时间");
                    return;
                }
                Calendar now = Calendar.getInstance();
                now.setTime(new Date());

                // 点击了选择日期按钮
                new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        endTime = date;
                        if (endTime.getTime() < startTime.getTime()) {
                            T.showShort(QingJiaActivity.this, "结束时间选择错误");
                            return;
                        }
                        String str = getDateStr(date);
                        tv_jieshushijian.setText(str);

                        //计算请假时间
                        int intervalDay;
                       int mo =(int) ((endTime.getTime() - startTime.getTime()) % (1000*3600*24));
                        Log.e("请假时间", "startTime: "+ startTime.getTime());
                        Log.e("请假时间", "endTime: "+endTime.getTime() );
                        Log.e("请假时间", "mo: "+mo );
                        mTianshu.setText(getDatePoor(endTime,startTime) );
//                        if (mo == 0) {
//                            intervalDay = (int) ((endTime.getTime() - startTime.getTime()) / (1000*3600*24));
//                            mTianshu.setText(intervalDay + "");
//                        } else {
//                            intervalDay = (int) ((endTime.getTime() - startTime.getTime()) / (1000*3600*24)) + 1;
//                            mTianshu.setText(intervalDay + "");
//                        }


//                        mCalendar.setTime(startTime);
//                        int startDay = mCalendar.get(Calendar.DAY_OF_MONTH);
//                        int startHour = mCalendar.get(Calendar.HOUR_OF_DAY);
//                        mCalendar.setTime(endTime);
//                        int endDay = mCalendar.get(Calendar.DAY_OF_MONTH);
//                        int endHour = mCalendar.get(Calendar.HOUR_OF_DAY);
//
//                        String intervalDay = endDay - startDay + "天";


                    }
                })
                        .setRange(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.YEAR) + 1)
                        .setDate(now)
                        .setSubmitColor(R.color.main_color)
                        .setCancelColor(R.color.main_color)
                        .setType(TimePickerView.Type.ALL)
                        .build().show();
                break;
            case R.id.btn_submit: // 提交

                if (TextUtils.isEmpty(tv_leixing.getText().toString().trim())) {
                    T.showShort(this, "请选择请假类型");
                    return;
                }
                if (TextUtils.isEmpty(tv_kaishishijian.getText().toString().trim())) {
                    T.showShort(this, "请选择开始时间");
                    return;
                }
                if (TextUtils.isEmpty(tv_jieshushijian.getText().toString().trim())) {
                    T.showShort(this, "请选择结束时间");
                    return;
                }
                if (TextUtils.isEmpty(et_shiyou.getText().toString().trim())) {
                    T.showShort(this, "请选择事由");
                    return;
                }

                RequestParams params = new RequestParams();
                params.put("leave_type", tv_leixing.getText().toString().trim());
                params.put("leave_start", tv_kaishishijian.getText().toString()
                        .trim());
                params.put("leave_end", tv_jieshushijian.getText().toString()
                        .trim());
                params.put("leave_days", mTianshu.getText().toString().trim());
                params.put("leave_reason", et_shiyou.getText().toString().trim());

                //从适配器中取出审批人集合
                List<ChildItem> shenPi = adapter.getList();
                StringBuffer sb_id = new StringBuffer();
                // 循环拼接添加成员id,每个id后加逗号
                for (int i = 0; i < shenPi.size(); i++) {
                    sb_id.append(shenPi.get(i).getId());
                    sb_id.append(",");
                    Log.d("adapterTag","适配器上的数据"+sb_id);
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

//                for (int i = 0; i < adapter.getCount(); i++) {
//                    AddShenHe ash = (AddShenHe) adapter.getItem(i);
//                    sb_id.append(ash.getId() + ",");
//                }

                if (TextUtils.isEmpty(sb_id.toString().trim())) {
                    T.showShort(this, "请选择审批人");
                    return;
                }
                params.put("leave_approvers", sb_id.deleteCharAt(sb_id.length() - 1).toString());
                params.put("ccuser_id", cs_id.deleteCharAt(cs_id.length() - 1).toString());
                String url = UrlTools.url1 + UrlTools.LEAVE_ADD_LEAVE1;
                L.e("审批-请假url" + url + " params" + params);
                AsyncHttpServiceHelper.post(url, params,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int arg0, Header[] arg1,
                                                  byte[] arg2) {
                                super.onSuccess(arg0, arg1, arg2);
                                final JsonBean json = JsonUtils
                                        .getMessage(new String(arg2));
                                if ("200".equals(json.getCode())) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            T.showShort(QingJiaActivity.this,
                                                    json.getMsg());
                                            finish();
                                            overridePendingTransition(
                                                    R.anim.aty_zoomclosein,
                                                    R.anim.aty_zoomcloseout);
                                        }
                                    });

                                } else {
                                    T.showShort(QingJiaActivity.this, json.getMsg());
                                }
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1,
                                                  byte[] arg2, Throwable arg3) {
                                Log.e("W",arg3.getMessage().toString());
                                super.onFailure(arg0, arg1, arg2, arg3);

                            }
                        });

                break;

        }
    }

    public String getDateStr(Date date) {
        // 获得日历实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");

        String format = sdf.format(date);

        return format;
    }

}