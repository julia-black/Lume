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
        loadRandomEvent(true)
    }

    fun applyFilter(filterEvent: FilterEvent) {
        this.filterEvent = filterEvent
        loadRandomEvent()
    }

    fun loadRandomEvent(isFirstAttach: Boolean = false) {
        viewState.showLoading(isShow = true, withoutBackground = !isFirstAttach)
        invokeSuspend {
            try {
                val randomEventRequest = RandomEventRequest(
                    cityId = if (filterEvent.isOnlyOnline) null else filterEvent.cityId,
                    eventTypes = filterEvent.selectedTypes.map { it.typeId },
                    personXCoordinate = if (filterEvent.isOnlyOnline) null else filterEvent.latitude,
                    personYCoordinate = if (filterEvent.isOnlyOnline) null else filterEvent.longitude,
                    distance = if (filterEvent.isOnlyOnline) null else filterEvent.distance.value,
                    isOnline = filterEvent.isOnlineForRequest()
                )
                val event = interactor.getRandomEvent(randomEventRequest)
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = !isFirstAttach)
                    this.event = event
                    event?.let {
                        viewState.showEvent(it)
                    }
                }
            } catch (e: ApiException) {
                runOnMainThread {
                    viewState.showLoading(isShow = false, withoutBackground = !isFirstAttach)
                    if (e.errorCode == Const.ERROR_CODE_NO_MATCHING_EVENTS) {
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
            viewState.showLoading(isShow = true, withoutBackground = true)
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
    }

    fun rejectEvent() {
        event?.eventUid?.let { eventUid ->
            viewState.showLoading(isShow = true, withoutBackground = true)
            invokeSuspend {
                try {
                    interactor.rejectEvent(eventUid)
                    runOnMainThread {
                        event = null
                        viewState.showLoading(isShow = false, withoutBackground = true)
                        loadRandomEvent()
                    }
                } catch (e: ApiException) {
                    runOnMainThread {
                        viewState.showLoading(isShow = false, withoutBackground = true)
                        viewState.showError(e.message)
                    }
                }
            }
        }
    }
}