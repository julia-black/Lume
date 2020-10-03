package com.singlelab.lume.base

import com.singlelab.lume.base.view.BaseView
import com.singlelab.lume.model.auth.Auth
import com.singlelab.lume.model.profile.PersonNotifications
import com.singlelab.lume.pref.Preferences
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.auth.AuthResponse
import com.singlelab.net.repositories.OnRefreshTokenListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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

    override fun onRefreshToken(auth: AuthResponse?) {
        if (auth != null) {
            preferences?.setAuth(Auth.fromResponse(auth)!!)
        }
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