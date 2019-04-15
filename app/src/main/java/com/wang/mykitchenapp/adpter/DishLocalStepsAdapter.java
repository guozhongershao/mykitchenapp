package com.wang.mykitchenapp.adpter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.bean.Dish;
import com.wang.mykitchenapp.inter.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by Wang on 2017/4/18.
 */

public class DishLocalStepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private List<Dish.StepsBean> mSteps;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    public DishLocalStepsAdapter(Context context,List<Dish.StepsBean> steps) {
        this.context = context;
        this.mSteps = steps;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //查找布局
        View view = layoutInflater.inflate(R.layout.item_dish_step, parent, false);
        //设置点击事件
        view.setOnClickListener(this);
        RecyclerView.ViewHolder holder = new DishStepHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(mSteps.get(position));
        Dish.StepsBean dishStep = mSteps.get(position);

        String attachmentUrl = null;
        String description = null;

        //强制转换
        DishLocalStepsAdapter.DishStepHolder stepHolder = (DishLocalStepsAdapter.DishStepHolder) holder;
        try {
            attachmentUrl = dishStep.getDistAttachmenturl();
            System.out.println("*************" + attachmentUrl);
        }catch (Exception e){
            e.printStackTrace();
        }
        description = dishStep.getDistDescription();
        //填充数据
        stepHolder.mTvDescription.setText(description);
        Glide.with(context).load(Uri.parse(attachmentUrl)).into(stepHolder.mIvAttachment);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    @Override
    public void onClick(View v) {
        mOnRecyclerViewItemClickListener.onItemClick(v,v.getTag());
    }

    public void refreshList(List<Dish.StepsBean> stepsBeanList){
        this.mSteps = stepsBeanList;
        notifyDataSetChanged();
    }

    public void addToList(Dish.StepsBean stepsBean){
        this.mSteps.add(stepsBean);
        notifyDataSetChanged();
    }
    /**
     * 为列表项田间监听器
     * @param listener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnRecyclerViewItemClickListener = listener;
    }

    public static class DishStepHolder extends RecyclerView.ViewHolder {

        private ImageView mIvAttachment;
        private TextView mTvDescription;

        public DishStepHolder(View itemView) {
            super(itemView);
            mIvAttachment = (ImageView) itemView.findViewById(R.id.iv_attachment);
            mTvDescription = (TextView) itemView.findViewById(R.id.tv_dish_detail_description);
        }
    }
}
