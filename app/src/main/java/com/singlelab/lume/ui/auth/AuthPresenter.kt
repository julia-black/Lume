package com.singlelab.lume.ui.auth

import android.util.Log
import androidx.navigation.NavController
import com.singlelab.data.model.Const
import com.singlelab.data.net.ApiException
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.auth.interactor.AuthInteractor
import com.singlelab.lume.ui.auth.router.AuthRouter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(
    private var interactor: AuthInteractor,
    private var router: AuthRouter,
    private val preferences: Preferences?
) :
    BasePresenter<AuthView>() {

    private var phone: String? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.d(Const.LOG_TAG, "view attach auth")
    }

    fun onClickSendCode(phone: String) {
        runOnMainThread {
            viewState.showLoading(true)
        }
        invokeSuspend {
            try {
                val responsePersonUid = interactor.sendCode(phone)
                this.phone = phone
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.onCodeSend(phone)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun onClickAuth(code: String) {
        if (phone != null) {
            runOnMainThread {
                viewState.showLoading(true)
            }
            invokeSuspend {
                try {
                    val auth = interactor.auth(phone!!, code)
                    runOnMainThread {
                        viewState.showLoading(false)
                        Log.d(Const.LOG_TAG, auth?.accessToken ?: "")
                        if (auth != null) {
                            preferences?.setAuth(auth)
                        }
                        viewState.onAuth()
                    }
                } catch (e: ApiException) {
                    runOnMainThread {
                        viewState.showLoading(false)
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }

    fun navigateToMyProfile(navController: NavController) {
        router.navigateToMyProfile(navController)
    }
}