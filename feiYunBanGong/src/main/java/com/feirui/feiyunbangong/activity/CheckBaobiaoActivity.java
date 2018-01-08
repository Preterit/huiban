package com.feirui.feiyunbangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.FormAdapter;
import com.feirui.feiyunbangong.adapter.NoScrollGridAdapter;
import com.feirui.feiyunbangong.entity.ReadFromDetail;
import com.feirui.feiyunbangong.fragment.FormListFragment;
import com.feirui.feiyunbangong.utils.AsyncHttpServiceHelper;
import com.feirui.feiyunbangong.utils.UrlTools;
import com.feirui.feiyunbangong.view.NoScrollGridView;
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
    private TextView form_time;
    private static final String FORM_TYPE = "formType";
    private FormListFragment.OnListFragmentInteractionListener mListener;
    private int mFormType = 1;
    private FormAdapter mFormAdapter;
    private List<ReadFromDetail.InforBean> mBeanList;
    private int position;
    private int id;


    public TextView centerTv;
    public ImageView leftIv, rightIv;
    public LinearLayout leftll;
    public RelativeLayout rightll;
    public RelativeLayout top;
    private NoScrollGridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_baobiao);
        initUI();
        initView();
        Bundle bundle = this.getIntent().getExtras();

        position = bundle.getInt("position");
        id = bundle.getInt("id");
        mFormType = bundle.getInt("type");
        Log.e("查看详细报表","条目id" + id + "");
        mBeanList = new ArrayList<>();
        loadMyForm();

//        if (mFormType == ReadFormActivity.MY_FORM) {
//            loadMyForm();
//        }
//        if (mFormType == ReadFormActivity.OTHER_FORM) {
//            loadOtherForm();
//        }

//        Log.d("条目类型---------------", mBeanList.toString());

    }


    private void initView() {
        initTitle();
        leftIv.setImageResource(R.drawable.arrows_left);
        centerTv.setText("查看报表");
    }

    private void initTitle() {
        top = (RelativeLayout) findViewById(R.id.top);
        leftll = (LinearLayout) findViewById(R.id.leftll);
        rightll = (RelativeLayout) findViewById(R.id.rightll);
        leftIv = (ImageView) findViewById(R.id.leftIv);
        centerTv = (TextView) findViewById(R.id.centerTv);
        rightIv = (ImageView) findViewById(R.id.rightIv);
        rightIv.setVisibility(View.VISIBLE);
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




    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(CheckBaobiaoActivity.this, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
        overridePendingTransition(R.anim.aty_zoomin,
                R.anim.aty_zoomout);
    }
    public void setData(List<ReadFromDetail.InforBean> data) {
        mBeanList.clear();
        try {
            mBeanList.addAll(data);
        } catch (Exception e) {
            Log.d("查看详细报表", "setData错误");
        }

    }

    /**
     * 读取其自己的列表数据
     */
    private void loadMyForm() {
        final ArrayList<String> imageUrls = new ArrayList<String>();
//        "http://123.57.45.74/feiybg1/public/index.php/api/form/details"
        String url = UrlTools.url + UrlTools.FORM_LIST_DETAILS;
        RequestParams requestParams = new RequestParams();
        requestParams.put("id",id+"");
        AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                ReadFromDetail readFromDetail = gson.fromJson(new String(responseBody), ReadFromDetail.class);
                Log.e("查看详细报表", "getInfor: "+readFromDetail.getInfor());
                Log.e("查看详细报表", "position"+ "------" + position );
                form_time.setText(readFromDetail.getInfor().getForm_time());//获取报表的时间
                today_work.setText(readFromDetail.getInfor().getOption_one_value());//获取今天的工作
                unfinish_work.setText(readFromDetail.getInfor().getOption_two_value());//获取今天未完成的工作
                tomorrow_work.setText(readFromDetail.getInfor().getOption_three_value());//获取今天未完成的工作
                remark_work.setText(readFromDetail.getInfor().getRemarks());//获取备注

                String picUrls =  readFromDetail.getInfor().getPicture();
                String[] str=picUrls.split(",");
                for (int i = 0; i < str.length; i++){
                    imageUrls.add(UrlTools.pcUrl + str[i]);
                }
                Log.e("查看详细报表", "图片路径: "+imageUrls.toString());
                // 发表的内容图片显示与隐藏：
                readFromDetail.getInfor().getPicture();
                if (imageUrls == null || imageUrls.size() == 0||picUrls.isEmpty()) {
                    gridview.setVisibility(View.GONE);
                } else {
                    gridview.setVisibility(View.VISIBLE);
                    gridview.setAdapter(new NoScrollGridAdapter(CheckBaobiaoActivity.this,
                            imageUrls));
                }
                // img_remark1.setImageURI(mBeanList.get(position).);
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                imageBrower(position, imageUrls);
            }
        });

    }

    /**
     * 读取其他人的列表数据
     */
    private void loadOtherForm() {
        final ArrayList<String> imageUrls = new ArrayList<String>();
        String url = UrlTools.url + UrlTools.FORM_LIST_DETAILS;
        RequestParams requestParams = new RequestParams();
        requestParams.put("id",id);
        AsyncHttpServiceHelper.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                ReadFromDetail readFromDetail = gson.fromJson(new String(responseBody), ReadFromDetail.class);

                Log.e("查看详细报表---其他人", "getInfor: "+readFromDetail.getInfor());
                form_time.setText(readFromDetail.getInfor().getForm_time());
                today_work.setText(readFromDetail.getInfor().getOption_one_value());//获取今天的工作
                unfinish_work.setText(readFromDetail.getInfor().getOption_two_value());//获取今天未完成的工作
                tomorrow_work.setText(readFromDetail.getInfor().getOption_three_value());//获取今天未完成的工作
                remark_work.setText(readFromDetail.getInfor().getRemarks());//获取备注

                String picUrls =  readFromDetail.getInfor().getPicture();
                String[] str=picUrls.split(",");
                for (int i = 0; i < str.length; i++){
                    imageUrls.add(str[i]);
                }
                Log.e("查看详细报表---其他人", "imageUrls: "+imageUrls.toString());
                // 发表的内容图片显示与隐藏：
                readFromDetail.getInfor().getPicture();
                if (imageUrls == null || imageUrls.size() == 0||picUrls.isEmpty()) {
                    gridview.setVisibility(View.GONE);
                } else {
                    gridview.setVisibility(View.VISIBLE);
                    gridview.setAdapter(new NoScrollGridAdapter(CheckBaobiaoActivity.this,
                            imageUrls));
                }
            }
        });
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                imageBrower(position, imageUrls);
            }
        });

    }


    private void initUI() {
        today_work = (TextView) findViewById(R.id.today_work);
        unfinish_work = (TextView) findViewById(R.id.today_unfinish_work);
        tomorrow_work = (TextView) findViewById(R.id.tomorrow_work);
        remark_work = (TextView) findViewById(R.id.remark_work);
        form_time = (TextView) findViewById(R.id.tv_form_time);
       gridview = (NoScrollGridView)findViewById(R.id.gridview_work);

    }
}
