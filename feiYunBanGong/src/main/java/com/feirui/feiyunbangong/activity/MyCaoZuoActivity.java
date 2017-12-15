package com.feirui.feiyunbangong.activity;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.MyFragmentPagerAdapter;
import com.feirui.feiyunbangong.fragment.CommitFragment;
import com.feirui.feiyunbangong.fragment.ReviseFragment;
import com.feirui.feiyunbangong.fragment.SendFragment;

public class MyCaoZuoActivity extends BaseActivity implements View.OnClickListener,
        ReviseFragment.OnFragmentInteractionListener,
        SendFragment.OnFragmentInteractionListener{
    private TabLayout mHandle_tab;
    private LinearLayout mLl_type_handle;
    //请假，报销，外出，付款，采购，其他
    private TextView righttv,mTv_leave, mTv_submit,mTv_out,mTv_spend,mTv_purchase,mTv_else;
    private boolean flag = true;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cao_zuo);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        initView();
        initListener();
        initDate();
        /**
         * button 传至到fragment2
         */
        Button button= (Button) findViewById(R.id.commit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommitFragment fragment21= (CommitFragment) myFragmentPagerAdapter.getItem(0);
                fragment21.showMessageFromActivity("我是activity传递过来的数据");
            }
        });
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("操作记录");
        righttv = (TextView) findViewById(R.id.righttv);
        righttv.setVisibility(View.VISIBLE);
        righttv.setText("类型");

        mHandle_tab = (TabLayout) findViewById(R.id.handle_tab);
        //ViewPager
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(myFragmentPagerAdapter);
        //将TabLayout和ViewPager绑定在一起，使双方各自的改变都能直接影响另一方，解放了开发人员对双方变动事件的监听
        mHandle_tab.setupWithViewPager(mViewPager);
        //指定tab的位置

        mTv_leave = (TextView) findViewById(R.id.tv_leave);
        mTv_submit = (TextView) findViewById(R.id.tv_submit);
        mTv_out = (TextView) findViewById(R.id.tv_out);
        mTv_spend = (TextView) findViewById(R.id.tv_spend);
        mTv_purchase = (TextView) findViewById(R.id.tv_purchase);
        mTv_else = (TextView) findViewById(R.id.tv_else);

        mLl_type_handle = (LinearLayout) findViewById(R.id.ll_type_handle);
        mLl_type_handle.setVisibility(View.GONE);

    }

    private void initListener() {
        righttv.setOnClickListener(this);
        mTv_leave.setOnClickListener(this);
        mTv_out.setOnClickListener(this);
        mTv_submit.setOnClickListener(this);
        mTv_spend.setOnClickListener(this);
        mTv_purchase.setOnClickListener(this);
        mTv_else.setOnClickListener(this);
        rightll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    mLl_type_handle.setVisibility(View.VISIBLE);
                } else {
                    mLl_type_handle.setVisibility(View.GONE);
                }
                flag = !flag;
            }
        });
    }

    private void initDate() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.righttv:
                if (flag){
                   mLl_type_handle.setVisibility(View.VISIBLE);
                }else {
                    mLl_type_handle.setVisibility(View.GONE);
                }
                flag = !flag;
                break;


        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        uri.getFragment();
    }
}
