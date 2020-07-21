package com.singlelab.lume.ui.my_profile

import android.graphics.Bitmap
import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.auth.AuthData
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import com.singlelab.lume.util.toBase64
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class MyProfilePresenter @Inject constructor(
    private var interactor: MyProfileInteractor,
    preferences: Preferences?
) : BasePresenter<MyProfileView>(preferences, interactor as BaseInteractor) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (AuthData.isAnon) {
            viewState.navigateToAuth()
        } else {
            loadProfile()
        }
    }

    private fun loadProfile() {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                if (!AuthData.isAnon) {
                    val profile = interactor.loadProfile()
                    runOnMainThread {
                        viewState.showLoading(false)
                        if (profile != null) {
                            viewState.showProfile(profile)
                        } else {
                            viewState.showError("Не удалось получить профиль")
                        }
                    }
                } else {
                    runOnMainThread {
                        preferences?.clearAuth()
                        viewState.navigateToAuth()
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

    fun updateImageProfile(image: Bitmap?) {
        image ?: return
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val uid = interactor.updateImageProfile(image.toBase64())
                runOnMainThread {
                    viewState.loadImage(uid?.imageUid)
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
}