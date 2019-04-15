package com.wang.mykitchenapp.service;

import com.wang.mykitchenapp.bean.Book;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface RetrofitService extends BaseRetrofitService {
    @GET("book/search")
    Observable<Book> getSearchBook(@Query("q") String name,
                                   @Query("tag") String tag,
                                   @Query("start") int start,
                                   @Query("count") int count);
}