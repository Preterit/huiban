package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.FragPagerAdapter;
import com.feirui.feiyunbangong.fragment.ShenPiFragment;
import com.feirui.feiyunbangong.fragment.TiJiaoFragment;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/4/27.
 */

public class CaoZuoJiLuUpdataActivity extends BaseActivity implements OnItemSelectedListener {
    private Spinner mSpinner;
    private ArrayAdapter<String> mAdapter;
    private String[] leixing = new String[]{"选择审批类型", "请假", "报销", "外出", "付款",
            "采购", "其他"};

    private ArrayList<Fragment> mFragments;
    private int mIndex;
    private ViewPager mPager;
    private RadioButton mTiJiao;
    private RadioButton mShenPi;
    private RadioGroup mRgTools;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_caozuoji);
        initView();
//        postQingQiu("选择审批类型");
    }



    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("操作记录");
        setRightVisibility(false);

        mSpinner = (Spinner) findViewById(R.id.spinner_lei_xing);

        mAdapter = new ArrayAdapter<String>(this, R.layout.sp_item, R.id.tv, leixing);
        mSpinner.setAdapter(mAdapter);
        //监听选择类型
        mSpinner.setOnItemSelectedListener(this);

        mPager = (ViewPager) findViewById(R.id.viewPagerContent);
        mRgTools = (RadioGroup) findViewById(R.id.rgTools);
        mTiJiao = (RadioButton) findViewById(R.id.rb_ti_jiao);
        mShenPi = (RadioButton) findViewById(R.id.rb_shen_pi);

    }

    /**
     * 提交 、审批的Fragment
     */

    private void setListener(final String str) {
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mTiJiao.setChecked(true);
                        postQingQiu(str);
                        break;
                    case 1:
                        mShenPi.setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRgTools.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rb_ti_jiao:
                        mPager.setCurrentItem(0);
                        postQingQiu(str);
                        Log.d("qing","请求的类型："+str);
                        break;
                    case R.id.rb_shen_pi:
                        mPager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    private void postQingQiu(String str) {

        TiJiaoFragment tiJiaoFragment = new TiJiaoFragment(str);
        ShenPiFragment shenPiFragment = new ShenPiFragment();

        mFragments = new ArrayList<>();
        mFragments.add(tiJiaoFragment);
        mFragments.add(shenPiFragment);
        FragPagerAdapter pagerAdapter = new FragPagerAdapter(getSupportFragmentManager(),mFragments);
        mPager.setAdapter(pagerAdapter);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0 : //选择审批类型
                Log.e("qing","yyyyy");
                setListener("选择审批类型");
                break;
            case 1:
                setListener("请假");
                Log.d("TAG","点击1位置");
                break;
            case 2:
                setListener("报销");
                Log.d("TAG","点击2位置");
                break;
            case 3:
                setListener("外出");
                Log.d("TAG","点击3位置");
                break;
            case 4:
                setListener("付款");
                Log.d("TAG","点击4位置");
                break;
            case 5:
                setListener("采购");
                Log.d("TAG","点击5位置");
                break;
            case 6:
                setListener("其他");
                Log.d("TAG","点击6位置");
                break;
        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
