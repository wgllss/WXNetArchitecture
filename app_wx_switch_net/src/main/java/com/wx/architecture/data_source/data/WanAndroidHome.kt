package com.wx.architecture.data_source.data

data class WanAndroidHome(
    val datas: MutableList<HomeItemBean>,
    val curPage: Int,
    val size: Int,
    val total: Int
)