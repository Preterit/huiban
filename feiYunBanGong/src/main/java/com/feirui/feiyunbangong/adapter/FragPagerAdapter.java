package com.feirui.feiyunbangong.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;



public class FragPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> frg;
    public FragPagerAdapter(FragmentManager fm,List<Fragment> frg) {
        super(fm);
        this.frg=frg;
    }



    @Override
    public Fragment getItem(int position) {
        return frg.get(position);
    }

    @Override
    public int getCount() {
        return frg.size();
    }
}
