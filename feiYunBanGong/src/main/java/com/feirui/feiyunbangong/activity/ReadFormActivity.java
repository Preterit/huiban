package com.feirui.feiyunbangong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.feirui.feiyunbangong.R;
import com.feirui.feiyunbangong.fragment.FormListFragment;
import com.viewpagerindicator.TabPageIndicator;

public class ReadFormActivity extends BaseActivity {

  private TabPageIndicator indicatorReadStatement;
  private ViewPager vpReadStatement;
  public static final int MY_FORM = 1;//我的报表
  public static final int OTHER_FORM = 2;//他人的报表

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.read_form_activity);
    initView();
  }

  private MyAdapter mMyAdapter;

  private void initView() {
    initTitle();
    setLeftDrawable(R.drawable.arrows_left);
    setCenterString("查看报表");
    setRightVisibility(false);

    mMyAdapter = new MyAdapter(getSupportFragmentManager());
    vpReadStatement = (ViewPager) findViewById(R.id.vpReadStatement);
    vpReadStatement.setAdapter(mMyAdapter);

    indicatorReadStatement = (TabPageIndicator) findViewById(R.id.indicatorReadStatement);
    indicatorReadStatement.setViewPager(vpReadStatement);

  }

  /**
   * A simple FragmentPagerAdapter that returns two TextFragment and a SupportMapFragment.
   */
  public static class MyAdapter extends FragmentPagerAdapter {

    String[] mTitles = new String[]{"我的报表", "收到的报表"};

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
          return FormListFragment.newInstance(MY_FORM);
        case 1:
          return FormListFragment.newInstance(OTHER_FORM);
        default:
          return null;
      }
    }
  }
}
