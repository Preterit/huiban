package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.fragment.MyReceiveTaskFragment;
import com.feirui.feiyunbangong.fragment.MyReleaseTaskFragment;

public class WoDeTaskActivity extends BaseActivity  {
    private MyReceiveTaskFragment receiveFrg;
    private MyReleaseTaskFragment releaseFrg;
    private RelativeLayout containerId;
    private RadioGroup taskSelectGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wo_de_task);
        containerId=(RelativeLayout) findViewById(R.id.containerId);
        taskSelectGroup=(RadioGroup)findViewById(R.id.taskSelectGroup);
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
    }

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("我的任务");

    }

    private void setListeners() {
        taskSelectGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FragmentTransaction  frgt=getSupportFragmentManager().beginTransaction();
                switch (checkedId){
                    case R.id.receiveButton:
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
                        if(releaseFrg!=null){
                            frgt.show(releaseFrg);
                            frgt.hide(receiveFrg);
                            frgt.commit();
                        }else{
                            releaseFrg=new MyReleaseTaskFragment();
                            frgt.add(R.id.containerId,releaseFrg);
                        }
                }

            }
        });


    }

}
