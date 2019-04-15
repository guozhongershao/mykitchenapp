package com.wang.mykitchenapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.adpter.DishCommentAdapter;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.entity.DishComment;
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
 * Created by Wang on 2017/4/18.
 */

public class DishDetailCommentFragment extends BaseFragment {
    private final String TAG = "DDCommentFragment";

    //控件
    private TextView mTv;
    private EditText mEtComment;
    private TextInputLayout mTextInputLayoutl;
    private RecyclerView recyclerView;
    private TwinklingRefreshLayout twinkLayout;
    private Button mBtnDoComment;
    //adapter
    private DishCommentAdapter mDishCommentAdapter;
    //数据
    private List<DishComment> mDishCommentList = new ArrayList<>();
    //dishId
    private int mDishId;
    //网络访问
    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(getActivity());
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    //returnInfo
    private JsonObject mReturnedJsonObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_dish_detail_comment,container,false);
        initView(view);
        return view;
    }

    @Override
    protected void initView(final View view) {
/*        mEtComment = (EditText) view.findViewById(R.id.et_dish_comment);
        mTextInputLayoutl = (TextInputLayout) view.findViewById(R.id.til_comment);
        mTv.setFocusable(true);
        mTv.setFocusableInTouchMode(true);
        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextInputLayoutl.setVisibility(View.VISIBLE);
                mEtComment.requestFocus();
                InputMethodManager imm = (InputMethodManager) mEtComment.getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });*/
        //getData();
        mDishId =  this.getActivity().getIntent().getIntExtra("dishId",1);
        mDishCommentAdapter = new DishCommentAdapter(getActivity(),mDishCommentList);
        getComment();
        mEtComment = (EditText) view.findViewById(R.id.et_comment);
        mBtnDoComment = (Button) view.findViewById(R.id.btn_dish_comment);
        mBtnDoComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCmment(view);
            }
        });
        mTextInputLayoutl = (TextInputLayout) view.findViewById(R.id.til_comment);
        recyclerView = (RecyclerView)view.findViewById(R.id.recView);
        twinkLayout = (TwinklingRefreshLayout) view.findViewById(R.id.swipe_container);
        initRecyclerView();
        //initTwinklingRefreshLayout();
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
        recyclerView.setAdapter(mDishCommentAdapter);

        mDishCommentAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object object) {
                Snackbar.make(recyclerView,"Click", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                System.out.println(dy + "*****************");

                System.out.println(mTextInputLayoutl.getHeight()+ "*************");
                if (!recyclerView.canScrollVertically(-1)) {
                    mTextInputLayoutl.setVisibility(View.VISIBLE);
                } else if (!recyclerView.canScrollVertically(1)) {
                    mTextInputLayoutl.setVisibility(View.GONE);
                } else if (dy < -20) {
                    System.out.println(dy + "上滑");
                    mTextInputLayoutl.setVisibility(View.VISIBLE);
                } else if (dy > 20) {
                    System.out.println(dy + "下滑");
                    mTextInputLayoutl.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 获取评论
     */
    private void getComment(){
        //构造参数
        Log.d(TAG, "getComment: " + mDishId);
        Map<String,String> map = new HashMap<>();
        map.put("dishId",String.valueOf(mDishId));
        String url = Constant.BASE_URL + "dish/getDishComments.do";
        mCompositeSubscription.add(mDataManager.getData(url,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        mDishCommentList = getDishComments(jsonObject.get("dishComments"));
                        Log.d(TAG, "onNext: " + mDishCommentList.size());
                        mDishCommentAdapter.refreshList(mDishCommentList);
                    }
                })
        );
    }
    /**
     * 对菜谱评论
     * @param view
     */
    private void doCmment(final View view){
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("dishId",String.valueOf(mDishId));
        if(null != Constant.currentUserName){
            map.put("author",Constant.currentUserName);
        }
        String comment = mEtComment.getText().toString();
        map.put("content",comment);
        String url = Constant.BASE_URL + "dish/doComment.do";
        //网络请求
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {
                                   Log.d(TAG, "onCompleted: ");
                               }
                               @Override
                               public void onError(Throwable e) {
                                   Log.d(TAG, "onError: ");
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(JsonObject jsonObject) {
                                   mReturnedJsonObject = jsonObject;
                                   int doCommentResult = mReturnedJsonObject.get("doCommentResult").getAsInt();
                                   switch (doCommentResult) {
                                       case Constant.DO_DISH_COMMENT_OK:
                                           onCommentSucceed(view);
                                           break;
                                       case Constant.DO_DISH_COMMENT_ERROR:
                                           onCommentFailed(view);
                                           break;
                                       default:
                                           break;
                                   }
                               }
                           }
                )
        );
    }

    private List<DishComment> getDishComments(JsonElement jsonElement){
        Gson gson = new Gson();
        List<DishComment> dishCommentList = new ArrayList<>();
        dishCommentList = gson.fromJson(jsonElement,new TypeToken<List<DishComment>>(){}.getType());
        return dishCommentList;
    }

    /**
     * 评论成功
     * @param view
     */
    private void onCommentSucceed(View view){
        Snackbar.make(view, "评论成功", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        DishComment dishComment = null;
        Gson gson = new Gson();
        dishComment = gson.fromJson(mReturnedJsonObject.get("newestComment"),new TypeToken<DishComment>(){}.getType());
        mDishCommentAdapter.addToList(dishComment);
        recyclerView.scrollToPosition(0);
        mEtComment.setText("");
    }

    /**
     * 评论失败
     * @param view
     */
    private void onCommentFailed(View view){
        Snackbar.make(view, "评论失败", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    /**
     * 重写Destroy
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }
}
