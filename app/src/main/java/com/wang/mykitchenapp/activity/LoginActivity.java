package com.wang.mykitchenapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A login screen that offers login via account/password.
 */
public class LoginActivity extends AppCompatActivity{
    private final String TAG = "LoginActivity";

    // UI references.
    private AutoCompleteTextView mAccountView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private Button mAccountSignInButton;
    private TextView mSignUpView;
    private View mProgressView;

    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(this);
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = null;

    //returnInfo
    private JsonObject mReturnedJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化控件
        initView();

    }

    /**
     * 初始化控件
     */
    private void initView(){
        // Set up the login form.
        mAccountView = (AutoCompleteTextView) findViewById(R.id.account);
        mAccountSignInButton = (Button) findViewById(R.id.account_sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mPasswordView = (EditText)findViewById(R.id.password);
        mSignUpView = (TextView)findViewById(R.id.link_signup);
        //进度条
        mProgressView = findViewById(R.id.login_progress);

        mCompositeSubscription = new CompositeSubscription();

        //为登录按钮添加绑定事件
        mAccountSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mSignUpView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 登录
     * @param url
     * @param map
     */
    private void login(String url, final Map<String,String> map){
        Log.v(TAG,"login");
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
                        //关闭进度条
                        showProgress(false);
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"登录失败",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.d(TAG, "onNext: ");
                        mReturnedJsonObject = jsonObject;
                        int loginResult = getExecuteResult();
                        switch (loginResult){
                            case Constant.LOGIN_SUCCEED:
                                Log.d(TAG, "onNext: login succeed");
                                //关闭进度条
                                showProgress(false);
                                setUserLoginStatus(true);
                                UserInfoBean user = getUserInfo();
                                user.setUserPassword(map.get("password"));
                                refreshUserInfo(user);
                                jump2MainActivity();
                                break;
                            case Constant.LOGIN_PASSWORD_WRONG:
                                Toast.makeText(getApplicationContext(),"密码错误",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case Constant.LOGIN_ACCOUNT_NOT_EXIST:
                                Toast.makeText(getApplicationContext(),"账号不存在",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case Constant.LOGIN_ERROR:
                                Toast.makeText(getApplicationContext(),"发生错误",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
        );
    }

    /**
     * 登录前校验
     */
    private void attemptLogin() {
        Log.d(TAG, "attemptLogin: ");
        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account =  mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid account.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        } else if (!isAccountValid(account)) {
            mAccountView.setError(getString(R.string.error_invalid_email));
            focusView = mAccountView;
            cancel = true;
        }
        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Map<String,String> map = new HashMap<String,String>();
            map.put("userName",account);
            map.put("password",password);
            String url = Constant.BASE_URL + "user/login.do";
            showProgress(true);
            login(url,map);
        }
    }

    /**
     * 密码校验
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        // 以字母开头，长度在6~16之间，只能包含字符、数字和下划线
        String regStr = "^[a-zA-Z]\\w{6,16}$";
        return password.matches(regStr);
    }

    /**
     * 用户名校验
     * @param account
     * @return
     */
    private boolean isAccountValid(String account) {
        // 昵称格式：长度在6~16之间，支持中英文、数字、减号或下划线
        String regStr = "^[_a-zA-Z0-9-]{6,16}$";
        return account.matches(regStr);
    }

    /**
     * 获取登录操作结果
     * @return
     */
    private int getExecuteResult(){
        return mReturnedJsonObject.get("executeResult").getAsInt();
    }

    /**
     * 获取用户信息，包括accessToken
     * @return
     */
    private UserInfoBean getUserInfo(){
        JsonElement jsonElementUser = mReturnedJsonObject.get("userInfo");
        Gson gson = new Gson();
        if(jsonElementUser != null) {
            return  gson.fromJson(jsonElementUser, UserInfoBean.class);
        }else{
            return null;
        }
    }

    /**
     * 更新当前用户信息
     * @param user
     */
    private void refreshUserInfo(UserInfoBean user){
        Constant.currentUserName = user.getUserName();
        Constant.user = user;
        SharedPreferencesUtils.saveIntoSharePreferences(getApplicationContext(),"userInfo",user,"user_data");
    }


    /**
     * 设置当前用户的登录状态
     * @param isLogin
     */
    private void setUserLoginStatus(Boolean isLogin){
        Constant.isLoged = isLogin;
    }

    private void jump2MainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }
    /**
     * 重写Destroy
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }
}

