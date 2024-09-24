package com.wx.architecture.ui.viewmodel

import com.wx.architecture.code.base.BaseViewModel
import com.wx.architecture.data_source.repository.RepositoryPoxy
import com.wx.test.api.data.wanandroid.home.HomeData
import kotlinx.coroutines.flow.onEach

class MainViewModel : BaseViewModel() {

    private val repository by lazy { RepositoryPoxy.instance }

    fun getHomeList(page: Int) {
        flowAsyncWorkOnViewModelScopeLaunch {
            val time = System.currentTimeMillis()
            repository.callApiSuspendMethod(HomeData::class.java, "getHomeListB", page).onEach {
                errorMsgLiveData.postValue(it.data?.datas!![0].title)
                android.util.Log.e("MainViewModel", "four 444 ${it.data?.datas!![0].title}")
                android.util.Log.e("MainViewModel", "耗时:${(System.currentTimeMillis() - time)} ms")
            }
        }
    }
}