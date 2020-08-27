package com.singlelab.lume.ui.swiper_event

import com.singlelab.lume.base.BaseInteractor
import com.singlelab.lume.base.BasePresenter
import com.singlelab.lume.model.Const
import com.singlelab.lume.model.event.Event
import com.singlelab.lume.model.event.FilterEvent
import com.singlelab.lume.pref.Preferences
import com.singlelab.lume.ui.swiper_event.interactor.SwiperEventInteractor
import com.singlelab.lume.util.toDateFormat
import com.singlelab.net.exceptions.ApiException
import com.singlelab.net.exceptions.NotConnectionException
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
        if (preferences != null && preferences.isFirstLaunch()) {
            sendPushToken(preferences.getPushToken())
        }
    }

    private fun sendPushToken(token: String?) {
        token ?: return
        invokeSuspend {
            try {
                interactor.updatePushToken(token)
                preferences?.setFirstLaunch(false)
            } catch (e: ApiException) {
            }
        }
    }

    fun applyFilter(filterEvent: FilterEvent) {
        if (this.filterEvent != filterEvent) {
            this.filterEvent = filterEvent
            loadRandomEvent()
        }
    }

    fun loadRandomEvent(isFirstAttach: Boolean = false) {
        viewState.showLoading(isShow = true, withoutBackground = !isFirstAttach)
        invokeSuspend {
            try {
                val randomEventRequest = RandomEventRequest(
                    cityId = if (filterEvent.isOnlyOnline) null else filterEvent.cityId,
                    eventTypes = filterEvent.selectedTypes.map { it.id },
                    personXCoordinate = if (filterEvent.isOnlyOnline) null else filterEvent.latitude,
                    personYCoordinate = if (filterEvent.isOnlyOnline) null else filterEvent.longitude,
                    distance = if (filterEvent.isOnlyOnline) null else filterEvent.distance.value,
                    isOnline = filterEvent.isOnlineForRequest(),
                    minimalStartTime = filterEvent.minimalStartTime?.toDateFormat(Const.DATE_FORMAT_TIME_ZONE),
                    maximalEndTime = filterEvent.maximalEndTime?.toDateFormat(Const.DATE_FORMAT_TIME_ZONE)
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
                    if (e is NotConnectionException) {
                        viewState.hideFilter()
                    }
                    if (e.errorCode == Const.ERROR_CODE_NO_MATCHING_EVENTS) {
                        viewState.showEmptySwipes(filterEvent.isFullFilter())
                    } else {
                        viewState.showError(
                            e.message,
                            withRetry = e is NotConnectionException,
                            callRetry = { loadRandomEvent(isFirstAttach) }
                        )
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