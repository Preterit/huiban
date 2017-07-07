package com.feirui.feiyunbangong.activity;

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

public class RenWuListActivity extends AppCompatActivity {
    private ImageView iv_back;
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
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener((View.OnClickListener) this);
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
