package com.wang.mykitchenapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.activity.BbsNoteDetailActivity;
import com.wang.mykitchenapp.adpter.BbsNoteAdapter;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.entity.BbsNote;
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

public class BBSAllFragment extends BaseFragment {
    private final String TAG ="BBSAllFragment";

    //组件
    private TwinklingRefreshLayout twinkLayout;
    private RecyclerView recyclerView;
    private TextView mTvCamera;
    //adapter
    private BbsNoteAdapter mBbsNoteAdapter;

    //数据
    private List<BbsNote> mBbsNotes = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.fragment_bbs_note_all,container,false);
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
        recyclerView = (RecyclerView)view.findViewById(R.id.recView);
        twinkLayout = (TwinklingRefreshLayout)view.findViewById(R.id.swipe_container);
        mBbsNoteAdapter = new BbsNoteAdapter(getActivity(),mBbsNotes);
        refresh();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                Snackbar.make(mTvCamera, "相册", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
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
        recyclerView.setAdapter(mBbsNoteAdapter);

        mBbsNoteAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                int bbsNoteId = ((BbsNote)data).getBbnoId();
                Intent intent = new Intent(getActivity(), BbsNoteDetailActivity.class);
                intent.putExtra("noteId", bbsNoteId);
                startActivity(intent);
                Log.d(TAG, "onItemClick: " + bbsNoteId);
            }
        });
    }

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
        String url = Constant.BASE_URL + "bbs/getBbsNotes.do";
        //网络请求
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {

                               }
                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(JsonObject jsonObject) {
                                   mReturnedJsonObject = jsonObject;
                                   Gson gson = new Gson();
                                   mBbsNotes = gson.fromJson(mReturnedJsonObject.get("BbsNotes"),new TypeToken<List<BbsNote>>(){}.getType());
                                   Log.d(TAG, "onNext: " + mBbsNotes.size());
                                   mBbsNoteAdapter.refreshList(mBbsNotes);
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
        String url = Constant.BASE_URL + "bbs/getBbsNotes.do";
        //网络请求
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {

                               }
                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(JsonObject jsonObject) {
                                   mReturnedJsonObject = jsonObject;
                                   Gson gson = new Gson();
                                   mBbsNotes = gson.fromJson(mReturnedJsonObject.get("BbsNotes"),new TypeToken<List<BbsNote>>(){}.getType());
                                   mBbsNoteAdapter.addToList(mBbsNotes);
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
