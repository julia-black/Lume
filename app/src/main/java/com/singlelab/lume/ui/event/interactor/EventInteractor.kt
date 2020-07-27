package com.singlelab.lume.ui.event.interactor

import com.singlelab.lume.model.event.Event
import com.singlelab.net.model.event.ParticipantRequest

interface EventInteractor {
    suspend fun getEvent(uid: String): Event?

    suspend fun acceptEvent(participantRequest: ParticipantRequest): Event?

    suspend fun rejectEvent(personUid: String, eventUid: String)
}