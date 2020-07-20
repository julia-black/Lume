package com.singlelab.lume.ui.creating_event

import com.singlelab.data.exceptions.ApiException
import com.singlelab.data.model.event.Event
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.creating_event.interactor.CreatingEventInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class CreatingEventPresenter @Inject constructor(
    private val interactor: CreatingEventInteractor,
    preferences: Preferences?
) : BasePresenter<CreatingEventView>(preferences, interactor as BaseInteractor) {

    fun createEvent(event: Event) {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val eventUid = interactor.createEvent(event)?.eventUid
                runOnMainThread {
                    viewState.showLoading(false)
                    eventUid?.let {
                        viewState.onCompleteCreateEvent(it)
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