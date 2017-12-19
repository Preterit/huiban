package com.feirui.feiyunbangong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.feirui.feiyunbangong.fragment.CommitFragment;
import com.feirui.feiyunbangong.fragment.ReviseFragment;
import com.feirui.feiyunbangong.fragment.SendFragment;

import java.util.List;

/**
 * Created by xy on 2017-12-14.
 * ViewPager 的适配器
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"我提交的","我审批的","抄送我的"};
    List<Fragment> mList;
    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> mList) {
        super(fm);
        this.mList = mList;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        if(mList.size()<=0){
            return 0;
        }
        return mList.size();
    }

    //设置tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
