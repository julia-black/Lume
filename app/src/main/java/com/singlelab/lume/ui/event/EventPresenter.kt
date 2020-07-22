package com.singlelab.lume.ui.event

import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.event.interactor.EventInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class EventPresenter @Inject constructor(
    private val interactor: EventInteractor,
    preferences: Preferences?
) : BasePresenter<EventView>(preferences, interactor as BaseInteractor) {

    var event: Event? = null

    fun loadEvent(uid: String) {
        viewState.showLoading(true)
        try {
            invokeSuspend {
                val event = interactor.getEvent(uid)
                runOnMainThread {
                    viewState.showLoading(false)
                    this.event = event
                    event?.let {
                        viewState.showEvent(it)
                    }
                }
            }
        } catch (e: ApiException) {
            viewState.showLoading(false)
            viewState.showError(e.message)
        }
    }

    fun onClickAdministrator() {
        event?.administrator?.personUid?.let {
            if (it == AuthData.uid) {
                viewState.toMyProfile()
            } else {
                viewState.toProfile(it)
            }
        }
    }
}