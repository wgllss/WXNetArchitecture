package com.wx.test.api.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitUtils private constructor() {
    val baseUrl = "https://www.wanandroid.com/"

    companion object {
        val instance by lazy { RetrofitUtils() }
    }

    private inline val retrofit: Retrofit
        get() {
            val logging = HttpLoggingInterceptor()
            val timeout = 30000L
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HeaderInterceptor())
                .addInterceptor(logging)
//                .addInterceptor(RetrofitClient.BaseUrlInterceptor())
                .callTimeout(timeout, TimeUnit.MILLISECONDS)
                //设置连接超时
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                //设置从主机读信息超时
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                //设置写信息超时
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                .build();
            return Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        }

    fun <T> create(service: Class<T>?): T {
        if (service == null) {
            throw RuntimeException("Api service is null!")
        }
        return retrofit.create(service)!!
    }
}