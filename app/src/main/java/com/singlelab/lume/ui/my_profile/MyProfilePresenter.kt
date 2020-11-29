package com.singlelab.lume.ui.my_profile

import android.graphics.Bitmap
import android.util.Log
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.profile.Badge
import com.singlelab.lume.model.profile.Person
import com.singlelab.lume.model.profile.PersonNotifications
import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.my_profile.interactor.MyProfileInteractor
import com.singlelab.lume.util.resize
import com.singlelab.lume.util.toBase64
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.exceptions.NotConnectionException
import com.singlelab.net.model.auth.AuthData
import kotlinx.coroutines.delay
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class MyProfilePresenter @Inject constructor(
    private var interactor: MyProfileInteractor,
    preferences: Preferences?
) : BasePresenter<MyProfileView>(preferences, interactor as BaseInteractor) {

    var profile: Profile? = null

    var friends: List<Person>? = null

    var badges: List<Badge>? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (preferences != null && preferences.isFirstLaunch()) {
            sendPushToken(preferences.getPushToken())
        }
    }

    override fun onLoadedNotification(notifications: PersonNotifications) {
        super.onLoadedNotification(notifications)
        viewState.showNewBadge(notifications.hasNewBadges)
    }

    fun loadProfile(isFirstAttach: Boolean = false) {
        viewState.showLoading(isShow = true, withoutBackground = !isFirstAttach)
        invokeSuspend {
            try {
                if (!AuthData.isAnon) {
                    showProfileFromCache(isFirstAttach)
                    profile = interactor.loadProfile()
                    profile?.let {
                        interactor.saveProfile(it)
                        preferences?.setCity(it.cityId, it.cityName)
                        preferences?.setAge(it.age)
                    }
                    runOnMainThread {
                        viewState.showLoading(isShow = false, withoutBackground = !isFirstAttach)
                        if (profile != null) {
                            viewState.showProfile(profile!!)
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
                    viewState.showError(
                        message = e.message,
                        withRetry = e is NotConnectionException,
                        callRetry = { loadProfile(isFirstAttach) }
                    )
                }
            }
        }
    }

    fun logout() {
        invokeSuspend {
            try {
                sendPushToken("")
                interactor.clearDatabase()
            } catch (e: ApiException) {
            }
            preferences?.clearAuth()
            runOnMainThread {
                viewState.navigateToAuth()
            }
        }
    }

    fun updateImageProfile(image: Bitmap?) {
        image ?: return
        viewState.showLoading(isShow = true, withoutBackground = true)
        invokeSuspend {
            try {
                val imageStr = image.resize().toBase64()
                val miniImageStr = image.resize(200).toBase64()
                val uid = interactor.updateImageProfile(imageStr, miniImageStr)
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

    fun loadFriends() {
        profile?.personUid?.let {
            Log.d(Const.LOG_TAG, "personUid = it")
            invokeSuspend {
                try {
                    showFriendsFromCache()
                    Log.d(Const.LOG_TAG, "loading")
                    friends = interactor.loadFriends(it)
                    Log.d(Const.LOG_TAG, "loaded")
                    friends?.let {
                        interactor.saveFriends(it)
                    }
                    runOnMainThread {
                        viewState.onLoadedFriends(friends)
                    }
                } catch (e: ApiException) {
                    runOnMainThread {
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }

    fun loadBadges() {
        profile?.personUid?.let {
            invokeSuspend {
                try {
                    badges = interactor.loadBadges(it)
                    runOnMainThread {
                        badges?.let {
                            viewState.onLoadedBadges(it)
                            updateNotifications()
                        }
                    }
                } catch (e: ApiException) {
                    runOnMainThread {
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }

    private suspend fun showProfileFromCache(isFirstAttach: Boolean) {
        profile = interactor.loadProfileFromCache()
        if (profile != null) {
            delay(Const.MIN_DELAY_FOR_TRANSITION)
            runOnMainThread {
                viewState.showLoading(isShow = false, withoutBackground = !isFirstAttach)
                viewState.showProfile(profile!!)
            }
        }
    }

    private fun showFriendsFromCache() {
        invokeSuspend {
            val friends = interactor.loadFriendsFromCache()
            Log.d(Const.LOG_TAG, "friends = $friends")
            if (friends != null) {
                runOnMainThread {
                    viewState.onLoadedFriends(friends)
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