package com.singlelab.lume.base

import com.singlelab.data.net.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.MvpView
import kotlin.coroutines.CoroutineContext

open class BasePresenter<ViewT: MvpView> : MvpPresenter<ViewT>() {
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
}