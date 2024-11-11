package com.wx.architecture.ui.viewmodel

import com.wx.architecture.code.base.BaseViewModel
import com.wx.architecture.data_source.repository.NetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NetRepository) : BaseViewModel() {


    fun getHomeList() {
        flowAsyncWorkOnViewModelScopeLaunch {
            repository.getHomeList().onEach {
                val title = it.datas!![0].title
                android.util.Log.e("MainViewModel", "one 111 ${title}")
                errorMsgLiveData.postValue(title)
            }
        }
    }
}