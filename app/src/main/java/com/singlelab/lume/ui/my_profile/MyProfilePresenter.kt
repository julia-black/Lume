package com.singlelab.lume.ui.my_profile

import android.graphics.Bitmap
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.model.view.PagerTab
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import com.singlelab.lume.util.toBase64
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class MyProfilePresenter @Inject constructor(
    private var interactor: MyProfileInteractor,
    preferences: Preferences?
) : BasePresenter<MyProfileView>(preferences, interactor as BaseInteractor) {

    var profile: Profile? = null

    var friends: List<Person>? = null

    var selectedTab: PagerTab = PagerTab.FRIENDS

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (preferences != null && preferences.isFirstLaunch()) {
            sendPushToken(preferences.getPushToken())
        }
    }

    fun loadProfile(isFirstAttach: Boolean = false) {
        viewState.showLoading(isShow = true, withoutBackground = !isFirstAttach)
        invokeSuspend {
            try {
                if (!AuthData.isAnon) {
                    profile = interactor.loadProfile()
                    profile?.let {
                        preferences?.setCity(it.cityId, it.cityName)
                    }
                    runOnMainThread {
                        viewState.showLoading(isShow = false, withoutBackground = !isFirstAttach)
                        if (profile != null) {
                            viewState.showProfile(profile!!)
                            loadFriends(profile!!.personUid)
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
                    viewState.showLoading(isShow = false, withoutBackground = !isFirstAttach)
                    viewState.showError(e.message)
                }
            }
        }
    }

    fun logout() {
        invokeSuspend {
            try {
                interactor.updatePushToken("")
            } catch (e: ApiException) {
            }
            preferences?.clearAuth()
            viewState.navigateToAuth()
        }
    }

    fun updateImageProfile(image: Bitmap?) {
        image ?: return
        viewState.showLoading(isShow = true, withoutBackground = true)
        invokeSuspend {
            try {
                val uid = interactor.updateImageProfile(image.toBase64())
                runOnMainThread {
                    viewState.loadImage(uid)
                    viewState.showLoading(isShow = false, withoutBackground = true)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = true)
                    viewState.showError(e.message)
                }
            }
        }
    }

    private fun loadFriends(personUid: String) {
        invokeSuspend {
            try {
                friends = interactor.loadFriends(personUid)
                runOnMainThread {
                    viewState.showFriends(friends)
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showError(e.message)
                }
            }
        }
    }

    private fun sendPushToken(token: String?) {
        token ?: return
        invokeSuspend {
            try {
                interactor.updatePushToken(token)
                preferences?.setFirstLaunch(false)
            } catch (e: ApiException) {
            }
        }
    }
}