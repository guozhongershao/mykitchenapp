package com.wang.mykitchenapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.adpter.BbsReplyAdapter;
import com.wang.mykitchenapp.bean.BbsReply;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.entity.BbsNote;
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

import static com.wang.mykitchenapp.utils.ImageLoadUtils.loadCircleImage;

public class BbsNoteDetailActivity extends AppCompatActivity {
    private final String TAG = "BbsNoteDetailActivity";

    //context
    private Context context = this;
    //控件
    private ImageView mIvAvatar = null;
    private TextView mTvAuthor = null;
    private TextView mTvTitle = null;
    private TextView mTvContent = null;
    private TextView mTvDate = null;
    private RecyclerView mRvReplys = null;
    private LinearLayout mLlComment = null;
    private FloatingActionButton fab = null;
    private EditText mEtComment = null;
    private Button mBtnComment = null;
    //adapter
    private BbsReplyAdapter mBbsReplyAdapter = null;
    //intent
    private Intent intent = null;
    //mNoteId
    private int mNoteId = 0;

    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(context);
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    //returnInfo
    private JsonObject mReturnedJsonObject;
    //主贴信息
    private BbsNote mBbsNote = null;
    //回复
    private List<BbsReply> mBbsReplies = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_note_detail);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLlComment.requestFocus();
                mLlComment.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) mLlComment.getContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                fab.setVisibility(View.GONE);
            }
        });

        initView();
    }

    /**
     * 初始化activity
     */
    private void initView(){

        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //初始化控件
        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
        mTvAuthor = (TextView) findViewById(R.id.tv_author);
        mBtnComment = (Button) findViewById(R.id.btn_comment);
        mBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCmment();
            }
        });
        mEtComment = (EditText) findViewById(R.id.et_comment);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mRvReplys = (RecyclerView) findViewById(R.id.recView);
        mLlComment = (LinearLayout) findViewById(R.id.ll_comment);
        mBbsReplyAdapter = new BbsReplyAdapter(context, mBbsReplies);
        initRecyclerView();

        intent = getIntent();
        mNoteId =  intent.getIntExtra("noteId",0);
        Log.d(TAG, "initView: " + mNoteId);
        //获取帖子详情
        getNoteDetail();

    }

    /**
     * 初始化recycleView
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        //设置布局管理器
        mRvReplys.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        mRvReplys.setItemAnimator(new DefaultItemAnimator());
        //设置Adapter
        mRvReplys.setAdapter(mBbsReplyAdapter);

        mBbsReplyAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                Log.d(TAG, "onItemClick: " );
            }
        });
    }

    /**
     * 获取帖子详情，包括评论列表
     */
    public void getNoteDetail(){
        Log.d(TAG, "getNoteDetail: ");
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("noteId",String.valueOf(mNoteId));
        String url = Constant.BASE_URL + "bbs/getNoteDetail.do";
        //网络请求
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                               @Override
                               public void onCompleted() {

                               }
                               @Override
                               public void onError(Throwable e) {
                                   e.printStackTrace();
                               }

                               @Override
                               public void onNext(JsonObject jsonObject) {
                                   mReturnedJsonObject = jsonObject;
                                   Gson gson = new Gson();
                                   mBbsNote = gson.fromJson(mReturnedJsonObject.get("bbsNote"),new TypeToken<BbsNote>(){}.getType());
                                   mBbsReplies = gson.fromJson(mReturnedJsonObject.get("bbsReplys"),new TypeToken<List<BbsReply>>(){}.getType());
                                   showNote();
                               }
                           }
                )
        );
    }

    /**
     * 展示帖子详情
     */
    private void showNote(){
        mTvTitle.setText(mBbsNote.getBbnoTitle());
        mTvContent.setText(mBbsNote.getBbnoContent());
        mTvDate.setText(mBbsNote.getBbnoPostdate());
        mTvAuthor.setText(mBbsNote.getBbnoAuthor());
        mTvAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BbsNoteDetailActivity.this,UserInfoActivity.class);
                intent.putExtra("userName",mTvAuthor.getText().toString());
                startActivity(intent);
            }
        });
        loadCircleImage(this,mBbsNote.getAuthorAvatar(),mIvAvatar);
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BbsNoteDetailActivity.this,UserInfoActivity.class);
                intent.putExtra("userName",mTvAuthor.getText().toString());
                startActivity(intent);
            }
        });
        mBbsReplyAdapter.refreshList(mBbsReplies);
    }

    /**
     * 评论
     */
    private void doCmment(){
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("noteId",String.valueOf(mNoteId));
        if(null != Constant.currentUserName){
            map.put("author",Constant.currentUserName);
        }
        String comment = mEtComment.getText().toString();
        map.put("content",comment);
        String url = Constant.BASE_URL + "bbs/doReply.do";
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
                                       case Constant.DO_NOTE_COMMENT_OK:
                                           onCommentSucceed();
                                           break;
                                       case Constant.DO_NOTE_COMMENT_ERROR:
                                           onCommentFailed();
                                           break;
                                       default:
                                           break;
                                   }
                               }
                           }
                )
        );
    }

    /**
     * 评论成功
     */
    private void onCommentSucceed(){
        BbsReply bbsReply = null;
        Gson gson = new Gson();
        bbsReply = gson.fromJson(mReturnedJsonObject.get("newestComment"),new TypeToken<BbsReply>(){}.getType());
        mBbsReplyAdapter.addToList(bbsReply);
        mRvReplys.scrollToPosition(0);
        mEtComment.setText("");
        InputMethodManager imm = (InputMethodManager) mLlComment.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLlComment.getWindowToken(), 0);
        mLlComment.clearFocus();
        mLlComment.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
        Snackbar.make(mEtComment, "评论成功", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    /**
     * 评论失败
     */
    private void onCommentFailed(){
        Snackbar.make(mEtComment, "评论失败", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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


    /**
     * 重写返回按钮功能
     */
    @Override
    public void onBackPressed() {
        InputMethodManager imm = (InputMethodManager) mLlComment.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()){
            imm.hideSoftInputFromWindow(mLlComment.getWindowToken(), 0);
            mLlComment.clearFocus();
            mLlComment.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }else{
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
