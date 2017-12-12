package com.feirui.feiyunbangong.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.fragment.TaskJieDanFragment;
import com.feirui.feiyunbangong.fragment.TaskJinXingZhongFragment;
import com.feirui.feiyunbangong.fragment.TaskQuanBuRenWuFragment;
import com.feirui.feiyunbangong.fragment.TaskYiWanChengFragment;
/**
 * 新建立的任务单页面
 * */

public class RenWuListActivity extends AppCompatActivity {
    private ImageView iv_back;
    private ImageView iv_rw;
    private ViewPager viewPager;
    TabLayout tabLayout;
    Fragment fragment = new Fragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_wu_list);
        initView();
    }

    private void initView() {
        iv_back= (ImageView) findViewById(R.id.iv_back_renwudan);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_rw = (ImageView) findViewById(R.id.iv_rw);
        iv_rw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence items[] = {"发布任务", "个人任务详情","支付页面"};

                AlertDialog alertDialog = new AlertDialog.Builder(RenWuListActivity.this).setIcon(R.drawable.selectimage)
                        .setTitle("请选择方式").setItems(items, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        startActivity(new Intent(RenWuListActivity.this, ReleaseTask.class));
                                        break;
                                    case 1:
                                        startActivity(new Intent(RenWuListActivity.this, WoDeTaskActivity.class));
                                        break;
                                    case 2:
//                                        startActivity(new Intent(RenWuListActivity.this,ZhiFuActivity.class));
                                        break;
                                }

                            }
                        }).show();
            }
        });
        viewPager= (ViewPager) findViewById(R.id.vp_renwudan);
        tabLayout= (TabLayout) findViewById(R.id.tl_tab_renwudan);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter{

        public SectionsPagerAdapter(FragmentManager fm){super(fm);}

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    fragment = new TaskQuanBuRenWuFragment();
                    break;
                case 1 :
                    fragment = new TaskJieDanFragment();
                    break;
                case 2 :
                    fragment = new TaskJinXingZhongFragment();
                    break;
                case 3 :
                    fragment = new TaskYiWanChengFragment();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "全部";
                case 1:
                    return "待接单";
                case 2:
                    return "进行中";
                case 3:
                    return "已完成";

            }
            return null;
        }
    }
}
