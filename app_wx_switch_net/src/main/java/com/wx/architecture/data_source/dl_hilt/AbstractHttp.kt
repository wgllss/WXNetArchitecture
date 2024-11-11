package com.wx.architecture.data_source.dl_hilt

import com.wx.architecture.data_source.dl_hilt.annotation.BindHttpUrlConnection
import com.wx.architecture.data_source.dl_hilt.annotation.BindOkhttp
import com.wx.architecture.data_source.net.HttpUrlConnectionImpl
import com.wx.architecture.data_source.net.INetApi
import com.wx.architecture.data_source.net.OkhttpImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class AbstractHttp {

    @BindOkhttp
    @Singleton
    @Binds
    abstract fun bindOkhttp(h: OkhttpImpl): INetApi

    @BindHttpUrlConnection
    @Singleton
    @Binds
    abstract fun bindHttpUrlConnection(h: HttpUrlConnectionImpl): INetApi
}