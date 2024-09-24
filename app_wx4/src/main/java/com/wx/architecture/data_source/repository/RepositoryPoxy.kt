package com.wx.architecture.data_source.repository

import com.wx.architecture.code.base.BaseRepositoryProxy
import com.wx.test.api.net.NetApi
import com.wx.test.api.retrofit.RetrofitUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.javaType

class RepositoryPoxy private constructor() : BaseRepositoryProxy() {

    val service = NetApi::class.java
    val api by lazy { RetrofitUtils.instance.create(service) }


    companion object {
        val instance by lazy { RepositoryPoxy() }
    }

    fun <R> callApiMethod(clazzR: Class<R>, methodName: String, vararg args: Any): Flow<R> {
        return flow {
            val clssss = mutableListOf<Class<out Any>>()
            args?.forEach {
                clssss.add(javaClassTransform(it.javaClass))
            }
            val parameterTypes = clssss.toTypedArray()
            val call = (service.getMethod(methodName, *parameterTypes)?.invoke(api, *(args ?: emptyArray())) as Call<R>)
            call?.execute()?.body()?.let {
                emit(it as R)
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun <R> callApiSuspendMethod(clazzR: Class<R>, methodName: String, vararg args: Any): Flow<R> {
        return flow {
            val funcds = findSuspendMethod(service, methodName, args)
            if (args == null) {
                emit(funcds?.callSuspend(api) as R)
            } else {
                emit(funcds?.callSuspend(api, *args) as R)
            }
        }
    }
}