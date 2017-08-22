package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.fragment.FormListFragment;
import com.viewpagerindicator.TabPageIndicator;
/**
 * 任务单界面
 * */
public class RenWuDanActivity extends BaseActivity {

    private TabPageIndicator indicatorReadStatement;
    private ViewPager vpReadStatement;
    public static final int QUANBU = 1;//全部任务
    public static final int DAIJIEDAN = 2;//
    public static final int JINXINGZHONG = 3;//进行中
    public static final int YIWANCHENG = 4;//他人的报表


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_wu_dan);
        initView();
    }
    private MyAdapter mMyAdapter;

    private void initView() {
        initTitle();
        setLeftDrawable(R.drawable.arrows_left);
        setCenterString("任务单");
        setRightVisibility(false);

        mMyAdapter = new MyAdapter(getSupportFragmentManager());
        vpReadStatement = (ViewPager) findViewById(R.id.vRenWuStatement);
        vpReadStatement.setAdapter(mMyAdapter);

        indicatorReadStatement = (TabPageIndicator) findViewById(R.id.indicatorRenWuStatement);
        indicatorReadStatement.setViewPager(vpReadStatement);
    }

    /**
     * A simple FragmentPagerAdapter that returns two TextFragment and a SupportMapFragment.
     */
    public static class MyAdapter extends FragmentPagerAdapter {

        String[] mTitles = new String[]{"全部", "待接单","进行中","已完成"};

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FormListFragment.newInstance(QUANBU);
                case 1:
                    return FormListFragment.newInstance(DAIJIEDAN);
                case 2:
                    return FormListFragment.newInstance(JINXINGZHONG);
                case 3:
                    return FormListFragment.newInstance(YIWANCHENG);
                default:
                    return null;
            }
        }
    }

}
