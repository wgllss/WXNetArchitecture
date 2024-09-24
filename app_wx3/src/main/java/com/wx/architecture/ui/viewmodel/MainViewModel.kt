package com.wx.architecture.ui.viewmodel

import com.wx.architecture.code.base.BaseViewModel
import com.wx.architecture.data_source.kapt.INetApiRepository
import com.wx.architecture.data_source.repository.RepositoryPoxy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class MainViewModel : BaseViewModel() {

    private val repository by lazy { RepositoryPoxy.instance }

    fun getHomeList(page: Int) {
        flowAsyncWorkOnViewModelScopeLaunch {
            val time = System.currentTimeMillis()
            repository.callApiMethod(INetApiRepository::class.java).getHomeList(page).onEach {
                errorMsgLiveData.postValue(it.data?.datas!![0].title)
                android.util.Log.e("MainViewModel", "three 333 ${it.data?.datas!![0].title}")
                android.util.Log.e("MainViewModel", "耗时:${(System.currentTimeMillis() - time)} ms")
            }
        }
    }
}