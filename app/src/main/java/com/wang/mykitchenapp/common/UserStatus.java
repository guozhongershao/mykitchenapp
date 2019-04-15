package com.wang.mykitchenapp.common;

import android.content.Context;

import com.google.gson.JsonObject;
import com.wang.mykitchenapp.bean.UserInfoBean;
import com.wang.mykitchenapp.manager.DataManager;
import com.wang.mykitchenapp.utils.SharedPreferencesUtils;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wang on 2017/4/6.
 */

public final class UserStatus {

    //Retrofit DataManager
    private static DataManager mDataManager;
    //RxJAVA compositeSubscription
    private static CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    //returnInfo
    private JsonObject mReturnedJsonObject;
    /**
     * 检查用户登录状态
     * @param context
     * @return
     */
    public static boolean isLogin(Context context){
        String islogin = SharedPreferencesUtils.getFromSharePreferences(context,"islogin","user_data");
        return (null == islogin) && islogin.isEmpty() && islogin.equals("y");
    }

    /**
     * 获取当前登录用户的信息
     * @param context
     * @return
     */
    public static UserInfoBean getUserInfoBean(Context context){
        UserInfoBean userInfoBean = null;
        return userInfoBean;
    }
}
