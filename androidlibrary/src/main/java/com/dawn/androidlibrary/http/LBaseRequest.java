package com.dawn.androidlibrary.http;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求Retrofit和RxJava结合
 */
@SuppressWarnings("unused")
public class LBaseRequest {
    /**
     * 获取Retrofit对象
     */
    public static Retrofit getRetrofit(String baseUrl){
        return new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
