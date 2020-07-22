package com.singlelab.lume.ui.person

import com.singlelab.net.exceptions.ApiException
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.person.interactor.PersonInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class PersonPresenter @Inject constructor(
    private var interactor: PersonInteractor,
    preferences: Preferences?
) : BasePresenter<PersonView>(preferences, interactor as BaseInteractor) {

    fun loadProfile(personUid: String) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val profile = interactor.loadPerson(personUid)
                runOnMainThread {
                    viewState.showLoading(false)
                    if (profile != null) {
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
                    viewState.onAddedToFriends()
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
                    viewState.onRemovedFromFriends()
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