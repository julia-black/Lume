package com.singlelab.lume.ui.swiper_event

import com.singlelab.net.exceptions.ApiException
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.swiper_event.interactor.SwiperEventInteractor
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SwiperEventPresenter @Inject constructor(
    private val interactor: SwiperEventInteractor,
    preferences: Preferences?
) : BasePresenter<SwiperEventView>(preferences, interactor as BaseInteractor) {

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

    fun loadNextEvent() {
        runOnMainThread {
            viewState.showLoading(false)
            viewState.showEvent(
                Event(
                    null,
                    "Событие 1",
                    10,
                    20,
                    10f,
                    10f,
                    "Описание события 1",
                    "2020-07-21T14:00:23.239",
                    "2020-07-21T14:00:23.239"
                )
            )
        }
    }
}