package com.wx.architecture.data_source.repository

import com.wx.architecture.code.base.BaseRepositoryProxy
import com.wx.test.api.net.NetApi
import com.wx.test.api.retrofit.RetrofitUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Proxy
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.javaType

class RepositoryPoxy private constructor() : BaseRepositoryProxy() {

    val service = NetApi::class.java
    val api by lazy { RetrofitUtils.instance.create(service) }


    companion object {
        val instance by lazy { RepositoryPoxy() }
    }

    fun <R> callApiMethod(serviceR: Class<R>): R {
        return Proxy.newProxyInstance(serviceR.classLoader, arrayOf(serviceR)) { proxy, method, args ->
            flow {
                val funcds = findSuspendMethod(service, method.name, args)
                if (args == null) {
                    emit(funcds?.callSuspend(api))
                } else {
                    emit(funcds?.callSuspend(api, *args))
                }
//                emit((service.getMethod(method.name, *parameterTypes)?.invoke(api, *(args ?: emptyArray())) as Call<HomeData>).execute().body())
            }.catch {
                if (it is InvocationTargetException) {
                    throw Throwable(it.targetException)
                } else {
                    it.printStackTrace()
                    throw it
                }
            }
        } as R
    }
}