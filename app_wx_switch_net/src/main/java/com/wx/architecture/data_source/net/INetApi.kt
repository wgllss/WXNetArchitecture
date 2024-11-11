package com.wx.architecture.data_source.net

interface INetApi {

    /**
     * Get请求
     * @param url:请求地址
     * @param clazzR:返回对象类型
     * @param header:请求头
     * @param map:请求参数
     */

    suspend fun <R> getApi(url: String, clazzR: Class<R>, header: MutableMap<String, String>? = null, map: MutableMap<String, Any>? = null): R


    /**
     * Get请求
     * @param url:请求地址
     * @param clazzR:返回对象类型
     * @param header:请求头
     * @param map:请求参数
     * @param body:请求body
     */
    suspend fun <R> postApi(url: String, clazzR: Class<R>, header: MutableMap<String, String>? = null, body: String? = null): R

}