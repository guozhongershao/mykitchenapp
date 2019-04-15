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
import com.wang.mykitchenapp.bean.UserInfoBean;
import com.wang.mykitchenapp.inter.OnRecyclerViewItemClickListener;

import java.util.List;

import static com.wang.mykitchenapp.utils.ImageLoadUtils.loadCircleImage;

/**
 * Created by Wang on 2017/4/14.
 */

public class FocusAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener{
    private final String TAG = "DishListAdapter";

    private List<UserInfoBean> mUserOverviewList;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    public FocusAdapter(Context context,List<UserInfoBean> mUserOverviewList) {

        this.context = context;
        this.mUserOverviewList = mUserOverviewList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //查找布局
        View view = layoutInflater.inflate(R.layout.item_user_overview, parent, false);
        //设置点击事件
        view.setOnClickListener(this);
        ViewHolder holder = new UserOverviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(mUserOverviewList.get(position));
        UserInfoBean userInfoBean = mUserOverviewList.get(position);
        String userName = null;
        String avatarUrl = null;

        //强制转换
        UserOverviewHolder userOverviewHolder = (UserOverviewHolder) holder;
        try {
            userName = userInfoBean.getUserNickName();
            Log.v(TAG,userName);
        }catch (Exception e){
            e.printStackTrace();
        }
        avatarUrl = userInfoBean.getUserAvatar();
        //填充数据
        userOverviewHolder.mTvUserNickName.setText(userName);
        loadCircleImage(context,avatarUrl,userOverviewHolder.mIvAvatar);
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
        return mUserOverviewList.size();
    }

    /**
     * 更新数据
     * @param userInfoBeanList
     */
    public void refreshList(List<UserInfoBean> userInfoBeanList){
        this.mUserOverviewList = userInfoBeanList;
        notifyDataSetChanged();
    }

    public void addToList(List<UserInfoBean> userInfoBeanList){
        mUserOverviewList.addAll(userInfoBeanList);
        notifyDataSetChanged();
    }

    public static class UserOverviewHolder extends ViewHolder{

        private ImageView mIvAvatar;
        private TextView mTvUserNickName;

        public UserOverviewHolder(View itemView) {
            super(itemView);
            mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_user_avatar);
            mTvUserNickName = (TextView) itemView.findViewById(R.id.tv_user_nickname);
        }
    }
}
