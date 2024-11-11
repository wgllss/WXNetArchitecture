package com.wx.architecture.code.utils

import okhttp3.RequestBody

object BuildParamUtils {

    fun buildParamUrl(url: String, map: MutableMap<String, Any>? = null): String {
        val sb = StringBuilder(url)
            .append("?")
        map?.forEach {
            sb.append(it.key)
            sb.append("=")
            sb.append(it.value)
        }
        return sb.toString()
    }
}