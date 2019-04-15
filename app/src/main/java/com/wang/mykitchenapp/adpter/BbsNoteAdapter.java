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
import com.wang.mykitchenapp.entity.BbsNote;
import com.wang.mykitchenapp.inter.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by Wang on 2017/4/14.
 */

public class BbsNoteAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener{
    private final String TAG = "BbsNoteAdapter";

    private List<BbsNote> mBbsNotes;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    public BbsNoteAdapter(Context context,List<BbsNote> mBbsNotes) {
        this.context = context;
        this.mBbsNotes = mBbsNotes;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //查找布局
        View view = layoutInflater.inflate(R.layout.item_bbs_note, parent, false);
        //设置点击事件
        view.setOnClickListener(this);
        ViewHolder holder = new BbsNoteHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(mBbsNotes.get(position));
        BbsNote bbsNote = mBbsNotes.get(position);

        String author = null;
        String date = null;
        String avatar = null;
        String title = null;
        String replyCount = null;

        //强制转换
        BbsNoteHolder bbsNoteHolder = (BbsNoteHolder) holder;
        try {
            avatar = Constant.BASE_URL_IMAGES + bbsNote.getAuthorAvatar();
            Log.v(TAG,avatar);
        }catch (Exception e){
            e.printStackTrace();
        }
        date = bbsNote.getBbnoPostdate();
        author = Constant.BASE_AUTHOR + bbsNote.getBbnoAuthor();
        title = bbsNote.getBbnoTitle();
        replyCount = String.valueOf(bbsNote.getReplayCount());
        //填充数据
        bbsNoteHolder.mTvAuthor.setText(author);
        bbsNoteHolder.mTvDate.setText(date);
        bbsNoteHolder.mTvTitle.setText(title);
        bbsNoteHolder.mTvReplyCount.setText(replyCount);
        Glide.with(context).load(avatar).into(bbsNoteHolder.mIvAvatar);
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
        return mBbsNotes.size();
    }

    /**
     * 更新数据
     * @param bbsNotes
     */
    public void refreshList(List<BbsNote> bbsNotes){
        this.mBbsNotes = bbsNotes;
        notifyDataSetChanged();
    }

    public void addToList(List<BbsNote> bbsNotes){
        this.mBbsNotes.addAll(bbsNotes);
        notifyDataSetChanged();
    }

    public static class BbsNoteHolder extends ViewHolder{
        private ImageView mIvAvatar;
        private TextView mTvAuthor;
        private TextView mTvDate;
        private TextView mTvTitle;
        private TextView mTvReplyCount;


        public BbsNoteHolder(View view) {
            super(view);
            mIvAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
            mTvAuthor = (TextView) view.findViewById(R.id.tv_author);
            mTvDate = (TextView) view.findViewById(R.id.tv_date);
            mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            mTvReplyCount = (TextView) view.findViewById(R.id.tv_reply_count);
        }
    }
}
