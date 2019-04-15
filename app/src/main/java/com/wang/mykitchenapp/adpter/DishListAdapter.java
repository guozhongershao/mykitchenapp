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

import com.bumptech.glide.Glide;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.entity.DishOverview;
import com.wang.mykitchenapp.inter.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by Wang on 2017/4/14.
 */

public class DishListAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener{
    private final String TAG = "DishListAdapter";

    private List<DishOverview> mDishList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    public DishListAdapter(Context context,List<DishOverview> mDishList) {

        this.context = context;
        this.mDishList = mDishList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //查找布局
        View view = layoutInflater.inflate(R.layout.item_dish_list, parent, false);
        //设置点击事件
       view.setOnClickListener(this);
        ViewHolder holder = new DishHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(mDishList.get(position));
        DishOverview dishOverview = mDishList.get(position);
        String albumURL = null;
        String title = null;
        String author = null;
        String overview = null;

        //强制转换
        DishHolder dishHolder = (DishHolder) holder;
        try {
            albumURL = Constant.BASE_URL_IMAGES + dishOverview.getDishAlbum();
            Log.v(TAG,albumURL);
        }catch (Exception e){
            e.printStackTrace();
        }
        title = dishOverview.getDishName();
        author = Constant.BASE_AUTHOR + dishOverview.getDishAuthor();
        overview = dishOverview.getDishDescription();
        //填充数据
        dishHolder.textViewDishAuthor.setText(author);
        dishHolder.textViewDishTitle.setText(title);
        dishHolder.textViewDishDate.setText(overview);
        Glide.with(context).load(albumURL).into(dishHolder.imageViewDishAlbum);
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
        return mDishList.size();
    }

    /**
     * 更新数据
     * @param dishOverviewList
     */
    public void refreshList(List<DishOverview> dishOverviewList){
        this.mDishList = dishOverviewList;
        notifyDataSetChanged();
    }

    public void addToList(List<DishOverview> dishOverviewList){
        mDishList.addAll(dishOverviewList);
        notifyDataSetChanged();
    }

   public static class DishHolder extends ViewHolder{

       private ImageView imageViewDishAlbum;
       private TextView textViewDishTitle;
       private TextView textViewDishDate;
       private TextView textViewDishAuthor;

       public DishHolder(View itemView) {
           super(itemView);
           imageViewDishAlbum = (ImageView) itemView.findViewById(R.id.iv_albums);
           textViewDishDate = (TextView) itemView.findViewById(R.id.tv_date);
           textViewDishTitle = (TextView) itemView.findViewById(R.id.tv_title);
           textViewDishAuthor = (TextView) itemView.findViewById(R.id.tv_author);
       }
   }
}
