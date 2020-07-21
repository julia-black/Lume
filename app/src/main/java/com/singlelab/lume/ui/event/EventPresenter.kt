package com.singlelab.lume.ui.event

import com.singlelab.data.exceptions.ApiException
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

    var eventUid: String? = null

    fun loadEvent(uid: String) {
        eventUid = uid
        viewState.showLoading(true)
        try {
            invokeSuspend {
                val event = interactor.getEvent(uid)
                runOnMainThread {
                    viewState.showLoading(false)
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
}