package com.singlelab.lume.base

import com.singlelab.data.model.auth.Auth
import com.singlelab.data.net.CoroutineContextProvider
import com.singlelab.data.repositories.OnRefreshTokenListener
import com.singlelab.lume.pref.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.MvpView
import kotlin.coroutines.CoroutineContext

open class BasePresenter<ViewT : MvpView>(
    protected val preferences: Preferences?,
    private val baseInteractor: BaseInteractor
) : MvpPresenter<ViewT>(), OnRefreshTokenListener {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        baseInteractor.setOnRefreshTokenListener(this)
    }

    protected fun invokeSuspend(block: suspend () -> Unit) {
        scope.launch { block.invoke() }
    }

    protected fun runOnMainThread(block: () -> Unit) {
        scope.launch(CoroutineContextProvider().main) {
            block.invoke()
        }
    }

    override fun onRefreshToken(auth: Auth?) {
        if (auth != null) {
            preferences?.setAuth(auth)
        }
    }
}