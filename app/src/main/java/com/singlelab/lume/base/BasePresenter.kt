package com.singlelab.lume.base

import com.singlelab.lume.base.view.BaseView
import com.singlelab.lume.model.auth.Auth
import com.singlelab.lume.model.profile.PersonNotifications
import com.singlelab.lume.pref.Preferences
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.repositories.OnRefreshTokenListener
import kotlinx.coroutines.*
import moxy.MvpPresenter
import kotlin.coroutines.CoroutineContext

open class BasePresenter<ViewT : BaseView>(
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
        updateNotifications()
        if (AuthData.isAnon) {
            preferences?.setAnon(true)
        }
        if (preferences != null && preferences.isFirstLaunch()) {
            loadPromo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun loadPromo() {
        invokeSuspend {
            try {
                val promoInfo = baseInteractor.getPromo()
                promoInfo?.let {
                    preferences?.setEventPromoRewardEnabled(promoInfo.isEventPromoRewardEnabled)
                }
            } catch (e: ApiException) {
            }
        }
    }

    open fun onLoadedNotification(notifications: PersonNotifications) {
        viewState.showNotifications(notifications)
    }

    fun updateNotifications() {
        if (AuthData.isAnon) {
            viewState.showNotifications(PersonNotifications())
        } else {
            invokeSuspend {
                try {
                    val notifications = baseInteractor.getNotifications()
                    runOnMainThread {
                        onLoadedNotification(notifications)
                    }
                } catch (e: ApiException) {
                }
            }
        }
    }

    override fun onRefreshToken(accessToken: String, refreshToken: String?) {
        preferences?.setAuth(Auth(accessToken, refreshToken))
    }

    override fun onRefreshTokenFailed() {
        preferences?.clearAuth()
        runOnMainThread {
            viewState.toAuth()
        }
    }

    protected fun invokeSuspend(block: suspend () -> Unit) {
        scope.launch { block.invoke() }
    }

    protected fun runOnMainThread(block: () -> Unit) {
        scope.launch(CoroutineContextProvider().main) {
            block.invoke()
        }
    }
}