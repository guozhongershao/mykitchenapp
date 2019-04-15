package com.wang.mykitchenapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.entity.BbsNote;
import com.wang.mykitchenapp.manager.DataManager;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BbsDoNoteActivity extends AppCompatActivity {
    private final String TAG = "BbsDoNoteActivity";

    private Intent mIntent = null;
    private String mTopicName = null;
    //控件
    private EditText mEtTitle = null;
    private EditText mEtContent = null;
    private Button mBtnPublish = null;
    private LinearLayout mLlDoNote = null;
    private ProgressBar mProgressBar = null;

    //context
    private Context context = this;

    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(context);
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    //returnInfo
    private JsonObject mReturnedJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_do_note);

        initView();
    }

    /**
     * 初始化Activity
     */
    private void initView(){
        //初始话toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //获取intent
        mIntent = getIntent();
        mTopicName = mIntent.getStringExtra("topicName");
        Log.d(TAG, "initView: " + mTopicName);
        mBtnPublish = (Button) findViewById(R.id.btn_publish);
        mBtnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempToPublish();
            }
        });
        mEtTitle = (EditText) findViewById(R.id.et_title);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mLlDoNote = (LinearLayout) findViewById(R.id.ll_do_note);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
    }

    /**
     * 发表前校验
     */
    private void attempToPublish() {
        Log.d(TAG, "attempToPublish: ");
        // Reset errors.
        mEtTitle.setError(null);
        mEtContent.setError(null);

        // Store values at the time of the login attempt.
        String title =  mEtTitle.getText().toString();
        String content = mEtContent.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid account.
        if (TextUtils.isEmpty(title)) {
            mEtTitle.setError(getString(R.string.error_field_required));
            focusView = mEtTitle;
            cancel = true;
        } else if (title.length() <= 0 || title.length() > 125 ) {
            mEtTitle.setError(getString(R.string.error_invalid_format));
            focusView = mEtTitle;
            cancel = true;
        }
        // Check for a valid password.
        if (TextUtils.isEmpty(content)) {
            mEtContent.setError(getString(R.string.error_field_required));
            focusView = mEtContent;
            cancel = true;
        } else if (content.length() <= 0 || content.length() > 256) {
            mEtContent.setError(getString(R.string.error_invalid_format));
            focusView = mEtContent;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            doCmment();
        }
    }

    private void doCmment(){
        //构造参数
        Map<String,String> map = new HashMap<>();
        map.put("topicName",String.valueOf(mTopicName));
        if(null != Constant.currentUserName){
            map.put("author",Constant.currentUserName);
        }
        String title = mEtTitle.getText().toString();
        map.put("title",title);
        String content = mEtContent.getText().toString();
        map.put("content",content);
        String url = Constant.BASE_URL + "bbs/doPublishNote.do";
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
                                   int doCommentResult = mReturnedJsonObject.get("doPublishResult").getAsInt();
                                   switch (doCommentResult) {
                                       case Constant.DO_NOTE_PUBLISH_OK:
                                           onPublishSucceed();
                                           break;
                                       case Constant.DO_NOTE_PUBLISH_ERROR:
                                           onPublishFaild();
                                           break;
                                       default:
                                           break;
                                   }
                               }
                           }
                )
        );
    }

    private void onPublishSucceed(){
        showProgress(false);
        Snackbar.make(mEtContent, "发表帖子成功", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Gson gson = new Gson();
        BbsNote bbsNote = gson.fromJson(mReturnedJsonObject.get("newestNote"),new TypeToken<BbsNote>(){}.getType());
        Intent intent = new Intent(BbsDoNoteActivity.this,BbsNoteDetailActivity.class);
        intent.putExtra("noteId",bbsNote.getBbnoId());
        startActivity(intent);
        finish();
    }

    private void onPublishFaild(){
        showProgress(false);
        Snackbar.make(mEtContent, "发表帖子失败", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLlDoNote.setVisibility(show ? View.GONE : View.VISIBLE);
            mLlDoNote.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLlDoNote.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mLlDoNote.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
