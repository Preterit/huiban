package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.FormAdapter;
import com.feirui.feiyunbangong.entity.ReadFormEntity;
import com.feirui.feiyunbangong.fragment.FormListFragment;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class CheckBaobiaoActivity extends FragmentActivity {

    private TextView today_work;
    private TextView unfinish_work;
    private TextView tomorrow_work;
    private TextView remark_work;
    private ImageView img_remark1;
    private ImageView img_remark2;
    private ImageView img_remark3;
    private TextView form_time;
    private static final String FORM_TYPE = "formType";
    private FormListFragment.OnListFragmentInteractionListener mListener;
    private int mFormType = 1;
    private FormAdapter mFormAdapter;
    private List<ReadFormEntity.InforBean> mBeanList;
    private int position;


    public TextView centerTv;
    public ImageView leftIv, rightIv;
    public LinearLayout leftll, rightll;
    public RelativeLayout top;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_baobiao);



        Bundle bundle = this.getIntent().getExtras();

        position = bundle.getInt("position");
        mBeanList = new ArrayList<ReadFormEntity.InforBean>();





        if (mFormType == ReadFormActivity.MY_FORM) {
            loadMyForm();
        }
        if (mFormType == ReadFormActivity.OTHER_FORM) {
            loadOtherForm();
        }

        initUI();
        initView();

        Log.d("条目类型---------------", mBeanList.toString());

    }


    // [InforBean{id=313, staff_id=415, option_one='今天完成工作', option_two='未完成工作', option_three='明日工作计划', picture='', remarks='备注备注备注', form_time='2017-04-11 15:46:30', company_id=100172, type_id=1, name='李策', pic='http://123.57.45.74/feiybg/public/static/staff_head/415/793eb37f1ed73eaa94ea01b0bcedf790.jpeg', staff_duties=null},
    // InforBean{id=311, staff_id=415, option_one='这个是月报', option_two='未完成工作', option_three='下月工作', picture='', remarks='', form_time='2017-04-07 14:17:51', company_id=100172, type_id=3, name='李策', pic='http://123.57.45.74/feiybg/public/static/staff_head/415/793eb37f1ed73eaa94ea01b0bcedf790.jpeg', staff_duties=null},
    // InforBean{id=310, staff_id=415, option_one='这个是周报', option_two='未完成工作', option_three='下周工作', picture='', remarks='', form_time='2017-04-07 14:16:47', company_id=100172, type_id=2, name='李策', pic='http://123.57.45.74/feiybg/public/static/staff_head/415/793eb37f1ed73eaa94ea01b0bcedf790.jpeg', staff_duties=null}, InforBean{id=309, staff_id=415, option_one='这个是日报', option_two='这个是未完成工作', option_three='这是明天的工作', picture='', remarks='', form_time='2017-04-07 14:16:01', company_id=100172, type_id=1, name='李策', pic='http://123.57.45.74/feiybg/public/static/staff_head/415/793eb37f1ed73eaa94ea01b0bcedf790.jpeg', staff_duties=null}]


    private void initView() {

        initTitle();
        leftIv.setImageResource(R.drawable.arrows_left);
        centerTv.setText("列表详细页面");
  //     Log.d("条目类型---------------", mBeanList.get(position).toString());
//        switch (mBeanList.get(position).getType_id()) {
//            case 1:
//                //日报
//                centerTv.setText("日报");
//                break;
//            case 2:
//                centerTv.setText("周报");
//                //周报
//                break;
//            case 3:
//                centerTv.setText("月报");
//                //月报
//                break;
//            case 4:
//                centerTv.setText("业绩报表");
//                //业绩报表
//                break;
//        }
    }

    private void initTitle() {
        top = (RelativeLayout) findViewById(R.id.top);
        leftll = (LinearLayout) findViewById(R.id.leftll);
        rightll = (LinearLayout) findViewById(R.id.rightll);
        leftIv = (ImageView) findViewById(R.id.leftIv);
        centerTv = (TextView) findViewById(R.id.centerTv);
        rightIv = (ImageView) findViewById(R.id.rightIv);
        if (leftIv != null) {
            leftll.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.aty_zoomclosein, R.anim.aty_zoomcloseout);
                }
            });
        }
    }


    /**
     * 读取其自己的列表数据
     */
    private void loadMyForm() {
        String url = UrlTools.pcUrl + UrlTools.MY_FORM_LIST;
        RequestParams requestParams = new RequestParams();
        AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                ReadFormEntity readFormEntity = gson.fromJson(new String(responseBody), ReadFormEntity.class);

                readFormEntity.getInfor();

                try {
                    setData(readFormEntity.getInfor());
                    mBeanList.get(position).getForm_time();

                } catch (Exception e) {
                    Log.d("CheckBaobiao--------tag", "mBeanList赋值错误");
                }

                Log.d("mBeanList的值", "onSuccess: "+mBeanList.toString());
                form_time.setText(mBeanList.get(position).getForm_time());
                today_work.setText(mBeanList.get(position).getOption_one());//获取今天的工作
                unfinish_work.setText(mBeanList.get(position).getOption_two());//获取今天未完成的工作
                tomorrow_work.setText(mBeanList.get(position).getOption_three());//获取今天未完成的工作
                remark_work.setText(mBeanList.get(position).getRemarks());//获取备注

               // img_remark1.setImageURI(mBeanList.get(position).);
            }
        });
    }

    public void setData(List<ReadFormEntity.InforBean> data) {
        mBeanList.clear();
        try {
            mBeanList.addAll(data);
        } catch (Exception e) {
            Log.d("tag", "setData错误");
        }

    }

    /**
     * 读取其他人的列表数据
     */
    private void loadOtherForm() {
        String url = UrlTools.pcUrl + UrlTools.OTHER_FORM_LIST;
        RequestParams requestParams = new RequestParams();
        AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                ReadFormEntity readFormEntity = gson.fromJson(new String(responseBody), ReadFormEntity.class);
                readFormEntity.getInfor();

                try {
                    setData(readFormEntity.getInfor());
                    mBeanList.get(position).getForm_time();

                } catch (Exception e) {
                    Log.d("CheckBaobiao--------tag", "mBeanList赋值错误");
                }

                Log.d("mBeanList集合的时间的值", "onSuccess: "+mBeanList.get(position).getForm_time());
                form_time.setText(mBeanList.get(position).getForm_time());
                today_work.setText(mBeanList.get(position).getOption_one());//获取今天的工作
                unfinish_work.setText(mBeanList.get(position).getOption_two());//获取今天未完成的工作
                tomorrow_work.setText(mBeanList.get(position).getOption_three());//获取明天的工作
                remark_work.setText(mBeanList.get(position).getRemarks());//获取备注
            }
        });
    }


    private void initUI() {
        today_work = (TextView) findViewById(R.id.today_work);
        unfinish_work = (TextView) findViewById(R.id.today_unfinish_work);
        tomorrow_work = (TextView) findViewById(R.id.tomorrow_work);
        remark_work = (TextView) findViewById(R.id.remark_work);
        img_remark1 = (ImageView) findViewById(R.id.img_remark1);
        img_remark2 = (ImageView) findViewById(R.id.img_remark2);
        img_remark3 = (ImageView) findViewById(R.id.img_remark3);
        form_time = (TextView) findViewById(R.id.tv_form_time);

    }
}
