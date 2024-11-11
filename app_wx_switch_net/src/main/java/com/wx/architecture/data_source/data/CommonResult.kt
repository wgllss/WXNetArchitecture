package com.wx.architecture.data_source.data

open class CommonResult<T> {
    var data: T? = null
    var errorCode: Int? = 0
    var errorMsg: String? = null
}