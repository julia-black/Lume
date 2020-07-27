package com.singlelab.lume.ui.events

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.events.interactor.EventsInteractor
import com.singlelab.net.exceptions.ApiException
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class EventsPresenter @Inject constructor(
    private val interactor: EventsInteractor,
    preferences: Preferences?
) : BasePresenter<EventsView>(preferences, interactor as BaseInteractor) {

    fun loadEvents() {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val events = interactor.getEvents()
                runOnMainThread {
                    viewState.showLoading(false)
                    events?.let {
                        viewState.showEvents(events)
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