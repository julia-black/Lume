package com.singlelab.lume.ui.my_profile

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.auth.AuthData
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class MyProfilePresenter @Inject constructor(
    private var interactor: MyProfileInteractor,
    preferences: Preferences?
) : BasePresenter<MyProfileView>(preferences, interactor as BaseInteractor) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (!AuthData.accessToken.isNullOrEmpty()) {
            loadProfile()
        } else {
            viewState.navigateToAuth()
        }
    }

    private fun loadProfile() {
        runOnMainThread {
            viewState.showLoading(true)
        }
        invokeSuspend {
            try {
                val profile = interactor.loadProfile()
                runOnMainThread {
                    viewState.showLoading(false)
                    if (profile != null) {
                        viewState.showProfile(profile)
                    } else {
                        viewState.showError("Не удалось получить профиль")
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

    fun logout() {
        preferences?.clearAuth()
        viewState.navigateToAuth()
    }
}