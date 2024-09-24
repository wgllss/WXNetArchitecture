package com.wx.architecture.code.ex

import androidx.lifecycle.MutableLiveData
import com.wx.test.api.data.wanandroid.CommonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception


fun <T> Flow<T>.flowOnIOAndCatch(errorMsgLiveData: MutableLiveData<String>? = null): Flow<T> =
    flowOn(Dispatchers.IO)
        .catch {
            it.printStackTrace()
            errorMsgLiveData?.value = it.parseErrorString();
        }

suspend fun <T> Flow<T>.flowOnIOAndCatchAAndCollect() {
    flowOnIOAndCatch().collect()//这里，开始结束全放在异步里面处理
}