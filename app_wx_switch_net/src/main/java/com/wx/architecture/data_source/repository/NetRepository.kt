package com.wx.architecture.data_source.repository

import com.wx.architecture.data_source.data.HomeData
import com.wx.architecture.data_source.data.WanAndroidHome
import com.wx.architecture.data_source.dl_hilt.annotation.BindHttpUrlConnection
import com.wx.architecture.data_source.dl_hilt.annotation.BindOkhttp
import com.wx.architecture.data_source.net.INetApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


//class NetRepository @Inject constructor(@BindHttpUrlConnection val netHttp: INetApi) {
class NetRepository @Inject constructor(@BindOkhttp val netHttp: INetApi) {

    suspend fun getHomeList(): Flow<WanAndroidHome> {
        return flow {
            netHttp.getApi("https://www.wanandroid.com/article/list/0/json", HomeData::class.java).data?.let { emit(it) }
        }
    }
}