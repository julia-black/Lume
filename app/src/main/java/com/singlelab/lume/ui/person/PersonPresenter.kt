package com.singlelab.lume.ui.person

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.profile.Profile
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.person.interactor.PersonInteractor
import com.singlelab.net.exceptions.ApiException
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class PersonPresenter @Inject constructor(
    private var interactor: PersonInteractor,
    preferences: Preferences?
) : BasePresenter<PersonView>(preferences, interactor as BaseInteractor) {

    private var profile: Profile? = null

    fun loadProfile(personUid: String) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val profile = interactor.loadPerson(personUid)
                runOnMainThread {
                    viewState.showLoading(false)
                    if (profile != null) {
                        this.profile = profile
                        viewState.showProfile(profile)
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

    fun addToFriends(personUid: String) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                interactor.addToFriends(personUid)
                runOnMainThread {
                    viewState.showLoading(false)
                    profile?.let {
                        it.isFriend = true
                        viewState.showProfile(it)
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

    fun removeFromFriends(personUid: String) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                interactor.removeFromFriends(personUid)
                runOnMainThread {
                    viewState.showLoading(false)
                    profile?.let {
                        it.isFriend = false
                        viewState.showProfile(it)
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