package com.wang.mykitchenapp.fragment;

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
import android.widget.TextView;

import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.adpter.DishStepsAdapter;
import com.wang.mykitchenapp.bean.Dish;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wang on 2017/4/18.
 */

public class DishDetailFragment extends BaseFragment {
    private final String TAG = "DishDetailFragment";

    private TextView mTvIngredient;
    private RecyclerView mRecycleView;

    private DishStepsAdapter mDishStepsAdapter;
    //数据
    private Dish mDish;

    private List<Dish.StepsBean> mDishSteps = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_dish_detail,container,false);
        initView(view);
        return view;

    }

    @Override
    protected void initView(View view) {
        mTvIngredient = (TextView) view.findViewById(R.id.tv_dish_detail_ingredient);
        mRecycleView = (RecyclerView)view.findViewById(R.id.rv_dish_detail);
        mDishStepsAdapter =  new DishStepsAdapter(getActivity(),mDishSteps);
        initRecyclerView();
        showDetails(mDish);
    }

    /**
     * 将数据绑定到控件
     * @param dish
     */
    public void showDetails(Dish dish){
        if(null != dish){
            String strIngredient = "";
            Log.d(TAG, "showDetails: "+ dish.getIngredients().size());
            for(int i = 0; i < dish.getIngredients().size();i++){
                strIngredient += dish.getIngredients().get(i).getDiinIngredient() ;
                strIngredient += " : ";
                strIngredient += dish.getIngredients().get(i).getDiinMeasure();
                strIngredient += "\n";
            }
            mTvIngredient.setText(strIngredient);
        }
    }


    /**
     * 初始化recycleView
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        mRecycleView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        //设置Adapter
        mRecycleView.setAdapter(mDishStepsAdapter);
    }

    public Dish getDish() {
        return mDish;
    }

    public void setDish(Dish dish) {
        this.mDish = dish;
    }

    public List<Dish.StepsBean> getmDishSteps() {
        return mDishSteps;
    }

    public void setmDishSteps(List<Dish.StepsBean> mDishSteps) {
        this.mDishSteps = mDishSteps;
    }

    public DishStepsAdapter getmDishStepsAdapter() {
        return mDishStepsAdapter;
    }

    public void setmDishStepsAdapter(DishStepsAdapter mDishStepsAdapter) {
        this.mDishStepsAdapter = mDishStepsAdapter;
    }
}
