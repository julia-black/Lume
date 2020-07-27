package com.singlelab.lume.ui.participants.interactor

import com.singlelab.lume.model.event.Event
import com.singlelab.net.model.event.ParticipantRequest

interface ParticipantsInteractor {
    suspend fun approvePerson(participantRequest: ParticipantRequest): Event?

    suspend fun rejectPerson(personUid: String, eventUid: String): Event?
}