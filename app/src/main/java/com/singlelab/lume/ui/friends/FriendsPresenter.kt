package com.singlelab.lume.ui.friends

import com.singlelab.net.exceptions.ApiException
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.friends.interactor.FriendsInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class FriendsPresenter @Inject constructor(
    private var interactor: FriendsInteractor,
    preferences: Preferences?
) : BasePresenter<FriendsView>(preferences, interactor as BaseInteractor) {

    fun loadFriends() {
        viewState.showLoading(true)
        try {
            invokeSuspend {
                val friends = interactor.getFriends()
                runOnMainThread {
                    viewState.showLoading(false)
                    viewState.showFriends(friends)
                }
            }
        } catch (e: ApiException) {
            viewState.showLoading(false)
            viewState.showError(e.message)
        }
    }
}