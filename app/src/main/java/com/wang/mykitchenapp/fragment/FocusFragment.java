package com.wang.mykitchenapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.activity.UserInfoActivity;
import com.wang.mykitchenapp.adpter.FocusAdapter;
import com.wang.mykitchenapp.bean.UserInfoBean;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.inter.OnRecyclerViewItemClickListener;
import com.wang.mykitchenapp.manager.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wang on 2017/4/5.
 */

public class FocusFragment extends BaseFragment {
    private final String TAG ="FocusFragment";

    //组件
    private TwinklingRefreshLayout twinkLayout;
    private RecyclerView recyclerView;
    //adapter
    private FocusAdapter mFocusAdapter;

    //数据
    private List<UserInfoBean> mUserInfoBeanList = new ArrayList<>();
    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(this.getActivity());
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    //returnInfo
    private JsonObject mReturnedJsonObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,container,false);
        initView(view);
        initTwinklingRefreshLayout();
        initRecyclerView();
        return view;
    }

    /**
     * 初始化fragment
     * @param view
     */
    protected void initView(View view){
        refresh();
        recyclerView = (RecyclerView)view.findViewById(R.id.recView);
        twinkLayout = (TwinklingRefreshLayout)view.findViewById(R.id.swipe_container);
        twinkLayout.setEnableLoadmore(false);
        twinkLayout.setEnableRefresh(false);
        mFocusAdapter = new FocusAdapter(getActivity(),mUserInfoBeanList);

    }

    /**
     * 初始化上拉下拉控价
     */
    private void initTwinklingRefreshLayout() {
        //设置头部可拉伸的最大高度
        twinkLayout.setWaveHeight(100);
        //头部固定高度(在此高度上显示刷新状态)
        twinkLayout.setHeaderHeight(100);
        //底部高度
        twinkLayout.setBottomHeight(100);
        //设置刷新事件
        twinkLayout.setOnRefreshListener(new TwinklingRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refresh();
                refreshLayout.finishRefreshing();//结束刷新
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();//加载更多动画
            }
        });
    }

    /**
     * 初始化recycleView
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置Adapter
        recyclerView.setAdapter(mFocusAdapter);

        mFocusAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                String userName = ((UserInfoBean) data).getUserName();
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });
    }

    /**
     * 刷新列表
     */
    public void refresh(){
        Log.d(TAG, "refresh: ");
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("userName",Constant.currentUserName);
        String url = Constant.BASE_URL + "user/getFocusList.do";
        //网络请求
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {
                                   Gson gson = new Gson();
                                   mUserInfoBeanList = gson.fromJson(mReturnedJsonObject.get("focusList"),new TypeToken<List<UserInfoBean>>(){}.getType());
                                   mFocusAdapter.refreshList(mUserInfoBeanList);
                               }
                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(JsonObject jsonObject) {
                                   mReturnedJsonObject = jsonObject;
                               }
                           }
                )
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
