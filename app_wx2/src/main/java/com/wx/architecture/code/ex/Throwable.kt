package com.wx.architecture.code.ex

import com.google.gson.JsonSyntaxException
import com.wx.architecture.R
import com.wx.architecture.code.app.AppGlobals
import com.wx.architecture.code.utils.isNetWorkActive
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable?.parseErrorString(): String = when (this) {
    is ConnectException, is SocketException -> {
        if (message?.contains("Network is unreachable") == true)
            getString(R.string.Mobilenetuseless_msg)
        if (message?.contains("Failed to connect to") == true)
            getString(R.string.failed_to_connect_to)
        else
            getString(R.string.ConnectException)
    }
    is HttpException -> {
        if (message?.contains("HTTP 50") == true) {
            message!!.substring(0, 8)
        } else {
            getString(R.string.HttpException)
        }
    }
    is InterruptedIOException -> {
        if (message?.contains("timeout") == true)
            getString(R.string.SocketTimeoutException)
        else
            getString(R.string.ConnectException)
    }
    is UnknownHostException -> getString(R.string.UnknownHostException)
    is JsonSyntaxException -> getString(R.string.JsonSyntaxException)
    is SocketTimeoutException, is TimeoutException -> getString(R.string.SocketTimeoutException)
    is IllegalArgumentException -> {
        if (message?.contains("baseUrl must end in ") == true)
            if (AppGlobals.sApplication.isNetWorkActive()) getString(R.string.HostBaseUrlError)
            else getString(R.string.Mobilenetuseless_msg)
        else message ?: getString(R.string.ElseNetException)
    }
    else -> getString(R.string.ElseNetException)
}

fun getString(resID: Int) = AppGlobals.sApplication.getString(resID)