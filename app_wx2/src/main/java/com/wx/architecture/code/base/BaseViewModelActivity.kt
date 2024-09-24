package com.wx.architecture.code.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelLazy
import com.wx.architecture.code.dialog.ShowUIDialog
import java.lang.reflect.ParameterizedType


open class BaseViewModelActivity<VM : BaseViewModel>(@LayoutRes val layResID: Int) : AppCompatActivity() {
    private var loading: ShowUIDialog? = null

    protected val viewModel by lazyViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layResID)
        initX()
    }

    protected fun initX() {
        bindObserve()
    }

    //是否loading
    open fun isShowloading(): Boolean? {
        return loading?.isShowing()
    }

    open fun showloading(showText: String?) {
        if (null == loading) loading = ShowUIDialog(this)
        if (isShowloading() == true) return
        if (showText != null) loading?.show(showText)
    }

    open fun hideLoading() {
        loading?.dismiss()
        loading = null
    }

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }

    protected fun bindObserve() {
        viewModel?.run {
            showUIDialog.observe(this@BaseViewModelActivity) { it ->
                if (it.isShow) showloading(it.msg) else hideLoading()
            }
            errorMsgLiveData.observe(this@BaseViewModelActivity) {
                //公共toast 提示样式可以自行封装
                Toast.makeText(this@BaseViewModelActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @MainThread
    inline fun lazyViewModels(): Lazy<VM> {
        val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        return ViewModelLazy(cls.kotlin, { viewModelStore }, { defaultViewModelProviderFactory })
    }
}