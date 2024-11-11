package com.wx.architecture.code.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wx.architecture.code.data.DialogBean
import com.wx.architecture.code.ex.flowOnIOAndCatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    val errorMsgLiveData by lazy { MutableLiveData<String>() }
    val showUIDialog by lazy { MutableLiveData<DialogBean>() }

    fun show(strMessage: String = "正在请求数据") {
        val showBean = showUIDialog.value ?: DialogBean(strMessage, true)
        showBean.isShow = true
        showBean.msg = strMessage
        showUIDialog.postValue(showBean)
    }

    fun hide() {
        val showBean = showUIDialog.value ?: DialogBean("", true)
        showBean.isShow = false
        showUIDialog.postValue(showBean)
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }

    protected fun <T> Flow<T>.flowOnIOAndCatch(): Flow<T> = flowOnIOAndCatch(errorMsgLiveData)

    protected fun <T> Flow<T>.onStartAndShow(strMessage: String = "正在请求数据"): Flow<T> = onStart {
        show()
    }

    protected fun <T> Flow<T>.onCompletionAndHide(): Flow<T> = onCompletion {
        hide()
    }

    protected suspend fun <T> Flow<T>.onStartShowAndFlowOnIOAndCatchAndOnCompletionAndHideAndCollect() {
        onStartAndShow().onCompletionAndHide().flowOnIOAndCatch().collect()//这里，开始结束全放在异步里面处理
    }

    fun <T> flowAsyncWorkOnViewModelScopeLaunch(flowAsyncWork: suspend () -> Flow<T>) {
        viewModelScope.launch {
            flowAsyncWork.invoke().onStartShowAndFlowOnIOAndCatchAndOnCompletionAndHideAndCollect()
        }
    }

    fun <T> flowAsyncWorkOnViewModelScopeLaunch2(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            block.invoke(this)
//            flowAsyncWork.invoke().onStartShowAndFlowOnIOAndCatchAndOnCompletionAndHideAndCollect()
        }
    }

//    block: suspend CoroutineScope.() -> Unit
}