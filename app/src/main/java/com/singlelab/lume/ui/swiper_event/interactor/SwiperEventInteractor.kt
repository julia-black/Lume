package com.singlelab.lume.ui.swiper_event.interactor

import com.singlelab.lume.model.event.Event
import com.singlelab.net.model.event.ParticipantRequest
import com.singlelab.net.model.event.RandomEventRequest

interface SwiperEventInteractor {
    suspend fun getRandomEvent(randomEventRequest: RandomEventRequest): Event?

    suspend fun acceptEvent(participantRequest: ParticipantRequest)

    suspend fun rejectEvent(eventUid: String)

    suspend fun updatePushToken(token: String)

    suspend fun sendReport(uid: String, reasonReport: String)
}