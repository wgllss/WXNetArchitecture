package com.wx.architecture.data_source.repository

import com.wx.test.api.retrofit.RetrofitUtils
import com.wx.test.api.data.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.ParameterizedType

open class BaseRepository<T>() {
    val service by lazy { createService() }

    private fun createService(): T {
        val clazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
        return RetrofitUtils.instance.create(clazz)
    }

    //
    suspend fun <T> apiCall(api: suspend () -> BaseResponse<T>): BaseResponse<T> = withContext(Dispatchers.IO) { api.invoke() }
}