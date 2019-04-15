package com.wang.mykitchenapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.JsonObject;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.manager.DataManager;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ModifyUserInfoActivity extends AppCompatActivity {
    private final String TAG = "ModifyUserInfoActivity";

    private Context context = this;

    //控件
    private EditText mEtPassword;
    private EditText mEtNickName;
    private EditText mEtEmail;
    private RadioGroup mRgGender;
    private Button mBtnModify;

    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(context);
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    //returnInfo
    private JsonObject mReturnedJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);

        initView();
    }

    private void initView(){
        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mEtPassword = (EditText) findViewById(R.id.password);
        mEtEmail = (EditText) findViewById(R.id.email);
        mEtNickName = (EditText) findViewById(R.id.nick_name);
        mRgGender = (RadioGroup) findViewById(R.id.gender);
        mBtnModify = (Button) findViewById(R.id.btn_modify);

        mBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doModify();
            }
        });

        mEtEmail.setText(Constant.user.getUserEmail());
        mEtNickName.setText(Constant.user.getUserNickName());
        if("male".equals(Constant.user.getUserGender())){
            mRgGender.check(R.id.rb_male);
        }else{
            mRgGender.check(R.id.rb_female);
        }
    }

    private void doModify(){
        //构造参数
        Map<String,String> map = new HashMap<>();
        if(null != Constant.currentUserName){
            map.put("userName",Constant.currentUserName);
        }
        String password = mEtPassword.getText().toString();
        String nickName = mEtNickName.getText().toString();
        Log.d(TAG, "doModify: " + nickName);
        String gender = ((RadioButton)findViewById(mRgGender.getCheckedRadioButtonId())).getText().toString();
        String email = mEtEmail.getText().toString();
        if(null != password && !"".equals(password)){
            map.put("password",password);
        }
        if(null != nickName && !"".equals(nickName)){
            map.put("nickName",nickName);
        }
        if(null != gender && !"".equals(gender)){
            if("男".equals(gender)){
                gender = "female";
            }else{
                gender = "male";
            }
            map.put("gender",gender);
        }
        if(null != email && !"".equals(email)){
            map.put("email",email);
        }

        String url = Constant.BASE_URL + "user/modifyUserInfo.do";
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
                                   int doCommentResult = mReturnedJsonObject.get("modifyResult").getAsInt();
                                   switch (doCommentResult) {
                                       case Constant.MODIFY_OK:
                                           onModifySucceed();
                                           break;
                                       case Constant.MODIFY_ERROR:
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

    private void onModifySucceed(){
        Intent intent = new Intent(ModifyUserInfoActivity.this, UserInfoActivity.class);
        intent.putExtra("userName",Constant.currentUserName);
        startActivity(intent);
        finish();
    }

    private void onCommentFailed(){

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
