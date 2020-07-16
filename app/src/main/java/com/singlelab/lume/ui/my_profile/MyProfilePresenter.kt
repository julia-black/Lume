package com.singlelab.lume.ui.my_profile

import androidx.navigation.NavController
import com.singlelab.data.exceptions.ApiException
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import com.singlelab.lume.ui.my_profile.router.MyProfileRouter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class MyProfilePresenter @Inject constructor(
    private var interactor: MyProfileInteractor,
    private var router: MyProfileRouter,
    preferences: Preferences?
) : BasePresenter<MyProfileView>(preferences, interactor as BaseInteractor) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (preferences != null && !preferences.getAccessToken().isNullOrEmpty()) {
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
                interactor.loadProfile()
                runOnMainThread {
                    viewState.showLoading(false)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun navigateToAuth(navController: NavController) {
        router.navigateToAuth(navController)
    }

    fun logout(navController: NavController) {
        preferences?.clearAuth()
        router.navigateToAuth(navController)
    }
}