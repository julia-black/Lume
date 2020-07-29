package com.singlelab.lume.ui.auth

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
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

    private var personUid: String? = null
    private var phone: String? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (!AuthData.isAnon) {
            runOnMainThread {
                viewState.showLoading(true)
            }
            invokeSuspend {
                try {
                    runOnMainThread {
                        viewState.showLoading(false)
                        viewState.toProfile()
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

    fun onClickSendCode(phone: String) {
        runOnMainThread {
            viewState.showLoading(true)
        }
        invokeSuspend {
            try {
                personUid = interactor.sendCode(phone)
//                preferences?.setUid(personUid)
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
        if (phone != null && personUid != null) {
            runOnMainThread {
                viewState.showLoading(true)
            }
            invokeSuspend {
                try {
                    interactor.auth(personUid!!, phone!!, code)
//                    if (auth != null) {
//                        preferences?.setAuth(auth)
//                    }
                    val isPersonFilled = interactor.isPersonFilled()
                    runOnMainThread {
                        viewState.showLoading(false)
                        if (!isPersonFilled) {
                            viewState.toRegistration()
                        } else {
                            preferences?.setAnon(false)
                            viewState.toProfile()
                        }
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
}