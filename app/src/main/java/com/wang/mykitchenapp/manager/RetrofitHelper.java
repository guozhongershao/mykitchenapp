package com.wang.mykitchenapp.manager;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.wang.mykitchenapp.BuildConfig;
import com.wang.mykitchenapp.common.Constant;
import com.wang.mykitchenapp.service.BaseRetrofitService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by win764-1 on 2016/12/12.
 */

public class RetrofitHelper {

    private Context mCntext =null;
    private OkHttpClient client = null;
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;
    //单例模式,传参为 Context
    public static RetrofitHelper getInstance(Context context){
        if (instance == null){
            instance = new RetrofitHelper(context);
        }
        return instance;
    }
    private RetrofitHelper(Context mContext){
        mCntext = mContext;
        init();
    }

    private void init() {
        //创建OkHttpClient.Bulder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //用于将接受的json转换为对象
        GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别

            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }
        client = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public BaseRetrofitService getBaseRetrofitService(){
        return mRetrofit.create(BaseRetrofitService.class);
    }
}
