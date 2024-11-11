package com.wx.architecture.code.ex

class ExceptionEX(val errorCode: Int, val errorMessage: String) : Exception(errorMessage) {
}