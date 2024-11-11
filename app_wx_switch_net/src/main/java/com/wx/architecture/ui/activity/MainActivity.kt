package com.wx.architecture.ui.activity

import android.view.View
import com.wx.architecture.R
import com.wx.architecture.code.base.BaseViewModelActivity
import com.wx.architecture.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseViewModelActivity<MainViewModel>(R.layout.activity_main), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn1 -> {
                viewModel.getHomeList()
            }
            else -> {}
        }
    }
}