package com.wang.mykitchenapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.activity.BbsDoNoteActivity;
import com.wang.mykitchenapp.adpter.DishDetailAdapter;
import com.wang.mykitchenapp.manager.DataManager;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wang on 2017/4/5.
 */

public class BBSFragment extends BaseFragment {
    private final String TAG ="BBSFragment";

    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(this.getActivity());
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    //returnInfo
    private JsonObject mReturnedJsonObject;

    //fragment
    private Fragment mBBSExperienceFragment,mBBSAllFragment,mBBSQuestionFragment;
    //FragmentList
    private List<Fragment> mFragmentList = new ArrayList<>();
    //nameList
    private List<String> mTitleList = new ArrayList<>();
    //FragmentManager
    private FragmentManager mFragmentManager;
    //控件
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mFab;
    //adapter
    private DishDetailAdapter mBBSFragmentAdapter;

    //分页
    private int mPageNum = 1;
    private final int mPageSize = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbs,container,false);
        initView(view);
        return view;
    }

    /**
     * 初始化fragment
     * @param view
     */
    protected void initView(View view){
        initViewPager(view);
        mTabLayout = (TabLayout) view.findViewById(R.id.tl_bbs);
        mTabLayout.setupWithViewPager(mViewPager);
        initFab(view);
    }

    /**
     * 初始化viewPager
     * @param view
     */
    private void initViewPager(View view){
        mViewPager = (ViewPager) view.findViewById(R.id.vp_bbs);
        mBBSExperienceFragment = new BBSExperienceFragment();
        mBBSAllFragment = new BBSAllFragment();
        mBBSQuestionFragment = new BBSQuestionFragment();
        mFragmentList.add(mBBSAllFragment);
        mFragmentList.add(mBBSQuestionFragment);
        mFragmentList.add(mBBSExperienceFragment);
        mTitleList.add("全部帖子");
        mTitleList.add("求指教");
        mTitleList.add("我来教");
        mFragmentManager = getActivity().getSupportFragmentManager();
        mBBSFragmentAdapter = new DishDetailAdapter(mFragmentManager,mFragmentList,mTitleList);
        mViewPager.setAdapter(mBBSFragmentAdapter);
    }

    private void initFab(View view){
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewPager.getCurrentItem();
                Intent intent = new Intent(getActivity(), BbsDoNoteActivity.class);
                switch (position){
                    case 0:
                        intent.putExtra("topicName","comprehensive");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("topicName","question");
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("topicName","experience");
                        startActivity(intent);
                        break;
                }
                Log.d(TAG, "onClick: " + position);
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
