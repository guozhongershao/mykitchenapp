package com.wang.mykitchenapp.utils;

import com.google.gson.JsonObject;
import com.wang.mykitchenapp.inter.Callback;
import com.wang.mykitchenapp.manager.DataManager;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Wang on 2017/4/27.
 */

public class NetUtils {
    private final String TAG = "NetUtils";
    //网络访问工具
    private static CompositeSubscription mCompositeSubscription;
    private static DataManager mDataManager;
    //返回消息
    private static JsonObject mReturnedJsonObject;

    public static void doPost(String url, final Map<String,String> map, final Callback callback){
        mCompositeSubscription.add(mDataManager.postData(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        callback.onResponse(mReturnedJsonObject);
                    }
                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(mReturnedJsonObject);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        mReturnedJsonObject = jsonObject;
                    }
                })
        );
    }
}
