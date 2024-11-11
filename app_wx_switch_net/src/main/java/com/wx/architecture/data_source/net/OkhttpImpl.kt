package com.wx.architecture.data_source.net

import com.google.gson.Gson
import com.wx.architecture.code.utils.BuildParamUtils.buildParamUrl
import com.wx.architecture.code.utils.RequestBodyCreate
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class OkhttpImpl @Inject constructor() : INetApi {

    private val okHttpClient by lazy { OkHttpClient() }
    private val gson by lazy { Gson() }

    override suspend fun <R> getApi(url: String, clazzR: Class<R>, header: MutableMap<String, String>?, map: MutableMap<String, Any>?): R {
        try {
            val request = Request.Builder().url(buildParamUrl(url, map))
            header?.forEach {
                request.addHeader(it.key, it.value)
            }
            val response = okHttpClient.newCall(request.build()).execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                android.util.Log.e("OkhttpImpl","okhttp 请求:${json}")
                return gson.fromJson<R>(json, clazzR)
            } else {
                throw RuntimeException("response fail")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun <R> postApi(url: String, clazzR: Class<R>, header: MutableMap<String, String>?, body: String?): R {
        try {
            val request = Request.Builder().url(url)
            header?.forEach {
                request.addHeader(it.key, it.value)
            }
            body?.let {
                request.post(RequestBodyCreate.toBody(it))
            }
            val response = okHttpClient.newCall(request.build()).execute()
            if (response.isSuccessful) {
                return gson.fromJson<R>(response.body.toString(), clazzR)
            } else {
                throw RuntimeException("response fail")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}