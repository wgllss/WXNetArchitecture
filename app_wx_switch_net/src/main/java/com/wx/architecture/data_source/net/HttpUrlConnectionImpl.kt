package com.wx.architecture.data_source.net

import com.google.gson.Gson
import com.wx.architecture.code.utils.BuildParamUtils
import com.wx.architecture.data_source.net.http.HttpUrlConnectionRequest
import javax.inject.Inject

class HttpUrlConnectionImpl @Inject constructor() : INetApi {
    private val gson by lazy { Gson() }

    override suspend fun <R> getApi(url: String, clazzR: Class<R>, header: MutableMap<String, String>?, map: MutableMap<String, Any>?): R {
        val json = HttpUrlConnectionRequest.getResult(BuildParamUtils.buildParamUrl(url, map), header)
        android.util.Log.e("OkhttpImpl", "HttpUrlConnection 请求:${json}")
        return gson.fromJson<R>(json, clazzR)
    }

    override suspend fun <R> postApi(url: String, clazzR: Class<R>, header: MutableMap<String, String>?, body: String?): R {
        val json = HttpUrlConnectionRequest.postData(url, header, body)
        return gson.fromJson<R>(json, clazzR)
    }
}