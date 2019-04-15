package com.wang.mykitchenapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wang.mykitchenapp.R;
import com.wang.mykitchenapp.bean.UserInfoBean;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.manager.DataManager;
import com.wang.mykitchenapp.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class WelcomeActivity extends AppCompatActivity {
    private final String TAG = "WelcomeActivity";
    //欢迎界面图
    private ImageView mWelcomeImageView;

    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(this);
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    //returnInfo
    private JsonObject mReturnedJsonObject;

    private UserInfoBean mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.attemptLogin();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        Log.v(TAG, "initWelcomeActivity");
        mWelcomeImageView = (ImageView) findViewById(R.id.iv_welcome);
    }

    /**
     * 延时跳转到MainActivity
     */
    private void jumpToMainActivity() {
        final Intent localIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        Timer timer = new Timer();
        TimerTask tast = new TimerTask() {
            @Override
            public void run() {
                startActivity(localIntent);
                finish();
                //WelcomeActivity.this.finish();
            }
        };
        //延时1S跳转
        timer.schedule(tast, 1000);
    }

    //登录前检测
    private void attemptLogin() {
        mUser = SharedPreferencesUtils.getFromSharePreferences(this, "userInfo", UserInfoBean.class, "user_data");
        if (null != mUser) {
            login(mUser);
        }else {
            jumpToMainActivity();
        }
    }

    /**
     * 登录，保持登录状态
     */
    private void login(final UserInfoBean user) {
        Log.v(TAG, "login");
        String url = Constant.BASE_URL + "user/login.do";
        Map<String, String> map = new HashMap<>();
        map.put("userName", user.getUserName());
        map.put("password", user.getUserPassword());
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        if (mReturnedJsonObject != null) {
                            //获取信息
                            JsonElement jsonElementUser = mReturnedJsonObject.get("userInfo");
                            Gson gson = new Gson();
                            UserInfoBean returnUserInfoObject = gson.fromJson(jsonElementUser, UserInfoBean.class);
                            //accessToken
                            String accessToken = returnUserInfoObject.getUserAccessToken();
                            //是否返回了AccessToken
                            if (null != accessToken && !"".equals(accessToken)) {
                                SharedPreferencesUtils.saveIntoSharePreferences(getApplicationContext(), "accessToken", accessToken, "user_data");
                                Constant.accessToken = accessToken;
                                Log.d("AccessToken", accessToken);
                            } else {
                                Log.d("returnError", "没有accessToken返回");
                            }
                            //sessionID
                            String sessionId = mReturnedJsonObject.get("sessionId").toString();
                            if (null != sessionId && !"".equals(sessionId)) {
                                SharedPreferencesUtils.saveIntoSharePreferences(getApplicationContext(), "sessionId", accessToken, "user_data");
                                Constant.sessionId = sessionId;
                                Log.d("sessionId", sessionId);
                            } else {
                                Log.d("returnError", "没有sessionId返回");
                            }
                            //保存用户信息
                            returnUserInfoObject.setUserPassword(user.getUserPassword());
                            SharedPreferencesUtils.saveIntoSharePreferences(getApplicationContext(), "userInfo", returnUserInfoObject, "user_data");
                            //设置用户登录状态
                            Constant.isLoged = true;
                            //设置当前用户
                            Constant.currentUserName  = user.getUserName();
                            Constant.user = user;
                            Log.v(TAG, "登录成功");

                            jumpToMainActivity();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //关闭进度条
                        e.printStackTrace();
                        Log.v(TAG, "登录失败");
                        jumpToMainActivity();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        mReturnedJsonObject = jsonObject;
                    }
                })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
        Log.v(TAG,"OnDestroy WelcomeActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        WelcomeActivity.this.finish();
        Log.v(TAG,"OnStop WelcomeActivity");
    }
}
