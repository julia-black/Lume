package com.singlelab.lume.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.singlelab.data.net.CoroutineContextProvider
import com.singlelab.lume.MainActivity
import com.singlelab.lume.base.listeners.OnToolbarListener
import com.singlelab.lume.base.view.ErrorView
import com.singlelab.lume.base.view.LoadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import kotlin.coroutines.CoroutineContext

open class BaseFragment : MvpAppCompatFragment(), ErrorView, LoadingView {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    protected fun invokeSuspend(block: suspend () -> Unit) {
        scope.launch { block.invoke() }
    }

    protected fun runOnMainThread(block: () -> Unit) {
        scope.launch(CoroutineContextProvider().main) {
            block.invoke()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (this is OnToolbarListener) {
            (activity as MainActivity?)?.setToolbarListener(this)
        }
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showLoading(isShow: Boolean) {
        (activity as MainActivity?)?.showLoading(isShow)
    }
}