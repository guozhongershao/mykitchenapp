package com.wang.mykitchenapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.mykitchenapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wang on 2017/4/16.
 */

public class HomeFragment extends BaseFragment {
    private final String TAG = "HomeFragment";

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private LayoutInflater mLayoutInflater;
    private List<View> mViewList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }

    /**
     * 初始化组件/界面
     */
    @Override
    protected void initView(View view){
        //初始化ViewPager
        mViewPager = (ViewPager) view.findViewById(R.id.vp_home);
        //初始化inflater
        mLayoutInflater = LayoutInflater.from(getContext());
        //初始化Tablayout
        mTabLayout = (TabLayout) view.findViewById(R.id.tbl_home);
        //初始化Fragment
        mViewList.add(LayoutInflater.from(view.getContext()).inflate(R.layout.header_home, null));
        mViewList.add(LayoutInflater.from(view.getContext()).inflate(R.layout.header_home, null));
        mViewList.add(LayoutInflater.from(view.getContext()).inflate(R.layout.header_home, null));

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mViewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mViewList.get(position));
                return mViewList.get(position);
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
