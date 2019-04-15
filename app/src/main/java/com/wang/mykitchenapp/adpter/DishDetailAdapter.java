package com.wang.mykitchenapp.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Wang on 2017/4/5.
 */

public class DishDetailAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    public DishDetailAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.mFragmentList = fragmentList;
        this.mTitleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mTitleList.size();
    }

    public CharSequence getPageTitle(int position){
        return mTitleList.get(position % mTitleList.size());
    }
}
