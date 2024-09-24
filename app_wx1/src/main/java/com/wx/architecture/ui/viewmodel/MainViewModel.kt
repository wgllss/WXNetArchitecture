package com.wx.architecture.ui.viewmodel

import com.wx.architecture.code.base.BaseViewModel
import com.wx.architecture.data_source.repository.NetRepository
import kotlinx.coroutines.flow.onEach

class MainViewModel : BaseViewModel() {

    private val repository by lazy { NetRepository.instance }

    fun getHomeList(page: Int) {
        flowAsyncWorkOnViewModelScopeLaunch {
            repository.getHomeList(page).onEach {
                errorMsgLiveData.postValue(it.data?.datas!![0].title)
                android.util.Log.e("MainViewModel", "one 111 ${it.data?.datas!![0].title}")
            }
        }
    }
}