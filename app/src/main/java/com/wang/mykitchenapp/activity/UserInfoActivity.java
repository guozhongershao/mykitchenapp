package com.wang.mykitchenapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.bean.Dish;
import com.wang.mykitchenapp.bean.UserInfoBean;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.entity.BbsNote;
import com.wang.mykitchenapp.manager.DataManager;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.wang.mykitchenapp.utils.ImageLoadUtils.loadCircleImage;

public class UserInfoActivity extends AppCompatActivity {
    private final String TAG = "UserInfoActivity";

    private Context context = this;
    //控件
    //用户信息
    private ImageView mIvAvatar;
    private TextView mTvUserName;
    private TextView mTvNickname;
    private TextView mTvEmail;
    //菜谱信息
    private RelativeLayout mRlDish;
    private ImageView mIvAlbum;
    private TextView mTvDishTitle;
    private TextView mTvDescription;

    //帖子信息
    private LinearLayout mLlNote;
    private TextView mTvNoteTitle;
    private TextView mTvNoteContent;
    private TextView mTvDate;
    //关注按钮；
    private ImageView mIvFocus;
    private boolean isFocused;

    //网络访问
    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(this);
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    //returnInfo
    private JsonObject mReturnedJsonObject;

    //用户名
    private String mUserName = null;
    //用户信息
    private UserInfoBean mUser = null;
    //最新菜谱
    private Dish mDish = null;
    //最新帖子
    private BbsNote mNote = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();

        getUserInfo();
    }

    private void initView(){

        Intent intent = getIntent();
        mUserName = intent.getStringExtra("userName");

        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        initComponent();
    }

    private void initComponent(){
        mIvAvatar = (ImageView) findViewById(R.id.iv_avatar);
        mTvUserName = (TextView) findViewById(R.id.tv_user_name);
        mTvNickname = (TextView) findViewById(R.id.tv_nickname);
        mTvEmail = (TextView) findViewById(R.id.tv_email);
        //菜谱信息
        mRlDish = (RelativeLayout) findViewById(R.id.dish);
        mRlDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this,DishDetailActivity.class);
                intent.putExtra("dishId", mDish.getDishId());
                startActivity(intent);
            }
        });
        mIvAlbum = (ImageView) findViewById(R.id.iv_album);
        mTvDishTitle = (TextView) findViewById(R.id.tv_dish_title);
        mTvDescription = (TextView) findViewById(R.id.tv_dish_description);
        //帖子信息
        mLlNote = (LinearLayout) findViewById(R.id.ll_note);
        mLlNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, BbsNoteDetailActivity.class);
                intent.putExtra("noteId", mNote.getBbnoId());
                startActivity(intent);
            }
        });
        mTvNoteTitle = (TextView) findViewById(R.id.tv_note_title);
        mTvNoteContent = (TextView) findViewById(R.id.tv_note_content);
        mTvDate = (TextView) findViewById(R.id.tv_note_date);
        //关注按钮；
        mIvFocus = (ImageView) findViewById(R.id.iv_focus);
        mIvFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusAction(v);
            }
        });
        if(mUserName.equals(Constant.currentUserName)){
            mIvFocus.setVisibility(View.GONE);
        }
    }
    /**
     * 获取用户信息
     */
    private void getUserInfo(){
        Log.d(TAG, "getUserInfo: ");
        //构造参数
        Map<String,String> map = new HashMap<>();
        if(Constant.currentUserName != null && !Constant.currentUserName.equals(mUserName)){
            map.put("userName",Constant.currentUserName);
            map.put("aimUserName",mUserName);
        }else {
            map.put("aimUserName",mUserName);
        }

        if(Constant.currentUserName == null){

        }
        String url = Constant.BASE_URL + "user/getUserInfo.do";
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
                                   onSucceed();
                               }
                           }
                )
        );
    }

    private void onSucceed(){
        Gson gson = new Gson();
        mUser = gson.fromJson(mReturnedJsonObject.get("userInfo"),UserInfoBean.class);
        loadCircleImage(context,mUser.getUserAvatar(),mIvAvatar);
        mTvUserName.setText(mUser.getUserName());
        mTvNickname.setText(mUser.getUserNickName());
        if(mUser.getUserEmail() != null){
            mTvEmail.setText(mUser.getUserEmail());
        }
        //展示最新帖子
        if(null != mReturnedJsonObject.get("newestNote")){
            mNote = gson.fromJson(mReturnedJsonObject.get("newestNote"),BbsNote.class);
            mTvNoteTitle.setText(mNote.getBbnoTitle());
            mTvNoteContent.setText(mNote.getBbnoContent());
            mTvDate.setText(mNote.getBbnoPostdate());
        }
        //展示最新菜谱
        if(null != mReturnedJsonObject.get("newestDish")){
            mDish = gson.fromJson(mReturnedJsonObject.get("newestDish"),Dish.class);
            Glide.with(context).load(Constant.BASE_URL_IMAGES + mDish.getDishAlbum()).into(mIvAlbum);
            mTvDishTitle.setText(mDish.getDishName());
            mTvDescription.setText(mDish.getDishDescription());
        }
        //是否关注
        isFocused = mReturnedJsonObject.get("isFocused").getAsBoolean();
        if(isFocused){
            mIvFocus.setImageResource(R.drawable.ic_star_24dp);
        }
    }

    /**
     * 关注用户操作
     */
    private void focusAction(final View view){
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("itsName",mUserName);
        if(null != Constant.currentUserName){
            map.put("myName",Constant.currentUserName);
        }
        map.put("isToFocus",String.valueOf(!isFocused));
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
                                           mIvFocus.setImageResource(R.drawable.ic_star_primarry_24dp);
                                           Snackbar.make(view, Constant.DO_FOCUS, Snackbar.LENGTH_LONG)
                                                   .setAction("Action", null).show();
                                           break;
                                       case Constant.UNDO_FOCUS_OK:
                                           mDish.setFocused(false);
                                           mIvFocus.setImageResource(R.drawable.ic_star_border_primarry_24dp);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        if(Constant.currentUserName == null || !Constant.currentUserName.equals(mUserName)){
            menu.removeItem(R.id.action_modify);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_modify:
                Intent intent = new Intent(UserInfoActivity.this,ModifyUserInfoActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
