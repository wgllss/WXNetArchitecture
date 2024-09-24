package com.wx.test.api.retrofit

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink

class RequestBodyWrapper constructor(val postBody: String) : RequestBody() {

    override fun contentType(): MediaType? {
        return "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    override fun writeTo(sink: BufferedSink) {
        sink.write(postBody.toByteArray(), 0, postBody.toByteArray().size)
    }
}