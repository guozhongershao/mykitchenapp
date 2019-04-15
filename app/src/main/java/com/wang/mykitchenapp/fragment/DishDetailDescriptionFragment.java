package com.wang.mykitchenapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.activity.UserInfoActivity;
import com.wang.mykitchenapp.bean.Dish;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.manager.DataManager;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wang on 2017/4/18.
 */

public class DishDetailDescriptionFragment extends BaseFragment {
    private final String TAG = "DDDescriptionFragment";

    //控件
    private ImageView mIvMark;
    private TextView mTvAuthor;
    private TextView mTvTitle;
    private TextView mTvDescription;

    //数据
    private Dish mDish;

    //网络访问
    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(this.getContext());
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    //returnInfo
    private JsonObject mReturnedJsonObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_dish_detail_description,container,false);
        initView(view);
        return view;
    }


    @Override
    protected void initView(final View view) {
        mIvMark = (ImageView) view.findViewById(R.id.btn_mark);
        mTvAuthor = (TextView) view.findViewById(R.id.tv_dish_detail_author);
        mTvAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),UserInfoActivity.class);
                intent.putExtra("userName",mTvAuthor.getText());
                startActivity(intent);
            }
        });
        mTvTitle = (TextView) view.findViewById(R.id.tv_dish_detail_title);
        mTvDescription = (TextView) view.findViewById(R.id.tv_dish_detail_description);

        mIvMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusAction(v);
            }
        });

        showDescription(mDish);
    }

    /**
     * 展示菜谱description
     */
    public void showDescription(Dish dish){
        if(null != dish) {
            mTvTitle.setText(dish.getDishName());
            mTvAuthor.setText(dish.getDishAuthor());
            mTvDescription.setText(dish.getDishDescription());
            if (dish.isFocused()) {
                mIvMark.setImageResource(R.drawable.ic_star_primarry_24dp);
            }
        }
    }

    /**
     * 关注用户操作
     */
    private void focusAction(final View view){
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("itsName",mDish.getDishAuthor());
        if(null != Constant.currentUserName){
            map.put("myName",Constant.currentUserName);
        }
        map.put("isToFocus",String.valueOf(!(mDish.isFocused())));
        String url = Constant.BASE_URL + "user/focusAction.do";
        //网络请求
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {
                                   JsonElement jsonElementDishDetail = mReturnedJsonObject.get("actionCollectResult");
                                   switch (jsonElementDishDetail.getAsInt()) {
                                       case Constant.DO_FOCUS_OK:
                                           mDish.setFocused(true);
                                           mIvMark.setImageResource(R.drawable.ic_star_primarry_24dp);
                                           Snackbar.make(view, Constant.DO_FOCUS, Snackbar.LENGTH_LONG)
                                                   .setAction("Action", null).show();
                                           break;
                                       case Constant.UNDO_FOCUS_OK:
                                           mDish.setFocused(false);
                                           mIvMark.setImageResource(R.drawable.ic_star_border_primarry_24dp);
                                           Snackbar.make(view, Constant.UNDO_FOCUS, Snackbar.LENGTH_LONG)
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

    /**
     * 取消关注
     */
    private void unMark(){

    }

    public Dish getmDish() {
        return mDish;
    }

    public void setmDish(Dish mDish) {
        this.mDish = mDish;
    }
}
