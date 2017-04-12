package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.adapter.FragPagerAdapter;
import com.feirui.feiyunbangong.fragment.MyReceiveTaskFragment;
import com.feirui.feiyunbangong.fragment.MyReleaseTaskFragment;

import java.util.ArrayList;

public class WoDeTaskActivity extends BaseActivity  {
    private MyReceiveTaskFragment receiveFrg;
    private MyReleaseTaskFragment releaseFrg;
    private ViewPager containerId;
    private RadioGroup taskSelectGroup;
    private ArrayList<Fragment> frgs;
    private RadioButton receiveButton;
    private RadioButton releaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wo_de_task);
        containerId=(ViewPager) findViewById(R.id.containerId);

        taskSelectGroup=(RadioGroup)findViewById(R.id.taskSelectGroup);
        receiveButton=(RadioButton)taskSelectGroup.findViewById(R.id.receiveButton);
        releaseButton=(RadioButton)taskSelectGroup.findViewById(R.id.releaseButton);

        receiveFrg = new MyReceiveTaskFragment();
        releaseFrg=new MyReleaseTaskFragment();
        FragmentTransaction ts=getSupportFragmentManager().beginTransaction();
        ts.add(R.id.containerId,receiveFrg);
        ts.add(R.id.containerId,releaseFrg);
        ts.show(receiveFrg);
        ts.hide(releaseFrg);
        ts.commit();
        setListeners();
        initView();
        setAdapter();
    }

    private void setAdapter() {
        frgs=new ArrayList<Fragment>();
        frgs.add(new MyReceiveTaskFragment());
        frgs.add(new MyReleaseTaskFragment());
        FragPagerAdapter adapter = new FragPagerAdapter(getSupportFragmentManager(), frgs);
        containerId.setAdapter(adapter);
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("我的任务");

    }

    private void setListeners() {
        containerId.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                FragmentTransaction  frgt=getSupportFragmentManager().beginTransaction();
                switch (checkedId){
                    case R.id.receiveButton:
                        containerId.setCurrentItem(0);
                        if(receiveFrg!=null){
                            frgt.show(receiveFrg);
                            frgt.hide(releaseFrg);
                            frgt.commit();
                        }else{
                            receiveFrg=new MyReceiveTaskFragment();
                            frgt.add(R.id.containerId,receiveFrg);
                        }
                        break;
                    case R.id.releaseButton:
                        containerId.setCurrentItem(1);
                        if(releaseFrg!=null){
                            frgt.show(releaseFrg);
                            frgt.hide(receiveFrg);
                            frgt.commit();
                        }else{
                            releaseFrg=new MyReleaseTaskFragment();
                            frgt.add(R.id.containerId,releaseFrg);
                        }
                        break;
                }

            }
        });


    }

}
