package com.wang.mykitchenapp.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.entity.DishComment;
import com.wang.mykitchenapp.inter.OnRecyclerViewItemClickListener;

import java.util.List;

import static com.wang.mykitchenapp.utils.ImageLoadUtils.loadCircleImage;

/**
 * Created by Wang on 2017/4/14.
 */

public class DishCommentAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener{
    private final String TAG = "DishCommentAdapter";

    private List<DishComment> mDishCommentList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    public DishCommentAdapter(Context context,List<DishComment> mDishCommentList) {
        this.context = context;
        this.mDishCommentList = mDishCommentList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //查找布局
        View view = layoutInflater.inflate(R.layout.item_dish_comment, parent, false);
        //设置点击事件
        view.setOnClickListener(this);
        ViewHolder holder = new DishCommentHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(mDishCommentList.get(position));
        DishComment dishComment = mDishCommentList.get(position);

        String author = null;
        String date = null;
        String avatar = null;
        String content = null;

        //强制转换
        DishCommentHolder dishCommentHolder = (DishCommentHolder) holder;
        try {
            avatar = dishComment.getAvatar();
            Log.v(TAG,avatar);
        }catch (Exception e){
            e.printStackTrace();
        }
        date = dishComment.getDicmPostdate();
        author = Constant.BASE_AUTHOR + dishComment.getDicmAuthor();
        content = dishComment.getDicmContent();
        //填充数据
        dishCommentHolder.mTvContent.setText(content);
        dishCommentHolder.mTvDate.setText(date);
        dishCommentHolder.mTvAuthor.setText(author);

        loadCircleImage(context,avatar,dishCommentHolder.mIvAvatar);
    }

    /**
     * 为列表项添加点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        mOnRecyclerViewItemClickListener.onItemClick(v,v.getTag());
    }

    /**
     * 为列表项田间监听器
     * @param listener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnRecyclerViewItemClickListener = listener;
    }

    /**
     * 获取列表总数
     * @return
     */
    @Override
    public int getItemCount() {
        return mDishCommentList.size();
    }

    /**
     * 更新数据
     * @param dishCommentList
     */
    public void refreshList(List<DishComment> dishCommentList){
        this.mDishCommentList = dishCommentList;
        notifyDataSetChanged();
    }

    public void addToList(DishComment dishComment){
        mDishCommentList.add(0,dishComment);
        notifyDataSetChanged();
    }

    public static class DishCommentHolder extends ViewHolder{
        private ImageView mIvAvatar;
        private TextView mTvAuthor;
        private TextView mTvDate;
        private TextView mTvContent;


        public DishCommentHolder(View view) {
            super(view);
            mIvAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            mTvAuthor = (TextView) view.findViewById(R.id.tv_author);
            mTvDate = (TextView) view.findViewById(R.id.tv_date);
            mTvContent = (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
