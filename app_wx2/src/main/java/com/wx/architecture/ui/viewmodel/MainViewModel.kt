package com.wx.architecture.ui.viewmodel

import com.wx.architecture.code.base.BaseViewModel
import com.wx.architecture.data_source.kapt.RNetApiRepository
import kotlinx.coroutines.flow.onEach

class MainViewModel : BaseViewModel() {

    private val repository by lazy { RNetApiRepository() }

    fun getHomeList(page: Int) {
        flowAsyncWorkOnViewModelScopeLaunch {
            val time = System.currentTimeMillis()
            repository.getHomeList(page).onEach {
                errorMsgLiveData.postValue(it.data?.datas!![0].title)
                android.util.Log.e("MainViewModel", "two 222 ${it.data?.datas!![0].title}")
                android.util.Log.e("MainViewModel", "耗时:${(System.currentTimeMillis() - time)} ms")
            }
        }
    }
}