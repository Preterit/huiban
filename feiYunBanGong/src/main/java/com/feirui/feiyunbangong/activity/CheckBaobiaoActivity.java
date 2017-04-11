package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_baobiao);



        Bundle bundle = this.getIntent().getExtras();
        int position = bundle.getInt("position");


        initUI(position);

        if (mFormType == ReadFormActivity.MY_FORM) {
            loadMyForm();
        }
        if (mFormType == ReadFormActivity.OTHER_FORM) {
            loadOtherForm();
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
                mFormAdapter.setData(readFormEntity.getInfor());
            }
        });
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
                mBeanList.addAll(readFormEntity.getInfor());
            }
        });
    }

    private void initUI(int  position) {
        today_work = (TextView) findViewById(R.id.today_work);
        unfinish_work = (TextView) findViewById(R.id.today_unfinish_work);
        tomorrow_work = (TextView) findViewById(R.id.tomorrow_work);
        remark_work = (TextView) findViewById(R.id.remark_work);
        img_remark1 = (ImageView) findViewById(R.id.img_remark1);
        img_remark2 = (ImageView) findViewById(R.id.img_remark2);
        img_remark3 = (ImageView) findViewById(R.id.img_remark3);
        form_time = (TextView) findViewById(R.id.tv_form_time);

        form_time.setText(mBeanList.get(position).getForm_time());

    }
}
