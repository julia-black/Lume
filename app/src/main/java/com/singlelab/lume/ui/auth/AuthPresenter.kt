package com.singlelab.lume.ui.auth

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.Const
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.auth.interactor.AuthInteractor
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AuthPresenter @Inject constructor(
    private var interactor: AuthInteractor,
    preferences: Preferences?
) : BasePresenter<AuthView>(preferences, interactor as BaseInteractor) {

    private var phone: String? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (!AuthData.isAnon) {
            runOnMainThread {
                viewState.showLoading(isShow = true, withoutBackground = true)
            }
            invokeSuspend {
                try {
                    runOnMainThread {
                        viewState.showLoading(isShow = false, withoutBackground = true)
                        viewState.toProfile()
                    }
                } catch (e: ApiException) {
                    runOnMainThread {
                        viewState.showLoading(isShow = false, withoutBackground = true)
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }

    fun onClickSendCode(phone: String, isPushSend: Boolean) {
        runOnMainThread {
            viewState.showLoading(isShow = true, withoutBackground = true)
        }
        invokeSuspend {
            try {
                val personUid = interactor.sendCode(phone)
                preferences?.setUid(personUid)
                this.phone = phone
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.onCodeSend(phone)
                }
            } catch (e: ApiException) {
                if (e.errorCode == Const.ERROR_CODE_NEW_PUSH_TOKEN) {
                    onClickSendCode(phone, false)
                } else {
                    runOnMainThread {
                        viewState.showLoading(isShow = false, withoutBackground = true)
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }

    fun onClickAuth(code: String) {
        if (phone != null) {
            runOnMainThread {
                viewState.showLoading(isShow = true, withoutBackground = true)
            }
            invokeSuspend {
                try {
                    val auth = interactor.auth(phone!!, code)
                    if (auth != null) {
                        preferences?.setAuth(auth)
                    }
                    val isPersonFilled = interactor.isPersonFilled()
                    runOnMainThread {
                        viewState.showLoading(isShow = false, withoutBackground = true)
                        if (!isPersonFilled) {
                            viewState.toRegistration()
                        } else {
                            preferences?.setAnon(false)
                            viewState.toProfile()
                        }
                    }
                } catch (e: ApiException) {
                    runOnMainThread {
                        viewState.showLoading(isShow = false, withoutBackground = true)
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }
}