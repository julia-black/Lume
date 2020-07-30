package com.singlelab.lume.ui.swiper_event

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.model.event.FilterEvent
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.swiper_event.interactor.SwiperEventInteractor
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.model.auth.AuthData
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.event.ParticipantStatus
import com.singlelab.net.model.event.RandomEventRequest
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SwiperEventPresenter @Inject constructor(
    private val interactor: SwiperEventInteractor,
    preferences: Preferences?
) : BasePresenter<SwiperEventView>(preferences, interactor as BaseInteractor) {

    var event: Event? = null

    var filterEvent = FilterEvent(cityId = AuthData.cityId, cityName = AuthData.cityName)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadRandomEvent()
    }

    fun applyFilter(filterEvent: FilterEvent) {
        this.filterEvent = filterEvent
        loadRandomEvent()
    }

    fun loadRandomEvent() {
        viewState.showLoading(true)
        invokeSuspend {
            try {
                val randomEventRequest = RandomEventRequest(
                    cityId = filterEvent.cityId,
                    eventTypes = filterEvent.selectedTypes.map { it.typeId },
                    personXCoordinate = filterEvent.latitude,
                    personYCoordinate = filterEvent.longitude,
                    distance = filterEvent.distance.value
                )
                val event = interactor.getRandomEvent(randomEventRequest)
                runOnMainThread {
                    viewState.showLoading(false)
                    this.event = event
                    event?.let {
                        viewState.showEvent(it)
                    }
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(false)
                    if (e.errorCode == Const.ERROR_CODE_NO_MATCHING) {
                        viewState.showEmptySwipes()
                    } else {
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }

    fun acceptEvent() {
        val uid = AuthData.uid ?: return
        event?.eventUid?.let { eventUid ->
            viewState.showLoading(true)
            invokeSuspend {
                try {
                    interactor.acceptEvent(
                        ParticipantRequest(
                            uid,
                            eventUid,
                            if (event!!.isOpenForInvitations) ParticipantStatus.ACTIVE.id else ParticipantStatus.WAITING_FOR_APPROVE_FROM_EVENT.id
                        )
                    )
                    runOnMainThread {
                        viewState.toAcceptedEvent(event!!.isOpenForInvitations, eventUid)
                        event = null
                        viewState.showLoading(false)
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

    fun rejectEvent() {
        event?.eventUid?.let { eventUid ->
            viewState.showLoading(true)
            invokeSuspend {
                try {
                    interactor.rejectEvent(eventUid)
                    runOnMainThread {
                        event = null
                        viewState.showLoading(false)
                        loadRandomEvent()
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
}