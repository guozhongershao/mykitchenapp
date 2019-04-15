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
import com.wang.mykitchenapp.activity.DishDetailActivity;
import com.wang.mykitchenapp.adpter.DishListAdapter;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.entity.DishOverview;
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

public class CollectionFragment extends BaseFragment {
    private final String TAG ="DishFragment";

    //组件
    private TwinklingRefreshLayout twinkLayout;
    private RecyclerView recyclerView;
    //adapter
    private DishListAdapter dishListAdapter;

    //数据
    private List<DishOverview> dishOverviewList = new ArrayList<>();
    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(this.getActivity());
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    //returnInfo
    private JsonObject mReturnedJsonObject;

    //分页
    private int mPageNum = 1;
    private final int mPageSize = 10;

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
        dishListAdapter = new DishListAdapter(getActivity(),dishOverviewList);

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
                loadMore();
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
        recyclerView.setAdapter(dishListAdapter);

        dishListAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                int id = ((DishOverview) data).getDishId();
                Intent intent = new Intent(getActivity(), DishDetailActivity.class);
                intent.putExtra("dishId", id);
                startActivity(intent);
                // getActivity().overridePendingTransition(R.anim.left_enter, R.anim.right_exit);
            }
        });
    }

/*    public void getData(){
        for (int i = 0;i < 20; i++){
            DishOverview dishOverview = new DishOverview();
            dishOverview.setDishAlbum(Constant.BASE_URL_IMAGES + "defaultAlbum.jpg");
            dishOverview.setDishAuthor("最帅成员");
            dishOverview.setDishName("红烧牛肉" + i);
            dishOverviewList.add(dishOverview);
        }
    }*/

    /**
     * 刷新列表
     */
    public void refresh(){
        Log.d(TAG, "refresh: ");
        mPageNum = 1;
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("pageNum",String.valueOf(mPageNum));
        map.put("pageSize",String.valueOf(mPageSize));
        map.put("userName",Constant.currentUserName);
        String url = Constant.BASE_URL + "user/getCollections.do";
        //网络请求
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {
                                   Gson gson = new Gson();
                                   dishOverviewList = gson.fromJson(mReturnedJsonObject.get("dishs"),new TypeToken<List<DishOverview>>(){}.getType());
                                   dishListAdapter.addToList(dishOverviewList);
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

    /**
     * 加载更多列表
     */
    public void loadMore(){
        Log.d(TAG, "loadMore: ");
        mPageNum ++;
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("pageNum",String.valueOf(mPageNum));
        map.put("pageSize",String.valueOf(mPageSize));
        map.put("userName",Constant.currentUserName);
        String url = Constant.BASE_URL + "user/getCollections.do";
        //网络请求
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {
                                   Gson gson = new Gson();
                                   dishOverviewList = gson.fromJson(mReturnedJsonObject.get("dishs"),new TypeToken<List<DishOverview>>(){}.getType());
                                   dishListAdapter.addToList(dishOverviewList);
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
