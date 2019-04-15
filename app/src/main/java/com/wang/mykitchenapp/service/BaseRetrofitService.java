package com.wang.mykitchenapp.service;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Wang on 2017/3/28.
 */

public interface BaseRetrofitService {
    @GET
    Observable<JsonObject> getData(
            @Url String url,
            @QueryMap Map<String, String> map
    );

    @FormUrlEncoded
    @POST
    Observable<JsonObject> postData(
            @Url String url,
            @FieldMap Map<String, String> map
    );

    @GET
    JsonObject getDataWithoutRx(
            @Url String url,
            @QueryMap Map<String, String> map
    );

    @Multipart
    @POST
    Observable<JsonObject> uploadFile(
            @Url String url,
            @PartMap Map<String, RequestBody> map
    );

    @Multipart
    @POST
    Observable<JsonObject> register(
            @Url String url,
            @FieldMap Map<String , String> map,
            @Part Part image
            );
}
