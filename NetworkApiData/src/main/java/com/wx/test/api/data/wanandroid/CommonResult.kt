package com.wx.test.api.data.wanandroid

open class CommonResult<T> {
    var data: T? = null
    var errorCode: Int? = 0
    var errorMsg: String? = null
}