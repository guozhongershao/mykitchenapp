package com.wang.mykitchenapp.manager;

import android.content.Context;

import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;


public class DataManager {
    private RetrofitHelper mRetrofitHelper = null;
    public DataManager(Context context){
        this.mRetrofitHelper = RetrofitHelper.getInstance(context);
    }

    //get请求
    public Observable<JsonObject> getData(String url, Map<String,String> map){
        return mRetrofitHelper.getBaseRetrofitService().getData(url, map);
    }

    //post请求
    public Observable<JsonObject> postData(String url, Map<String,String> map){
        return mRetrofitHelper.getBaseRetrofitService().postData(url, map);
    }

    //get请求，不异步处理
    public JsonObject getDataWithoutRx(String url,Map<String,String> map){
        return mRetrofitHelper.getBaseRetrofitService().getDataWithoutRx(url,map);
    }
}
