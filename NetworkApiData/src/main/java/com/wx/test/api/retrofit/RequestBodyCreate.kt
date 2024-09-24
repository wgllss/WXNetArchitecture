package com.wx.test.api.retrofit

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

object RequestBodyCreate {

    fun toBody(body: String) = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), body)
}