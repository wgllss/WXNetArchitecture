package com.wx.architecture

import android.app.Application
import com.wx.architecture.code.app.AppGlobals

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppGlobals.sApplication = this
    }
}