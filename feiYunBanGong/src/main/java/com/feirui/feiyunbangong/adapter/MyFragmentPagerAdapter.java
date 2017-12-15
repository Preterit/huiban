package com.feirui.feiyunbangong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.feirui.feiyunbangong.fragment.CommitFragment;
import com.feirui.feiyunbangong.fragment.ReviseFragment;
import com.feirui.feiyunbangong.fragment.SendFragment;

/**
 * Created by xy on 2017-12-14.
 * ViewPager 的适配器
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"我提交的","我审批的","抄送我的"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1){
            return new ReviseFragment();
        }else if (position == 2){
            return new SendFragment();
        }
        return new CommitFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //设置tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
