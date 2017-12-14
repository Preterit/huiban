package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.FragPagerAdapter;
import com.feirui.feiyunbangong.fragment.MyReleaseTaskFragment;
import com.feirui.feiyunbangong.fragment.Release_JieShouFragment;

import java.util.ArrayList;
/**
 * 老版任务单我的任务
* */
public class WoDeTaskActivity extends BaseActivity  {
    private Release_JieShouFragment receiveFrg;
    private MyReleaseTaskFragment releaseFrg;
    private ViewPager viewPagerId;
    private RadioGroup taskSelectGroup;
    private ArrayList<Fragment> frgs;
    private RadioButton receiveButton;
    private RadioButton releaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wo_de_task);
        viewPagerId=(ViewPager) findViewById(R.id.viewPagerId);

        taskSelectGroup=(RadioGroup)findViewById(R.id.taskSelectGroup);
        receiveButton=(RadioButton)taskSelectGroup.findViewById(R.id.receiveButton);
        releaseButton=(RadioButton)taskSelectGroup.findViewById(R.id.releaseButton);

        receiveFrg = new Release_JieShouFragment();
        releaseFrg=new MyReleaseTaskFragment();

        setListeners();
        initView();
        setAdapter();
    }

    private void setAdapter() {
        frgs=new ArrayList<Fragment>();
        frgs.add(new Release_JieShouFragment());
        frgs.add(new MyReleaseTaskFragment());
        FragPagerAdapter adapter = new FragPagerAdapter(getSupportFragmentManager(), frgs);
        viewPagerId.setAdapter(adapter);
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("我的任务");

    }

    private void setListeners() {
        viewPagerId.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        receiveButton.setChecked(true);
                        break;
                    case 1:
                        releaseButton.setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        taskSelectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.receiveButton:
                        viewPagerId.setCurrentItem(0);

                        break;
                    case R.id.releaseButton:
                        viewPagerId.setCurrentItem(1);

                        break;
                }

            }
        });


    }


}
