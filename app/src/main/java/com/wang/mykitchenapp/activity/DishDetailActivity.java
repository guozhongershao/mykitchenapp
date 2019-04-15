package com.wang.mykitchenapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.adpter.DishDetailAdapter;
import com.wang.mykitchenapp.bean.Dish;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.fragment.DishDetailCommentFragment;
import com.wang.mykitchenapp.fragment.DishDetailDescriptionFragment;
import com.wang.mykitchenapp.fragment.DishDetailFragment;
import com.wang.mykitchenapp.manager.DataManager;
import com.wang.mykitchenapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DishDetailActivity extends AppCompatActivity {
    private final String TAG = "DishDetailActivity";

    //接收数据Intent
    private Intent mIntent;
    private int mDishId;
    //控件
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ImageView mIvHeader;
    private ImageView mIvHeaderBack;
    //
    private boolean mIsCollected = false;

    //viewPager
    private ViewPager mViewPager;
    //adapter
    private DishDetailAdapter mDishDetailAdapter;
    //Fragments
    private Fragment mDishDetailFragment,mDishDetailCommentFragment,mDishDetailDescriptionFragment;
    //FragmentList
    private List<Fragment> mFragmentList;
    //Fragment Title List
    private  List<String> mTitleList;
    //FragmentManager
    private FragmentManager mFragmentManager;

    //网络访问
    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(this);
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    //returnInfo
    private JsonObject mReturnedJsonObject;
    private Dish mDishDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        initView();
    }


    /**
     * 初始化组件/界面
     */
    private void initView(){

        mIntent = getIntent();
        //dishId 为1 设置为默认菜谱
        mDishId = mIntent.getIntExtra("dishId",1);
        //初始化fab
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectAction(view);
            }
        });

        //初始化Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //初始化头部图片
        mIvHeader = (ImageView) findViewById(R.id.iv_content);
        mIvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mIvHeader, Constant.DISH_COLLET, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //初始化ViewPager
        mViewPager = (ViewPager) findViewById(R.id.vp_dish_detail);
        //初始化TabLayout
        mTabLayout = (TabLayout) findViewById(R.id.tl_dish_detail);
        //初始化Fragment
        mDishDetailCommentFragment = ViewUtils.createFragment(DishDetailCommentFragment.class,false);
        mDishDetailFragment = ViewUtils.createFragment(DishDetailFragment.class,false);
        mDishDetailDescriptionFragment = ViewUtils.createFragment(DishDetailDescriptionFragment.class,false);
        //构建FragmentList
        mFragmentList = new ArrayList<>();
        mFragmentList.add(mDishDetailDescriptionFragment);
        mFragmentList.add(mDishDetailFragment);
        mFragmentList.add(mDishDetailCommentFragment);
        //构建TitleList
        mTitleList = new ArrayList<>();
        mTitleList.add("简介");
        mTitleList.add("详情");
        mTitleList.add("评论");
        //初始化FragmentManager
        mFragmentManager = getSupportFragmentManager();
        //初始化Adapter;
        mDishDetailAdapter = new DishDetailAdapter(mFragmentManager,mFragmentList,mTitleList);
        //向ViewPager绑定Adapter;
        mViewPager.setAdapter(mDishDetailAdapter);
        //向TabLayout绑定ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
        getDishDetail();
    }

    /**
     * 获取菜谱详情
     */
    private void getDishDetail(){
        Log.d(TAG, "getDishDetail: ");
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("dishId",String.valueOf(mDishId));
        if(null != Constant.currentUserName){
            map.put("userName",Constant.currentUserName);
        }
        String url = Constant.BASE_URL + "dish/getDishDetail.do";
        //网络请求
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {
                                   Gson gson = new Gson();
                                   JsonElement jsonElementDishDetail = mReturnedJsonObject.get("dishDetail");
                                   mDishDetail = gson.fromJson(jsonElementDishDetail,Dish.class);
                                   showDish();
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
     * 数据加载完成，展示菜谱
     */
    private void showDish(){
        //展示Fragment
        ((DishDetailDescriptionFragment)mDishDetailDescriptionFragment).showDescription(mDishDetail);
        ((DishDetailDescriptionFragment)mDishDetailDescriptionFragment).setmDish(mDishDetail);
        ((DishDetailFragment)mDishDetailFragment).showDetails(mDishDetail);
        ((DishDetailFragment)mDishDetailFragment).setDish(mDishDetail);
        ((DishDetailFragment)mDishDetailFragment).getmDishStepsAdapter().refreshList(mDishDetail.getSteps());
        //展示header
        Glide.with(DishDetailActivity.this).load(Constant.BASE_URL_IMAGES + mDishDetail.getDishAlbum()).into(mIvHeader);
        mIsCollected = mDishDetail.isCollected();
        if(mIsCollected){
            mFab.setImageResource(R.drawable.ic_favorite_white_24dp);
        }
    }

    /**
     * 收藏/取消收藏
     * @param view
     */
    private void collectAction(final View view){
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("dishId",String.valueOf(mDishId));
        if(null != Constant.currentUserName){
            map.put("userName",Constant.currentUserName);
        }
        map.put("isToCollect",String.valueOf(!mIsCollected));
        String url = Constant.BASE_URL + "user/collectAction.do";
        //网络请求
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {
                                   JsonElement jsonElementDishDetail = mReturnedJsonObject.get("actionCollectResult");
                                   switch (jsonElementDishDetail.getAsInt()) {
                                       case Constant.DO_COLLECT_OK:
                                           mIsCollected = true;
                                           mFab.setImageResource(R.drawable.ic_favorite_white_24dp);
                                           Snackbar.make(view, Constant.DISH_COLLET, Snackbar.LENGTH_LONG)
                                                   .setAction("Action", null).show();
                                           break;
                                       case Constant.UNDO_COLLECT_OK:
                                           mIsCollected = false;
                                           mFab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                                           Snackbar.make(view, Constant.DISH_UN_COLLET, Snackbar.LENGTH_LONG)
                                                   .setAction("Action", null).show();
                                           break;
                                       default:
                                           break;
                                   }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
