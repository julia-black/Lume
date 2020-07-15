package com.singlelab.lume.ui.auth

import android.util.Log
import com.singlelab.data.model.Const
import com.singlelab.data.net.ApiException
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.ui.auth.interactor.AuthInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(private var interactor: AuthInteractor) :
    BasePresenter<AuthView>() {

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
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.onCodeSend()
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