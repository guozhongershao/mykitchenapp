package com.wang.mykitchenapp.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.bean.BbsReply;
import com.wang.mykitchenapp.inter.OnRecyclerViewItemClickListener;

import java.util.List;

import static com.wang.mykitchenapp.utils.ImageLoadUtils.loadCircleImage;

/**
 * Created by Wang on 2017/4/14.
 */

public class BbsReplyAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener{
    private final String TAG = "BbsReplyAdapter";

    private List<BbsReply> mBbsReplies;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    public BbsReplyAdapter(Context context,List<BbsReply> mBbsReplys) {
        this.context = context;
        this.mBbsReplies = mBbsReplys;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //查找布局
        View view = layoutInflater.inflate(R.layout.item_bbs_reply, parent, false);
        //设置点击事件
        view.setOnClickListener(this);
        ViewHolder holder = new BbsReplyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(mBbsReplies.get(position));
        BbsReply bbsReply = mBbsReplies.get(position);

        String author = null;
        String date = null;
        String avatar = null;
        String content = null;

        //强制转换
        BbsReplyHolder bbsReplyHolder = (BbsReplyHolder) holder;
        try {
            avatar = bbsReply.getAuthorAvatar();
        }catch (Exception e){
            e.printStackTrace();
        }
        date = bbsReply.getBbrePostdate();
        author = bbsReply.getBbreAuthor();
        content = bbsReply.getBbreContent();
        //填充数据
        bbsReplyHolder.mTvAuthor.setText(author);
        bbsReplyHolder.mTvContent.setText(content);
        bbsReplyHolder.mTvDate.setText(date);
        loadCircleImage(context,avatar,bbsReplyHolder.mIvAvatar);
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
        return mBbsReplies.size();
    }

    /**
     * 更新数据
     * @param bbsReplies
     */
    public void refreshList(List<BbsReply> bbsReplies){
        this.mBbsReplies = bbsReplies;
        notifyDataSetChanged();
    }

    public void addToList(BbsReply bbsReply){
        this.mBbsReplies.add(0,bbsReply);
        notifyDataSetChanged();
    }

    public static class BbsReplyHolder extends ViewHolder{
        private ImageView mIvAvatar;
        private TextView mTvAuthor;
        private TextView mTvDate;
        private TextView mTvContent;


        public BbsReplyHolder(View view) {
            super(view);
            mIvAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            mTvAuthor = (TextView) view.findViewById(R.id.tv_author);
            mTvDate = (TextView) view.findViewById(R.id.tv_date);
            mTvContent = (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
