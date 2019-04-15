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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class RegisterActivity extends AppCompatActivity {
    // UI references.
    private AutoCompleteTextView mAccountView;
    private EditText mPasswordView;
    private Button mAccountSignOnButton;
    private RadioGroup mGenderRadioGroup;
    private AutoCompleteTextView mNickNameView;
    private AutoCompleteTextView mEmailView;
    private View mRegisterForm;
    private View mProgressView;

    //Retrofit DataManager
    private DataManager mDataManager = new DataManager(this);
    //RxJAVA compositeSubscription
    private CompositeSubscription mCompositeSubscription = null;

    //returnInfo
    private JsonObject mReturnedJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAccountView = (AutoCompleteTextView) findViewById(R.id.account);
        mAccountSignOnButton = (Button) findViewById(R.id.account_sign_up_button);
        mPasswordView = (EditText)findViewById(R.id.password);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mGenderRadioGroup = (RadioGroup) findViewById(R.id.gender);
        mNickNameView = (AutoCompleteTextView) findViewById(R.id.nick_name);
        mRegisterForm = (View) findViewById(R.id.account_register_form);
        //进度条
        mProgressView = findViewById(R.id.login_progress);


        mCompositeSubscription = new CompositeSubscription();

        mAccountSignOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    public void register(String url, Map<String, String> params){
        mCompositeSubscription.add(mDataManager.postData(url, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        //关闭进度条
                        showProgress(false);
                        if (null != mReturnedJsonObject){
                            //获取信息
                            JsonElement registerResult = mReturnedJsonObject.get("registerResult");
                            if (null != registerResult)
                                Log.d("registerResult",registerResult.toString());
                            if(null != registerResult && Constant.REGISTER_SUCCEED == Integer.parseInt(mReturnedJsonObject.get("registerResult").toString())) {
                                JsonElement jsonElementUser = mReturnedJsonObject.get("userInfo");
                                Gson gson = new Gson();
                                UserInfoBean returnUserInfoObject = gson.fromJson(jsonElementUser, UserInfoBean.class);

                                //accessToken
                                String accessToken = returnUserInfoObject.getUserAccessToken();
                                if (null != accessToken && !"".equals(accessToken)) {
                                    SharedPreferencesUtils.saveIntoSharePreferences(getApplicationContext(), "accessToken", accessToken, "user_data");
                                    Constant.accessToken = accessToken;
                                    Log.d("AccessToken", accessToken);
                                } else {
                                    Log.d("returnError","没有accessToken返回");
                                }
                                //sessionID
                                String sessionId = mReturnedJsonObject.get("sessionId").toString();
                                if (null != sessionId && !"".equals(sessionId)) {
                                    SharedPreferencesUtils.saveIntoSharePreferences(getApplicationContext(), "sessionId", accessToken, "user_data");
                                    Constant.sessionId = sessionId;
                                    Log.d("sessionId", sessionId);
                                } else {
                                    Log.d("returnError","没有sessionId返回");
                                }
                                //保存用户信息
                                String userName = returnUserInfoObject.getUserName();
                                SharedPreferencesUtils.saveIntoSharePreferences(getApplicationContext(), userName + "userInfo", returnUserInfoObject, "user_data");
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //关闭进度条
                        showProgress(false);
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"请求失败",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        mReturnedJsonObject = jsonObject;
                    }
                })
        );
    }

    public void onError(String result) {
        //显示错误信息
        if(null == result || "".equals(result)){
            result = "请求失败";
        }
        Toast.makeText(getApplicationContext(),result,
                Toast.LENGTH_SHORT).show();
    }

    private void attemptRegister() {
        // Reset errors.
        mAccountView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String account = mAccountView.getText().toString();
        String password = mPasswordView.getText().toString();
        String gender = ((RadioButton)findViewById(mGenderRadioGroup.getCheckedRadioButtonId())).getText().toString();
        Log.d("gender",gender);
        String nickName = mNickNameView.getText().toString();
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid account.
        if (TextUtils.isEmpty(account)) {
            mAccountView.setError(getString(R.string.error_field_required));
            focusView = mAccountView;
            cancel = true;
        } else if (!isAccountValid(account)) {
            mAccountView.setError(getString(R.string.error_invalid_account));
            focusView = mAccountView;
            cancel = true;
        }
        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isAccountValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a nickName
        if (!TextUtils.isEmpty(nickName) && !isNickNameValid(nickName)) {
            mNickNameView.setError(getString(R.string.error_invalid_nickname));
            focusView = mNickNameView;
            cancel = true;
        }
        //Check for email
        if (!TextUtils.isEmpty(email) && !isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Map<String,String> params = new HashMap<>();
            params.put("userName", account);
            params.put("nickName",nickName);
            params.put("email",email);
            params.put("password", password);
            if(gender.equals("男")){
                params.put("gender","female");
            }else{
                params.put("gender","male");
            }
            String url = Constant.BASE_URL + "user/register.do";
            showProgress(true);
            register(Constant.BASE_URL + "user/register.do",params);
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
     * 用户昵称校验
     * @param nickName
     * @return
     */
    private  boolean isNickNameValid(String nickName){
        String regStr = "^[\\u4e00-\\u9fa5_a-zA-Z0-9-]{1,16}$";
        return nickName.matches(regStr);
    }

    private  boolean isEmailValid(String email){
        String regStr = "^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        return email.matches(regStr);
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

            mRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
