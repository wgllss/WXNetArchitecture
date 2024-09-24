package com.wx.test.api.data

//data class BaseResponse<T>(val code :Int,var errorMsg:String,var t:T) {
data class BaseResponse<T>(var gsm: String, var data: T)