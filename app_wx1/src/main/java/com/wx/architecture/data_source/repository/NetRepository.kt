package com.wx.architecture.data_source.repository

import com.google.gson.Gson
import com.wx.test.api.net.NetApi
import com.wx.test.api.retrofit.RequestBodyWrapper
import com.wx.test.api.retrofit.RetrofitUtils
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.RequestBody

class NetRepository private constructor() {
    val service by lazy { RetrofitUtils.instance.create(NetApi::class.java) }

    companion object {
        val instance by lazy { NetRepository() }
    }

    // 示例get 请求
    fun getHomeList() = flow { emit(service.getHomeList()) }

    // 示例get 请求2
    fun getHomeList(page: Int) = flow { emit(service.getHomeList(page)) }

    fun getHomeList(page: Int, a: Int) = flow { emit(service.getHomeList(page, a)) }

    fun getHomeList(page: Int, f: Float) = flow { emit(service.getHomeList(page, f)) }

    // 示例get 请求2
    fun getHomeList2222(page: Int) = flow { emit(service.getHomeList2222(page)) }

    fun getHomeList3333(page: Int) = flow { emit(service.getHomeList3333(page)) }

    fun getHomeList5555(page: Int, ss: String, map: Map<String, String>) = flow { emit(service.getHomeList5555(page, ss, map)) }

    fun getHomeList6666(
        page: Int, float: Float, long: Long, double: Double, byte: Byte,
        short: Short, char: Char, boolean: Boolean, string: String, body: RequestBodyWrapper
    ) = flow {
        emit(service.getHomeList6666(page, float, long, double, byte, short, char, boolean, string, body))
    }

    fun register(username: String, password: String, repassword: String) = flow { emit(service.register(username, password, repassword)) }

    //
//    /************************* 以下只 示例写法，接口调不通，因为找不到那么多 公开接口  全是 Retrofit的用法 来测试 *****************************************************/
//
//
    fun post1(body: RequestBody) = flow { emit(service.post1(body)) }

    fun post12(body: RequestBody, map: Map<String, String>) = flow { emit(service.post12(body, map)) }

    fun post1222(id: Long, asr: String) = flow {
        val map = mutableMapOf<String, Any>()
        map["id"] = id
        map["asr"] = asr
        val mapHeader = HashMap<String, Any>()
        mapHeader["v"] = 1000
        mapHeader["device_sn"] = "Avidfasfa1213"
        emit(service.post1222(RequestBodyWrapper(Gson().toJson(map)), mapHeader))
    }

    //我这个接口 ，请求前需要 判断处理一下，拿到数据后也需要再处理一下
    fun post333(id: Long, asr: String, m: String, n: String, list: List<String>) = flow {
        val map = mutableMapOf<String, Any>()
        map["id"] = id
        map["asr"] = asr
        val mapHeader = HashMap<String, Any>()
        mapHeader["v"] = 1000
        mapHeader["device_sn"] = "Avidfasfa1213"

        //接口调用前 根据 需要处理操作
        list.forEach {
            if (map.containsKey(id.toString())) {
                ///
            }
        }

        val result = service.post1222(RequestBodyWrapper(Gson().toJson(map)), mapHeader)
        // 拿到数据后需要处理操作
        val result1 = result
        emit(result1)
    }.map {
        //需要再转化一下
        it
    }.filter {
        //过滤一下
        it.length == 3
    }
}